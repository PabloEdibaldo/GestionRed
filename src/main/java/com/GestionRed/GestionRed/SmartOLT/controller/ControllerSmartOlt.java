package com.GestionRed.GestionRed.SmartOLT.controller;


import com.GestionRed.GestionRed.SmartOLT.services.ServiceConfigOnus;
import com.GestionRed.GestionRed.dto.dtoRedIpv4.RedIpv4Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/SmartOlt/")
@CrossOrigin(origins = "*")
public class ControllerSmartOlt {
    private final ServiceConfigOnus serviceConfigOnus;


    @GetMapping("getZones/")
    @ResponseStatus(HttpStatus.OK)
    public Object getAllZones(){
        return serviceConfigOnus.getOnusZones();
    }

    @GetMapping("GetAllUnconfiguredONUs/")
    @ResponseStatus(HttpStatus.OK)
    public Object GetAllUnconfiguredONUs(){
        return serviceConfigOnus.GetAllUnconfiguredONUs();
    }

}
