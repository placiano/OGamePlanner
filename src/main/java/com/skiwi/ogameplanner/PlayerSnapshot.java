package com.skiwi.ogameplanner;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static com.skiwi.ogameplanner.Building.*;
import static com.skiwi.ogameplanner.Resource.*;

/**
 * @author Frank van Heeswijk
 */
public class PlayerSnapshot {
    private final ServerSettings serverSettings;

    private final List<Action> actions = new ArrayList<>();

    private int time = 0;

    private final EnumMap<Resource, Double> resources = new EnumMap<>(Resource.class);
    private final EnumMap<Building, Integer> buildings = new EnumMap<>(Building.class);
    private final EnumMap<Research, Integer> researches = new EnumMap<>(Research.class);
    private final EnumMap<Ship, Integer> ships = new EnumMap<>(Ship.class);

    private Building buildingInProgress = null;

    public PlayerSnapshot(ServerSettings serverSettings) {
        this.serverSettings = serverSettings;
    }

    public ServerSettings getServerSettings() {
        return serverSettings;
    }

    public int getTime() {
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

    public List<Action> generateActions() {
        List<Action> actions = new ArrayList<>();
        addBuildingActions(actions);
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

    public PlayerSnapshot copyForNewAction(Action action) {
        PlayerSnapshot playerSnapshot = new PlayerSnapshot(serverSettings);
        playerSnapshot.actions.add(action);
        playerSnapshot.time = time;
        playerSnapshot.resources.putAll(resources);
        playerSnapshot.buildings.putAll(buildings);
        playerSnapshot.researches.putAll(researches);
        playerSnapshot.ships.putAll(ships);
        return playerSnapshot;
    }

    public boolean satisfiesResourcesCost(ActionCost actionCost) {
        if (getResourceAmount(METAL) < actionCost.getMetal()) {
            return false;
        }
        if (getResourceAmount(CRYSTAL) < actionCost.getCrystal()) {
            return false;
        }
        if (getResourceAmount(DEUTERIUM) < actionCost.getDeuterium()) {
            return false;
        }
        if (getResourceAmount(DARK_MATTER) < actionCost.getDarkMatter()) {
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

        double metalProduction = METAL_MINE.getHourlyResourceProduction(this) / 60d;
        double crystalProduction = CRYSTAL_MINE.getHourlyResourceProduction(this) / 60d;
        double deuteriumProduction = DEUTERIUM_SYNTHESIZER.getHourlyResourceProduction(this) / 60d;

        //TODO create better system to add resources
        resources.merge(METAL, metalProduction * actionCost.getTime(), (amount, production) -> amount + production);
        resources.merge(CRYSTAL, crystalProduction * actionCost.getTime(), (amount, production) -> amount + production);
        resources.merge(DEUTERIUM, deuteriumProduction * actionCost.getTime(), (amount, production) -> amount + production);
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
    }

    public boolean isCurrentlyUpgradingBuilding(Building building) {
        return (buildingInProgress == building);
    }
}
