package com.skiwi.ogameplanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static com.skiwi.ogameplanner.Building.CRYSTAL_MINE;
import static com.skiwi.ogameplanner.Building.METAL_MINE;
import static com.skiwi.ogameplanner.Building.SOLAR_PLANT;

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

//        System.out.println("---TEST---");
//        List<Building> testBuilderOrder = Arrays.asList(SOLAR_PLANT, SOLAR_PLANT, METAL_MINE, METAL_MINE, METAL_MINE, CRYSTAL_MINE, METAL_MINE, SOLAR_PLANT, CRYSTAL_MINE, METAL_MINE, CRYSTAL_MINE, SOLAR_PLANT, METAL_MINE, SOLAR_PLANT, METAL_MINE, SOLAR_PLANT, CRYSTAL_MINE, METAL_MINE, METAL_MINE, METAL_MINE);
//        long testTime = calculateTimeForBuildOrder(testBuilderOrder);
//        System.out.println("testTime = " + testTime);
//        List<Building> testBuildOrder2 = Arrays.asList(SOLAR_PLANT, SOLAR_PLANT, METAL_MINE, METAL_MINE, METAL_MINE, CRYSTAL_MINE, SOLAR_PLANT, METAL_MINE, CRYSTAL_MINE, SOLAR_PLANT, METAL_MINE, CRYSTAL_MINE, SOLAR_PLANT, METAL_MINE, METAL_MINE, SOLAR_PLANT, CRYSTAL_MINE, METAL_MINE, METAL_MINE, METAL_MINE);
//        long testTime2 = calculateTimeForBuildOrder(testBuildOrder2);
//        System.out.println("testTime2 = " + testTime2);
//        System.out.println("---/TEST---");

        List<Building> buildOrder = initializeBuildOrder();
        long leastTime = calculateTimeForBuildOrder(buildOrder);
        System.out.println("Lookahead: " + lookahead);
        System.out.println("Time: " + leastTime + " / Build Order: " + buildOrder);

        while (true) {
            List<Building> possibleBuildings = calculatePossibleBuildings(initialPlayerSnapshot);
            List<Building> bestBuildings = new ArrayList<>();
            List<Integer> bestPositions = new ArrayList<>();

            List<List<Building>> allBuildings = new ArrayList<>();
            List<List<Integer>> allPositions = new ArrayList<>();
            //loop over greedy-1 until greedy-n
            for (int currentLookahead = 1; currentLookahead <= lookahead; currentLookahead++) {
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

                long time = calculateTimeForBuildOrder(buildOrder);
                if (time < leastTime) {
                    leastTime = time;
                    bestBuildings = buildings;
                    bestPositions = positions;
                }

                for (int j = buildings.size() - 1; j >= 0; j--) {
                    int position = positions.get(j);
                    buildOrder.remove(position);
                }
            }

            if (bestBuildings.isEmpty()) {
                //no improvement
                return reconstructPlayerSnapshot(buildOrder);
            } else {
                for (int i = 0; i < bestBuildings.size(); i++) {
                    buildOrder.add(bestPositions.get(i), bestBuildings.get(i));
                }
                System.out.println("Time: " + leastTime + " / Build Order: " + buildOrder);
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
        //TODO expand this
        List<Building> buildOrder = new ArrayList<>();
        for (int i = 0; i < goal.getBuildingLevel(METAL_MINE); i++) {
            buildOrder.add(METAL_MINE);
        }
        return buildOrder;
    }

    private long calculateTimeForBuildOrder(List<Building> buildOrder) {
        return reconstructPlayerSnapshot(buildOrder).getTime();
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
        return Arrays.asList(METAL_MINE, CRYSTAL_MINE, SOLAR_PLANT);
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
                        throw new IllegalStateException("This should not happen");
                    }
                }
            }
        }

        return playerSnapshot;
    }
}
