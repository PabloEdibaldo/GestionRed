package com.GestionRed.GestionRed.services;

import com.GestionRed.GestionRed.dto.dtoMonitoring.MonitoringResponse;
import com.GestionRed.GestionRed.dto.dtoNetworkAccessPoint.NetworkAccessPointRequest;
import com.GestionRed.GestionRed.dto.dtoNetworkAccessPoint.NetworkAccessPointResponse;
import com.GestionRed.GestionRed.model.BoxNap;
import com.GestionRed.GestionRed.repository.NetworkAccessPointRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NetworkAccessPointService {
    private final NetworkAccessPointRepository networkAccessPointRepository;

    public List<NetworkAccessPointResponse> getallNetworkAccessPoint(){
        List<BoxNap> boxNapList = networkAccessPointRepository.findAll();
        return boxNapList.stream().map(this::mapToNetworkAccessPoint).toList();
    }

    private NetworkAccessPointResponse mapToNetworkAccessPoint(BoxNap boxNap) {
        return NetworkAccessPointResponse.builder()
                .id(boxNap.getId())
                .name(boxNap.getName())
                .coordinates(boxNap.getCoordinates())
                .location(boxNap.getLocation())
                .ports(boxNap.getPorts())
                .details(boxNap.getDetails())
                .build();
    }
    public void createNetworkAccessPoint(@NonNull NetworkAccessPointRequest networkAccessPointRequest){
        BoxNap boxNap = BoxNap.builder()
                .name(networkAccessPointRequest.getName())
                .coordinates(networkAccessPointRequest.getCoordinates())
                .location(networkAccessPointRequest.getLocation())
                .ports(networkAccessPointRequest.getPorts())
                .details(networkAccessPointRequest.getDetails())
                .build();
        networkAccessPointRepository.save(boxNap);
        log.info("NetworkAccessPoint {} is saved",boxNap.getId());
    }
    public void updateNetworkAccessPoint(Long id, NetworkAccessPointRequest networkAccessPointRequest){
        Optional<BoxNap> optionalBoxNap = networkAccessPointRepository.findById(id);
        if(optionalBoxNap.isPresent()){
            BoxNap existingBoxNap = optionalBoxNap.get();

            existingBoxNap.setName(networkAccessPointRequest.getName());
            existingBoxNap.setCoordinates(networkAccessPointRequest.getCoordinates());
            existingBoxNap.setLocation(networkAccessPointRequest.getLocation());
            existingBoxNap.setPorts(networkAccessPointRequest.getPorts());
            existingBoxNap.setDetails(networkAccessPointRequest.getDetails());

            networkAccessPointRepository.save(existingBoxNap);
            log.info("NetworkAccessPoint {} updated" , id);
        }else {
            log.warn("NetworkAccessPoint with ID {} not found", id);
        }
    }

    public void deleteNetworkAccessPoint(Long id){
        Optional<BoxNap> optionalBoxNap = networkAccessPointRepository.findById(id);
        if(optionalBoxNap.isPresent()){
            networkAccessPointRepository.deleteById(id);
        }else{
            log.warn("NetworkAccessPoint with ID {} not found", id);
        }
    }


}
