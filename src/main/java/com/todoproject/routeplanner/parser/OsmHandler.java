package com.todoproject.routeplanner.parser;

import com.todoproject.routeplanner.model.Edge;
import com.todoproject.routeplanner.model.Node;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OsmHandler extends DefaultHandler {
    private final Map<Long, Node> allNodes = new HashMap<>();
    private final List<Long> currentWayNodes =  new ArrayList<>();
    private boolean isDrivable = false;

    public Map<Long, Node> getGraph() {
        return allNodes;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("node")) {
            long id = Long.parseLong(attributes.getValue("id"));
            double lat = Double.parseDouble(attributes.getValue("lat"));
            double lon = Double.parseDouble(attributes.getValue("lon"));
            allNodes.put(id, new Node(id, lat, lon));
        }

        else if (qName.equalsIgnoreCase("way")) {
            currentWayNodes.clear();
            isDrivable = false;
        }

        else if (qName.equalsIgnoreCase("nd")) {
            currentWayNodes.add(Long.parseLong(attributes.getValue("ref")));
        }

        else if (qName.equalsIgnoreCase("tag")) {
            String k = attributes.getValue("k");
            if ("highway".equals(k)) {
                isDrivable = true;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("way") && isDrivable) {
            for (int i = 0; i < currentWayNodes.size() - 1; i++) {
                long uId = currentWayNodes.get(i);
                long vId = currentWayNodes.get(i + 1);

                Node u = allNodes.get(uId);
                Node v = allNodes.get(vId);

                if (u != null && v != null) {
                    double dist = calculateHaversine(u.getLat(), u.getLon(), v.getLat(), v.getLon());
                    u.getEdges().add(new Edge(vId, dist));
                    v.getEdges().add(new Edge(uId, dist));
                }
            }
        }
    }

    public static double calculateHaversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * 6371 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
