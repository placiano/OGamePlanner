package com.skiwi.ogameplanner.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Frank van Heeswijk
 */
public class BasicBuildOrder implements Iterable<GameObject> {
    private final List<GameObject> gameObjects = new ArrayList<>();

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
    public Iterator<GameObject> iterator() {
        return gameObjects.iterator();
    }
}
