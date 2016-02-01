package com.skiwi.ogameplanner;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Predicate;

/**
 * @author Frank van Heeswijk
 */
public class Planner {
    private final PlayerSnapshot initialPlayerSnapshot;
    private final PlayerSnapshot goal;
    private final Predicate<PlayerSnapshot> goalPredicate;
    private final Heuristic heuristic;

    public Planner(PlayerSnapshot initialPlayerSnapshot, PlayerSnapshot goal, Predicate<PlayerSnapshot> goalPredicate, Heuristic heuristic) {
        this.initialPlayerSnapshot = initialPlayerSnapshot;
        this.goal = goal;
        this.goalPredicate = goalPredicate;
        this.heuristic = heuristic;
    }

    public PlayerSnapshot plan() {
        if (goalPredicate.test(initialPlayerSnapshot)) {
            return initialPlayerSnapshot;
        }

        long limit = heuristic.calculate(initialPlayerSnapshot, goal);

        while (true) {
            System.out.println("Limit: " + limit);

            PlayerSnapshot earliestMatchingSnapshot = null;
            PlayerSnapshot earliestLeafSnapshot = null;

            Deque<PlayerSnapshot> stack = new ArrayDeque<>();
            stack.push(initialPlayerSnapshot);
            while (!stack.isEmpty()) {
                PlayerSnapshot playerSnapshot = stack.pop();

                long estimatedCost = playerSnapshot.getTime() + heuristic.calculate(playerSnapshot, goal);
                if (estimatedCost > limit) {
                    if (earliestLeafSnapshot == null || playerSnapshot.getTime() < earliestLeafSnapshot.getTime()) {
                        earliestLeafSnapshot = playerSnapshot;
                    }
                    continue;
                }

                //custom addition to IDA* as cost between to snapshots is never < 0
                if (earliestMatchingSnapshot != null && playerSnapshot.getTime() >= earliestMatchingSnapshot.getTime()) {
                    continue;
                }

                if (goalPredicate.test(playerSnapshot)) {
                    System.out.println("MATCH @ t = " + playerSnapshot.getTime());
                    if (earliestMatchingSnapshot == null) {
                        earliestMatchingSnapshot = playerSnapshot;
                    }
                    else {
                        if (playerSnapshot.getTime() < earliestMatchingSnapshot.getTime()) {
                            earliestMatchingSnapshot = playerSnapshot;
                        }
                    }
                    continue;
                }

                for (Action action : playerSnapshot.generateActions()) {
                    if (action.isAllowed(playerSnapshot)) {
                        stack.push(action.performAction(playerSnapshot));
                    }
                }
            }

            if (earliestMatchingSnapshot != null) {
                return earliestMatchingSnapshot;
            }

            //finish iteration
            limit = earliestLeafSnapshot.getTime() + heuristic.calculate(earliestLeafSnapshot, goal);
        }
    }
}
