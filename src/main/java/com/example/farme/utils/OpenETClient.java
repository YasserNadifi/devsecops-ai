package com.example.farme.utils;

import com.example.farme.Dto.OpenETRequestBody;
import com.example.farme.Dto.OpenETResponse;
import com.example.farme.model.Coordinate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpenETClient {

    @Value("${openet.api.key}")
    private String apiKey;

    @Value("${openet.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public OpenETClient() {
        this.restTemplate = new RestTemplate();
    }

    public List<Double> getDailyETData(List<Coordinate> coordinates) {
        double avgLat = coordinates.stream().mapToDouble(Coordinate::getLatitude).average().orElse(0.0);
        double avgLon = coordinates.stream().mapToDouble(Coordinate::getLongitude).average().orElse(0.0);

        OpenETRequestBody body = new OpenETRequestBody();
        body.setDate_range(List.of("2025-04-08", "2025-04-14"));
        body.setGeometry(List.of(avgLon, avgLat));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", apiKey);

        HttpEntity<OpenETRequestBody> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<OpenETResponse> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    OpenETResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody().getData()
                        .stream()
                        .map(OpenETResponse.DataPoint::getEt)
                        .collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
