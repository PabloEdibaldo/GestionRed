package com.GestionRed.GestionRed.controller;

import com.GestionRed.GestionRed.dto.RouterRequest;
import com.GestionRed.GestionRed.dto.RouterResponse;
import com.GestionRed.GestionRed.services.RouterService;

import lombok.RequiredArgsConstructor;
import me.legrange.mikrotik.MikrotikApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/routers")
@RequiredArgsConstructor

public class RouterController {
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
    public ResponseEntity<List<Map<String,String>>>rebootSystem(@RequestParam String ip, @RequestParam String admin,@RequestParam String password){
        try{
            List<Map<String,String>> resourceData = routerService.systemResourcePrint(ip,admin,password,"/system/resource/print");
            return ResponseEntity.ok(resourceData);
        }catch (MikrotikApiException e){
            throw  new RuntimeException(e);
        }
    }


}
