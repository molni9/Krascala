package com.example.krascala.algorithm;

import com.example.krascala.model.Edge;
import com.example.krascala.model.Graph;
import com.example.krascala.model.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Основной класс алгоритма Краскала для поиска минимального остовного дерева (MST)
public class KruskalAlgorithm<T> {

    public List<Edge<T>> findMST(Graph<T> graph) {
        List<Edge<T>> mst = new ArrayList<>();
        DisjointSet<Vertex<T>> disjointSet = new DisjointSet<>(graph.getVertices());

        Collections.sort(graph.getEdges());

        for (Edge<T> edge : graph.getEdges()) {
            Vertex<T> u = edge.getSource();
            Vertex<T> v = edge.getDestination();

            if (disjointSet.find(u) != disjointSet.find(v)) {
                mst.add(edge);
                disjointSet.union(u, v);
            }
        }

        return mst;
    }

    private static class DisjointSet<T> {
        private final Map<T, T> parents;
        private final Map<T, Integer> rank;

        public DisjointSet(List<T> elements) {
            parents = new HashMap<>();
            rank = new HashMap<>();
            for (T element : elements) {
                parents.put(element, element);
                rank.put(element, 0);
            }
        }

        public T find(T item) {
            if (parents.get(item) != item) {
                parents.put(item, find(parents.get(item)));
            }
            return parents.get(item);
        }

        public void union(T u, T v) {
            T rootU = find(u);
            T rootV = find(v);

            if (rootU == rootV) return;

            int rankU = rank.get(rootU);
            int rankV = rank.get(rootV);

            if (rankU > rankV) {
                parents.put(rootV, rootU);
            } else if (rankU < rankV) {
                parents.put(rootU, rootV);
            } else {
                parents.put(rootV, rootU);
                rank.put(rootU, rankU + 1);
            }
        }
    }
}