package com.alta.computator.utils;

import com.alta.computator.model.altitudeMap.AltitudeMap;
import com.alta.computator.model.altitudeMap.TileState;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.List;
import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Calculates the route for movement. Implements A* algorithm.
 */
@Slf4j
public final class MovementRouteComputator {

    private Node[][] searchArea;
    private java.util.List<Node> open;

    /**
     * Initialize new instance of {@link MovementRouteComputator}.
     */
    public MovementRouteComputator() {
        this.open = new ArrayList<>();
    }

    /**
     * Calculates the route of movement.
     *
     * @param startCoordinate   - the start coordinate of route.
     * @param endCoordinate     - the end coordinate of route.
     * @return the {@link List} of point that route contains or null if calculating of route fail.
     */
    public Deque<Point> calculateRoute(Point startCoordinate, Point endCoordinate, AltitudeMap altitudeMap) {
        if (startCoordinate.equals(endCoordinate)) {
            return null;
        }

        try {
            Node startNode = new Node(null, startCoordinate, this.getDistance(startCoordinate, endCoordinate));
            this.open.add(startNode);
            this.searchArea = new Node[altitudeMap.getTotalCountOfTileOnXAxis()][altitudeMap.getTotalCountOfTileOnYAxis()];
            this.searchArea[startCoordinate.x][startCoordinate.y] = startNode;

            while (!this.open.isEmpty()) {
                this.findNodesWithMinimumDistance().forEach(node -> {
                    this.addAdjacentNodes(node, endCoordinate, altitudeMap);
                    this.open.remove(node);
                });
                Node nodeWithMinimumDistance = this.findNodeWithMinimumDistance();
                if (nodeWithMinimumDistance == null) {
                    continue;
                }

                if (nodeWithMinimumDistance.coordinate.equals(endCoordinate)) {
                    return this.buildRoute(nodeWithMinimumDistance);
                }
            }

            return null;
        } finally {
            this.open.clear();
            this.searchArea = new Node[0][0];
        }
    }

    private java.util.List<Node> findNodesWithMinimumDistance() {
        if (this.open.isEmpty()) {
            return Collections.emptyList();
        }

        Node nodeWithMinDistance = this.findNodeWithMinimumDistance();
        if (nodeWithMinDistance == null) {
            log.error("Node with minimum distance not found");
            return Collections.emptyList();
        }

        return this.open.stream()
                .filter(node -> node.distance == nodeWithMinDistance.distance)
                .collect(Collectors.toList());
    }

    private Node findNodeWithMinimumDistance() {
        if (this.open.isEmpty()) {
            return null;
        }

        return this.open.stream().min(Comparator.comparingDouble(node -> node.distance)).orElse(null);
    }

    private Deque<Point> buildRoute(Node lastNode) {
        if (lastNode == null) {
            log.error("The last node is null. Route will not be build.");
            return new LinkedList<>();
        }

        Deque<Point> route = new LinkedList<>();
        Node currentNode = lastNode;
        do {
            route.addFirst(currentNode.coordinate);
            currentNode = currentNode.parent;
        } while (currentNode != null);

        return route;
    }

    private void addAdjacentNodes(Node parent, Point endCoordinate, AltitudeMap altitudeMap) {
        // Upper
        this.addAdjacentNodeIfPossible(
                parent, endCoordinate, new Point(parent.coordinate.x, parent.coordinate.y - 1), altitudeMap
        );

        // Right
        this.addAdjacentNodeIfPossible(
                parent, endCoordinate, new Point(parent.coordinate.x + 1, parent.coordinate.y), altitudeMap
        );

        // Lower
        this.addAdjacentNodeIfPossible(
                parent, endCoordinate, new Point(parent.coordinate.x, parent.coordinate.y + 1), altitudeMap
        );

        // Left
        this.addAdjacentNodeIfPossible(
                parent, endCoordinate, new Point(parent.coordinate.x - 1, parent.coordinate.y), altitudeMap
        );
    }

    private void addAdjacentNodeIfPossible(Node parent,
                                           Point endCoordinate,
                                           Point adjacentCoordinate,
                                           AltitudeMap altitudeMap) {
        if (validCoordinate(adjacentCoordinate, altitudeMap) &&
                this.searchArea[adjacentCoordinate.x][adjacentCoordinate.y] == null) {
            Node adjacentNode = new Node(
                    parent,
                    adjacentCoordinate,
                    this.getDistance(adjacentCoordinate, endCoordinate)
            );

            this.open.add(adjacentNode);
            this.searchArea[adjacentCoordinate.x][adjacentCoordinate.y] = adjacentNode;
        }
    }

    private boolean validCoordinate(Point coordinate, AltitudeMap altitudeMap) {
        return coordinate.x >= 0 && coordinate.x <= altitudeMap.getTotalCountOfTileOnXAxis() &&
                coordinate.y >= 0 && coordinate.y <= altitudeMap.getTotalCountOfTileOnYAxis() &&
                altitudeMap.getTileState(coordinate.x, coordinate.y) != TileState.BARRIER;
    }

    private double getDistance(Point start, Point end) {
        return Math.abs(end.x - start.x) + Math.abs(end.y - start.y);
    }

    @AllArgsConstructor
    private class Node {
        private Node parent;
        private Point coordinate;
        private double distance;
    }
}
