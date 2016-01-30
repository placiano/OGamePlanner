package com.skiwi.ogameplanner;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.Predicate;

/**
 * @author Frank van Heeswijk
 */
public class Planner {
    private final PlayerSnapshot initialPlayerSnapshot;
    private Predicate<PlayerSnapshot> successPredicate;

    public Planner(PlayerSnapshot initialPlayerSnapshot, Predicate<PlayerSnapshot> successPredicate) {
        this.initialPlayerSnapshot = initialPlayerSnapshot;
        this.successPredicate = successPredicate;
    }

    public PlayerSnapshot plan() {
        PlayerSnapshot earliestMatchingPlayerSnapshot = null;

        PriorityQueue<PlayerSnapshot> queue = new PriorityQueue<>(Comparator.comparingLong(PlayerSnapshot::getTime));
        queue.add(initialPlayerSnapshot);
        while (!queue.isEmpty()) {
            PlayerSnapshot playerSnapshot = queue.poll();

            if (earliestMatchingPlayerSnapshot != null && playerSnapshot.getTime() >= earliestMatchingPlayerSnapshot.getTime()) {
                continue;
            }

            for (Action action : playerSnapshot.generateActions()) {
                if (action.isAllowed(playerSnapshot)) {
                    PlayerSnapshot resultingPlayerSnapshot = action.performAction(playerSnapshot);
                    if (successPredicate.test(resultingPlayerSnapshot)) {
                        if (earliestMatchingPlayerSnapshot == null) {
                            earliestMatchingPlayerSnapshot = resultingPlayerSnapshot;
                        } else {
                            if (resultingPlayerSnapshot.getTime() < earliestMatchingPlayerSnapshot.getTime()) {
                                earliestMatchingPlayerSnapshot = resultingPlayerSnapshot;
                            }
                        }
                    } else {
                        queue.add(resultingPlayerSnapshot);
                    }
                }
            }
        }

        return earliestMatchingPlayerSnapshot;
    }
}
