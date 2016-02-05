package com.skiwi.ogameplanner.objects;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Frank van Heeswijk
 */
public class BasicRequirementGraph implements RequirementGraph {
    private final Set<Requirement> nodes = new HashSet<>();
    private final Set<Edge> edges = new HashSet<>();

    public BasicRequirementGraph(GameObject gameObject) {
        initializeFromGameObject(gameObject);
    }

    private void initializeFromGameObject(GameObject gameObject) {
        Requirement rootRequirement = new BasicRequirement(gameObject, 1);
        addRequirementsOfRequirement(rootRequirement, null);
    }

    private void addRequirementsOfRequirement(Requirement requirement, Requirement previousRequirement) {
        addNode(requirement);
        if (previousRequirement != null) {
            addDirectedEdge(requirement, previousRequirement);
        }
        for (Requirement nextRequirement : requirement.getRequiredGameObject().getRequirements()) {
            addRequirementsOfRequirement(nextRequirement, requirement);
        }
    }

    private void addNode(Requirement requirement) {
        nodes.add(requirement);

        Set<Requirement> requirementsWithHigherLevel = getSameRequirementsWithHigherLevel(requirement);
        if (!requirementsWithHigherLevel.isEmpty()) {
            List<Requirement> sortedRequirementsWithHigherLevel = new ArrayList<>(requirementsWithHigherLevel);
            sortedRequirementsWithHigherLevel.sort(Comparator.comparingInt(Requirement::getRequiredLevel));
            Requirement nextHighestLevelRequirement = sortedRequirementsWithHigherLevel.get(0);
            addDirectedEdge(requirement, nextHighestLevelRequirement);
        }
    }

    private Set<Requirement> getSameRequirementsWithHigherLevel(Requirement requirement) {
        return nodes.stream().filter(req -> req.getRequiredGameObject().equals(requirement.getRequiredGameObject())).filter(req -> req.getRequiredLevel() > requirement.getRequiredLevel()).collect(Collectors.toSet());
    }

    private void addDirectedEdge(Requirement fromRequirement, Requirement toRequirement) {
        edges.add(new Edge(fromRequirement, toRequirement));
    }

    private enum Mark { UNMARKED, TEMPORARILY, PERMANENT }

    @Override
    public List<Requirement> getTopologicallySortedRequirements() {
        //Tarjan's algorithm - https://en.wikipedia.org/wiki/Topological_sorting

        List<Requirement> sortedRequirements = new ArrayList<>();
        Map<Requirement, Mark> nodeMarks = nodes.stream().collect(Collectors.toMap(node -> node, node -> Mark.UNMARKED));

        Set<Requirement> unmarkedNodes = nodeMarks.entrySet().stream().filter(entry -> entry.getValue() == Mark.UNMARKED).map(Map.Entry::getKey).collect(Collectors.toSet());
        while (!unmarkedNodes.isEmpty()) {
            Requirement unmarkedNode = unmarkedNodes.stream().findAny().get();
            topologicalSortVisit(unmarkedNode, sortedRequirements, nodeMarks);
            unmarkedNodes = nodeMarks.entrySet().stream().filter(entry -> entry.getValue() == Mark.UNMARKED).map(Map.Entry::getKey).collect(Collectors.toSet());
        }

        //we do not need to return the last requirements as that is the initial GameObject as Requirement(GameObject, 1)
        sortedRequirements.remove(sortedRequirements.size() - 1);
        return sortedRequirements;
    }

    private void topologicalSortVisit(Requirement node, List<Requirement> sortedRequirements, Map<Requirement, Mark> nodeMarks) {
        if (nodeMarks.get(node) == Mark.TEMPORARILY) {
            throw new IllegalStateException("the graph is not a DAG");
        }
        if (nodeMarks.get(node) == Mark.UNMARKED) {
            nodeMarks.put(node, Mark.TEMPORARILY);
            for (Edge outgoingEdge : getOutgoingEdgesOfNode(node)) {
                Requirement outNode = outgoingEdge.to;
                topologicalSortVisit(outNode, sortedRequirements, nodeMarks);
            }
            nodeMarks.put(node, Mark.PERMANENT);
            sortedRequirements.add(0, node);
        }
    }

    private Set<Edge> getOutgoingEdgesOfNode(Requirement node) {
        return edges.stream().filter(edge -> node.equals(edge.from)).collect(Collectors.toSet());
    }
    private static class Edge {
        private final Requirement from;
        private final Requirement to;

        private Edge(Requirement from, Requirement to) {
            this.from = from;
            this.to = to;
        }
    }
}
