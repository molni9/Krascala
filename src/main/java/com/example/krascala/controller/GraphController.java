package com.example.krascala.controller;

import com.example.krascala.algorithm.KruskalAlgorithm;
import com.example.krascala.model.Edge;
import com.example.krascala.model.Graph;
import com.example.krascala.model.Vertex;
import com.example.krascala.payload.GraphInput;
import com.example.krascala.payload.MSTResponse;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GraphController {

    @PostMapping("/mst")
    public MSTResponse calculateMST(@RequestBody GraphInput graphInput) {
        Graph<String> graph = new Graph<>();

        for (GraphInput.Node node : graphInput.getNodes()) {
            Vertex<String> vertex = new Vertex<>(node.getId());
            graph.addVertex(vertex);
        }

        for (GraphInput.Node node : graphInput.getNodes()) {
            Vertex<String> source = findVertex(graph.getVertices(), node.getId());
            for (GraphInput.Edge edge : node.getEdges()) {
                Vertex<String> destination = findVertex(graph.getVertices(), edge.getTo());
                graph.addEdge(new Edge<>(source, destination, edge.getWeight()));
            }
        }

        KruskalAlgorithm<String> kruskal = new KruskalAlgorithm<>();
        List<Edge<String>> mstEdges = kruskal.findMST(graph);

        return new MSTResponse(generateSvg(mstEdges, graphInput.getNodes()));
    }

    private Vertex<String> findVertex(List<Vertex<String>> vertices, String id) {
        return vertices.stream().filter(v -> v.getData().equals(id)).findFirst().orElse(null);
    }

    private String generateSvg(List<Edge<String>> mstEdges, List<GraphInput.Node> nodes) {
        StringBuilder svgBuilder = new StringBuilder("<svg width='800' height='600'>");

        for (GraphInput.Node node : nodes) {
            svgBuilder.append("<circle cx='").append(node.getX()).append("' cy='").append(node.getY())
                    .append("' r='20' fill='black'/>");
        }

        for (Edge<String> edge : mstEdges) {
            GraphInput.Node source = findNode(nodes, edge.getSource().getData());
            GraphInput.Node target = findNode(nodes, edge.getDestination().getData());
            svgBuilder.append("<line x1='").append(source.getX()).append("' y1='").append(source.getY())
                    .append("' x2='").append(target.getX()).append("' y2='").append(target.getY())
                    .append("' stroke='black' stroke-width='2'/>");
        }

        svgBuilder.append("</svg>");
        return svgBuilder.toString();
    }

    private GraphInput.Node findNode(List<GraphInput.Node> nodes, String id) {
        return nodes.stream().filter(n -> n.getId().equals(id)).findFirst().orElse(null);
    }
}
