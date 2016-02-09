package com.skiwi.ogameplanner.ogame;

import com.skiwi.ogameplanner.objects.BasicRequirement;
import com.skiwi.ogameplanner.objects.GameObject;
import com.skiwi.ogameplanner.objects.Requirement;

import static com.skiwi.ogameplanner.ogame.Building.RESEARCH_LAB;

/**
 * @author Frank van Heeswijk
 */
public enum Research implements GameObject {
    ENERGY_TECHNOLOGY {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(RESEARCH_LAB, 1)
            };
        }
    },
    LASER_TECHNOLOGY {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(ENERGY_TECHNOLOGY, 2)
            };
        }
    },
    ION_TECHNOLOGY {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(LASER_TECHNOLOGY, 5),
                new BasicRequirement(RESEARCH_LAB, 4),
                new BasicRequirement(ENERGY_TECHNOLOGY, 4)
            };
        }
    },
    HYPERSPACE_TECHNOLOGY {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(ENERGY_TECHNOLOGY, 5),
                new BasicRequirement(SHIELDING_TECHNOLOGY, 5),
                new BasicRequirement(RESEARCH_LAB, 7)
            };
        }
    },
    PLASMA_TECHNOLOGY {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(LASER_TECHNOLOGY, 10),
                new BasicRequirement(ION_TECHNOLOGY, 5),
                new BasicRequirement(ENERGY_TECHNOLOGY, 8)
            };
        }
    },
    COMBUSTION_DRIVE {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(ENERGY_TECHNOLOGY, 1)
            };
        }
    },
    IMPULSE_DRIVE {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(ENERGY_TECHNOLOGY, 1),
                new BasicRequirement(RESEARCH_LAB, 2)
            };
        }
    },
    HYPERSPACE_DRIVE {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(HYPERSPACE_TECHNOLOGY, 3)
            };
        }
    },
    ESPIONAGE_TECHNOLOGY {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(RESEARCH_LAB, 3)
            };
        }
    },
    COMPUTER_TECHNOLOGY {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(RESEARCH_LAB, 1)
            };
        }
    },
    ASTROPHYSICS {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[]{
                new BasicRequirement(ESPIONAGE_TECHNOLOGY, 4),
                new BasicRequirement(IMPULSE_DRIVE, 3)
            };
        }
    },
    INTERGALACTIC_RESEARCH_NETWORK {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(COMPUTER_TECHNOLOGY, 8),
                new BasicRequirement(HYPERSPACE_TECHNOLOGY, 8),
                new BasicRequirement(RESEARCH_LAB, 10)
            };
        }
    },
    GRAVITON_TECHNOLOGY {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(RESEARCH_LAB, 12)
            };
        }
    },
    WEAPONS_TECHNOLOGY {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(RESEARCH_LAB, 4)
            };
        }
    },
    SHIELDING_TECHNOLOGY {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(ENERGY_TECHNOLOGY, 3),
                new BasicRequirement(RESEARCH_LAB, 6)
            };
        }
    },
    ARMOUR_TECHNOLOGY {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(RESEARCH_LAB, 2)
            };
        }
    }
}
