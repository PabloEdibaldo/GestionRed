package com.GestionRed.GestionRed.services;
import com.GestionRed.GestionRed.dto.dtoRedInfo.RedInformationResponse;
import com.GestionRed.GestionRed.dto.dtoRedIpv4.RedIpv4Request;
import com.GestionRed.GestionRed.dto.dtoRedIpv4.RedIpv4Response;
import com.GestionRed.GestionRed.model.IpsForRedIpv4;
import com.GestionRed.GestionRed.model.Port;
import com.GestionRed.GestionRed.model.RedIpv4;
import com.GestionRed.GestionRed.model.Router;
import com.GestionRed.GestionRed.repository.IpsForRedIpv4Repository;
import com.GestionRed.GestionRed.repository.RedIpv4Repository;
import com.GestionRed.GestionRed.repository.RouterRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import me.legrange.mikrotik.MikrotikApiException;

import org.apache.commons.net.util.SubnetUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedIpv4Service {
    private final RedIpv4Repository redIpv4Repository;
    private final RouterService routerService;
    private final IpsForRedIpv4Repository ipsForRedIpv4Repository;
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

            createRed(redIpv4.getRed_ip(),redIpv4.getCidr(),true,redIpv4);

            /*
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
            */

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
    private RedIpv4Response mapToRedIpv4Response(@NonNull RedIpv4 redIpv4) {
        return RedIpv4Response.builder()
                .id(redIpv4.getId())
                .name(redIpv4.getName())
                .name_router(redIpv4.getName_router())
                .red_ip(redIpv4.getRed_ip())
                .cidr(redIpv4.getCidr())
                .use_type(redIpv4.getUse_type())
                .build();
    }
    //Update red ipv4
    public void updateRedIpv4(Long id, RedIpv4Request redIpv4Request) throws UnknownHostException {
        Optional<RedIpv4> optionalRedIpv4 = redIpv4Repository.findById(id);
        //check if object existing in the database
        if(optionalRedIpv4.isPresent()){
            //if object existing,take the request that was entered by the user
            RedIpv4 exitingModels = optionalRedIpv4.get();
            exitingModels.setName(redIpv4Request.getName());
            exitingModels.setName_router(redIpv4Request.getName_router());
            exitingModels.setRed_ip(redIpv4Request.getRed_ip());
            exitingModels.setCidr(redIpv4Request.getCidr());
            exitingModels.setUse_type(redIpv4Request.getUse_type());

            redIpv4Repository.save(exitingModels);
            log.info("Red Ipv4 {}  updated",id);
            //filter ips according to the id of the ipv4 object
            List<IpsForRedIpv4> ipsForRedIpv4s = ipsForRedIpv4Repository.findAll().stream().filter(x->Objects.equals(x.getRedIpv4().getId(), exitingModels.getId())).toList();
                for (IpsForRedIpv4 ipsForRedIpv4Update: ipsForRedIpv4s){
                    Long idIpsForRedIpv4 = ipsForRedIpv4Update.getId();
                    ipsForRedIpv4Repository.deleteById(idIpsForRedIpv4);
                }
                boolean createIps = true;
                createRed(exitingModels.getRed_ip(), exitingModels.getCidr(), createIps,exitingModels);
        }else
            log.warn("Red ipv4 if {} not found",id);
    }
    public void deleteRedIpv4(Long id){
        Optional<RedIpv4> optionalRedIpv4 = redIpv4Repository.findById(id);
        if(optionalRedIpv4.isPresent()){
            List<IpsForRedIpv4> ipsForRedIpv4s = ipsForRedIpv4Repository.findAll().stream().filter(x->Objects.equals(x.getRedIpv4().getId(), id)).toList();
            for (IpsForRedIpv4 ipsForRedIpv4Update: ipsForRedIpv4s){
                Long idIpsForRedIpv4 = ipsForRedIpv4Update.getId();
                ipsForRedIpv4Repository.deleteById(idIpsForRedIpv4);
            }
            redIpv4Repository.deleteById(id);
        }else
            log.warn(" Red ipv4  with Id {} not found", id);
    }
    @ResponseBody
    public ResponseEntity<?> pingRedIpv4(String ip) throws UnknownHostException {

        try {
            InetAddress inetAddress = InetAddress.getByName(ip);

            return (ResponseEntity<?>) Stream.iterate(1, i->i+1)
                    .limit(10)
                    .map(i ->{
                        long startTime = System.currentTimeMillis();
                        boolean reachable = false;
                        try {
                            reachable = inetAddress.isReachable(5000);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        long elapsedTime = System.currentTimeMillis() - startTime;
                        return reachable
                                ? "64 bytes from " + inetAddress.getHostAddress() + ": icmp_seq=" + i + " time=" + elapsedTime + " ms"
                                : "Request timed out for icmp_seq=" + i;
                    })

                    .collect(Collectors.collectingAndThen(Collectors.toList(),result ->{
                        Map<String,Object> responseBody = new HashMap<>();
                        responseBody.put("Status","success");
                        responseBody.put("pingResults",result);
                        return new ResponseEntity<>(responseBody,HttpStatus.OK);
                    }));

    }catch (IOException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    //Calculate CIDR
    public RedInformationResponse createRed(String ip, String cidr,boolean createIps,RedIpv4 redIpv4) throws UnknownHostException {
        String subnet = ip+"/"+cidr;

        final SubnetUtils utils = new SubnetUtils(subnet);
        final SubnetUtils.SubnetInfo info = utils.getInfo();
        //function for calculate CIDR using "SubnetUtils"
        String subnetInfo= subnet;
        String ipAddress= info.getAddress();
        String ipAddressHex = calculateHex(info.getAddress()).toString();
        String ipAddressBinary= Integer.toBinaryString(info.asInteger(info.getAddress()));
        //----------------------------------------------------------------------------------
        String netmask = info.getNetmask();
        String netmaskHex = calculateHex(info.getNetmask()).toString();
        String netmaskBinary= Integer.toBinaryString(info.asInteger(info.getNetmask()));
        //----------------------------------------------------------------------------------
        String networkAddress =info.getNetworkAddress();
        String networkAddressHex = calculateHex(info.getNetworkAddress()).toString();
        String networkAddressBinary=Integer.toBinaryString(info.asInteger(info.getNetworkAddress()));
        //----------------------------------------------------------------------------------
        String broadcastAddress = info.getBroadcastAddress();
        String lowAddress = info.getLowAddress();
        String highAddress= info.getHighAddress();

        Long totalUsableAddresses =  info.getAddressCountLong();

        String[] addresses = info.getAllAddresses();


        if (createIps){
            //create list for address
            //String[] addresses = info.getAllAddresses();

            for (String address:addresses){
                log.info("eehh:{}",address);

                IpsForRedIpv4 ipsForRedIpv4 = IpsForRedIpv4.builder()
                        .redIpv4(redIpv4)
                        .ipAddress(ipAddress)
                        .ip(address)
                        .status(0)
                        .build();
                ipsForRedIpv4Repository.save(ipsForRedIpv4);
            }
        }
        //Return response the calculate CIDR
        return new RedInformationResponse(subnetInfo,ipAddress,ipAddressHex,ipAddressBinary,netmask,netmaskHex,
                netmaskBinary,networkAddress,networkAddressHex,networkAddressBinary,broadcastAddress,lowAddress,
                highAddress,totalUsableAddresses);
    }
    //calculate values CIDR
    StringBuilder calculateHex(String ipaddress) throws UnknownHostException {
        byte[] ipaddressBytes = InetAddress.getByName(ipaddress).getAddress();

        StringBuilder hexStringBuilder = new StringBuilder();
        for(byte b: ipaddressBytes){
            hexStringBuilder.append(String.format("%02X",b & 0xFF));
        }
        return hexStringBuilder;
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //IP address query

    public List<String> ips(Long id){
        try {
            Optional<RedIpv4> optionalIpsForRedIpv4 = redIpv4Repository.findById(id);
            if(optionalIpsForRedIpv4.isEmpty()){
                return Collections.emptyList();
            }
            return ipsForRedIpv4Repository.findAll()
                    .stream().filter(ips->ips.getRedIpv4().getId().equals(id)&&ips.getStatus()==0)
                    .map(IpsForRedIpv4::getIp)
                    .collect(Collectors.toList());
        }catch (Exception e){
            return Collections.emptyList();
        }
    }
    //IP address query

    public Boolean putIps(Long id_port, String ip, String nameClient){
        log.info("idIpv4-->:{}",id_port);
        log.info("ip-->:{}",ip);

        List<IpsForRedIpv4> ports = ipsForRedIpv4Repository.findAll().stream().filter(x-> Objects.equals(x.getRedIpv4().getId(), id_port)).toList();

        for (IpsForRedIpv4 ipsForRedIpv4Filter:ports){
            log.info("ips iteradas{}",ipsForRedIpv4Filter.getIp());

            if (ipsForRedIpv4Filter.getIp() == ip && ipsForRedIpv4Filter.getStatus()==0) {

                ipsForRedIpv4Filter.setStatus(1);
                ipsForRedIpv4Filter.setNameClient(nameClient);
                ipsForRedIpv4Repository.save(ipsForRedIpv4Filter);
                return true;
            }
        }

        return false;

    }
    public Boolean editIpClient(String nameUser, Long idRedIpv4){
        List<IpsForRedIpv4> ipsForRedIpv4s = ipsForRedIpv4Repository.findAll().stream().filter(x->Objects.equals(x.getRedIpv4().getId(),idRedIpv4)).toList();
        for (IpsForRedIpv4 ipsForRedIpv4:ipsForRedIpv4s){
            if(Objects.equals(ipsForRedIpv4.getNameClient(),nameUser)){
                ipsForRedIpv4.setStatus(0);
                ipsForRedIpv4.setNameClient(null);
                ipsForRedIpv4Repository.delete(ipsForRedIpv4);
            return true;
            }

        }
        return false;
    }

}




/*
*
*
@Component
@RequiredArgsConstructor
class RouterConfig {
    private final RouterService routerService;
    void configureNetworkRouter(@NonNull RedIpv4Request redIpv4Request, String routerIpAddress, String routerUserMikrotik, String routerPassword)throws MikrotikApiException {
        String command = "/ip/address/add address = "+ redIpv4Request.getRed_ip()+"/"+redIpv4Request.getCidr()+" interface" + redIpv4Request.getCidr();
        routerService.systemResourcePrint(routerIpAddress,routerUserMikrotik,routerPassword,command);
    }



}

 */




                    /*
            InetAddress inetAddress = InetAddress.getByName(ip);
            List<String> pingResults = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                long startTime = System.currentTimeMillis();

                if (inetAddress.isReachable(5000)) {
                    long endTime = System.currentTimeMillis();
                    long elapsedTime = endTime = startTime;
                    System.out.println("64 bytes from " + inetAddress.getHostAddress() +
                            ": icmp_seq=" + i + " time=" + elapsedTime + " ms");
                    pingResults.add("64 bytes from " + inetAddress.getHostAddress() + ": icmp_seq=" + i + " time=" + elapsedTime + " ms");
                } else{
                    log.info("Request timed out for icmp_seq=" + i);
                }
            }
            // Create a JSON response object
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", "success");
            responseBody.put("pingResults", pingResults);

            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }*/