package com.example.krascala.algorithm;


import com.example.krascala.model.Edge;
import com.example.krascala.model.Graph;
import com.example.krascala.model.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        private final List<T> parents;
        private final List<Integer> rank;

        public DisjointSet(List<T> elements) {
            parents = new ArrayList<>(elements);
            rank = new ArrayList<>(Collections.nCopies(elements.size(), 0));
        }

        public T find(T item) {
            int index = parents.indexOf(item);
            if (parents.get(index) != item) {
                parents.set(index, find(parents.get(index)));
            }
            return parents.get(index);
        }

        public void union(T u, T v) {
            T rootU = find(u);
            T rootV = find(v);

            if (rootU == rootV) return;

            int indexU = parents.indexOf(rootU);
            int indexV = parents.indexOf(rootV);

            if (rank.get(indexU) > rank.get(indexV)) {
                parents.set(indexV, rootU);
            } else if (rank.get(indexU) < rank.get(indexV)) {
                parents.set(indexU, rootV);
            } else {
                parents.set(indexV, rootU);
                rank.set(indexU, rank.get(indexU) + 1);
            }
        }
    }
}
