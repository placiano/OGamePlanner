package com.skiwi.ogameplanner;

import java.util.Comparator;
import java.util.PriorityQueue;
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

        //ignoring the closed list as the same state should not occur more than once

        PriorityQueue<PlayerSnapshot> openQueue = new PriorityQueue<>(Comparator.comparingLong(playerSnapshot -> playerSnapshot.calculateEstimatedTotalTimeUsingHeuristic(heuristic, goal)));
        openQueue.add(initialPlayerSnapshot);
        while (!openQueue.isEmpty()) {
            PlayerSnapshot playerSnapshot = openQueue.poll();

            if (goalPredicate.test(playerSnapshot)) {
                return playerSnapshot;
            }

            for (Action action : playerSnapshot.generateActions()) {
                if (action.isAllowed(playerSnapshot)) {
                    PlayerSnapshot successor = action.performAction(playerSnapshot);

                    if (!openQueue.contains(successor)) {
                        openQueue.add(successor);
                    }
                }
            }
        }

        return null;
    }
}
