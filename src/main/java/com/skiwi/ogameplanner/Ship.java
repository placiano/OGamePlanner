package com.skiwi.ogameplanner;

import static com.skiwi.ogameplanner.Building.RESEARCH_LAB;
import static com.skiwi.ogameplanner.Building.SHIPYARD;
import static com.skiwi.ogameplanner.Research.COMBUSTION_DRIVE;

/**
 * @author Frank van Heeswijk
 */
public enum Ship implements GameObject {
    SMALL_CARGO(new Requirement(SHIPYARD, 2), new Requirement(COMBUSTION_DRIVE, 1));

    private Requirement[] requirements;

    Ship(Requirement... requirements) {
        this.requirements = requirements;
    }

    @Override
    public Requirement[] getRequirements() {
        return requirements;
    }
}
