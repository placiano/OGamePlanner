package com.skiwi.ogameplanner;

/**
 * @author Frank van Heeswijk
 */
public interface Heuristic {
    long calculate(PlayerSnapshot playerSnapshot, PlayerSnapshot goal);
}
