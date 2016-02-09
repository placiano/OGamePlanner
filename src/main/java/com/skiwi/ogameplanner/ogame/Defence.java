package com.skiwi.ogameplanner.ogame;

import com.skiwi.ogameplanner.objects.BasicRequirement;
import com.skiwi.ogameplanner.objects.GameObject;
import com.skiwi.ogameplanner.objects.Requirement;

import static com.skiwi.ogameplanner.ogame.Building.MISSILE_SILO;
import static com.skiwi.ogameplanner.ogame.Building.SHIPYARD;
import static com.skiwi.ogameplanner.ogame.Research.ENERGY_TECHNOLOGY;
import static com.skiwi.ogameplanner.ogame.Research.ION_TECHNOLOGY;
import static com.skiwi.ogameplanner.ogame.Research.LASER_TECHNOLOGY;
import static com.skiwi.ogameplanner.ogame.Research.PLASMA_TECHNOLOGY;
import static com.skiwi.ogameplanner.ogame.Research.SHIELDING_TECHNOLOGY;
import static com.skiwi.ogameplanner.ogame.Research.WEAPONS_TECHNOLOGY;

/**
 * @author Frank van Heeswijk
 */
public enum Defence implements GameObject {
    ROCKET_LAUNCHER {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 1)
            };
        }
    },
    LIGHT_LASER {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 2),
                new BasicRequirement(LASER_TECHNOLOGY, 3)
            };
        }
    },
    HEAVY_LASER {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 4),
                new BasicRequirement(ENERGY_TECHNOLOGY, 3),
                new BasicRequirement(LASER_TECHNOLOGY, 6)
            };
        }
    },
    GAUSS_CANNON {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 6),
                new BasicRequirement(WEAPONS_TECHNOLOGY, 3),
                new BasicRequirement(ENERGY_TECHNOLOGY, 6),
                new BasicRequirement(SHIELDING_TECHNOLOGY, 1)
            };
        }
    },
    ION_CANNON {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 4),
                new BasicRequirement(ION_TECHNOLOGY, 4)
            };
        }
    },
    PLASMA_TURRET {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIPYARD, 8),
                new BasicRequirement(PLASMA_TECHNOLOGY, 7)
            };
        }
    },
    SMALL_SHIELD_DOME {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIELDING_TECHNOLOGY, 2),
                new BasicRequirement(SHIPYARD, 1)
            };
        }
    },
    LARGE_SHIELD_DOME {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(SHIELDING_TECHNOLOGY, 6),
                new BasicRequirement(SHIPYARD, 6)
            };
        }
    },
    ANTI_BALLISTIC_MISSILES {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(MISSILE_SILO, 2)
            };
        }
    },
    INTERPLANETARY_MISSILES {
        @Override
        public Requirement[] getRequirements() {
            return new Requirement[] {
                new BasicRequirement(MISSILE_SILO, 4),
                new BasicRequirement(ENERGY_TECHNOLOGY, 1)
            };
        }
    }
}
