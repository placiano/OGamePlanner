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
public class BasicHeuristic implements Heuristic {
    @Override
    public long calculate(PlayerSnapshot playerSnapshot, PlayerSnapshot goal) {
        //this should ALWAYS return the smallest possible time

        //get the cost of building all buildings, researches, ships, etc. in the goal state + cost of reaching wanted amount of resources
        //calculate time it costs to get those resources using production available in goal state
        //subtract building time for every building, research, ship, etc.
        //TODO later add robotics factory + nanite factory levels

        //TODO later add non-buildings
        ActionCost totalActionCost = new ActionCost(0, 0, 0, 0, 0);
        for (Building building : Building.values()) {
            totalActionCost = totalActionCost.plus(building.calculateTotalUpgradeCost(goal, playerSnapshot.getBuildingLevel(building), goal.getBuildingLevel(building)));
        }

//        System.out.println("Total time cost: " + totalActionCost.getTime());
//        System.out.println("Total metal cost: " + totalActionCost.getMetal());
//        System.out.println("Total crystal cost: " + totalActionCost.getCrystal());
//        System.out.println("Total deuterium cost: " + totalActionCost.getDeuterium());

        int currentMetal = (int)Math.floor(playerSnapshot.getResourceAmount(METAL));
        int currentCrystal = (int)Math.floor(playerSnapshot.getResourceAmount(CRYSTAL));
        int currentDeuterium = (int)Math.floor(playerSnapshot.getResourceAmount(DEUTERIUM));

        int totalMetal = totalActionCost.getMetal() - currentMetal;
        int totalCrystal = totalActionCost.getCrystal() - currentCrystal;
        int totalDeuterium = totalActionCost.getDeuterium() - currentDeuterium;
        if (totalMetal < 0) {
            totalMetal = 0;
        }
        if (totalCrystal < 0) {
            totalCrystal = 0;
        }
        if (totalDeuterium < 0) {
            totalDeuterium = 0;
        }

        totalActionCost = new ActionCost(totalActionCost.getTime(), totalMetal, totalCrystal, totalDeuterium, totalActionCost.getDeuterium());
        //TODO what about DM?

//        System.out.println("Total-RES time cost: " + totalActionCost.getTime());
//        System.out.println("Total-RES metal cost: " + totalActionCost.getMetal());
//        System.out.println("Total-RES crystal cost: " + totalActionCost.getCrystal());
//        System.out.println("Total-RES deuterium cost: " + totalActionCost.getDeuterium());

        double metalHourlyProduction = METAL_MINE.getOptimalHourlyResourceProduction(goal);
        double crystalHourlyProduction = CRYSTAL_MINE.getOptimalHourlyResourceProduction(goal);
        double deuteriumHourlyProduction = DEUTERIUM_SYNTHESIZER.getOptimalHourlyResourceProduction(goal);

//        System.out.println("Hourly metal production in goal: " + metalHourlyProduction);
//        System.out.println("Hourly crystal production in goal: " + crystalHourlyProduction);
//        System.out.println("Hourly deuterium production in goal: " + deuteriumHourlyProduction);

        double metalWaitHours = (totalActionCost.getMetal()) / metalHourlyProduction;
        double crystalWaitHours = (totalActionCost.getCrystal()) / crystalHourlyProduction;
        double deuteriumWaitHours = (totalActionCost.getDeuterium()) / deuteriumHourlyProduction;

        //TODO will break if you ask for anything that requires deuterium when you have no deuterium synthesizer available
        double minimumResourceWaitHours = Double.MAX_VALUE;
        if (totalActionCost.getMetal() > 0) {
            minimumResourceWaitHours = Math.min(minimumResourceWaitHours, metalWaitHours);
        }
        if (totalActionCost.getCrystal() > 0) {
            minimumResourceWaitHours = Math.min(minimumResourceWaitHours, crystalWaitHours);
        }
        if (totalActionCost.getDeuterium() > 0) {
            minimumResourceWaitHours = Math.min(minimumResourceWaitHours, deuteriumWaitHours);
        }

        long minimumResourceWaitTime = (long)Math.ceil(minimumResourceWaitHours * 3600d);
        long minimumBuildingWaitTime = totalActionCost.getTime();

        if (minimumResourceWaitHours == Double.MAX_VALUE) {
            return minimumBuildingWaitTime; //cannot give more reasonable estimate as we have enough resources available currently
        }

        //minimum wait time can never be less than the time it takes to build the buildings
        long minimumWaitTime = Math.max(minimumResourceWaitTime - minimumBuildingWaitTime, minimumBuildingWaitTime);

        return minimumWaitTime;
    }
}
