package com.GestionRed.GestionRed.SmartOLT.services;

import com.GestionRed.GestionRed.SmartOLT.models.OnuDetailsCache;
import com.GestionRed.GestionRed.SmartOLT.repository.OnuDetailsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public String getCacheResponseData() {
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

            try {
                // Intenta parsear el JSON corregido
                JsonNode jsonNode;
                if (correctedJson.trim().startsWith("{")) {
                    // Si comienza con '{', es un objeto
                    jsonNode = mapper.readTree(correctedJson);
                } else if (correctedJson.trim().startsWith("[")) {
                    // Si comienza con '[', es un array
                    jsonNode = mapper.readTree("{\"data\":" + correctedJson + "}");
                } else {
                    // Si no comienza ni con '{' ni con '[', lo envolvemos en un objeto
                    jsonNode = mapper.readTree("{\"data\":" + correctedJson + "}");
                }

                // Si el parseo es exitoso, devuelve el JSON formateado
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
            } catch (JsonProcessingException e) {
                log.error("Error parsing JSON: " + e.getMessage());
                // Si hay un error en el parseo, intenta una corrección más agresiva
                correctedJson = "{\"data\":" + correctedJson.replaceAll("^\\s*\\{?\\s*", "").replaceAll("\\s*\\}?\\s*$", "") + "}";
                try {
                    JsonNode jsonNode = mapper.readTree(correctedJson);
                    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
                } catch (JsonProcessingException e2) {
                    log.error("Error parsing JSON after aggressive correction: " + e2.getMessage());
                    // Si aún hay un error, devuelve el JSON corregido sin formatear
                    return correctedJson;
                }
            }
        } else {
            log.error("No data found for ONU ID 1");
            return "{}";  // Devuelve un JSON vacío en caso de que no haya datos
        }
    }

    private static String correctCommonIssues(  String badJSON) {
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
        badJSON = badJSON.replaceAll("\"([^\"]+)\"\\s+([^,:\\}]+)", "\"$1 $2\"");
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
