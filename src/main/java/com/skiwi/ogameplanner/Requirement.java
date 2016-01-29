package com.skiwi.ogameplanner;

/**
 * @author Frank van Heeswijk
 */
public class Requirement {
    private final GameObject gameObject;
    private final int level;

    public Requirement(GameObject gameObject, int level) {
        this.gameObject = gameObject;
        this.level = level;
    }

    public boolean isAllowed(int checkLevel) {
        return (checkLevel >= level);
    }
}
