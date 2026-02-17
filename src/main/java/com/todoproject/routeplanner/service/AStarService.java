package com.todoproject.routeplanner.service;

import com.todoproject.routeplanner.model.Edge;
import com.todoproject.routeplanner.model.Node;
import com.todoproject.routeplanner.parser.OsmHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AStarService {

    private final GraphService graphService;

    public List<Node> findPath(long sourceId, long destinationId) {
        Map<Long, Node> graph = graphService.getGraph();
        Map<Long, Double> gScore = new HashMap<>();
        Map<Long, Long> cameFrom = new HashMap<>();
        Set<Long> closedSet = new HashSet<>();

        PriorityQueue<Long> openList = new PriorityQueue<>(Comparator.comparingDouble(
                id -> getFScore(graph, id, destinationId, gScore)
        ));

        gScore.put(sourceId, 0.0);
        openList.add(sourceId);

        while(!openList.isEmpty()) {
            long currId = openList.poll();

            if (closedSet.contains(currId)) continue;
            closedSet.add(currId);

            if (currId == destinationId) {
                return makePath(graph, cameFrom, currId);
            }

            Node currNode = graph.get(currId);
            if (currNode == null) continue;

            for (Edge edge : currNode.getEdges()) {
                double tentativeG = gScore.get(currId) + edge.getWeight();

                if (tentativeG < gScore.getOrDefault(edge.getTargetId(), Double.MAX_VALUE)) {
                    cameFrom.put(edge.getTargetId(), currId);
                    gScore.put(edge.getTargetId(), tentativeG);
                    openList.add(edge.getTargetId());
                }
            }
        }

        return Collections.emptyList();
    }

    public double getFScore(Map<Long, Node> graph, long currId, long destinationId, Map<Long, Double> gScore) {
        Node u = graph.get(currId);
        Node v = graph.get(destinationId);

        double g = gScore.getOrDefault(currId, Double.MAX_VALUE);
        double h = OsmHandler.calculateHaversine(u.getLat(), u.getLon(), v.getLat(), v.getLon());

        return g + h;
    }

    public List<Node> makePath(Map<Long, Node> graph, Map<Long, Long> cameFrom, long currId) {
        List<Node> fullPath = new ArrayList<>();
        fullPath.add(graph.get(currId));

        while(cameFrom.containsKey(currId)) {
            currId = cameFrom.get(currId);
            fullPath.addFirst(graph.get(currId));
        }

        return fullPath;
    }

}
