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
//        PlayerSnapshot earliestMatchingPlayerSnapshot = null;

        if (successPredicate.test(initialPlayerSnapshot)) {
            return initialPlayerSnapshot;
        }

//        PriorityQueue<PlayerSnapshot> queue = new PriorityQueue<>(Comparator.comparingLong(PlayerSnapshot::getTime));
//        queue.add(initialPlayerSnapshot);
//        while (!queue.isEmpty()) {
//            PlayerSnapshot playerSnapshot = queue.poll();
//            if (earliestMatchingPlayerSnapshot != null && playerSnapshot.getTime() >= earliestMatchingPlayerSnapshot.getTime()) {
//                continue;
//            }
//
//            for (Action action : playerSnapshot.generateActions()) {
//                if (action.isAllowed(playerSnapshot)) {
//                    PlayerSnapshot resultingPlayerSnapshot = action.performAction(playerSnapshot);
//                    if (successPredicate.test(resultingPlayerSnapshot)) {
//                        if (earliestMatchingPlayerSnapshot == null) {
//                            earliestMatchingPlayerSnapshot = resultingPlayerSnapshot;
//                        } else {
//                            if (resultingPlayerSnapshot.getTime() < earliestMatchingPlayerSnapshot.getTime()) {
//                                earliestMatchingPlayerSnapshot = resultingPlayerSnapshot;
//                            }
//                        }
//                    } else {
//                        queue.add(resultingPlayerSnapshot);
//                    }
//                }
//            }
//
//        return earliestMatchingPlayerSnapshot;

        long limit = 50;
        while (true) {
            limit *= 2;
            System.out.println("limit = " + limit);
//            PlayerSnapshot earliestMatchingPlayerSnapshot = dfsPlan(initialPlayerSnapshot, limit);
//            if (earliestMatchingPlayerSnapshot != null) {
//                return earliestMatchingPlayerSnapshot;
//            }

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

//    private PlayerSnapshot dfsPlan(PlayerSnapshot playerSnapshot, long limit) {
//        if (successPredicate.test(playerSnapshot)) {
//            return playerSnapshot;
//        }
//        if (playerSnapshot.getTime() > limit) {
//            return null;
//        }
//
//        PlayerSnapshot earliestMatchingPlayerSnapshot = null;
//        for (Action action : playerSnapshot.generateActions()) {
//            PlayerSnapshot resultingPlayerSnapshot = dfsPlan(action.performAction(playerSnapshot), limit);
//            if (resultingPlayerSnapshot != null && successPredicate.test(resultingPlayerSnapshot)) {
//                if (earliestMatchingPlayerSnapshot == null) {
//                    earliestMatchingPlayerSnapshot = resultingPlayerSnapshot;
//                }
//                else {
//                    if (resultingPlayerSnapshot.getTime() < earliestMatchingPlayerSnapshot.getTime()) {
//                        earliestMatchingPlayerSnapshot = resultingPlayerSnapshot;
//                    }
//                }
//            }
//        }
//
//        return earliestMatchingPlayerSnapshot;
//    }
}
