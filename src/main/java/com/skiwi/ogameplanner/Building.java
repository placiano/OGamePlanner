package com.skiwi.ogameplanner;

import static com.skiwi.ogameplanner.Resource.CRYSTAL;
import static com.skiwi.ogameplanner.Resource.DEUTERIUM;
import static com.skiwi.ogameplanner.Resource.METAL;

/**
 * @author Frank van Heeswijk
 */
public enum Building implements GameObject {
    METAL_MINE {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            int metalCost = (int)Math.floor(60 * Math.pow(1.5d, currentLevel));
            int crystalCost = (int)Math.floor(15 * Math.pow(1.5d, currentLevel));
            long time = calculateTime(metalCost, crystalCost, playerSnapshot);
            return new ActionCost(time, metalCost, crystalCost, 0, 0);
        }

        @Override
        public int getEnergyCost(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            return (int)Math.ceil(10 * currentLevel * Math.pow(1.1d, currentLevel));
        }

        @Override
        public double getHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            int metalMineLevel = playerSnapshot.getBuildingLevel(METAL_MINE);
            int economySpeed = playerSnapshot.getServerSettings().getEconomySpeed();
            return (30d + 30d * metalMineLevel * Math.pow(1.1d, metalMineLevel)) * economySpeed * calculateEnergyModifier(playerSnapshot);
        }
    },
    CRYSTAL_MINE {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            int metalCost = (int)Math.floor(48 * Math.pow(1.6d, currentLevel));
            int crystalCost = (int)Math.floor(24 * Math.pow(1.6d, currentLevel));
            long time = calculateTime(metalCost, crystalCost, playerSnapshot);
            return new ActionCost(time, metalCost, crystalCost, 0, 0);
        }

        @Override
        public int getEnergyCost(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            return (int)Math.ceil(10 * currentLevel * Math.pow(1.1d, currentLevel));
        }

        @Override
        public double getHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            int crystalMineLevel = playerSnapshot.getBuildingLevel(CRYSTAL_MINE);
            int economySpeed = playerSnapshot.getServerSettings().getEconomySpeed();
            return (15d + 20d * crystalMineLevel * Math.pow(1.1d, crystalMineLevel)) * economySpeed * calculateEnergyModifier(playerSnapshot);
        }
    },
    DEUTERIUM_SYNTHESIZER {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            int metalCost = (int)Math.floor(225 * Math.pow(1.5d, currentLevel));
            int crystalCost = (int)Math.floor(75 * Math.pow(1.5d, currentLevel));
            long time = calculateTime(metalCost, crystalCost, playerSnapshot);
            return new ActionCost(time, metalCost, crystalCost, 0, 0);
        }

        @Override
        public int getEnergyCost(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            return (int)Math.ceil(20 * currentLevel * Math.pow(1.1d, currentLevel));
        }

        @Override
        public double getHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            return 0d;  //TODO implement this later, depends on planet temperature too
        }
    },
    SOLAR_PLANT {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            int metalCost = (int)Math.floor(75 * Math.pow(1.5d, currentLevel));
            int crystalCost = (int)Math.floor(30 * Math.pow(1.5d, currentLevel));
            long time = calculateTime(metalCost, crystalCost, playerSnapshot);
            return new ActionCost(time, metalCost, crystalCost, 0, 0);
        }

        @Override
        public int getEnergyCost(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            return -(int)Math.ceil(20 * currentLevel * Math.pow(1.1d, currentLevel));
        }

        @Override
        public double getHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            return 0d;
        }
    },
    ROBOTICS_FACTORY {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            int metalCost = (int)Math.floor(400 * Math.pow(2d, currentLevel));
            int crystalCost = (int)Math.floor(120 * Math.pow(2d, currentLevel));
            int deuteriumCost = (int)Math.floor(200 * Math.pow(2d, currentLevel));
            long time = calculateTime(metalCost, crystalCost, playerSnapshot);
            return new ActionCost(time, metalCost, crystalCost, deuteriumCost, 0);
        }

        @Override
        public int getEnergyCost(PlayerSnapshot playerSnapshot) {
            return 0;
        }

        @Override
        public double getHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            return 0d;
        }
    },
    SHIPYARD(new Requirement(ROBOTICS_FACTORY, 2)) {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            int metalCost = (int)Math.floor(400 * Math.pow(2d, currentLevel));
            int crystalCost = (int)Math.floor(200 * Math.pow(2d, currentLevel));
            int deuteriumCost = (int)Math.floor(100 * Math.pow(2d, currentLevel));
            long time = calculateTime(metalCost, crystalCost, playerSnapshot);
            return new ActionCost(time, metalCost, crystalCost, deuteriumCost, 0);
        }

        @Override
        public int getEnergyCost(PlayerSnapshot playerSnapshot) {
            return 0;
        }

        @Override
        public double getHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            return 0d;
        }
    },
    RESEARCH_LAB {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            int metalCost = (int)Math.floor(200 * Math.pow(2d, currentLevel));
            int crystalCost = (int)Math.floor(400 * Math.pow(2d, currentLevel));
            int deuteriumCost = (int)Math.floor(200 * Math.pow(2d, currentLevel));
            long time = calculateTime(metalCost, crystalCost, playerSnapshot);
            return new ActionCost(time, metalCost, crystalCost, deuteriumCost, 0);
        }

        @Override
        public int getEnergyCost(PlayerSnapshot playerSnapshot) {
            return 0;
        }

        @Override
        public double getHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            return 0d;
        }
    };

    private Requirement[] requirements;

    Building(Requirement... requirements) {
        this.requirements = requirements;
    }

    @Override
    public Requirement[] getRequirements() {
        return requirements;
    }

    public abstract ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot);

    public abstract int getEnergyCost(PlayerSnapshot playerSnapshot);

    public abstract double getHourlyResourceProduction(PlayerSnapshot playerSnapshot);

    public ActionCost calculateWaitCost(PlayerSnapshot playerSnapshot) {
        ActionCost upgradeCost = getUpgradeCost(playerSnapshot);
        double metalWaitHours = (playerSnapshot.getResourceAmount(METAL) + upgradeCost.getMetal()) / METAL_MINE.getHourlyResourceProduction(playerSnapshot);
        double crystalWaitHours = (playerSnapshot.getResourceAmount(CRYSTAL) + upgradeCost.getCrystal()) / CRYSTAL_MINE.getHourlyResourceProduction(playerSnapshot);
        double deuteriumWaitHours = (playerSnapshot.getResourceAmount(DEUTERIUM) + upgradeCost.getDeuterium()) / DEUTERIUM_SYNTHESIZER.getHourlyResourceProduction(playerSnapshot);
        double minimumWaitHours = Math.max(metalWaitHours, Math.max(crystalWaitHours, deuteriumWaitHours));
        long minimumWaitSeconds = (long)Math.ceil(minimumWaitHours / 3600d);
        return new ActionCost(minimumWaitSeconds, 0, 0, 0, 0);
    }

    private static double calculateEnergyModifier(PlayerSnapshot playerSnapshot) {
        int metalEnergyCost = METAL_MINE.getEnergyCost(playerSnapshot);
        int crystalEnergyCost = CRYSTAL_MINE.getEnergyCost(playerSnapshot);
        int deuteriumEnergyCost = DEUTERIUM_SYNTHESIZER.getEnergyCost(playerSnapshot);
        int solarPlantEnergyCost = SOLAR_PLANT.getEnergyCost(playerSnapshot);
        if (solarPlantEnergyCost == 0) {
            return 1d;
        }
        else {
            double energyModifier = (metalEnergyCost + crystalEnergyCost + deuteriumEnergyCost) / (-solarPlantEnergyCost * 1d);
            return Math.min(0d, Math.max(energyModifier, 1d));
        }
    }

    public static long calculateTime(int metalCost, int crystalCost, PlayerSnapshot playerSnapshot) {
        int roboticsFactoryLevel = playerSnapshot.getBuildingLevel(ROBOTICS_FACTORY);
        int economySpeed = playerSnapshot.getServerSettings().getEconomySpeed();
        int naniteFactoryLevel = 0; //TODO add this
        double timeInHours = (metalCost + crystalCost) / (2500d * (1 + roboticsFactoryLevel) * economySpeed * Math.pow(2d, naniteFactoryLevel));
        return (long)Math.ceil(timeInHours * 3600d);
    }
}
