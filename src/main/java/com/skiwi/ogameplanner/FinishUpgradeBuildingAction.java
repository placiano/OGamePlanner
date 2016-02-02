package com.skiwi.ogameplanner;

/**
 * @author Frank van Heeswijk
 */
public class FinishUpgradeBuildingAction implements Action {
    private final Building building;

    private long performedTimeCost = -1;

    public FinishUpgradeBuildingAction(Building building) {
        this.building = building;
    }

    @Override
    public boolean isAllowed(PlayerSnapshot playerSnapshot) {
        return playerSnapshot.isCurrentlyUpgradingBuilding(building);
    }

    @Override
    public PlayerSnapshot performAction(PlayerSnapshot playerSnapshot) {
        //temp
        performedTimeCost = getTimeCost(playerSnapshot);

        PlayerSnapshot newPlayerSnapshot = playerSnapshot.copyForNewAction(this);
        newPlayerSnapshot.finishUpgradeBuilding(building);
        return newPlayerSnapshot;
    }

    public Building getBuilding() {
        return building;
    }

    @Override
    public long getTimeCost(PlayerSnapshot playerSnapshot) {
        return building.getUpgradeCost(playerSnapshot).getTime();
    }

    @Override
    public String toString() {
        return "FinishUpgradeBuildingAction(" + building + ", " + performedTimeCost + ")";
    }
}
