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

    public boolean isSatisfied(PlayerSnapshot playerSnapshot) {
        if (gameObject instanceof Building) {
            int snapshotLevel = playerSnapshot.getBuildingLevel((Building)gameObject);
            return (snapshotLevel >= level);
        }
        throw new UnsupportedOperationException();
    }
}
