package com.example.krascala.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphInput {
    private List<Node> nodes;
    private List<Edge> edges;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Node {
        private String id;
        private String color;
        private int x;
        private int y;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Edge {
        private String from;
        private String to;
        private int weight;
    }
}
