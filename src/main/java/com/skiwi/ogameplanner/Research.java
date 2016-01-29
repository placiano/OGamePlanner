package com.skiwi.ogameplanner;

import static com.skiwi.ogameplanner.Building.RESEARCH_LAB;

/**
 * @author Frank van Heeswijk
 */
public enum Research implements GameObject {
    ENERGY_TECHNOLOGY(new Requirement(RESEARCH_LAB, 1)),
    COMBUSTION_DRIVE(new Requirement(ENERGY_TECHNOLOGY, 1));

    private Requirement[] requirements;

    Research(Requirement... requirements) {
        this.requirements = requirements;
    }

    @Override
    public Requirement[] getRequirements() {
        return requirements;
    }
}
