package com.GestionRed.GestionRed.services;


import com.GestionRed.GestionRed.dto.dtoMonitoring.MonitoringRequest;
import com.GestionRed.GestionRed.dto.dtoMonitoring.MonitoringResponse;
import com.GestionRed.GestionRed.model.Monitoring;
import com.GestionRed.GestionRed.repository.MonitoringRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonitoringService {

    private final MonitoringRepository monitoringRepository;


public void createMonitoring(@NonNull MonitoringRequest monitoringRequest){

    Monitoring monitoring = Monitoring.builder()
            .issuer_name(monitoringRequest.getIssuer_name())
            .direction_ip(monitoringRequest.getDirection_ip())
            .maker(monitoringRequest.getMaker())
            .name_model(monitoringRequest.getName_model())
            .user(monitoringRequest.getUser())
            .password(monitoringRequest.getPassword())
            .monitoring_SNMP(monitoringRequest.getMonitoring_SNMP())
            .community_SNMP(monitoringRequest.getCommunity_SNMP())
            .version_SNMP(monitoringRequest.getVersion_SNMP())
            .build();
    monitoringRepository.save(monitoring);
    log.info("Monitoring {} in saved",monitoring.getId());
}
public List<MonitoringResponse> getAllMonitoring(){
    List<Monitoring> monitorings = monitoringRepository.findAll();
    return monitorings.stream().map(this::mapToMonitoringResponse).toList();
}
private MonitoringResponse mapToMonitoringResponse(@NonNull Monitoring monitoring) {
    return MonitoringResponse.builder()
            .id(monitoring.getId())
            .issuer_name(monitoring.getIssuer_name())
            .direction_ip(monitoring.getDirection_ip())
            .maker(monitoring.getMaker())
            .name_model(monitoring.getName_model())
            .user(monitoring.getUser())
            .password(monitoring.getPassword())
            .monitoring_SNMP(monitoring.getMonitoring_SNMP())
            .community_SNMP(monitoring.getCommunity_SNMP())
            .version_SNMP(monitoring.getVersion_SNMP())
            .build();
}
public  void updateMonitoring(Long id, MonitoringRequest monitoringRequest){
    Optional<Monitoring> optionalMonitoring = monitoringRepository.findById(id);

    if(optionalMonitoring.isPresent()){
        Monitoring existingMonitoring = optionalMonitoring.get();

        existingMonitoring.setIssuer_name(monitoringRequest.getIssuer_name());
        existingMonitoring.setDirection_ip(monitoringRequest.getDirection_ip());
        existingMonitoring.setMaker(monitoringRequest.getMaker());
        existingMonitoring.setName_model(monitoringRequest.getName_model());
        existingMonitoring.setUser(monitoringRequest.getUser());
        existingMonitoring.setPassword(monitoringRequest.getPassword());
        existingMonitoring.setMonitoring_SNMP(monitoringRequest.getMonitoring_SNMP());
        existingMonitoring.setCommunity_SNMP(monitoringRequest.getCommunity_SNMP());
        existingMonitoring.setVersion_SNMP(monitoringRequest.getVersion_SNMP());

        monitoringRepository.save(existingMonitoring);

        log.info("Monitoring {} updated ",id);
    }else {
        log.warn("Monitoring with ID {} not found", id);
    }
}
public void deleteMonitoring(Long id){
    Optional<Monitoring> optionalMonitoring = monitoringRepository.findById(id);

    if(optionalMonitoring.isPresent()){
        monitoringRepository.deleteById(id);
    }else {
        log.warn("Router with ID {} not found", id);
    }
}

}
