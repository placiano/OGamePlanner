package com.skiwi.ogameplanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Predicate;

import static com.skiwi.ogameplanner.Building.*;

/**
 * @author Frank van Heeswijk
 */
public class Planner {
    private final PlayerSnapshot initialPlayerSnapshot;
    private final PlayerSnapshot goal;
    private final Predicate<PlayerSnapshot> goalPredicate;
    private final Heuristic heuristic;
    private final int lookahead;

    public Planner(PlayerSnapshot initialPlayerSnapshot, PlayerSnapshot goal, Predicate<PlayerSnapshot> goalPredicate, Heuristic heuristic, int lookahead) {
        this.initialPlayerSnapshot = initialPlayerSnapshot;
        this.goal = goal;
        this.goalPredicate = goalPredicate;
        this.heuristic = heuristic;
        this.lookahead = lookahead;
    }

    //TODO later support other things than buildings too

    public PlayerSnapshot plan() {
        if (goalPredicate.test(initialPlayerSnapshot)) {
            return initialPlayerSnapshot;
        }

        PlayerSnapshot earliestPlayerSnapshot = null;

        for (int localLookahead = 1; localLookahead <= lookahead; localLookahead++) {
            PlayerSnapshot playerSnapshot = lookahead(localLookahead);
            if (earliestPlayerSnapshot == null || playerSnapshot.getTime() < earliestPlayerSnapshot.getTime()) {
                earliestPlayerSnapshot = playerSnapshot;
            }
            System.out.println();
        }

        return earliestPlayerSnapshot;
    }

    private enum ImprovementType { NONE, ADD, REPOSITION }

    private PlayerSnapshot lookahead(int localLookahead) {
        List<Building> buildOrder = initializeBuildOrder();
        long leastTime = calculateTimeForBuildOrder(buildOrder);
        System.out.println("Lookahead: " + localLookahead);
        System.out.println("Time: " + leastTime + " / Build Order: " + buildOrder);

        while (true) {
            ImprovementType improvementType = ImprovementType.NONE;

            //check for add improvement
            List<Building> possibleBuildings = calculatePossibleBuildings(initialPlayerSnapshot);
            List<Building> bestBuildings = new ArrayList<>();
            List<Integer> bestPositions = new ArrayList<>();

            List<List<Building>> allBuildings = new ArrayList<>();
            List<List<Integer>> allPositions = new ArrayList<>();
            //loop over greedy-1 until greedy-n
            for (int currentLookahead = 1; currentLookahead <= localLookahead; currentLookahead++) {
                buildOrdersForLookahead(possibleBuildings, buildOrder.size(), allBuildings, allPositions, new ArrayList<>(), new ArrayList<>(), currentLookahead, 0);
            }

            for (int i = 0; i < allBuildings.size(); i++) {
                List<Building> buildings = allBuildings.get(i);
                List<Integer> positions = allPositions.get(i);

                for (int j = 0; j < buildings.size(); j++) {
                    Building building = buildings.get(j);
                    int position = positions.get(j);
                    buildOrder.add(position, building);
                }

                //if -1 is returned, it means it takes infinite time
                long time = calculateTimeForBuildOrder(buildOrder);
                if (time > -1 && time < leastTime) {
                    improvementType = ImprovementType.ADD;
                    leastTime = time;
                    bestBuildings = buildings;
                    bestPositions = positions;
                }

                for (int j = buildings.size() - 1; j >= 0; j--) {
                    int position = positions.get(j);
                    buildOrder.remove(position);
                }
            }

            //check for reposition improvement
            int swapPosition = -1;
            int swapModifier = Integer.MIN_VALUE;

            for (int position = 0; position < buildOrder.size(); position++) {
                for (int modifier = -position; modifier < buildOrder.size() - position; modifier++) {
                    if (modifier == 0) {
                        continue;
                    }

                    Building buildingToMove = buildOrder.remove(position);
                    buildOrder.add(position + modifier, buildingToMove);

                    //if -1 is returned, it means it takes infinite time
                    long time = calculateTimeForBuildOrder(buildOrder);
                    if (time > -1 && time < leastTime) {
                        improvementType = ImprovementType.REPOSITION;
                        leastTime = time;
                        swapPosition = position;
                        swapModifier = modifier;
                    }

                    Building buildingToMoveBack = buildOrder.remove(position + modifier);
                    buildOrder.add(position, buildingToMoveBack);
                }
            }

            switch (improvementType) {
                case NONE:
                    //no improvement
                    return reconstructPlayerSnapshot(buildOrder);
                case ADD:
                    for (int i = 0; i < bestBuildings.size(); i++) {
                        buildOrder.add(bestPositions.get(i), bestBuildings.get(i));
                    }
                    System.out.println("Time: " + leastTime + " / Build Order: " + buildOrder);
                    break;
                case REPOSITION:
                    Building buildingToMove = buildOrder.remove(swapPosition);
                    buildOrder.add(swapPosition + swapModifier, buildingToMove);
                    System.out.println("Time: " + leastTime + " / Build Order: " + buildOrder);
                    break;
            }
        }
    }

    private void buildOrdersForLookahead(List<Building> possibleBuildings, int buildOrderSize, List<List<Building>> allBuildings, List<List<Integer>> allPositions, List<Building> buildings, List<Integer> positions, int limit, int depth) {
        if (depth == limit) {
            allBuildings.add(buildings);
            allPositions.add(positions);
            return;
        }

        for (Building building : possibleBuildings) {
            for (int position = 0; position < buildOrderSize + depth; position++) {
                List<Building> newBuildings = new ArrayList<>(buildings);
                List<Integer> newPositions = new ArrayList<>(positions);
                newBuildings.add(building);
                newPositions.add(position);
                buildOrdersForLookahead(possibleBuildings, buildOrderSize, allBuildings, allPositions, newBuildings, newPositions, limit, depth + 1);
            }
        }
    }

