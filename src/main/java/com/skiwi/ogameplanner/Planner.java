package com.skiwi.ogameplanner;

import java.util.ArrayDeque;
import java.util.Deque;
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
        if (successPredicate.test(initialPlayerSnapshot)) {
            return initialPlayerSnapshot;
        }

        long limit = 50;
        while (true) {
            limit *= 2;
            System.out.println("limit = " + limit);
            PlayerSnapshot earliestMatchingSnapshot = null;

            Deque<PlayerSnapshot> stack = new ArrayDeque<>();
            stack.add(initialPlayerSnapshot);
            while (!stack.isEmpty()) {
                PlayerSnapshot playerSnapshot = stack.pop();

                if (earliestMatchingSnapshot != null && playerSnapshot.getTime() >= earliestMatchingSnapshot.getTime()) {
                    continue;
                }

                if (successPredicate.test(playerSnapshot)) {
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

                if (playerSnapshot.getTime() > limit) {
                    continue;
                }

                for (Action action : playerSnapshot.generateActions()) {
                    if (action.isAllowed(playerSnapshot)) {
                        //TODO maybe we can sort on actions that take the least time?
                        stack.push(action.performAction(playerSnapshot));
                    }
                }
            }

            if (earliestMatchingSnapshot != null) {
                return earliestMatchingSnapshot;
            }
        }
    }
}
