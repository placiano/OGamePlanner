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

    public Planner(PlayerSnapshot initialPlayerSnapshot, PlayerSnapshot goal, Predicate<PlayerSnapshot> goalPredicate, Heuristic heuristic) {
        this.initialPlayerSnapshot = initialPlayerSnapshot;
        this.goal = goal;
        this.goalPredicate = goalPredicate;
        this.heuristic = heuristic;
    }

    //TODO later support other things than buildings too

    public PlayerSnapshot plan() {
        if (goalPredicate.test(initialPlayerSnapshot)) {
            return initialPlayerSnapshot;
        }

        List<Building> buildOrder = initializeBuildOrder();
        long leastTime = calculateTimeForBuildOrder(buildOrder);
        System.out.println("Time: " + leastTime + " / Build Order: " + buildOrder);

        while (true) {
            List<Building> possibleBuildings = calculatePossibleBuildings(initialPlayerSnapshot);
            Building bestBuilding = null;
            int bestPosition = -1;

            for (Building building : possibleBuildings) {
                for (int position = 0; position < buildOrder.size(); position++) {
                    buildOrder.add(position, building);
                    long time = calculateTimeForBuildOrder(buildOrder);
                    System.out.println("Time with " + building + " on between position " + position + " and " + (position + 1) + ": " + time);
                    if (time < leastTime) {
                        leastTime = time;
                        bestBuilding = building;
                        bestPosition = position;
                    }
                    buildOrder.remove(position);
                }
            }

            if (bestBuilding == null) {
                //no improvement
                return reconstructPlayerSnapshot(buildOrder);
            } else {
                buildOrder.add(bestPosition, bestBuilding);
                System.out.println("Time: " + leastTime + " / Build Order: " + buildOrder);
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
