package com.example.krascala.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Graph<T> {
    private List<Edge<T>> edges = new ArrayList<>();
    private List<Vertex<T>> vertices = new ArrayList<>();

    public void addEdge(Edge<T> edge) {
        edges.add(edge);
    }

    public void addVertex(Vertex<T> vertex) {
        vertices.add(vertex);
    }
}
