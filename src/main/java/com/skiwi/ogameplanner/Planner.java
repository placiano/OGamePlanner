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

    private enum ImprovementType { NONE, ADD, REPOSITION, REMOVE }

    private PlayerSnapshot lookahead(int localLookahead) {
        List<Building> buildOrder = initializeBuildOrder();
        long leastTime = 0;
        try {
            leastTime = calculateTimeForBuildOrder(buildOrder);
        } catch (BuildingNotAllowedException e) {
            throw new IllegalStateException("this should not happen as build order is possible");
        }
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

                List<Building> buildOrderBackup = new ArrayList<>(buildOrder);

                for (int j = 0; j < buildings.size(); j++) {
                    Building building = buildings.get(j);
                    int position = positions.get(j);
                    buildOrder.add(position, building);
                }

                //replacement for if -1 is returned, it means it takes infinite time
                List<Building> extraBuildings = new ArrayList<>();
                List<Integer> extraPositions = new ArrayList<>();

                long time = 0;
                try {
                    time = calculateTimeForBuildOrder(buildOrder);
                } catch (BuildingNotAllowedException ex) {
                    fixBrokenBuildOrder(buildOrder, ex, extraBuildings, extraPositions);
                    try {
                        time = calculateTimeForBuildOrder(buildOrder);
                    } catch (BuildingNotAllowedException e) {
                        throw new IllegalStateException("fixed build order is still broken");
                    }
                }

                if (time < leastTime) {
                    improvementType = ImprovementType.ADD;
                    leastTime = time;
                    bestBuildings = buildings;
                    bestPositions = positions;
                    bestBuildings.addAll(extraBuildings);
                    bestPositions.addAll(extraPositions);
                }

                //should instead remove inserted buildings, leave indices intact
//                for (int j = buildings.size() - 1; j >= 0; j--) {
//                    int position = positions.get(j);
//                    buildOrder.remove(position);
//                }

                //big hack
                buildOrder = buildOrderBackup;
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
                    long time = 0;
                    try {
                        time = calculateTimeForBuildOrder(buildOrder);
                    } catch (BuildingNotAllowedException e) {
                        //we can't handle building not allowed here
                        time = -1;
                    }
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

            //check for removal
            int removePosition = -1;

            for (int position = 0; position < buildOrder.size(); position++) {
                Building removedBuilding = buildOrder.remove(position);

                //check if removal is allowed
                //TODO rewrite this to keep track of protected buildings by their indices
                //TODO support other buildings than metal mine
                int metalMineLevel = (int)buildOrder.stream().filter(building -> building == METAL_MINE).count();
                if (metalMineLevel >= goal.getBuildingLevel(METAL_MINE)) {
                    //if -1 is returned, it means it takes infinite time
                    long time = 0;
                    try {
                        time = calculateTimeForBuildOrder(buildOrder);
                    } catch (BuildingNotAllowedException e) {
                        //we can't handle building not allowed here
                        time = -1;
                    }
                    if (time > -1 && time < leastTime) {
                        improvementType = ImprovementType.REMOVE;
                        leastTime = time;
                        removePosition = position;
                    }
                }

                buildOrder.add(position, removedBuilding);
            }

            switch (improvementType) {
                case NONE:
                    //no improvement
                    try {
                        return reconstructPlayerSnapshot(buildOrder);
                    } catch (BuildingNotAllowedException e) {
                        return null;    //this should not happen
                    }
                case ADD:
                    for (int i = 0; i < bestBuildings.size(); i++) {
                        buildOrder.add(bestPositions.get(i), bestBuildings.get(i));
                    }
                    break;
                case REPOSITION:
                    Building buildingToMove = buildOrder.remove(swapPosition);
                    buildOrder.add(swapPosition + swapModifier, buildingToMove);
                    break;
                case REMOVE:
                    buildOrder.remove(removePosition);
            }
            System.out.println("Time: " + leastTime + " / Build Order: " + buildOrder);
        }
    }

    private void fixBrokenBuildOrder(List<Building> buildOrder, BuildingNotAllowedException exception, List<Building> bestExtraBuildings, List<Integer> bestExtraPositions) {
        Building blockingBuilding = exception.getBuilding();
        int blockedPosition = exception.getBlockedPosition();

        long leastTime = Long.MAX_VALUE;
        for (int position = 0; position <= blockedPosition; position++) {
            List<Building> extraBuildings = new ArrayList<>();
            List<Integer> extraPositions = new ArrayList<>();

            List<Building> buildOrderCopy = new ArrayList<>(buildOrder);
            buildOrderCopy.add(position, blockingBuilding);
            extraBuildings.add(blockingBuilding);
            extraPositions.add(position);

            long time = -1;
            while (true) {
                try {
                    time = calculateTimeForBuildOrder(buildOrderCopy);
                    break;
                } catch (BuildingNotAllowedException ex) {
                    //TODO maybe later choose what the best position would be to add this building?
                    buildOrderCopy.add(position, ex.getBuilding());
                    extraBuildings.add(ex.getBuilding());
                    extraPositions.add(position);
                }
            }

            if (time < leastTime) {
                leastTime = time;
                bestExtraBuildings.clear();
                bestExtraPositions.clear();
                bestExtraBuildings.addAll(extraBuildings);
                bestExtraPositions.addAll(extraPositions);
            }
        }

        for (int i = 0; i < bestExtraBuildings.size(); i++) {
            Building building = bestExtraBuildings.get(i);
            int position = bestExtraPositions.get(i);
            buildOrder.add(position, building);
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

    private long calculateTimeForBuildOrder(List<Building> buildOrder) throws BuildingNotAllowedException {
        PlayerSnapshot playerSnapshot = reconstructPlayerSnapshot(buildOrder);
        return (playerSnapshot == null) ? -1 : playerSnapshot.getTime();
    }

    private static Action getActionPlayerSnapshotStartBuilding(PlayerSnapshot playerSnapshot, Building building) {
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

    private static Action getActionPlayerSnapshotFinishBuilding(PlayerSnapshot playerSnapshot, Building building) {
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

    private PlayerSnapshot reconstructPlayerSnapshot(List<Building> buildOrder) throws BuildingNotAllowedException {
        PlayerSnapshot playerSnapshot = initialPlayerSnapshot;

        int currentBuildingIndex = 0;
        while (currentBuildingIndex < buildOrder.size()) {
            Building currentBuilding = buildOrder.get(currentBuildingIndex);
            Action startBuildingAction = getActionPlayerSnapshotStartBuilding(playerSnapshot, currentBuilding);
            if (startBuildingAction != null) {
                playerSnapshot = startBuildingAction.performAction(playerSnapshot);
            }
            else {
                Action finishBuildingAction = getActionPlayerSnapshotFinishBuilding(playerSnapshot, currentBuilding);
                if (finishBuildingAction != null) {
                    playerSnapshot = finishBuildingAction.performAction(playerSnapshot);
                    currentBuildingIndex++;
                }
                else {
                    Action waitForBuildingAction = getActionPlayerSnapshotWaitForBuilding(playerSnapshot, currentBuilding);
                    if (waitForBuildingAction != null) {
                        playerSnapshot = waitForBuildingAction.performAction(playerSnapshot);
                    }
                    else {
                        throw new BuildingNotAllowedException(playerSnapshot.buildingThatBlocksStartingOrWaitingForBuilding(currentBuilding), currentBuildingIndex);
                    }
                }
            }
        }

        return playerSnapshot;
    }

    private static class BuildingNotAllowedException extends Exception {
        private final Building building;
        private final int blockedPosition;

        private BuildingNotAllowedException(Building building, int blockedPosition) {
            this.building = building;
            this.blockedPosition = blockedPosition;
        }

        public Building getBuilding() {
            return building;
        }

        public int getBlockedPosition() {
            return blockedPosition;
        }
    }
}
