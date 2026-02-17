package com.todoproject.routeplanner.service;

import com.todoproject.routeplanner.model.Node;
import com.todoproject.routeplanner.parser.OsmHandler;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.Map;

@Getter
@Slf4j
@Service
public class GraphService {
    @Value("${osm.file-path}")
    private String osmFilePath;

    private Map<Long, Node> graph;

    @PostConstruct
    public void loadGraph() {
        try {
            log.info("Parsing OSM file: {}", osmFilePath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            OsmHandler handler = new OsmHandler();

            saxParser.parse(new File(osmFilePath), handler);
            this.graph = handler.getGraph();

            log.info("Successfully parsed the osm file");
        } catch (Exception e) {
            log.info("Failed to parse the OSM file");
        }
    }

    public long findNearestNode(double lat, double lon) {
        long nearestId = -1;

        double minSquaredDist = Double.MAX_VALUE;

        for (Node node : graph.values()) {
            double dLat = lat - node.getLat();
            double dLon = lon - node.getLon();

            double squaredDist = dLat * dLat + dLon * dLon;

            if (squaredDist < minSquaredDist) {
                minSquaredDist = squaredDist;
                nearestId = node.getId();
            }
        }
        return nearestId;
    }
}
