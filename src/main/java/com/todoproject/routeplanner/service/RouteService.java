package com.todoproject.routeplanner.service;

import com.todoproject.routeplanner.model.Node;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {
    private final AStarService aStarService;
    private final GraphService graphService;

    public RouteService(
            AStarService aStarService,
            GraphService graphService
    ) {
        this.aStarService = aStarService;
        this.graphService = graphService;
    }

    public List<Node> calculateRouteById(long sourceId, long destinationId) {

        return aStarService.findPath(sourceId, destinationId);
    }
}
