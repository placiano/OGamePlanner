package com.skiwi.ogameplanner;

/**
 * @author Frank van Heeswijk
 */
public class FinishUpgradeBuildingAction implements Action {
    private final Building building;

    public FinishUpgradeBuildingAction(Building building) {
        this.building = building;
    }

    @Override
    public boolean isAllowed(PlayerSnapshot playerSnapshot) {
        return playerSnapshot.isCurrentlyUpgradingBuilding(building);
    }

    @Override
    public PlayerSnapshot performAction(PlayerSnapshot playerSnapshot) {
        PlayerSnapshot newPlayerSnapshot = playerSnapshot.copyForNewAction(this);
        newPlayerSnapshot.finishUpgradeBuilding(building);
        return newPlayerSnapshot;
    }

    //TODO toString that shows action description and cost
}
