package com.GestionRed.GestionRed.controller;
import com.GestionRed.GestionRed.dto.RouterRequest;
import com.GestionRed.GestionRed.dto.RouterResponse;
import com.GestionRed.GestionRed.dto.dtoMonitoring.MonitoringRequest;
import com.GestionRed.GestionRed.dto.dtoMonitoring.MonitoringResponse;
import com.GestionRed.GestionRed.dto.dtoNetworkAccessPoint.NetworkAccessPointRequest;
import com.GestionRed.GestionRed.dto.dtoNetworkAccessPoint.NetworkAccessPointResponse;
import com.GestionRed.GestionRed.dto.dtoRedInfo.RedInformationResponse;
import com.GestionRed.GestionRed.dto.dtoRedIpv4.RedIpv4Request;
import com.GestionRed.GestionRed.dto.dtoRedIpv4.RedIpv4Response;
import com.GestionRed.GestionRed.model.Router;
import com.GestionRed.GestionRed.services.MonitoringService;
import com.GestionRed.GestionRed.services.NetworkAccessPointService;
import com.GestionRed.GestionRed.services.RedIpv4Service;
import com.GestionRed.GestionRed.services.RouterService;
import lombok.RequiredArgsConstructor;
import me.legrange.mikrotik.MikrotikApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        String routerName = redIpv4Request.getName_router();

        Optional<Router> optionalRouter = routerService.getRouterByName(routerName);
        if (optionalRouter.isPresent()) {
            Router router = optionalRouter.get();
            String routerIpAddress = router.getIpAddress();
            String routerUserMikrotik = router.getUserMikrotik();
            String routerPassword = router.getPassword();

            redIpv4Service.createRedIpv4(redIpv4Request);

        }
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<RedIpv4Response>> getAllRouters(){
        return redIpv4Service.getAllRedIpv4();
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateRedIpv4(@PathVariable Long id,@RequestBody RedIpv4Request redIpv4Request){
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
    public RedInformationResponse calculateCIDR(@RequestParam String ip, @RequestParam String cidr){
        return redIpv4Service.createRed(ip,cidr);
    }
}

@RestController
@RequestMapping("api/routers")
@RequiredArgsConstructor
class RouterController {
    private final RouterService routerService;
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
    public ResponseEntity<List<Map<String,String>>> rebootSystem(@RequestParam String ip, @RequestParam String admin, @RequestParam String password){
        try{
            List<Map<String,String>> resourceData = routerService.systemResourcePrint(ip,admin,password,"/system/resource/print");
            return ResponseEntity.ok(resourceData);
        }catch (MikrotikApiException e){
            throw  new RuntimeException(e);
        }
    }
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




@RestController
@RequestMapping("api/box")
@RequiredArgsConstructor
class BoxController{
    private final NetworkAccessPointService networkAccessPointService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createnetworkAccessPoint(@RequestBody NetworkAccessPointRequest networkAccessPointRequest){
        networkAccessPointService.createNetworkAccessPoint(networkAccessPointRequest);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NetworkAccessPointResponse> getAllnetworkAccessPoint(){
        return networkAccessPointService.getallNetworkAccessPoint();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updatenetworkAccessPoint(@PathVariable Long id,@RequestBody NetworkAccessPointRequest networkAccessPointRequest){
        networkAccessPointService.updateNetworkAccessPoint(id, networkAccessPointRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletenetworkAccessPoint(@PathVariable Long id){
        networkAccessPointService.deleteNetworkAccessPoint(id);
    }
}