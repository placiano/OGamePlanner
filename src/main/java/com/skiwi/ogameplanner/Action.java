package com.skiwi.ogameplanner;

/**
 * @author Frank van Heeswijk
 */
public interface Action {
    boolean isAllowed(PlayerSnapshot playerSnapshot);

    PlayerSnapshot performAction(PlayerSnapshot playerSnapshot);

    long getTimeCost(PlayerSnapshot playerSnapshot);
}
