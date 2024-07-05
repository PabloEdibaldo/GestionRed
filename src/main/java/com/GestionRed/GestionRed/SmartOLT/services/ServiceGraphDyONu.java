package com.GestionRed.GestionRed.SmartOLT.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceGraphDyONu {
    private final ServiceConfigOnus serviceConfigOnus;

    public ResponseEntity<Map<String,byte[]>> GetONUSignalGraphDyONuUniqueExternalID(@RequestParam  String url){
        String[] timeIntervals = {"hourly","daily","weekly","monthly","yearly"};

        List<CompletableFuture<Map.Entry<String, byte[]>>> futures = Arrays.stream(timeIntervals)
                .map(timeStr -> CompletableFuture.supplyAsync(() -> {
                    try {
                        byte[] imageData = serviceConfigOnus.getONUSignalGraph(timeStr,url);
                        return Map.entry(timeStr, imageData);
                    } catch (Exception e) {
                        log.error("Error fetching image for {} interval: {}", timeStr, e.getMessage());
                        return Map.entry(timeStr, new byte[0]); // Retorna un array vacío en caso de error
                    }
                }))
                .toList();

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        Map<String, byte[]> result = allFutures.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        ).join();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);


    }

}
