package com.skiwi.ogameplanner;

import java.util.EnumMap;
import java.util.function.Predicate;

import static com.skiwi.ogameplanner.Building.METAL_MINE;
import static com.skiwi.ogameplanner.Resource.CRYSTAL;
import static com.skiwi.ogameplanner.Resource.DEUTERIUM;
import static com.skiwi.ogameplanner.Resource.METAL;

/**
 * @author Frank van Heeswijk
 */
public class OGamePlannerMain {
    public void run() {
        ServerSettings serverSettings = new ServerSettings(1, 1);
        PlayerSnapshot initialPlayerSnapshot = new PlayerSnapshot(serverSettings);

        EnumMap<Resource, Double> initialResources = new EnumMap<>(Resource.class);
        initialResources.put(METAL, 500d);
        initialResources.put(CRYSTAL, 500d);
        initialPlayerSnapshot.initializeResources(initialResources);

        PlayerSnapshot goal = new PlayerSnapshot(serverSettings);

        EnumMap<Building, Integer> goalBuildings = new EnumMap<>(Building.class);
        goalBuildings.put(METAL_MINE, 6);
        goal.initializeBuildings(goalBuildings);

        //TODO currently it checks for metal mine level, later check for Small Cargo 1
        Predicate<PlayerSnapshot> goalPredicate = playerSnapshot -> (playerSnapshot.getBuildingLevel(METAL_MINE) == 6);

        Heuristic heuristic = new BasicHeuristic();

        Planner planner = new Planner(initialPlayerSnapshot, goal, goalPredicate, heuristic);
        PlayerSnapshot earliestPlayerSnapshot = planner.plan();

        System.out.println("Time elapsed: " + earliestPlayerSnapshot.getTime() + " seconds");
        System.out.println("---");
        for (Resource resource : Resource.values()) {
            System.out.println(resource + ": " + earliestPlayerSnapshot.getResourceAmount(resource));
        }
        System.out.println("---");
        earliestPlayerSnapshot.getPerformedActions().forEach(System.out::println);

//        System.out.println();
//        System.out.println();
//        System.out.println();
//
//        PlayerSnapshot ps1 = initialPlayerSnapshot;
//        System.out.println("ps1.generateActions() = " + ps1.generateActions());
//        System.out.println("ps1.getResourceAmount(METAL) = " + ps1.getResourceAmount(METAL));
//        System.out.println("ps1.getResourceAmount(CRYSTAL) = " + ps1.getResourceAmount(CRYSTAL));
//        System.out.println("ps1.getResourceAmount(DEUTERIUM) = " + ps1.getResourceAmount(DEUTERIUM));
//
//        System.out.println();
//
//        PlayerSnapshot ps2 = ps1.generateActions().get(0).performAction(ps1);
//        System.out.println("ps2.generateActions() = " + ps2.generateActions());
//        System.out.println("ps2.getResourceAmount(METAL) = " + ps2.getResourceAmount(METAL));
//        System.out.println("ps2.getResourceAmount(CRYSTAL) = " + ps2.getResourceAmount(CRYSTAL));
//        System.out.println("ps2.getResourceAmount(DEUTERIUM) = " + ps2.getResourceAmount(DEUTERIUM));
//
//        System.out.println();
//
//        PlayerSnapshot ps3 = ps2.generateActions().get(0).performAction(ps2);
//        System.out.println("ps2.generateActions() = " + ps3.generateActions());
//        System.out.println("ps2.getResourceAmount(METAL) = " + ps3.getResourceAmount(METAL));
//        System.out.println("ps2.getResourceAmount(CRYSTAL) = " + ps3.getResourceAmount(CRYSTAL));
//        System.out.println("ps2.getResourceAmount(DEUTERIUM) = " + ps3.getResourceAmount(DEUTERIUM));
    }

    public static void main(String[] args) {
        new OGamePlannerMain().run();
    }
}
