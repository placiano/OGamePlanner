package com.skiwi.ogameplanner;

import static com.skiwi.ogameplanner.Building.*;
import static com.skiwi.ogameplanner.Resource.CRYSTAL;
import static com.skiwi.ogameplanner.Resource.DEUTERIUM;
import static com.skiwi.ogameplanner.Resource.METAL;

/**
 * @author Frank van Heeswijk
 */
public class WaitForBuildingAction implements Action {
    private final Building building;

    private long performedTimeCost = -1;

    public WaitForBuildingAction(Building building) {
        this.building = building;
    }

    @Override
    public boolean isAllowed(PlayerSnapshot playerSnapshot) {
        double metalHourlyProduction = METAL_MINE.getHourlyResourceProduction(playerSnapshot);
        double crystalHourlyProduction = CRYSTAL_MINE.getHourlyResourceProduction(playerSnapshot);
        double deuteriumHourlyProduction = DEUTERIUM_SYNTHESIZER.getHourlyResourceProduction(playerSnapshot);

        ActionCost upgradeCost = building.getUpgradeCost(playerSnapshot);

        double metalWaitHours = (playerSnapshot.getResourceAmount(METAL) + upgradeCost.getMetal()) / metalHourlyProduction;
        double crystalWaitHours = (playerSnapshot.getResourceAmount(CRYSTAL) + upgradeCost.getCrystal()) / crystalHourlyProduction;
        double deuteriumWaitHours = (playerSnapshot.getResourceAmount(DEUTERIUM) + upgradeCost.getDeuterium()) / deuteriumHourlyProduction;

        if (Double.isInfinite(metalWaitHours) || Double.isInfinite(crystalWaitHours) || Double.isInfinite(deuteriumWaitHours)) {
            return false;
        }

        if (upgradeCost.getMetal() > METAL_STORAGE.getStorageCapacity(playerSnapshot)) {
            return false;
        }
        if (upgradeCost.getCrystal() > CRYSTAL_STORAGE.getStorageCapacity(playerSnapshot)) {
            return false;
        }
        if (upgradeCost.getDeuterium() > DEUTERIUM_TANK.getStorageCapacity(playerSnapshot)) {
            return false;
        }

        return true;
    }

    @Override
    public PlayerSnapshot performAction(PlayerSnapshot playerSnapshot) {
        //temp
        performedTimeCost = getTimeCost(playerSnapshot);

        PlayerSnapshot newPlayerSnapshot = playerSnapshot.copyForNewAction(this);
        newPlayerSnapshot.wait(building.calculateWaitCost(playerSnapshot));
        return newPlayerSnapshot;
    }

    @Override
    public long getTimeCost(PlayerSnapshot playerSnapshot) {
        return building.calculateWaitCost(playerSnapshot).getTime();
    }

    public Building getBuilding() {
        return building;
    }

    @Override
    public String toString() {
        return "WaitForBuildingAction(" + building + ", " + performedTimeCost + ")";
    }
}
