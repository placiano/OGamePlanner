package com.skiwi.ogameplanner.ogame;

import com.skiwi.ogameplanner.objects.BasicRequirement;
import com.skiwi.ogameplanner.objects.GameObject;
import com.skiwi.ogameplanner.objects.Requirement;

import static com.skiwi.ogameplanner.ogame.Building.SHIPYARD;
import static com.skiwi.ogameplanner.ogame.Research.ARMOUR_TECHNOLOGY;
import static com.skiwi.ogameplanner.ogame.Research.COMBUSTION_DRIVE;
import static com.skiwi.ogameplanner.ogame.Research.ESPIONAGE_TECHNOLOGY;
import static com.skiwi.ogameplanner.ogame.Research.GRAVITON_TECHNOLOGY;
import static com.skiwi.ogameplanner.ogame.Research.HYPERSPACE_DRIVE;
import static com.skiwi.ogameplanner.ogame.Research.HYPERSPACE_TECHNOLOGY;
import static com.skiwi.ogameplanner.ogame.Research.IMPULSE_DRIVE;
import static com.skiwi.ogameplanner.ogame.Research.ION_TECHNOLOGY;
import static com.skiwi.ogameplanner.ogame.Research.LASER_TECHNOLOGY;
import static com.skiwi.ogameplanner.ogame.Research.PLASMA_TECHNOLOGY;
import static com.skiwi.ogameplanner.ogame.Research.SHIELDING_TECHNOLOGY;

/**
 * @author Frank van Heeswijk
 */
public enum Ship implements GameObject {
    LIGHT_FIGHTER {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 1),
                new BasicRequirement(COMBUSTION_DRIVE, 1)
            };
        }
    },
    HEAVY_FIGHTER {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 3),
                new BasicRequirement(ARMOUR_TECHNOLOGY, 2),
                new BasicRequirement(IMPULSE_DRIVE, 2)
            };
        }
    },
    CRUISER {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 5),
                new BasicRequirement(IMPULSE_DRIVE, 4),
                new BasicRequirement(ION_TECHNOLOGY, 2)
            };
        }
    },
    BATTLESHIP {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 7),
                new BasicRequirement(HYPERSPACE_DRIVE, 4)
            };
        }
    },
    SMALL_CARGO {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 2),
                new BasicRequirement(COMBUSTION_DRIVE, 2),
            };
        }
    },
    LARGE_CARGO {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 4),
                new BasicRequirement(COMBUSTION_DRIVE, 6),
            };
        }
    },
    COLONY_SHIP {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 4),
                new BasicRequirement(IMPULSE_DRIVE, 3)
            };
        }
    },
    BATTLECRUISER {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(LASER_TECHNOLOGY, 12),
                new BasicRequirement(HYPERSPACE_TECHNOLOGY, 5),
                new BasicRequirement(HYPERSPACE_DRIVE, 5),
                new BasicRequirement(SHIPYARD, 8)
            };
        }
    },
    BOMBER {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(IMPULSE_DRIVE, 6),
                new BasicRequirement(SHIPYARD, 8),
                new BasicRequirement(PLASMA_TECHNOLOGY, 5)
            };
        }
    },
    DESTROYER {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 9),
                new BasicRequirement(HYPERSPACE_DRIVE, 6),
                new BasicRequirement(HYPERSPACE_TECHNOLOGY, 5)
            };
        }
    },
    DEATHSTAR {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 12),
                new BasicRequirement(GRAVITON_TECHNOLOGY, 1),
                new BasicRequirement(HYPERSPACE_DRIVE, 7),
                new BasicRequirement(HYPERSPACE_TECHNOLOGY, 6)
            };
        }
    },
    RECYCLER {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 4),
                new BasicRequirement(COMBUSTION_DRIVE, 6),
                new BasicRequirement(SHIELDING_TECHNOLOGY, 2),
            };
        }
    },
    ESPIONAGE_PROBE {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 3),
                new BasicRequirement(COMBUSTION_DRIVE, 3),
                new BasicRequirement(ESPIONAGE_TECHNOLOGY, 2)
            };
        }
    }
    //TODO add solar satellite
}
