package com.skiwi.ogameplanner;

/**
 * @author Frank van Heeswijk
 */
public class UpgradeBuildingAction implements Action {
    private final Building building;

    public UpgradeBuildingAction(Building building) {
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
        return playerSnapshot.satisfiesCost(actionCost);
    }

    @Override
    public PlayerSnapshot performAction(PlayerSnapshot playerSnapshot) {
        PlayerSnapshot newPlayerSnapshot = playerSnapshot.copy();
        newPlayerSnapshot.upgradeBuilding(building);
        return newPlayerSnapshot;
    }
}
