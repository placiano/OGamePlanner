package com.skiwi.ogameplanner;

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
            int time = calculateTime(metalCost, crystalCost, playerSnapshot);
            return new ActionCost(time, metalCost, crystalCost, 0, 0);
        }
    },
    CRYSTAL_MINE {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            int metalCost = (int)Math.floor(48 * Math.pow(1.6d, currentLevel));
            int crystalCost = (int)Math.floor(24 * Math.pow(1.6d, currentLevel));
            int time = calculateTime(metalCost, crystalCost, playerSnapshot);
            return new ActionCost(time, metalCost, crystalCost, 0, 0);
        }
    },
    DEUTERIUM_SYNTHESIZER {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            int metalCost = (int)Math.floor(225 * Math.pow(1.5d, currentLevel));
            int crystalCost = (int)Math.floor(75 * Math.pow(1.5d, currentLevel));
            int time = calculateTime(metalCost, crystalCost, playerSnapshot);
            return new ActionCost(time, metalCost, crystalCost, 0, 0);
        }
    },
    SOLAR_PLANT {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            int metalCost = (int)Math.floor(75 * Math.pow(1.5d, currentLevel));
            int crystalCost = (int)Math.floor(30 * Math.pow(1.5d, currentLevel));
            int time = calculateTime(metalCost, crystalCost, playerSnapshot);
            return new ActionCost(time, metalCost, crystalCost, 0, 0);
        }
    },
    ROBOTICS_FACTORY {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            int metalCost = (int)Math.floor(400 * Math.pow(2d, currentLevel));
            int crystalCost = (int)Math.floor(120 * Math.pow(2d, currentLevel));
            int deuteriumCost = (int)Math.floor(200 * Math.pow(2d, currentLevel));
            int time = calculateTime(metalCost, crystalCost, playerSnapshot);
            return new ActionCost(time, metalCost, crystalCost, deuteriumCost, 0);
        }
    },
    SHIPYARD(new Requirement(ROBOTICS_FACTORY, 2)) {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            int metalCost = (int)Math.floor(400 * Math.pow(2d, currentLevel));
            int crystalCost = (int)Math.floor(200 * Math.pow(2d, currentLevel));
            int deuteriumCost = (int)Math.floor(100 * Math.pow(2d, currentLevel));
            int time = calculateTime(metalCost, crystalCost, playerSnapshot);
            return new ActionCost(time, metalCost, crystalCost, deuteriumCost, 0);
        }
    },
    RESEARCH_LAB {
        @Override
        public ActionCost getUpgradeCost(PlayerSnapshot playerSnapshot) {
            int currentLevel = playerSnapshot.getBuildingLevel(this);
            int metalCost = (int)Math.floor(200 * Math.pow(2d, currentLevel));
            int crystalCost = (int)Math.floor(400 * Math.pow(2d, currentLevel));
            int deuteriumCost = (int)Math.floor(200 * Math.pow(2d, currentLevel));
            int time = calculateTime(metalCost, crystalCost, playerSnapshot);
            return new ActionCost(time, metalCost, crystalCost, deuteriumCost, 0);
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

    public static int calculateTime(int metalCost, int crystalCost, PlayerSnapshot playerSnapshot) {
        int roboticsFactoryLevel = playerSnapshot.getBuildingLevel(ROBOTICS_FACTORY);
        int economySpeed = playerSnapshot.getServerSettings().getEconomySpeed();
        int naniteFactoryLevel = 0; //TODO add this
        double timeInHours = (metalCost + crystalCost) / (2500d * (1 + roboticsFactoryLevel) * economySpeed * Math.pow(2d, naniteFactoryLevel));
        return (int)Math.floor(timeInHours / 60d);
    }
}
