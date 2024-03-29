package com.GestionRed.GestionRed.services;

import com.GestionRed.GestionRed.dto.dtoNetworkAccessPoint.NetworkAccessPointRequest;
import com.GestionRed.GestionRed.dto.dtoNetworkAccessPoint.NetworkAccessPointResponse;
import com.GestionRed.GestionRed.model.BoxNap;
import com.GestionRed.GestionRed.model.Port;
import com.GestionRed.GestionRed.repository.NetworkAccessPointRepository;
import com.GestionRed.GestionRed.repository.PortRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NetworkAccessPointService {
    //-----------------------------------------------------------------------Start CRUD-----------------------------------------------------
    private final NetworkAccessPointRepository networkAccessPointRepository;
    private final PortRepository portRepository;


    //Return list Nap's
    public List<NetworkAccessPointResponse> getallNetworkAccessPoint(){
        List<BoxNap> boxNapList = networkAccessPointRepository.findAll();
        return boxNapList.stream().map(this::mapToNetworkAccessPoint).toList();
    }

    //map entity

    private NetworkAccessPointResponse mapToNetworkAccessPoint(@NonNull BoxNap boxNap) {

        return NetworkAccessPointResponse.builder()
                .id(boxNap.getId())
                .name(boxNap.getName())
                .coordinates(boxNap.getCoordinates())
                .location(boxNap.getLocation())
                .ports(boxNap.getPorts())
                .details(boxNap.getDetails())
                .build();
    }
    //Create NAP
    public void createNetworkAccessPoint(@NonNull NetworkAccessPointRequest networkAccessPointRequest){

        BoxNap boxNap = BoxNap.builder()
                .name(networkAccessPointRequest.getName())
                .coordinates(networkAccessPointRequest.getCoordinates())
                .location(networkAccessPointRequest.getLocation())
                .ports(networkAccessPointRequest.getPorts())
                .details(networkAccessPointRequest.getDetails())
                .build();
        //are saved in the database
        networkAccessPointRepository.save(boxNap);

        //creating nap box ports
        for (int portNumber=1;portNumber<= networkAccessPointRequest.getPorts();portNumber++){
            log.info("port number {}",portNumber);
            //initial values are assigned
            Port port = Port.builder().boxNap(boxNap).portNumber(portNumber).status(0).build();
            //are saved in the database
            portRepository.save(port);
        }
        log.info("NetworkAccessPoint {} is saved",boxNap.getId());
    }


    //update nap box
    public void updateNetworkAccessPoint(Long id, NetworkAccessPointRequest networkAccessPointRequest){
        //query the nap box bi id
        Optional<BoxNap> optionalBoxNap = networkAccessPointRepository.findById(id);
        //check if object is present in the database
        if(optionalBoxNap.isPresent()){
            //get you fields
            BoxNap existingBoxNap = optionalBoxNap.get();

            existingBoxNap.setName(networkAccessPointRequest.getName());
            existingBoxNap.setCoordinates(networkAccessPointRequest.getCoordinates());
            existingBoxNap.setLocation(networkAccessPointRequest.getLocation());
            existingBoxNap.setPorts(networkAccessPointRequest.getPorts());
            existingBoxNap.setDetails(networkAccessPointRequest.getDetails());
            //updated fields are saved
            networkAccessPointRepository.save(existingBoxNap);
            //creating a port list to filter the ports of the nap box with the "equals" function
            List<Port> ports = portRepository.findAll().stream().filter(x-> Objects.equals(x.getBoxNap().getId(), existingBoxNap.getId())).toList();

            //
                for (Port portsDelete : ports) {
                    Long idPort = portsDelete.getId();
                    portRepository.deleteById(idPort);
                }
                for (int portNumber=1;portNumber<= networkAccessPointRequest.getPorts();portNumber++){
                    log.info("port number {}",portNumber);

                    Port port = Port.builder().boxNap(existingBoxNap).portNumber(portNumber).status(0).build();
                    portRepository.save(port);
                }
            log.info("NetworkAccessPoint {} updated" , id);
        }else {
            log.warn("NetworkAccessPoint with ID {} not found", id);
        }
    }
    public void deleteNetworkAccessPoint(Long id){
        //make a query with the object id
        Optional<BoxNap> optionalBoxNap = networkAccessPointRepository.findById(id);
        //if exist
        if(optionalBoxNap.isPresent()){
            //create a list to filter the ips through the object id
            List<Port> ports = portRepository.findAll().stream().filter(x-> Objects.equals(x.getBoxNap().getId(), id)).toList();

            for (Port portsDelete:ports){
                Long idPort =portsDelete.getId();
                portRepository.deleteById(idPort);
            }
            networkAccessPointRepository.deleteById(id);
        }else{
            log.warn("NetworkAccessPoint with ID {} not found", id);
        }
    }
    //-----------------------------------------------------------------------end CRUD-----------------------------------------------------
    //Ports Query
    public List<Integer> portsAvailableByNapBox(Long id){
        try {
            Optional<BoxNap> optionalBoxNap = networkAccessPointRepository.findById(id);
            if(optionalBoxNap.isEmpty()){
                return Collections.emptyList();
            }
            return portRepository.findAll()
                    .stream().filter(port -> port.getBoxNap().getId().equals(id) && port.getStatus()==0).map(Port::getPortNumber).collect(Collectors.toList());
        }catch (Exception e){
            return Collections.emptyList();
        }
    }

    //NAP query
    public boolean consultNAP(Long id){
        Optional<BoxNap> optionalBoxNap = networkAccessPointRepository.findById(id);
        return optionalBoxNap.isPresent();
    }
    public Boolean putPort(Long id_port, int port, String nameClient){

        List<Port> ports = portRepository.findAll().stream().filter(x-> Objects.equals(x.getBoxNap().getId(), id_port)).toList();
        for (Port portFilter:ports){

            if (portFilter.getPortNumber() == port && portFilter.getStatus()==0){

                portFilter.setStatus(1);
                portFilter.setNameClient(nameClient);
                portRepository.save(portFilter);
                return true;
                        //ResponseEntity.status(HttpStatus.OK).body("port assigned to user:"+portFilter.getPortNumber());
            }
        }
        //Map<String,String> errorResponse = new HashMap<>();
        //errorResponse.put("error","port in use or not found");
        return false;
              /*
              * ResponseEntity.
                status(HttpStatus.NOT_FOUND).
                contentType(MediaType.APPLICATION_JSON).
                body(errorResponse);
              * */
    }

    public Boolean editPortUser(String nameUser,Long idBox){
        List<Port> ports = portRepository.findAll().stream().filter(x-> Objects.equals(x.getBoxNap().getId(), idBox)).toList();
        for (Port portFilter:ports){
            if (Objects.equals(portFilter.getNameClient(),nameUser)){

                portFilter.setStatus(0);
                portFilter.setNameClient(null);
                portRepository.save(portFilter);
                log.info("Client Delete");
                return true;
            }
        }
        return false;
    }
}
