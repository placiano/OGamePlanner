package com.skiwi.ogameplanner;

/**
 * @author Frank van Heeswijk
 */
public class StartUpgradeBuildingAction implements Action {
    private final Building building;

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
        //TODO save cost here?
        ActionCost actionCost = building.getUpgradeCost(playerSnapshot);
        return playerSnapshot.satisfiesResourcesCost(actionCost);
    }

    @Override
    public PlayerSnapshot performAction(PlayerSnapshot playerSnapshot) {
        PlayerSnapshot newPlayerSnapshot = playerSnapshot.copyForNewAction(this);
        newPlayerSnapshot.startUpgradeBuilding(building);
        return newPlayerSnapshot;
    }

    //TODO toString that shows action description and cost
}
