package com.skiwi.ogameplanner.objects;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "BasicRequirement(" + requiredGameObject + ", " + requiredLevel + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicRequirement that = (BasicRequirement) o;
        return requiredLevel == that.requiredLevel &&
                Objects.equals(requiredGameObject, that.requiredGameObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requiredGameObject, requiredLevel);
    }
}
