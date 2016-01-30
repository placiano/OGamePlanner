package com.skiwi.ogameplanner;

/**
 * @author Frank van Heeswijk
 */
public class WaitForBuildingAction implements Action {
    private final Building building;

    public WaitForBuildingAction(Building building) {
        this.building = building;
    }

    @Override
    public boolean isAllowed(PlayerSnapshot playerSnapshot) {
        return true;
    }

    @Override
    public PlayerSnapshot performAction(PlayerSnapshot playerSnapshot) {
        PlayerSnapshot newPlayerSnapshot = playerSnapshot.copyForNewAction(this);
        playerSnapshot.wait(building.calculateWaitCost(playerSnapshot));
        return newPlayerSnapshot;
    }

    //TODO toString that shows action description and cost
}
