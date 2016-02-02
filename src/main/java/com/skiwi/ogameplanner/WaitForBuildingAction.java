package com.skiwi.ogameplanner;

import static com.skiwi.ogameplanner.Building.CRYSTAL_MINE;
import static com.skiwi.ogameplanner.Building.DEUTERIUM_SYNTHESIZER;
import static com.skiwi.ogameplanner.Building.METAL_MINE;
import static com.skiwi.ogameplanner.Resource.CRYSTAL;
import static com.skiwi.ogameplanner.Resource.DEUTERIUM;
import static com.skiwi.ogameplanner.Resource.METAL;

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
        return true;
    }

    @Override
    public PlayerSnapshot performAction(PlayerSnapshot playerSnapshot) {
        PlayerSnapshot newPlayerSnapshot = playerSnapshot.copyForNewAction(this);
        newPlayerSnapshot.wait(building.calculateWaitCost(playerSnapshot));
        return newPlayerSnapshot;
    }

    @Override
    public long getTimeCost(PlayerSnapshot playerSnapshot) {
        double metalHourlyProduction = METAL_MINE.getHourlyResourceProduction(playerSnapshot);
        double crystalHourlyProduction = CRYSTAL_MINE.getHourlyResourceProduction(playerSnapshot);
        double deuteriumHourlyProduction = DEUTERIUM_SYNTHESIZER.getHourlyResourceProduction(playerSnapshot);

        ActionCost upgradeCost = building.getUpgradeCost(playerSnapshot);

        double metalWaitHours = (playerSnapshot.getResourceAmount(METAL) + upgradeCost.getMetal()) / metalHourlyProduction;
        double crystalWaitHours = (playerSnapshot.getResourceAmount(CRYSTAL) + upgradeCost.getCrystal()) / crystalHourlyProduction;
        double deuteriumWaitHours = (playerSnapshot.getResourceAmount(DEUTERIUM) + upgradeCost.getDeuterium()) / deuteriumHourlyProduction;

        if (Double.isInfinite(metalWaitHours) || Double.isInfinite(crystalWaitHours) || Double.isInfinite(deuteriumWaitHours)) {
            throw new IllegalStateException("this cannot happen if getTimeCost() is called after isAllowed()");
        }
        else {
            double minimumWaitHours = 0d;
            if (upgradeCost.getMetal() > 0) {
                minimumWaitHours = Math.max(minimumWaitHours, metalWaitHours);
            }
            if (upgradeCost.getCrystal() > 0) {
                minimumWaitHours = Math.max(minimumWaitHours, crystalWaitHours);
            }
            if (upgradeCost.getDeuterium() > 0) {
                minimumWaitHours = Math.max(minimumWaitHours, deuteriumWaitHours);
            }

            return (long)Math.ceil(minimumWaitHours * 3600d);
        }
    }

    public Building getBuilding() {
        return building;
    }

    @Override
    public String toString() {
        return "WaitForBuildingAction(" + building + ")";
    }
}
