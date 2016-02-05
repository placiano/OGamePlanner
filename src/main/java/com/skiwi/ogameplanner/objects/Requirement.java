package com.skiwi.ogameplanner.objects;

/**
 * @author Frank van Heeswijk
 */
public interface Requirement {
    GameObject getRequiredGameObject();

    int getRequiredLevel();
}
