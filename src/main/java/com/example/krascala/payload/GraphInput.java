package com.example.krascala.payload;

import lombok.Data;

import java.util.List;

@Data
public class GraphInput {

    private List<Node> nodes;

    @Data
    public static class Node {
        private String id;
        private List<Edge> edges;
        private int x;
        private int y;
    }

    @Data
    public static class Edge {
        private String from;
        private String to;
        private int weight;
    }
}

