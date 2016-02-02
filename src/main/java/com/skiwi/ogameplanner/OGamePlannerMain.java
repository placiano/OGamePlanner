package com.skiwi.ogameplanner;

import java.util.EnumMap;
import java.util.function.Predicate;

import static com.skiwi.ogameplanner.Building.METAL_MINE;
import static com.skiwi.ogameplanner.Resource.CRYSTAL;
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
        goalBuildings.put(METAL_MINE, 10);
        goal.initializeBuildings(goalBuildings);

        //TODO currently it checks for metal mine level, later check for Small Cargo 1
        Predicate<PlayerSnapshot> goalPredicate = playerSnapshot -> (playerSnapshot.getBuildingLevel(METAL_MINE) == 10);

        Heuristic heuristic = new BasicHeuristic();

        Planner planner = new Planner(initialPlayerSnapshot, goal, goalPredicate, heuristic, 3);
        PlayerSnapshot earliestPlayerSnapshot = planner.plan();

        System.out.println("Time elapsed: " + earliestPlayerSnapshot.getTime() + " seconds");
        System.out.println("---");
        for (Resource resource : Resource.values()) {
            System.out.println(resource + ": " + earliestPlayerSnapshot.getResourceAmount(resource));
        }
        System.out.println("---");
        earliestPlayerSnapshot.getPerformedActions().forEach(System.out::println);
    }

    public static void main(String[] args) {
        new OGamePlannerMain().run();
    }
}
