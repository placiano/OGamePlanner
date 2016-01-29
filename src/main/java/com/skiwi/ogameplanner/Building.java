package com.skiwi.ogameplanner;

/**
 * @author Frank van Heeswijk
 */
public enum Building implements GameObject {
    METAL_MINE,
    CRYSTAL_MINE,
    DEUTERIUM_SYNTHESIZER,
    SOLAR_PLANT,
    ROBOTICS_FACTORY,
    SHIPYARD(new Requirement(ROBOTICS_FACTORY, 2)),
    RESEARCH_LAB;

    private Requirement[] requirements;

    Building(Requirement... requirements) {
        this.requirements = requirements;
    }

    @Override
    public Requirement[] getRequirements() {
        return requirements;
    }
}
