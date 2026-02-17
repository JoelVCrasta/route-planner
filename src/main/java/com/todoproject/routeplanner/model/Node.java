package com.todoproject.routeplanner.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Node {
    private long id;
    private double lat;
    private double lon;
    private List<Edge> edges = new ArrayList<>();

    public Node(long id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }
}
