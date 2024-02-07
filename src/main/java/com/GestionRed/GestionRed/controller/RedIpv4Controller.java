package com.GestionRed.GestionRed.controller;
import com.GestionRed.GestionRed.dto.dtoRedInfo.RedInformationResponse;
import com.GestionRed.GestionRed.dto.dtoRedIpv4.RedIpv4Request;
import com.GestionRed.GestionRed.dto.dtoRedIpv4.RedIpv4Response;
import com.GestionRed.GestionRed.model.Router;

import com.GestionRed.GestionRed.services.RedIpv4Service;
import com.GestionRed.GestionRed.services.RouterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/redIpv4")
@RequiredArgsConstructor
public class RedIpv4Controller {
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
