package com.todoproject.routeplanner.controller;

import com.todoproject.routeplanner.dtos.RouteResponseDTO;
import com.todoproject.routeplanner.model.Node;
import com.todoproject.routeplanner.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route")
@CrossOrigin(origins = "http://localhost:3000")
public class RouteController {
    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/calculate")
    public ResponseEntity<List<RouteResponseDTO>> getRoute(@RequestParam long from, @RequestParam long to) {
        List<Node> route = routeService.calculateRouteById(from, to);
        return ResponseEntity.ok(RouteResponseDTO.fromEntityList(route));
    }
}
