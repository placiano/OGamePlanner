package com.skiwi.ogameplanner;

import java.util.*;
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

//                SortedSet<Action> sortedActions = new TreeSet<>(Comparator.comparingLong(action -> action.getTimeCost(playerSnapshot)));
//                int gaSize = playerSnapshot.generateActions().size();
//                int removedSize = 0;
//                for (Action action : playerSnapshot.generateActions()) {
//                    if (action.isAllowed(playerSnapshot)) {
//                        System.out.println("sortedActions.size()[1] = " + sortedActions.size());
//                        sortedActions.add(action);
//                        System.out.println("sortedActions.size()[2] = " + sortedActions.size());
//                    }
//                    else {
//                        removedSize++;
//                    }
//                }
//                int saSize = sortedActions.size();
//                if (gaSize != saSize + removedSize) {
//                    throw new IllegalStateException("unequal sizes: gaSize = " + gaSize + " / saSize = " + sortedActions.size() + " / removedSize = " + removedSize);
//                }
//                for (Action action : sortedActions) {
//                    stack.push(action.performAction(playerSnapshot));
//                }

                List<Action> sortedActions = new ArrayList<>();
                for (Action action : playerSnapshot.generateActions()) {
                    if (action.isAllowed(playerSnapshot)) {
                        sortedActions.add(action);
                    }
                }
                Collections.sort(sortedActions, Comparator.comparingLong(action -> action.getTimeCost(playerSnapshot)));
                for (Action action : sortedActions) {
                    stack.push(action.performAction(playerSnapshot));
                }
            }

            if (earliestMatchingSnapshot != null) {
                return earliestMatchingSnapshot;
            }
        }
    }
}
