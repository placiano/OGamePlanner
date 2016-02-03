package com.skiwi.ogameplanner;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.skiwi.ogameplanner.Building.*;
import static com.skiwi.ogameplanner.Resource.*;

/**
 * @author Frank van Heeswijk
 */
public class PlayerSnapshot {
    private final ServerSettings serverSettings;
    private final int averagePlanetTemperature;

    private final List<Action> performedActions = new ArrayList<>();

    private long time = 0;

    private final EnumMap<Resource, Double> resources = new EnumMap<>(Resource.class);
    private final EnumMap<Building, Integer> buildings = new EnumMap<>(Building.class);
    private final EnumMap<Research, Integer> researches = new EnumMap<>(Research.class);
    private final EnumMap<Ship, Integer> ships = new EnumMap<>(Ship.class);

    private Building buildingInProgress = null;

    public PlayerSnapshot(ServerSettings serverSettings, int averagePlanetTemperature) {
        this.serverSettings = serverSettings;
        this.averagePlanetTemperature = averagePlanetTemperature;

        for (Resource resource : Resource.values()) {
            resources.put(resource, 0d);
        }

        for (Building building : Building.values()) {
            buildings.put(building, 0);
        }

        for (Research research : Research.values()) {
            researches.put(research, 0);
        }

        for (Ship ship : Ship.values()) {
            ships.put(ship, 0);
        }
    }

    public ServerSettings getServerSettings() {
        return serverSettings;
    }

    public int getAveragePlanetTemperature() {
        return averagePlanetTemperature;
    }

    public List<Action> getPerformedActions() {
        return performedActions;
    }

    public long getTime() {
        return time;
    }

    public double getResourceAmount(Resource resource) {
        return resources.getOrDefault(resource, 0d);
    }

    public int getBuildingLevel(Building building) {
        return buildings.getOrDefault(building, 0);
    }

    public int getResearchLevel(Research research) {
        return researches.getOrDefault(research, 0);
    }

    public int getShipAmount(Ship ship) {
        return ships.getOrDefault(ship, 0);
    }

    public void initializeResources(Map<Resource, Double> resources) {
        this.resources.putAll(resources);
    }

    public void initializeBuildings(Map<Building, Integer> buildings) {
        this.buildings.putAll(buildings);
    }

    public void initializeResearches(Map<Research, Integer> researches) {
        this.researches.putAll(researches);
    }

    public void initializeShips(Map<Ship, Integer> ships) {
        this.ships.putAll(ships);
    }

    public List<Action> generateActions() {
        List<Action> actions = new ArrayList<>();
        addBuildingActions(actions);
        //TODO add actions for other things too
        return actions;
    }

    private void addBuildingActions(List<Action> actions) {
        if (buildingInProgress == null) {
            buildings.forEach((building, level) -> {
                if (building.satisfiesRequirements(this)) {
                    ActionCost upgradeCost = building.getUpgradeCost(this);
                    if (satisfiesResourcesCost(upgradeCost)) {
                        actions.add(new StartUpgradeBuildingAction(building));
                    }
                    else {
                        actions.add(new WaitForBuildingAction(building));
                    }
                }
            });
        }
        else {
            //TODO generate all possible actions for that building (including DM usage)
            Action finishBuildingAction = new FinishUpgradeBuildingAction(buildingInProgress);
            if (finishBuildingAction.isAllowed(this)) {
                actions.add(finishBuildingAction);
            }
        }
    }

    public PlayerSnapshot copyForNewAction(Action performedAction) {
        PlayerSnapshot playerSnapshot = new PlayerSnapshot(serverSettings, averagePlanetTemperature);
        playerSnapshot.performedActions.addAll(performedActions);
        playerSnapshot.performedActions.add(performedAction);
        playerSnapshot.time = time; //TODO maybe related ActionCost to Action and add it at this point?
        playerSnapshot.resources.putAll(resources);
        playerSnapshot.buildings.putAll(buildings);
        playerSnapshot.researches.putAll(researches);
        playerSnapshot.ships.putAll(ships);
        return playerSnapshot;
    }

    public boolean satisfiesResourcesCost(ActionCost actionCost) {
        if ((int)Math.floor(getResourceAmount(METAL)) < actionCost.getMetal()) {
            return false;
        }
        if ((int)Math.floor(getResourceAmount(CRYSTAL)) < actionCost.getCrystal()) {
            return false;
        }
        if ((int)Math.floor(getResourceAmount(DEUTERIUM)) < actionCost.getDeuterium()) {
            return false;
        }
        if ((int)Math.floor(getResourceAmount(DARK_MATTER)) < actionCost.getDarkMatter()) {
            return false;
        }
        if (actionCost.getMetal() > METAL_STORAGE.getStorageCapacity(this)) {
            return false;
        }
        if (actionCost.getCrystal() > CRYSTAL_STORAGE.getStorageCapacity(this)) {
            return false;
        }
        if (actionCost.getDeuterium() > DEUTERIUM_TANK.getStorageCapacity(this)) {
            return false;
        }
        return true;
    }

    private void addCost(ActionCost actionCost) {
        addTimeCost(actionCost);
        addResourcesCost(actionCost);
    }

    private void addTimeCost(ActionCost actionCost) {
        time += actionCost.getTime();

        double metalProduction = METAL_MINE.getHourlyResourceProduction(this) / 3600d;
        double crystalProduction = CRYSTAL_MINE.getHourlyResourceProduction(this) / 3600d;
        double deuteriumProduction = DEUTERIUM_SYNTHESIZER.getHourlyResourceProduction(this) / 3600d;

        //TODO create better system to add resources
        resources.merge(METAL, metalProduction * actionCost.getTime(), (amount, production) -> Math.min(amount + production, METAL_STORAGE.getStorageCapacity(this)));
        resources.merge(CRYSTAL, crystalProduction * actionCost.getTime(), (amount, production) -> Math.min(amount + production, CRYSTAL_STORAGE.getStorageCapacity(this)));
        resources.merge(DEUTERIUM, deuteriumProduction * actionCost.getTime(), (amount, production) -> Math.min(amount + production, DEUTERIUM_TANK.getStorageCapacity(this)));
    }

    private void addResourcesCost(ActionCost actionCost) {
        resources.merge(METAL, actionCost.getMetal() * 1d, (amount, cost) -> amount - cost);
        resources.merge(CRYSTAL, actionCost.getCrystal() * 1d, (amount, cost) -> amount - cost);
        resources.merge(DEUTERIUM, actionCost.getDeuterium() * 1d, (amount, cost) -> amount - cost);
        resources.merge(DARK_MATTER, actionCost.getDarkMatter() * 1d, (amount, cost) -> amount - cost);
    }

    public void wait(ActionCost actionCost) {
        addTimeCost(actionCost);
    }

    public void startUpgradeBuilding(Building building) {
        addResourcesCost(building.getUpgradeCost(this));
        buildingInProgress = building;
    }

    public void finishUpgradeBuilding(Building building) {
        addTimeCost(building.getUpgradeCost(this));
        buildings.merge(building, 1, (currentLevel, newLevels) -> currentLevel + newLevels);
        buildingInProgress = null;
    }

    public boolean isCurrentlyUpgradingBuilding(Building building) {
        return (buildingInProgress == building);
    }
}
