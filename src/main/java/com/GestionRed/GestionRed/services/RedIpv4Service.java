package com.GestionRed.GestionRed.services;
import com.GestionRed.GestionRed.dto.dtoRedInfo.RedInformationResponse;
import com.GestionRed.GestionRed.dto.dtoRedIpv4.RedIpv4Request;
import com.GestionRed.GestionRed.dto.dtoRedIpv4.RedIpv4Response;
import com.GestionRed.GestionRed.model.RedIpv4;
import com.GestionRed.GestionRed.model.Router;
import com.GestionRed.GestionRed.repository.RedIpv4Repository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import me.legrange.mikrotik.MikrotikApiException;

import org.apache.commons.net.util.SubnetUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedIpv4Service {
    private final RedIpv4Repository redIpv4Repository;
    private final RouterService routerService;

private final RouterConfig routerConfig;
    public void createRedIpv4(RedIpv4Request redIpv4Request) {
        try {
            RedIpv4 redIpv4 = RedIpv4.builder()
                    .name(redIpv4Request.getName())
                    .name_router(redIpv4Request.getName_router())
                    .red_ip(redIpv4Request.getRed_ip())
                    .cidr(redIpv4Request.getCidr())
                    .use_type(redIpv4Request.getUse_type())
                    .build();
            redIpv4Repository.save(redIpv4);

            Optional<Router> optionalRouter = routerService.getRouterByName(redIpv4Request.getName_router());
            optionalRouter.ifPresent(router -> {
                String routerIpAddress = router.getIpAddress();
                String routerUserMikrotik = router.getUserMikrotik();
                String routerPassword = router.getPassword();
                try {

                    routerConfig.configureNetworkRouter(redIpv4Request,routerIpAddress,routerUserMikrotik,routerPassword);
                }  catch (MikrotikApiException e) {
                    throw new RuntimeException(e);
                }
            });
            log.info("Red {} in saved", redIpv4.getId());
        } catch (Exception e) {
            log.error("Error saving", e);
        }
    }

    public Optional<List<RedIpv4Response>> getAllRedIpv4(){
        try{
        List<RedIpv4> redIpv4s = redIpv4Repository.findAll();
        List<RedIpv4Response> responses = redIpv4s.stream().map(this::mapToRedIpv4Response).toList();
        return Optional.of(responses);
    }catch (Exception e){
            log.error("Error getting all RedIpv4",e);
            return Optional.empty();
        }
    }

    private RedIpv4Response mapToRedIpv4Response(RedIpv4 redIpv4) {
        return RedIpv4Response.builder()
                .id(redIpv4.getId())
                .name(redIpv4.getName())
                .name_router(redIpv4.getName_router())
                .red_ip(redIpv4.getRed_ip())
                .cidr(redIpv4.getCidr())
                .use_type(redIpv4.getUse_type())
                .build();
    }
    public void updateRedIpv4(Long id, RedIpv4Request redIpv4Request){
        Optional<RedIpv4> optionalRedIpv4 = redIpv4Repository.findById(id);

        if(optionalRedIpv4.isPresent()){
            RedIpv4 exitingModels = optionalRedIpv4.get();
            exitingModels.setName(redIpv4Request.getName());
            exitingModels.setName_router(redIpv4Request.getName_router());
            exitingModels.setRed_ip(redIpv4Request.getRed_ip());
            exitingModels.setCidr(redIpv4Request.getCidr());
            exitingModels.setUse_type(redIpv4Request.getUse_type());

            redIpv4Repository.save(exitingModels);
            log.info("Red Ipv4 {}  updated",id);
        }else
            log.warn("Red ipv4 if {} not found",id);
    }

    public void deleteRedIpv4(Long id){
        Optional<RedIpv4> optionalRedIpv4 = redIpv4Repository.findById(id);
        if(optionalRedIpv4.isPresent()){
            redIpv4Repository.deleteById(id);
        }else
            log.warn(" Red ipv4  with Id {} not found", id);
    }


   public RedInformationResponse createRed(String ip, String cidr){
        String CIDR = ip+"/"+cidr;
        SubnetUtils subnetUtils = new SubnetUtils(CIDR);
        SubnetUtils.SubnetInfo subnetInfo = subnetUtils.getInfo();

        String networkAddress = subnetInfo.getNetworkAddress();
        String address = subnetInfo.getAddress();
        String subnetMask = subnetInfo.getNetmask();
        String broadcastAddress = subnetInfo.getBroadcastAddress();

       long totalAddresses = routerConfig.calculateTotalAddresses(cidr);
       long usableAddresses = routerConfig.calculateUsableAddresses(totalAddresses);

        return new RedInformationResponse(networkAddress,address,subnetMask,broadcastAddress,totalAddresses,usableAddresses);
    }

}
@Component
@RequiredArgsConstructor
class RouterConfig {
    private final RouterService routerService;
    void configureNetworkRouter(@NonNull RedIpv4Request redIpv4Request, String routerIpAddress, String routerUserMikrotik, String routerPassword)throws MikrotikApiException {
        String command = "/ip/address/add address = "+ redIpv4Request.getRed_ip()+"/"+redIpv4Request.getCidr()+" interface" + redIpv4Request.getCidr();
        routerService.systemResourcePrint(routerIpAddress,routerUserMikrotik,routerPassword,command);
    }

    long calculateTotalAddresses(String cidr) {
        int prefixLength = Integer.parseInt(cidr);
        return (long) Math.pow(2, 32 - prefixLength);
    }

    long calculateUsableAddresses(long totalAddresses) {

        return totalAddresses - 2;
    }

}


