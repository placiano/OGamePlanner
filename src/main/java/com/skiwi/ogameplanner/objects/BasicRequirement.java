package com.skiwi.ogameplanner.objects;

/**
 * @author Frank van Heeswijk
 */
public class BasicRequirement implements Requirement {
    private final GameObject requiredGameObject;
    private final int requiredLevel;

    public BasicRequirement(GameObject requiredGameObject, int requiredLevel) {
        this.requiredGameObject = requiredGameObject;
        this.requiredLevel = requiredLevel;
    }

    @Override
    public GameObject getRequiredGameObject() {
        return requiredGameObject;
    }

    @Override
    public int getRequiredLevel() {
        return requiredLevel;
    }
}
