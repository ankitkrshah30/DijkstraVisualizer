// src/main/java/com/dmsproject/dijkstravisualizer/NodeDistance.java
package com.dmsproject.dijkstravisualizer;

import java.util.Objects;

public class NodeDistance implements Comparable<NodeDistance> {
    private String nodeId;
    private double distance;

    public NodeDistance(String nodeId, double distance) {
        this.nodeId = nodeId;
        this.distance = distance;
    }

    public String getNodeId() {
        return nodeId;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        // Formats distance nicely for display
        String distStr = (distance == Double.POSITIVE_INFINITY) ? "∞" : String.format("%.1f", distance);
        return String.format("%s: %s", nodeId, distStr);
    }
    // For PriorityQueue sorting
    @Override
    public int compareTo(NodeDistance other) {
        // Compare by distance first (for priority queue)
        int cmp = Double.compare(this.distance, other.distance);
        if (cmp == 0) {
            // If distances are equal, compare by nodeId for consistent ordering
            cmp = this.nodeId.compareTo(other.nodeId);
        }
        return cmp;
    }

    // Required for correct behavior in PriorityQueue when updating distances
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeDistance that = (NodeDistance) o;
        return Double.compare(that.distance, distance) == 0 && Objects.equals(nodeId, that.nodeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, distance);
    }
}