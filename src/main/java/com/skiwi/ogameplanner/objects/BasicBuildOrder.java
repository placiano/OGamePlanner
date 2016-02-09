package com.skiwi.ogameplanner.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Frank van Heeswijk
 */
public class BasicBuildOrder implements BuildOrder, Iterable<GameObject> {
    private List<GameObject> gameObjects = new ArrayList<>();

    private List<GameObject> savedState;

    public BasicBuildOrder() {

    }

    public BasicBuildOrder(List<GameObject> gameObjects) {
        this.gameObjects.addAll(gameObjects);
    }

    public BasicBuildOrder(RequirementGraph requirementGraph) {
        initializeFromRequirementGraph(requirementGraph);
    }

    private void initializeFromRequirementGraph(RequirementGraph requirementGraph) {
        List<Requirement> sortedRequirements = requirementGraph.getTopologicallySortedRequirements();
        Map<GameObject, Integer> currentGameObjectLevel = new HashMap<>();
        for (Requirement requirement : sortedRequirements) {
            GameObject requiredGameObject = requirement.getRequiredGameObject();
            int requiredLevel = requirement.getRequiredLevel();
            int levels = requiredLevel - currentGameObjectLevel.getOrDefault(requiredGameObject, 0);
            for (int i = 0; i < levels; i++) {
                gameObjects.add(requiredGameObject);
            }
            currentGameObjectLevel.put(requiredGameObject, requiredLevel);
        }
    }

    @Override
    public void saveState() {
        savedState = new ArrayList<>(gameObjects);
    }

    @Override
    public void restoreState() {
        if (savedState == null) {
            throw new IllegalStateException("the state has not been saved");
        }
        gameObjects = savedState;
    }

    @Override
    public void add(int index, GameObject gameObject) {
        gameObjects.add(index, gameObject);
    }

    @Override
    public void moveBy(int index, int delta) {
        GameObject gameObject = gameObjects.remove(index);
        gameObjects.add(index + delta, gameObject);
    }

    @Override
    public GameObject get(int index) {
        return gameObjects.get(index);
    }

    @Override
    public int size() {
        return gameObjects.size();
    }

    @Override
    public Iterator<GameObject> iterator() {
        return gameObjects.iterator();
    }
}
