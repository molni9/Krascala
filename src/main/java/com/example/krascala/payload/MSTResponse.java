package com.example.krascala.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class MSTResponse {
    private String svg;
    private List<Map<String, Object>> mstEdges;
}
