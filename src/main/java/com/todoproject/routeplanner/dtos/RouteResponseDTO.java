package com.todoproject.routeplanner.dtos;


import com.todoproject.routeplanner.model.Node;

import java.util.List;

public record RouteResponseDTO(
        long id,
        double lat,
        double lon
) {
    public static RouteResponseDTO fromEntity(Node node) {
        return new RouteResponseDTO(
                node.getId(),
                node.getLat(),
                node.getLon()
        );
    }

    public static List<RouteResponseDTO> fromEntityList(List<Node> nodes) {
        return nodes.stream().map(RouteResponseDTO::fromEntity).toList();
    }
}
