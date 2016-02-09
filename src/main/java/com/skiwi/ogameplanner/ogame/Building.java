package com.skiwi.ogameplanner.ogame;

import com.skiwi.ogameplanner.objects.BasicRequirement;
import com.skiwi.ogameplanner.objects.GameObject;
import com.skiwi.ogameplanner.objects.Requirement;

import static com.skiwi.ogameplanner.ogame.Research.COMPUTER_TECHNOLOGY;
import static com.skiwi.ogameplanner.ogame.Research.ENERGY_TECHNOLOGY;

/**
 * @author Frank van Heeswijk
 */
public enum Building implements GameObject {
    METAL_MINE {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[0];
        }
    },
    CYRSTAL_MINE {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[0];
        }
    },
    DEUTERIUM_SYNTHESIZER {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SOLAR_PLANT, 1)
            };
        }
    },
    SOLAR_PLANT {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[0];
        }
    },
    FUSION_REACTOR {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(DEUTERIUM_SYNTHESIZER, 5),
                new BasicRequirement(ENERGY_TECHNOLOGY, 3)
            };
        }
    },
    //TODO add solar satellite
    METAL_STORAGE {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[0];
        }
    },
    CRYSTAL_STORAGE {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[0];
        }
    },
    DEUTERIUM_TANK {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[0];
        }
    },
    ROBOTICS_FACTORY {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(DEUTERIUM_SYNTHESIZER, 1)
            };
        }
    },
    SHIPYARD {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(ROBOTICS_FACTORY, 2)
            };
        }
    },
    RESEARCH_LAB {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(DEUTERIUM_SYNTHESIZER, 1)
            };
        }
    },
    ALLIANCE_DEPOT {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[0];
        }
    },
    MISSILE_SILO {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 1)
            };
        }
    },
    NANITE_FACTORY {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(ROBOTICS_FACTORY, 10),
                new BasicRequirement(COMPUTER_TECHNOLOGY, 10)
            };
        }
    },
    TERRAFORMER {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(NANITE_FACTORY, 1),
                new BasicRequirement(ENERGY_TECHNOLOGY, 12)
            };
        }
    }
}
