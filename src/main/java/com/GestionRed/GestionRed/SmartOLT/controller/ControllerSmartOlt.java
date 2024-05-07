package com.GestionRed.GestionRed.SmartOLT.controller;


import com.GestionRed.GestionRed.SmartOLT.dto.RequestDtoAuthorizeONU;
import com.GestionRed.GestionRed.SmartOLT.services.ServiceConfigOnus;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/SmartOlt/")
@CrossOrigin(origins = "*")
public class ControllerSmartOlt {
    private static final Logger log = LoggerFactory.getLogger(ControllerSmartOlt.class);
    private final ServiceConfigOnus serviceConfigOnus;

    @GetMapping("getZones/")
    @ResponseStatus(HttpStatus.OK)
    public Object getAllZones(){
        return serviceConfigOnus.OptionCase(1,"/system/get_zones",null);
    }

    @GetMapping("GetAllUnconfiguredONUs/")
    @ResponseStatus(HttpStatus.OK)
    public Object GetAllUnconfiguredONUs()  {
        return serviceConfigOnus.OptionCase(1,"/onu/unconfigured_onus",null);
    }

    @GetMapping("ListOLT/")
    @ResponseStatus(HttpStatus.OK)
    public Object ListOLT()  {
        return serviceConfigOnus.OptionCase(1,"/system/get_olts",null);
    }

    @GetMapping("GetONUTypesList/")
    @ResponseStatus(HttpStatus.OK)
    public Object GetONUTypesList()  {
        return serviceConfigOnus.OptionCase(1,"/system/get_onu_types",null);
    }

    @GetMapping("GetODBsList/")
    @ResponseStatus(HttpStatus.OK)
    public Object GetODBsList(@RequestParam String id_zone)  {
        return serviceConfigOnus.OptionCase(1, String.format("/system/get_odbs/%s",id_zone),null);
    }

    @GetMapping("GetSpeedProfilesList/")
    @ResponseStatus(HttpStatus.OK)
    public Object GetSpeedProfilesList()  {
        return serviceConfigOnus.OptionCase(1, "/system/get_speed_profiles",null);
    }

    @GetMapping("OnuGetAllOnusDetails/")
    @ResponseStatus(HttpStatus.OK)
    public Object OnuGetAllOnusDetails()  {
        return serviceConfigOnus.OptionCase(1, "/onu/get_all_onus_details",null);
    }

    @GetMapping("GetVlans/")
    @ResponseStatus(HttpStatus.OK)
    public Object GetVlans(@RequestParam int id_onu)  {
        return serviceConfigOnus.GetVlans("/olt/get_vlans/"+id_onu);
    }


    @PostMapping("AuthorizeONU/")
    @ResponseStatus(HttpStatus.OK)
    public Object AuthorizeONU(@RequestBody RequestDtoAuthorizeONU requestDtoAuthorizeONU)  {

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("olt_id", requestDtoAuthorizeONU.getOlt_id());
        formData.add("pon_type", requestDtoAuthorizeONU.getPon_type());
        formData.add("board", requestDtoAuthorizeONU.getBoard());
        formData.add("port", requestDtoAuthorizeONU.getPort());
        formData.add("sn", requestDtoAuthorizeONU.getSn());
        formData.add("vlan", requestDtoAuthorizeONU.getVlan());
        formData.add("onu_type", requestDtoAuthorizeONU.getOnu_type());
        formData.add("zone", requestDtoAuthorizeONU.getZone());
        formData.add("odb", requestDtoAuthorizeONU.getOdb());
        formData.add("name", requestDtoAuthorizeONU.getName());
        formData.add("address_or_comment", requestDtoAuthorizeONU.getAddress_or_comment());
        formData.add("onu_mode", requestDtoAuthorizeONU.getOnu_mode());
        formData.add("onu_external_id", requestDtoAuthorizeONU.getOnu_external_id());
        formData.add("upload_speed_profile_name", requestDtoAuthorizeONU.getUpload_speed_profile_name());
        formData.add("download_speed_profile_name", requestDtoAuthorizeONU.getDownload_speed_profile_name());


        return serviceConfigOnus.OptionCase(2,"/onu/authorize_onu",formData);
    }


















































}
