package com.skiwi.ogameplanner;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static com.skiwi.ogameplanner.Resource.*;

/**
 * @author Frank van Heeswijk
 */
public class PlayerSnapshot {
    private final ServerSettings serverSettings;

    private int time = 0;

    private final EnumMap<Resource, Integer> resources = new EnumMap<>(Resource.class);
    private final EnumMap<Building, Integer> buildings = new EnumMap<>(Building.class);
    private final EnumMap<Research, Integer> researches = new EnumMap<>(Research.class);
    private final EnumMap<Ship, Integer> ships = new EnumMap<>(Ship.class);

    public PlayerSnapshot(ServerSettings serverSettings) {
        this.serverSettings = serverSettings;
    }

    public ServerSettings getServerSettings() {
        return serverSettings;
    }

    public int getTime() {
        return time;
    }

    public int getResourceAmount(Resource resource) {
        return resources.getOrDefault(resource, 0);
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
        return actions;
    }

    private void addBuildingActions(List<Action> actions) {
        buildings.forEach((building, level) -> {
            Action action = new UpgradeBuildingAction(building);
            if (action.isAllowed(this)) {
                actions.add(action);
            }
        });
    }

    public PlayerSnapshot copy() {
        PlayerSnapshot playerSnapshot = new PlayerSnapshot(serverSettings);
        playerSnapshot.time = time;
        playerSnapshot.resources.putAll(resources);
        playerSnapshot.buildings.putAll(buildings);
        playerSnapshot.researches.putAll(researches);
        playerSnapshot.ships.putAll(ships);
        return playerSnapshot;
    }

    public boolean satisfiesCost(ActionCost actionCost) {
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
        time += actionCost.getTime();
        resources.merge(METAL, actionCost.getMetal(), (amount, cost) -> amount - cost);
        resources.merge(CRYSTAL, actionCost.getCrystal(), (amount, cost) -> amount - cost);
        resources.merge(DEUTERIUM, actionCost.getDeuterium(), (amount, cost) -> amount - cost);
        resources.merge(DARK_MATTER, actionCost.getDarkMatter(), (amount, cost) -> amount - cost);
    }

    public void upgradeBuilding(Building building) {
        ActionCost actionCost = building.getUpgradeCost(this);
        addCost(actionCost);
        buildings.merge(building, 1, (currentLevel, newLevels) -> currentLevel + newLevels);
    }
}
