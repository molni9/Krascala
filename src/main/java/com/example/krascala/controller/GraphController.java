package com.example.krascala.controller;

import com.example.krascala.algorithm.KruskalAlgorithm;
import com.example.krascala.model.Edge;
import com.example.krascala.model.Graph;
import com.example.krascala.model.Vertex;
import com.example.krascala.payload.GraphInput;
import com.example.krascala.payload.MSTResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GraphController {

    @PostMapping("/mst")
    public MSTResponse calculateMST(@RequestBody GraphInput graphInput) {
        if (graphInput.getEdges() == null || graphInput.getEdges().isEmpty()) {
            throw new IllegalArgumentException("Edges cannot be null or empty");
        }

        Graph<String> graph = new Graph<>();
        Map<String, Vertex<String>> vertexMap = new HashMap<>();

        for (GraphInput.Node node : graphInput.getNodes()) {
            Vertex<String> vertex = new Vertex<>(node.getId());
            graph.addVertex(vertex);
            vertexMap.put(node.getId(), vertex);
        }

        for (GraphInput.Edge edge : graphInput.getEdges()) {
            Vertex<String> source = vertexMap.get(edge.getFrom());
            Vertex<String> destination = vertexMap.get(edge.getTo());
            graph.addEdge(new Edge<>(source, destination, edge.getWeight()));
        }

        KruskalAlgorithm<String> kruskal = new KruskalAlgorithm<>();
        List<Edge<String>> mstEdges = kruskal.findMST(graph);

        List<Map<String, Object>> mstEdgesJson = mstEdges.stream().map(edge -> {
            Map<String, Object> map = new HashMap<>();
            map.put("from", edge.getSource().getData());
            map.put("to", edge.getDestination().getData());
            map.put("weight", edge.getWeight());
            return map;
        }).collect(Collectors.toList());

        return new MSTResponse(generateSvg(graphInput.getEdges(), graphInput.getNodes()), mstEdgesJson);
    }

    private String generateSvg(List<GraphInput.Edge> allEdges, List<GraphInput.Node> nodes) {
        StringBuilder svgBuilder = new StringBuilder("<svg width='800' height='600'>");

        for (GraphInput.Node node : nodes) {
            svgBuilder.append("<circle cx='").append(node.getX()).append("' cy='").append(node.getY())
                    .append("' r='20' fill='").append(node.getColor()).append("'/>")
                    .append("<text x='").append(node.getX()).append("' y='").append(node.getY() - 25);
        }

        for (GraphInput.Edge edge : allEdges) {
            GraphInput.Node source = findNode(nodes, edge.getFrom());
            GraphInput.Node target = findNode(nodes, edge.getTo());
            svgBuilder.append("<line x1='").append(source.getX()).append("' y1='").append(source.getY())
                    .append("' x2='").append(target.getX()).append("' y2='").append(target.getY())
                    .append("' stroke='black' stroke-width='2' class='graph-edge' data-from='").append(edge.getFrom())
                    .append("' data-to='").append(edge.getTo()).append("' data-weight='").append(edge.getWeight())
                    .append("'/>")
                    .append("<text x='").append((source.getX() + target.getX()) / 2)
                    .append("' y='").append((source.getY() + target.getY()) / 2)
                    .append("' fill='black'>").append(edge.getWeight()).append("</text>");
        }

        svgBuilder.append("</svg>");
        return svgBuilder.toString();
    }

    private GraphInput.Node findNode(List<GraphInput.Node> nodes, String id) {
        return nodes.stream().filter(n -> n.getId().equals(id)).findFirst().orElse(null);
    }
}
