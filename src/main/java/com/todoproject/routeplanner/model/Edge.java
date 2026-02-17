package com.todoproject.routeplanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Edge {
    private long targetId;
    private double weight;
}
