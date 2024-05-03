package com.GestionRed.GestionRed.SmartOLT.controller;


import com.GestionRed.GestionRed.SmartOLT.services.ServiceConfigOnus;
import com.GestionRed.GestionRed.dto.dtoRedIpv4.RedIpv4Response;
import com.fasterxml.jackson.core.JsonProcessingException;
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
        return serviceConfigOnus.OptionCase(1,"/system/get_zones");
    }

    @GetMapping("GetAllUnconfiguredONUs/")
    @ResponseStatus(HttpStatus.OK)
    public Object GetAllUnconfiguredONUs()  {
        return serviceConfigOnus.OptionCase(1,"/onu/unconfigured_onus");
    }

    @GetMapping("ListOLT/")
    @ResponseStatus(HttpStatus.OK)
    public Object ListOLT()  {
        return serviceConfigOnus.OptionCase(1,"/system/get_olts");
    }


    @GetMapping("GetONUTypesList/")
    @ResponseStatus(HttpStatus.OK)
    public Object GetONUTypesList()  {
        return serviceConfigOnus.OptionCase(1,"/system/get_onu_types");
    }

    @GetMapping("GetODBsList/")
    @ResponseStatus(HttpStatus.OK)
    public Object GetODBsList(@RequestParam String id_zone)  {
        return serviceConfigOnus.OptionCase(1, String.format("/system/get_odbs/%s",id_zone));
    }

    @GetMapping("GetSpeedProfilesList/")
    @ResponseStatus(HttpStatus.OK)
    public Object GetSpeedProfilesList()  {
        return serviceConfigOnus.OptionCase(1, "/system/get_speed_profiles");
    }






}
