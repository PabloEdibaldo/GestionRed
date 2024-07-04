package com.GestionRed.GestionRed.SmartOLT.services;

import com.GestionRed.GestionRed.SmartOLT.models.OnuDetailsCache;
import com.GestionRed.GestionRed.SmartOLT.repository.OnuDetailsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class OnuDetailsCacheService {

    private final OnuDetailsRepository onuDetailsRepository;
    private final ServiceConfigOnus serviceConfigOnus;

    public void saveOrUpdateCache(String responseData) {
        OnuDetailsCache cache = onuDetailsRepository.findById(1L).orElse(new OnuDetailsCache());
        cache.setResponseData(responseData);
        cache.setTimestamp(LocalDateTime.now());
        onuDetailsRepository.save(cache);
        log.info("Data saved to database successfully.");
    }

    public Object getCacheResponseData() throws JsonProcessingException {
        Logger log = LoggerFactory.getLogger(this.getClass());
        ObjectMapper mapper = new ObjectMapper();

        Optional<String> response = onuDetailsRepository.findById(1L).map(OnuDetailsCache::getResponseData);

        if (response.isPresent()) {
            String rawData = response.get();

            String cleanedData = rawData
                    .replaceFirst("<200 OK OK,", "")
                    .replaceFirst(", status=true, response_code=success},.+", "}")
                    .replaceAll("=", ":")
                    .replaceAll("onus:", " ");

            String modified = cleanedData.replaceAll("([a-zA-Z_]+):([^\\s,\\[\\]{}\"]+)", "\"$1\":\"$2\"")
                    .replaceAll(": null", ":null")
                    .replaceAll(": ", ":\"\"");
            String correctedJson = correctCommonIssues(modified);

            return correctedJson.replaceAll("\"(\\d{4}-\\d{2}-\\d{2}) \"(\\d{2})\"\":\\s*\"(\\d{2})\":\\s*(\\d{2})", "\"$1 $2:$3:$4\"")
                    .replaceAll("\"([^\"]+)\":\\s*,", "\"$1\": null,")
                    .replaceAll("\"([^\"]+)\":\\s*}", "\"$1\": null}");





        } else {
            log.error("No data found for ONU ID 1");
            return null;  // Devuelve un JSON vacío en caso de que no haya datos
        }
    }

    private static String correctCommonIssues(  String badJSON) {
        badJSON = badJSON.trim();

        // Manejar el caso específico de "{ [" al inicio
        if (badJSON.startsWith("{ [")) {
            badJSON = badJSON.substring(2).trim(); // Eliminar '{ ['
        }

        // Manejar el caso específico de caracteres extra al final
        while (badJSON.endsWith("]}") && badJSON.charAt(badJSON.lastIndexOf("]") - 1) == '}') {
            badJSON = badJSON.substring(0, badJSON.length() - 1).trim();
        }
        badJSON = badJSON.replaceAll("'", "\"");
        badJSON = badJSON.replaceAll(": ,", ": \"\",");
        badJSON = badJSON.replaceAll(": null", ": null");
        badJSON = badJSON.replaceAll("\\[\\s*\\]", "[]");
        badJSON = badJSON.replaceAll("(\\w+):", "\"$1\":");
        badJSON = badJSON.replaceAll(": ([^\\s\"\\{\\[]+)", ": \"$1\"");
        badJSON = badJSON.replaceAll("\\s+", " ");
        badJSON = badJSON.replaceAll("\"([^\"]+)\"\\s*\\[([^\\]]+)\\]", "\"$1 [$2]\"");
        badJSON = badJSON.replaceAll(":\\s*,", ": null,");
        badJSON = badJSON.replaceAll("\"([^\"]+)\"\\s+([^,:\\}]+)", "\"$1 $2\"");
        badJSON = badJSON.replaceAll("\"([^\"]+)\"\\s+([^,:\\}]+)\\s+([^,:\\}]+)\\s+([^,:\\}]+)", "\"$1 $2 $3 $4\"");
        badJSON = badJSON.replaceAll("\"(\\d{4}-\\d{2}-\\d{2})\"\\s+\"(\\d{2})\":\\s+\"(\\d{2})\":\\s+(\\d{2})", "\"$1 $2:$3:$4\"");

        return badJSON;


    }

        public Object getAllOnusDetails() {
           return  serviceConfigOnus.OptionCase(
                    1,
                    "/onu/get_all_onus_details",
                    null);
    }
    public void updateCache() {
            Object responseData = getAllOnusDetails();
            if (responseData != null) {
                saveOrUpdateCache(responseData.toString());
            }
    }



}
