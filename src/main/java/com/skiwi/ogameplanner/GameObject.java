package com.skiwi.ogameplanner;

/**
 * @author Frank van Heeswijk
 */
public interface GameObject {
    Requirement[] getRequirements();

    default boolean satisfiesRequirements(PlayerSnapshot playerSnapshot) {
        for (Requirement requirement : getRequirements()) {
            if (!requirement.isSatisfied(playerSnapshot)) {
                return false;
            }
        }
        return true;
    }
}
