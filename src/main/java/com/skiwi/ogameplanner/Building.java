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
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot, int startLevel) {
            int metalCost = (int)Math.floor(60 * Math.pow(1.5d, startLevel));
            int crystalCost = (int)Math.floor(15 * Math.pow(1.5d, startLevel));
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
            return (30d + (30d * metalMineLevel * Math.pow(1.1d, metalMineLevel) * calculateEnergyModifier(playerSnapshot))) * economySpeed;
        }

        @Override
        public double getOptimalHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            int metalMineLevel = playerSnapshot.getBuildingLevel(METAL_MINE);
            int economySpeed = playerSnapshot.getServerSettings().getEconomySpeed();
            return (30d + (30d * metalMineLevel * Math.pow(1.1d, metalMineLevel))) * economySpeed;
        }

        @Override
        public long getStorageCapacity(PlayerSnapshot playerSnapshot) {
            return 0;
        }
    },
    CRYSTAL_MINE {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot, int startLevel) {
            int metalCost = (int)Math.floor(48 * Math.pow(1.6d, startLevel));
            int crystalCost = (int)Math.floor(24 * Math.pow(1.6d, startLevel));
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
            return (15d + (20d * crystalMineLevel * Math.pow(1.1d, crystalMineLevel) * calculateEnergyModifier(playerSnapshot))) * economySpeed;
        }

        @Override
        public double getOptimalHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            int crystalMineLevel = playerSnapshot.getBuildingLevel(CRYSTAL_MINE);
            int economySpeed = playerSnapshot.getServerSettings().getEconomySpeed();
            return (15d + (20d * crystalMineLevel * Math.pow(1.1d, crystalMineLevel))) * economySpeed;
        }

        @Override
        public long getStorageCapacity(PlayerSnapshot playerSnapshot) {
            return 0;
        }
    },
    DEUTERIUM_SYNTHESIZER {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot, int startLevel) {
            int metalCost = (int)Math.floor(225 * Math.pow(1.5d, startLevel));
            int crystalCost = (int)Math.floor(75 * Math.pow(1.5d, startLevel));
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
            int deuteriumSynthesizerLevel = playerSnapshot.getBuildingLevel(DEUTERIUM_SYNTHESIZER);
            int economySpeed = playerSnapshot.getServerSettings().getEconomySpeed();
            int averagePlanetTemperature = playerSnapshot.getAveragePlanetTemperature();
            double planetTemperatureModifier = (-0.004d * averagePlanetTemperature) + 1.36d;
            return (10d * deuteriumSynthesizerLevel * Math.pow(1.1d, deuteriumSynthesizerLevel) * planetTemperatureModifier * calculateEnergyModifier(playerSnapshot)) * economySpeed;
        }

        @Override
        public double getOptimalHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            int deuteriumSynthesizerLevel = playerSnapshot.getBuildingLevel(DEUTERIUM_SYNTHESIZER);
            int economySpeed = playerSnapshot.getServerSettings().getEconomySpeed();
            int averagePlanetTemperature = playerSnapshot.getAveragePlanetTemperature();
            double planetTemperatureModifier = (-0.004d * averagePlanetTemperature) + 1.36d;
            return (10d * deuteriumSynthesizerLevel * Math.pow(1.1d, deuteriumSynthesizerLevel) * planetTemperatureModifier) * economySpeed;
        }

        @Override
        public long getStorageCapacity(PlayerSnapshot playerSnapshot) {
            return 0;
        }
    },
    SOLAR_PLANT {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot, int startLevel) {
            int metalCost = (int)Math.floor(75 * Math.pow(1.5d, startLevel));
            int crystalCost = (int)Math.floor(30 * Math.pow(1.5d, startLevel));
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

        @Override
        public double getOptimalHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            return 0d;
        }

        @Override
        public long getStorageCapacity(PlayerSnapshot playerSnapshot) {
            return 0;
        }
    },
    METAL_STORAGE {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot, int startLevel) {
            int metalCost = (int)Math.floor(1000 * Math.pow(2, startLevel));
            long time = calculateTime(metalCost, 0, playerSnapshot);
            return new ActionCost(time, metalCost, 0, 0, 0);
        }

        @Override
        public int getEnergyCost(PlayerSnapshot playerSnapshot) {
            return 0;
        }

        @Override
        public double getHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            return 0;
        }

        @Override
        public double getOptimalHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            return 0;
        }

        @Override
        public long getStorageCapacity(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            return (long)(5000L * Math.floor(2.5d * Math.exp((currentLevel * 20d) / 33d)));
        }
    },
    CRYSTAL_STORAGE {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot, int startLevel) {
            int metalCost = (int)Math.floor(1000 * Math.pow(2, startLevel));
            int crystalCost = (int)Math.floor(500 * Math.pow(2, startLevel));
            long time = calculateTime(metalCost, crystalCost, playerSnapshot);
            return new ActionCost(time, metalCost, crystalCost, 0, 0);
        }

        @Override
        public int getEnergyCost(PlayerSnapshot playerSnapshot) {
            return 0;
        }

        @Override
        public double getHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            return 0;
        }

        @Override
        public double getOptimalHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            return 0;
        }

        @Override
        public long getStorageCapacity(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            return (long)(5000L * Math.floor(2.5d * Math.exp((currentLevel * 20d) / 33d)));
        }
    },
    DEUTERIUM_TANK {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot, int startLevel) {
            int metalCost = (int)Math.floor(1000 * Math.pow(2, startLevel));
            int crystalCost = (int)Math.floor(1000 * Math.pow(2, startLevel));
            long time = calculateTime(metalCost, crystalCost, playerSnapshot);
            return new ActionCost(time, metalCost, crystalCost, 0, 0);
        }

        @Override
        public int getEnergyCost(PlayerSnapshot playerSnapshot) {
            return 0;
        }

        @Override
        public double getHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            return 0;
        }

        @Override
        public double getOptimalHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            return 0;
        }

        @Override
        public long getStorageCapacity(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            return (long)(5000L * Math.floor(2.5d * Math.exp((currentLevel * 20d) / 33d)));
        }
    },
    ROBOTICS_FACTORY {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot, int startLevel) {
            int metalCost = (int)Math.floor(400 * Math.pow(2d, startLevel));
            int crystalCost = (int)Math.floor(120 * Math.pow(2d, startLevel));
            int deuteriumCost = (int)Math.floor(200 * Math.pow(2d, startLevel));
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

        @Override
        public double getOptimalHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            return 0d;
        }

        @Override
        public long getStorageCapacity(PlayerSnapshot playerSnapshot) {
            return 0;
        }
    },
    SHIPYARD(new Requirement(ROBOTICS_FACTORY, 2)) {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot, int startLevel) {
            int metalCost = (int)Math.floor(400 * Math.pow(2d, startLevel));
            int crystalCost = (int)Math.floor(200 * Math.pow(2d, startLevel));
            int deuteriumCost = (int)Math.floor(100 * Math.pow(2d, startLevel));
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

        @Override
        public double getOptimalHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            return 0d;
        }

        @Override
        public long getStorageCapacity(PlayerSnapshot playerSnapshot) {
            return 0;
        }
    },
    RESEARCH_LAB {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot, int startLevel) {
            int metalCost = (int)Math.floor(200 * Math.pow(2d, startLevel));
            int crystalCost = (int)Math.floor(400 * Math.pow(2d, startLevel));
            int deuteriumCost = (int)Math.floor(200 * Math.pow(2d, startLevel));
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

        @Override
        public double getOptimalHourlyResourceProduction(PlayerSnapshot playerSnapshot) {
            return 0d;
        }

        @Override
        public long getStorageCapacity(PlayerSnapshot playerSnapshot) {
            return 0;
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

    public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot) {
        return getUpgradeCost(playerSnapshot, playerSnapshot.getBuildingLevel(this));
    }

    public abstract ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot, int startLevel);

    public abstract int getEnergyCost(PlayerSnapshot playerSnapshot);

    public abstract double getHourlyResourceProduction(PlayerSnapshot playerSnapshot);

    public abstract double getOptimalHourlyResourceProduction(PlayerSnapshot playerSnapshot);

    public abstract long getStorageCapacity(PlayerSnapshot playerSnapshot);

    public ActionCost calculateWaitCost(PlayerSnapshot playerSnapshot) {
        ActionCost upgradeCost = getUpgradeCost(playerSnapshot);

        double metalWaitHours = (upgradeCost.getMetal() - playerSnapshot.getResourceAmount(METAL)) / METAL_MINE.getHourlyResourceProduction(playerSnapshot);
        double crystalWaitHours = (upgradeCost.getCrystal() - playerSnapshot.getResourceAmount(CRYSTAL)) / CRYSTAL_MINE.getHourlyResourceProduction(playerSnapshot);
        double deuteriumWaitHours = (upgradeCost.getDeuterium() - playerSnapshot.getResourceAmount(DEUTERIUM)) / DEUTERIUM_SYNTHESIZER.getHourlyResourceProduction(playerSnapshot);

        double minimumWaitHours = 0d;
        if (upgradeCost.getMetal() > 0) {
            minimumWaitHours = Math.max(minimumWaitHours, metalWaitHours);
        }
        if (upgradeCost.getCrystal() > 0) {
            minimumWaitHours = Math.max(minimumWaitHours, crystalWaitHours);
        }
        if (upgradeCost.getDeuterium() > 0) {
            minimumWaitHours = Math.max(minimumWaitHours, deuteriumWaitHours);
        }

        long minimumWaitSeconds = (long)Math.ceil(minimumWaitHours * 3600d);
        return new ActionCost(minimumWaitSeconds, 0, 0, 0, 0);
    }

    public ActionCost calculateTotalUpgradeCost(PlayerSnapshot playerSnapshot, int startLevel, int endLevel) {
        ActionCost totalActionCost = new ActionCost(0, 0, 0, 0, 0);
        for (int currentLevel = startLevel; currentLevel < endLevel; currentLevel++) {
            totalActionCost = totalActionCost.plus(this.getUpgradeCost(playerSnapshot, currentLevel));
        }
        return totalActionCost;
    }

    private static double calculateEnergyModifier(PlayerSnapshot playerSnapshot) {
        int metalEnergyCost = METAL_MINE.getEnergyCost(playerSnapshot);
        int crystalEnergyCost = CRYSTAL_MINE.getEnergyCost(playerSnapshot);
        int deuteriumEnergyCost = DEUTERIUM_SYNTHESIZER.getEnergyCost(playerSnapshot);
        int solarPlantEnergyCost = SOLAR_PLANT.getEnergyCost(playerSnapshot);
        if (solarPlantEnergyCost == 0) {
            return 0d;
        }
        else {
            double energyModifier = (-solarPlantEnergyCost * 1d) / (metalEnergyCost + crystalEnergyCost + deuteriumEnergyCost);
            return Math.min(Math.max(energyModifier, 0d), 1d);
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
