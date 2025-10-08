package com.sairajtravels.site.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/trip")
public class TripPlannerController {

    private static final Logger log = LoggerFactory.getLogger(TripPlannerController.class);

    private final RestTemplate rest = new RestTemplate();

    @Value("${ors.api.key}")
    private String orsApiKey;

    @Value("${nominatim.useragent:saairaj-travels@example.com}")
    private String nominatimUserAgent;

    @PostMapping("/plan")
    public ResponseEntity<?> planTrip(@RequestBody Map<String, Object> req) {
        try {
            log.info("➡ Incoming Trip Plan Request: {}", req);

            @SuppressWarnings("unchecked")
            Map<String, Object> from = (Map<String, Object>) req.get("from");
            @SuppressWarnings("unchecked")
            Map<String, Object> to = (Map<String, Object>) req.get("to");

            if (from == null || to == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing from/to in request"));
            }

            // ✅ new roundTrip flag
            boolean roundTrip = req.get("roundTrip") != null
                    && Boolean.parseBoolean(req.get("roundTrip").toString());

            double fromLat = toDouble(from.get("lat"));
            double fromLng = toDouble(from.get("lng"));
            double toLat = toDouble(to.get("lat"));
            double toLng = toDouble(to.get("lng"));

            log.info("Original coords - from: {},{}  to: {},{}", fromLat, fromLng, toLat, toLng);

            // Snap endpoints
            double[] snappedFrom = snapToNearestRoad(fromLat, fromLng);
            double[] snappedTo = snapToNearestRoad(toLat, toLng);

            if (snappedFrom != null) {
                fromLat = snappedFrom[0];
                fromLng = snappedFrom[1];
            }
            if (snappedTo != null) {
                toLat = snappedTo[0];
                toLng = snappedTo[1];
            }

            // ORS request
            String orsUrl = "https://api.openrouteservice.org/v2/directions/driving-car";

            Map<String, Object> body = new HashMap<>();
            List<List<Double>> coords = Arrays.asList(
                    Arrays.asList(fromLng, fromLat),
                    Arrays.asList(toLng, toLat)
            );
            body.put("coordinates", coords);
            body.put("instructions", false);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", orsApiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            try {
                @SuppressWarnings("rawtypes")
                ResponseEntity<Map> orsResp = rest.exchange(orsUrl, HttpMethod.POST, entity, Map.class);
                @SuppressWarnings("unchecked")
                Map<String, Object> respBody = orsResp.getBody();

                if (respBody != null) {
                    log.info("ORS top-level keys: {}", respBody.keySet());
                    log.info("ORS full response: {}", respBody);
                }

                Map<String, Object> result = buildResponseFromORS(respBody, roundTrip);
                log.info("Parsed result from ORS: {}", result);

                if (result == null) result = new HashMap<>();
                result.put("fallback", false);
                result.put("message", "ORS success");

                log.info("✅ ORS SUCCESS: Driving route fetched. Distance={}m, Duration={}s",
                        result.get("distance"), result.get("duration"));

                return ResponseEntity.ok(result);
            } catch (RestClientResponseException rcre) {
                log.warn("⚠️ ORS call failed: {} \"{}\"",
                        rcre.getStatusCode().value(), safeTruncate(rcre.getResponseBodyAsString(), 1000));
            } catch (Exception ex) {
                log.error("❌ ORS unexpected error", ex);
            }

            // --- Fallback ---
            double km = haversineKm(fromLat, fromLng, toLat, toLng);
            double meters = km * 1000.0;
            double durationSec = (km / 60.0) * 3600.0; // assume 60 km/h
            double fuelPricePerLiter = 100.99;
            double fuelEfficiencyKmpl = 15.0;
            double fuelCost = (meters / 1000.0 / fuelEfficiencyKmpl) * fuelPricePerLiter;
            double tollCost = km * 2.0;

            if (roundTrip) {
                meters *= 2;
                durationSec *= 2;
                fuelCost *= 2;
                tollCost *= 2;
            }

            Map<String, Object> fallback = new HashMap<>();
            Map<String, Object> geometry = new HashMap<>();
            geometry.put("type", "LineString");
            geometry.put("coordinates", Arrays.asList(
                    Arrays.asList(fromLng, fromLat),
                    Arrays.asList(toLng, toLat)
            ));

            fallback.put("geometry", geometry);
            fallback.put("distance", meters);
            fallback.put("duration", durationSec);
            fallback.put("fuelCost", fuelCost);
            fallback.put("tollCost", tollCost);
            fallback.put("roundTrip", roundTrip);
            fallback.put("fallback", true);
            fallback.put("message", "Fallback straight-line");

            log.warn("⚠️ FALLBACK USED: distance={}m, duration={}s",
                    meters, durationSec);

            return ResponseEntity.ok(fallback);

        } catch (Exception e) {
            log.error("planTrip error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // --- Helper methods below (kept intact) ---

    private double[] snapToNearestRoad(double lat, double lon) {
        try {
            Map<String, Object> root = nominatimReverse(lat, lon);
            if (isRoutablePlace(root)) {
                return new double[]{lat, lon};
            }

            int[] radii = new int[]{100, 300, 600, 1000, 2000};
            for (int r : radii) {
                for (int i = 0; i < 8; i++) {
                    double angle = Math.toRadians(i * 45.0);
                    double dx = r * Math.cos(angle);
                    double dy = r * Math.sin(angle);

                    double candLat = lat + (dy / 111000.0);
                    double lonFactor = 111000.0 * Math.cos(Math.toRadians(lat));
                    double candLon = lon + (dx / lonFactor);

                    Map<String, Object> cand = nominatimReverse(candLat, candLon);
                    if (isRoutablePlace(cand)) {
                        if (cand != null && cand.containsKey("lat") && cand.containsKey("lon")) {
                            double snappedLat = Double.parseDouble(cand.get("lat").toString());
                            double snappedLon = Double.parseDouble(cand.get("lon").toString());
                            return new double[]{snappedLat, snappedLon};
                        } else {
                            return new double[]{candLat, candLon};
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("snapToNearestRoad error", e);
        }
        return null;
    }

    private Map<String, Object> nominatimReverse(double lat, double lon) {
        try {
            String url = String.format(
                    "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=%s&lon=%s&zoom=18&addressdetails=1",
                    lat, lon);

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", nominatimUserAgent);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> resp = rest.exchange(url, HttpMethod.GET, entity, Map.class);
            if (resp.getStatusCode().is2xxSuccessful()) {
                @SuppressWarnings("unchecked")
                Map<String, Object> body = resp.getBody();
                return body;
            }
        } catch (Exception e) {
            log.warn("Nominatim reverse exception", e);
        }
        return null;
    }

    private boolean isRoutablePlace(Map<String, Object> place) {
        if (place == null) return false;
        Object cls = place.get("class");
        Object typ = place.get("type");
        if (cls != null && "highway".equals(cls.toString())) return true;
        if (typ != null) {
            String t = typ.toString();
            List<String> goodTypes = Arrays.asList(
                    "residential", "service", "road", "tertiary", "secondary", "primary",
                    "unclassified", "track", "footway", "pedestrian", "path"
            );
            if (goodTypes.contains(t)) return true;
        }
        return false;
    }

    private Map<String, Object> buildResponseFromORS(Map<String, Object> raw, boolean roundTrip) {
        Map<String, Object> out = new HashMap<>();
        if (raw == null) {
            log.warn("buildResponseFromORS: raw is null");
            return out;
        }

        log.info("buildResponseFromORS: Processing response with keys: {}", raw.keySet());
        try {
            if (raw.containsKey("features")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> features = (List<Map<String, Object>>) raw.get("features");
                if (!features.isEmpty()) {
                    Map<String, Object> f0 = features.get(0);
                    @SuppressWarnings("unchecked")
                    Map<String, Object> geometry = (Map<String, Object>) f0.get("geometry");
                    @SuppressWarnings("unchecked")
                    Map<String, Object> properties = (Map<String, Object>) f0.get("properties");

                    Double distance = null;
                    Double duration = null;

                    if (properties != null) {
                        Object summaryObj = properties.get("summary");
                        if (summaryObj instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> summary = (Map<String, Object>) summaryObj;
                            distance = asDouble(summary.get("distance"));
                            duration = asDouble(summary.get("duration"));
                        }
                    }

                    if (distance == null) distance = 0.0;
                    if (duration == null) duration = 0.0;

                    if (roundTrip) {
                        distance *= 2;
                        duration *= 2;
                    }

                    addCosts(out, geometry, distance, duration, roundTrip);
                    return out;
                }
            }

            if (raw.containsKey("routes")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> routes = (List<Map<String, Object>>) raw.get("routes");
                if (!routes.isEmpty()) {
                    Map<String, Object> r0 = routes.get(0);

                    Map<String, Object> geometry;
                    Double distance = asDouble(r0.get("distance"));
                    Double duration = asDouble(r0.get("duration"));

                    Object geomObj = r0.get("geometry");
                    if (geomObj instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> geometryMap = (Map<String, Object>) geomObj;
                        geometry = geometryMap;
                    } else {
                        geometry = new HashMap<>();
                    }

                    if (distance == null) distance = 0.0;
                    if (duration == null) duration = 0.0;

                    if (roundTrip) {
                        distance *= 2;
                        duration *= 2;
                    }

                    addCosts(out, geometry, distance, duration, roundTrip);
                    return out;
                }
            }
        } catch (Exception e) {
            log.warn("Error parsing ORS response", e);
        }
        return out;
    }

    private Double asDouble(Object o) {
        if (o == null) return 0.0;
        if (o instanceof Number) return ((Number) o).doubleValue();
        try { return Double.parseDouble(o.toString()); }
        catch (Exception e) { return 0.0; }
    }

    private void addCosts(Map<String, Object> out, Map<String, Object> geometry,
                          double distanceMeters, double durationSeconds, boolean roundTrip) {
        double fuelEfficiency = 15.0;
        double fuelPrice = 100.99;
        double fuelCost = (distanceMeters / 1000.0 / fuelEfficiency) * fuelPrice;
        double tollCost = (distanceMeters / 1000.0) * 2.0;

        out.put("geometry", geometry);
        out.put("distance", distanceMeters);
        out.put("duration", durationSeconds);
        out.put("fuelCost", fuelCost);
        out.put("tollCost", tollCost);
        out.put("roundTrip", roundTrip);
    }

    private double toDouble(Object o) {
        if (o == null) return 0.0;
        if (o instanceof Number) return ((Number) o).doubleValue();
        try { return Double.parseDouble(o.toString()); }
        catch (Exception e) { return 0.0; }
    }

    private double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon/2) * Math.sin(dLon/2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }

    private String safeTruncate(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max) + "...";
    }
}
