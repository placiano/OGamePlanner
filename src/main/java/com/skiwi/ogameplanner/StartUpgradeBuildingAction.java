package com.skiwi.ogameplanner;

/**
 * @author Frank van Heeswijk
 */
public class StartUpgradeBuildingAction implements Action {
    private final Building building;

    private long performedTimeCost = -1;

    public StartUpgradeBuildingAction(Building building) {
        this.building = building;
    }

    @Override
    public boolean isAllowed(PlayerSnapshot playerSnapshot) {
        for (Requirement requirement : building.getRequirements()) {
            if (!requirement.isSatisfied(playerSnapshot)) {
                return false;
            }
        }
        ActionCost actionCost = building.getUpgradeCost(playerSnapshot);
        return playerSnapshot.satisfiesResourcesCost(actionCost);
    }

    @Override
    public PlayerSnapshot performAction(PlayerSnapshot playerSnapshot) {
        //temp
        performedTimeCost = getTimeCost(playerSnapshot);

        PlayerSnapshot newPlayerSnapshot = playerSnapshot.copyForNewAction(this);
        newPlayerSnapshot.startUpgradeBuilding(building);
        return newPlayerSnapshot;
    }

    public Building getBuilding() {
        return building;
    }

    @Override
    public long getTimeCost(PlayerSnapshot playerSnapshot) {
        return 0;
    }

    @Override
    public String toString() {
        return "StartUpgradeBuildingAction(" + building + ", " + performedTimeCost + ")";
    }
}
