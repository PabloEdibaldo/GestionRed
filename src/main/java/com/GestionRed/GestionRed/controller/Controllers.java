package com.GestionRed.GestionRed.controller;
import com.GestionRed.GestionRed.dto.RouterRequest;
import com.GestionRed.GestionRed.dto.RouterResponse;
import com.GestionRed.GestionRed.dto.dtoMonitoring.MonitoringRequest;
import com.GestionRed.GestionRed.dto.dtoMonitoring.MonitoringResponse;
import com.GestionRed.GestionRed.dto.dtoNetworkAccessPoint.NetworkAccessPointRequest;
import com.GestionRed.GestionRed.dto.dtoNetworkAccessPoint.NetworkAccessPointResponse;
import com.GestionRed.GestionRed.dto.dtoNetworkAccessPoint.dtoPort.PortRequest;
import com.GestionRed.GestionRed.dto.dtoNetworkAccessPoint.dtoPort.nameUserRequest;
import com.GestionRed.GestionRed.dto.dtoQueriesFromOtherMicroservices.QueriesFromOtherMicroservicesRequest;
import com.GestionRed.GestionRed.dto.dtoRedInfo.RedInformationResponse;
import com.GestionRed.GestionRed.dto.dtoRedIpv4.RedIpv4Request;
import com.GestionRed.GestionRed.dto.dtoRedIpv4.RedIpv4Response;
import com.GestionRed.GestionRed.model.Router;
import com.GestionRed.GestionRed.repository.RouterRepository;
import com.GestionRed.GestionRed.services.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.legrange.mikrotik.MikrotikApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@RequiredArgsConstructor
@RestController
@RequestMapping("api/redIpv4")
class RedIpv4Controller {
    private final RedIpv4Service redIpv4Service;
    private final RouterService routerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createRedIpv4(@RequestBody RedIpv4Request redIpv4Request){
        redIpv4Service.createRedIpv4(redIpv4Request);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<RedIpv4Response>> getAllRouters(){
        return redIpv4Service.getAllRedIpv4();
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateRedIpv4(@PathVariable Long id,@RequestBody RedIpv4Request redIpv4Request) throws UnknownHostException {
        redIpv4Service.updateRedIpv4(id, redIpv4Request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRedIpv4(@PathVariable Long id){
        redIpv4Service.deleteRedIpv4(id);
    }

    //-----------------------------------------Get info red (CIDR)----------------------------------------
    @GetMapping("/calculateCIDR")
    @ResponseStatus(HttpStatus.OK)
    public RedInformationResponse calculateCIDR(@RequestParam String ip, @RequestParam String cidr) throws UnknownHostException {
        return redIpv4Service.createRed(ip,cidr,false,null);
    }
    @GetMapping("/ping")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> pingIp(@RequestParam String ip) throws UnknownHostException {
        return redIpv4Service.pingRedIpv4(ip);
    }
}

@RestController
@RequestMapping("api/routers")
@RequiredArgsConstructor
class RouterController {
    private final RouterService routerService;
    private final RouterRepository routerRepository;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createRouter(@RequestBody RouterRequest routerRequest){
        routerService.createRouter(routerRequest);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RouterResponse> getAllRouters(){
        return routerService.getAllRouters();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateRouter(@PathVariable Long id,@RequestBody RouterRequest routerRequest){
        routerService.updateRouter(id, routerRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRouter(@PathVariable Long id){
        routerService.deleteRouter(id);
    }
    @GetMapping("system/reboot")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Map<String,String>>> rebootSystem(@RequestParam String ip,
                                                                 @RequestParam String admin,
                                                                 @RequestParam String password){
        try{
            List<Map<String,String>> resourceData = routerService.systemResourcePrint(ip,admin,password,"/interface/print where type=ether or type=vlan");
            return ResponseEntity.ok(resourceData);
        }catch (MikrotikApiException e){
            throw  new RuntimeException(e);
        }
    }
    @GetMapping("/router/info")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> RouterInfo(@RequestParam Long idRouter) throws MikrotikApiException, IOException {
/*
*
*
*
        Optional<Router> router = routerRepository.findById(idRouter);
        if(router.isPresent()){
            Router existingRouter = router.get();
            MonitoringInterfaces(interfaceMonitoring,existingRouter.getIpAddress(),existingRouter.getUserMikrotik(),existingRouter.getPassword());
        }
 */
        return routerService.ConfigRouter(idRouter);
    }
    @GetMapping("/MonitoringInterfaces")
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String,String>> MonitoringInterfaces(@RequestParam String interfaceMonitoring,
                                     @RequestParam String ip,
                                     @RequestParam String userMikrotik,
                                     @RequestParam String password) throws MikrotikApiException {
        return routerService.trafficMonitoringInterfaces(interfaceMonitoring, ip, userMikrotik, password);
    }

    //-----------------------------------------Query to obtain tha name of a router-----------------------------

}

@RestController
@RequestMapping("api/monitoring")
@RequiredArgsConstructor
class MonitoringController{
    private final MonitoringService monitoringService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createMonitoring(@RequestBody MonitoringRequest monitoringRequest){
        monitoringService.createMonitoring(monitoringRequest);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MonitoringResponse> getAllMonitoring(){
        return monitoringService.getAllMonitoring();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateMonitoring(@PathVariable Long id,@RequestBody MonitoringRequest monitoringRequest){
        monitoringService.updateMonitoring(id, monitoringRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMonitoring(@PathVariable Long id){
        monitoringService.deleteMonitoring(id);
    }
}



@Slf4j
@RestController
@RequestMapping("api/box")
@RequiredArgsConstructor
class BoxController{
    private final NetworkAccessPointService networkAccessPointService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNetworkAccessPoint(@RequestBody NetworkAccessPointRequest networkAccessPointRequest){
        networkAccessPointService.createNetworkAccessPoint(networkAccessPointRequest);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NetworkAccessPointResponse> getAllNetworkAccessPoint(){
        return networkAccessPointService.getallNetworkAccessPoint();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateNetworkAccessPoint(@PathVariable Long id,@RequestBody NetworkAccessPointRequest networkAccessPointRequest){
        networkAccessPointService.updateNetworkAccessPoint(id, networkAccessPointRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNetworkAccessPoint(@PathVariable Long id){
        networkAccessPointService.deleteNetworkAccessPoint(id);
    }
    //---------------------------------------------Check the ports of each box--------------------------------------------------
    @GetMapping("/ports/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> Ports (@PathVariable Long id ){
        return networkAccessPointService.portsAvailableByNapBox(id);
    }

    @GetMapping("/consultBox/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean consultBox(@PathVariable Long id){
        return networkAccessPointService.consultNAP(id);
    }

    @PostMapping("/userAssignedPort")
    @ResponseStatus(HttpStatus.OK)
    public Boolean postPort(@RequestBody PortRequest portRequest){
        log.info("informacion de otro microservicio:{}",portRequest);
        return networkAccessPointService.putPort(portRequest.getBoxNap(),portRequest.getPortNumber(),portRequest.getNameClient());
    }
    @PostMapping("/EditPortNap")
    @ResponseStatus(HttpStatus.OK)
    public Boolean editPortNap(@RequestBody nameUserRequest nameUserRequest){
        log.info("Nombre del cliente: {}",nameUserRequest.getNameUser());
        return networkAccessPointService.editPortUser(nameUserRequest.getNameUser(),nameUserRequest.getIdNap());
    }
}

@Slf4j
@RestController
@RequestMapping("api/QueriesFromOtherMicroservices")
@AllArgsConstructor
class QueriesFromOtherMicroservices{
    private final QueriesFromOtherMicroservicesService queriesFromOtherMicroservicesService;

    //-------------------------create promotion provided by Users microservice---------------------------
  /*  @PostMapping("/createPromotion")
    @ResponseStatus(HttpStatus.OK)
    public void  createPromotion(@RequestBody QueriesFromOtherMicroservicesRequest.PromotionRequest promotionRequest) throws MikrotikApiException {
        log.info("Promotion:{}",promotionRequest);
        queriesFromOtherMicroservicesService.createQueueForPromotion(promotionRequest);
    }*/
    //----------------------------------------assign promotion client -----------------------------------------------------
    @PostMapping("/assignPromotionClient")
    @ResponseStatus(HttpStatus.OK)
    public Object assignPromotionClient(@RequestBody QueriesFromOtherMicroservicesRequest.AssignPromotionRequest assignPromotionRequest) throws MikrotikApiException {
        log.info("assign promotion client (data):{}",assignPromotionRequest);
        return queriesFromOtherMicroservicesService.
                InteractionWithTheSwitch(
                        1,
                        assignPromotionRequest.getIdRouter(),
                        assignPromotionRequest,
                        null,
                        null,
                        null);
    }
    //----------------------------------------Create client PPPoE-----------------------------------------------------
    @PostMapping("/createClientPPPoE")
    @ResponseStatus(HttpStatus.OK)
    public Object createClientPPPoE(@RequestBody QueriesFromOtherMicroservicesRequest.ClientPPPoERequest clientPPPoERequest) throws MikrotikApiException {
        log.info("Client desde el otro microservicio:{}",clientPPPoERequest);
        return queriesFromOtherMicroservicesService.
                InteractionWithTheSwitch(
                        2,
                        clientPPPoERequest.getIdRouter(),
                        null,
                        clientPPPoERequest,
                        null,
                        null);
    }
    //----------------------------------------Remove customer from the promotions list to assign them to a PPPoE profile-----------------------------
    @PostMapping("/packageChangeOfPPPClient")
    @ResponseStatus(HttpStatus.OK)
    public Object packageChangeOfPPPClient(@RequestBody QueriesFromOtherMicroservicesRequest.DeleteClientInListPromotion deleteClientInListPromotion)throws MikrotikApiException{
        return queriesFromOtherMicroservicesService.
                InteractionWithTheSwitch(
                        3,
                        deleteClientInListPromotion.getIdRouter(),
                        null,
                        null,
                        deleteClientInListPromotion,
                        null);
    }

    //----------------------------------------Cut off the client's internet-----------------------------
    @PostMapping("/cutServiceClient")
    @ResponseStatus(HttpStatus.OK)
    public Object CutServiceClient(@RequestBody QueriesFromOtherMicroservicesRequest.CutServiceClientRequest cutServiceClientRequest)throws MikrotikApiException{

        return queriesFromOtherMicroservicesService.
                InteractionWithTheSwitch(
                        4,
                        cutServiceClientRequest.getIdRouter(),
                        null,
                        null,
                        null,
                        cutServiceClientRequest);


    }

}
