package com.dmsproject.dijkstravisualizer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class AlgorithmStep {

    public enum StepType {
        INITIALIZED, // Algorithm setup
        NODE_EXTRACTED, // A node is extracted from the priority queue
        EDGE_RELAXED, // An edge's target node distance is updated
        PATH_FOUND, // Shortest path to a specific end node is found
        NO_PATH_FOUND, // No path exists to a specific end node
        ALGORITHM_FINISHED_ALL_PATHS // Algorithm completed for all reachable nodes
    }

    private final StepType type;
    private final String description;
    private final String currentNodeId;
    private final String relaxedSourceNodeId;
    private final String relaxedTargetNodeId;
    private final Map<String, Double> distances;
    private final ObservableList<NodeDistance> priorityQueueState;
    private final Set<String> visitedNodes;
    private final Map<String, String> predecessors; // Snapshot of predecessors for path reconstruction
    private final List<String> path;
    private final String endNodeId;
    private final boolean isAllPathsMode;

    // --- PRIMARY / COMPREHENSIVE CONSTRUCTOR ---
    // All other constructors delegate to this one.
    public AlgorithmStep(StepType type, String description, String currentNodeId,
                         String relaxedSourceNodeId, String relaxedTargetNodeId,
                         Map<String, Double> distances, PriorityQueue<NodeDistance> priorityQueue,
                         Set<String> visitedNodes, Map<String, String> predecessors,
                         List<String> path, String endNodeId, boolean isAllPathsMode) {
        this.type = type;
        this.description = description;
        this.currentNodeId = currentNodeId;
        this.relaxedSourceNodeId = relaxedSourceNodeId;
        this.relaxedTargetNodeId = relaxedTargetNodeId;

        // Create immutable copies of collections.
        // For maps, explicitly filter out null values before making immutable copy
        // to prevent NullPointerException from Map.copyOf if a predecessor value is null.
        Map<String, Double> safeDistances = new HashMap<>();
        if (distances != null) {
            for (Map.Entry<String, Double> entry : distances.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) { // Ensure no null keys/values
                    safeDistances.put(entry.getKey(), entry.getValue());
                }
            }
        }
        this.distances = Collections.unmodifiableMap(safeDistances); // Use unmodifiableMap for safety

        Map<String, String> safePredecessors = new HashMap<>();
        if (predecessors != null) {
            for (Map.Entry<String, String> entry : predecessors.entrySet()) {
                // Only include entries where both key (nodeId) and value (predecessorId) are non-null.
                // This handles cases where the source node might have a null predecessor.
                if (entry.getKey() != null && entry.getValue() != null) {
                    safePredecessors.put(entry.getKey(), entry.getValue());
                }
            }
        }
        this.predecessors = Collections.unmodifiableMap(safePredecessors); // Use unmodifiableMap for safety

        // Copy visited nodes
        this.visitedNodes = (visitedNodes != null) ? Collections.unmodifiableSet(new HashSet<>(visitedNodes)) : Collections.emptySet();

        // Copy PQ content to ObservableList and sort for consistent display
        this.priorityQueueState = FXCollections.observableArrayList();
        if (priorityQueue != null) {
            this.priorityQueueState.addAll(priorityQueue);
            FXCollections.sort(this.priorityQueueState); // NodeDistance MUST be Comparable
        }

        this.path = (path != null) ? Collections.unmodifiableList(new ArrayList<>(path)) : null;
        this.endNodeId = endNodeId;
        this.isAllPathsMode = isAllPathsMode;
    }


    // --- CONVENIENCE CONSTRUCTORS (Delegating to the primary constructor) ---

    // For INITIALIZED, NODE_EXTRACTED, NO_PATH_FOUND steps
    public AlgorithmStep(StepType type, String description, String currentNodeId,
                         Map<String, Double> distances, PriorityQueue<NodeDistance> priorityQueue,
                         Set<String> visitedNodes, Map<String, String> predecessors, String endNodeId) {
        this(type, description, currentNodeId, null, null,
                distances, priorityQueue, visitedNodes, predecessors,
                null, endNodeId, false);
    }

    // For EDGE_RELAXED steps
    public AlgorithmStep(StepType type, String description, String currentNodeId,
                         String relaxedSourceNodeId, String relaxedTargetNodeId,
                         Map<String, Double> distances, PriorityQueue<NodeDistance> priorityQueue,
                         Set<String> visitedNodes, Map<String, String> predecessors, String endNodeId) {
        this(type, description, currentNodeId, relaxedSourceNodeId, relaxedTargetNodeId,
                distances, priorityQueue, visitedNodes, predecessors,
                null, endNodeId, false);
    }

    // For PATH_FOUND step (when a specific path is found to an end node)
    public AlgorithmStep(StepType type, String description, String currentNodeId, // currentNodeId here is the endNodeId of the path
                         Map<String, Double> distances, PriorityQueue<NodeDistance> priorityQueue,
                         Set<String> visitedNodes, Map<String, String> predecessors,
                         List<String> path, String endNodeId) {
        this(type, description, currentNodeId, null, null,
                distances, priorityQueue, visitedNodes, predecessors,
                path, endNodeId, false);
    }

    // For ALGORITHM_FINISHED_ALL_PATHS (when Dijkstra finishes processing all reachable nodes)
    public AlgorithmStep(StepType type, String description,
                         Map<String, Double> distances, PriorityQueue<NodeDistance> priorityQueue,
                         Set<String> visitedNodes, Map<String, String> predecessors) {
        this(type, description, null, null, null,
                distances, priorityQueue, visitedNodes, predecessors,
                null, null, true);
    }


    // --- GETTERS ---
    public StepType getType() { return type; }
    public String getDescription() { return description; }
    public String getCurrentNodeId() { return currentNodeId; }
    public String getRelaxedSourceNodeId() { return relaxedSourceNodeId; }
    public String getRelaxedTargetNodeId() { return relaxedTargetNodeId; }
    public Map<String, Double> getDistances() { return distances; }
    public ObservableList<NodeDistance> getPriorityQueueState() { return priorityQueueState; }
    public Set<String> getVisitedNodes() { return visitedNodes; }
    public Map<String, String> getPredecessors() { return predecessors; }
    public List<String> getPath() { return path; }
    public String getEndNodeId() { return endNodeId; }
    public boolean isAllPathsMode() { return isAllPathsMode; }
}