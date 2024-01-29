package com.GestionRed.GestionRed.controller;
import com.GestionRed.GestionRed.dto.dtoRedIpv4.RedIpv4Request;
import com.GestionRed.GestionRed.dto.dtoRedIpv4.RedIpv4Response;
import com.GestionRed.GestionRed.services.RedIpv4Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/redIpv4")
@RequiredArgsConstructor
public class RedIpv4Controller {
    private final RedIpv4Service redIpv4Service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createRedIpv4(@RequestBody RedIpv4Request redIpv4Request){
        redIpv4Service.createRedIpv4(redIpv4Request);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RedIpv4Response> getAllRouters(){
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

}