    private List<Building> initializeBuildOrder() {
        //TODO rewrite this
        PlayerSnapshot dummyPlayerSnapshot = new PlayerSnapshot(initialPlayerSnapshot.getServerSettings(), initialPlayerSnapshot.getAveragePlanetTemperature());

        int metalStorageLevel = 0;
        int crystalStorageLevel = 0;

        List<Building> buildOrder = new ArrayList<>();
        for (int i = 0; i < goal.getBuildingLevel(METAL_MINE); i++) {
            while (true) {
                ActionCost upgradeCost = METAL_MINE.getUpgradeCost(dummyPlayerSnapshot, i);
                boolean noStorageUpgrades = true;
                if (upgradeCost.getMetal() > METAL_STORAGE.getStorageCapacity(createDummyPlayerSnapshotForStorage(METAL_STORAGE, metalStorageLevel))) {
                    buildOrder.add(METAL_STORAGE);
                    metalStorageLevel++;
                    noStorageUpgrades = false;
                }
                if (upgradeCost.getCrystal() > CRYSTAL_STORAGE.getStorageCapacity(createDummyPlayerSnapshotForStorage(CRYSTAL_STORAGE, crystalStorageLevel))) {
                    buildOrder.add(CRYSTAL_STORAGE);
                    crystalStorageLevel++;
                    noStorageUpgrades = false;
                }
                if (noStorageUpgrades) {
                    break;
                }
            }

            buildOrder.add(METAL_MINE);
        }
        return buildOrder;
    }

    private PlayerSnapshot createDummyPlayerSnapshotForStorage(Building storageBuilding, int level) {
        PlayerSnapshot dummyPlayerSnapshot = new PlayerSnapshot(initialPlayerSnapshot.getServerSettings(), initialPlayerSnapshot.getAveragePlanetTemperature());
        EnumMap<Building, Integer> initialBuildings = new EnumMap<>(Building.class);
        initialBuildings.put(storageBuilding, level);
        dummyPlayerSnapshot.initializeBuildings(initialBuildings);
        return dummyPlayerSnapshot;
    }

    private long calculateTimeForBuildOrder(List<Building> buildOrder) {
        PlayerSnapshot playerSnapshot = reconstructPlayerSnapshot(buildOrder);
        return (playerSnapshot == null) ? -1 : playerSnapshot.getTime();
    }

    private static Action getActionPlayerSnapshotStartBuilding(PlayerSnapshot playerSnapshot, Building building) {
        for (Action action : playerSnapshot.generateActions()) {
            if (action.isAllowed(playerSnapshot)) {
                if (action instanceof FinishUpgradeBuildingAction) {
                    if (((FinishUpgradeBuildingAction)action).getBuilding() == building) {
                        return action;
                    }
                }
            }
        }
        return null;
    }

    private static Action getActionPlayerSnapshotFinishBuilding(PlayerSnapshot playerSnapshot, Building building) {
        for (Action action : playerSnapshot.generateActions()) {
            if (action.isAllowed(playerSnapshot)) {
                if (action instanceof StartUpgradeBuildingAction) {
                    if (((StartUpgradeBuildingAction)action).getBuilding() == building) {
                        return action;
                    }
                }
            }
        }
        return null;
    }

    private static Action getActionPlayerSnapshotWaitForBuilding(PlayerSnapshot playerSnapshot, Building building) {
        for (Action action : playerSnapshot.generateActions()) {
            if (action.isAllowed(playerSnapshot)) {
                if (action instanceof WaitForBuildingAction) {
                    if (((WaitForBuildingAction)action).getBuilding() == building) {
                        return action;
                    }
                }
            }
        }
        return null;
    }

    private List<Building> calculatePossibleBuildings(PlayerSnapshot playerSnapshot) {
        //TODO this should be done differently
        return Arrays.asList(METAL_MINE, CRYSTAL_MINE, DEUTERIUM_SYNTHESIZER, METAL_STORAGE, CRYSTAL_STORAGE, DEUTERIUM_TANK, SOLAR_PLANT, ROBOTICS_FACTORY);
    }

    private PlayerSnapshot reconstructPlayerSnapshot(List<Building> buildOrder) {
        PlayerSnapshot playerSnapshot = initialPlayerSnapshot;

        int currentBuildingIndex = 0;
        while (currentBuildingIndex < buildOrder.size()) {
            Building currentBuilding = buildOrder.get(currentBuildingIndex);
            Action startBuildingAction = getActionPlayerSnapshotStartBuilding(playerSnapshot, currentBuilding);
            if (startBuildingAction != null) {
                playerSnapshot = startBuildingAction.performAction(playerSnapshot);
                currentBuildingIndex++;
            }
            else {
                Action finishBuildingAction = getActionPlayerSnapshotFinishBuilding(playerSnapshot, currentBuilding);
                if (finishBuildingAction != null) {
                    playerSnapshot = finishBuildingAction.performAction(playerSnapshot);
                }
                else {
                    Action waitForBuildingAction = getActionPlayerSnapshotWaitForBuilding(playerSnapshot, currentBuilding);
                    if (waitForBuildingAction != null) {
                        playerSnapshot = waitForBuildingAction.performAction(playerSnapshot);
                    }
                    else {
                        return null;
                    }
                }
            }
        }

        return playerSnapshot;
    }
}
