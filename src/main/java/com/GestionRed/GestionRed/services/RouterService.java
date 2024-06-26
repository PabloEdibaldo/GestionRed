package com.GestionRed.GestionRed.services;
import com.GestionRed.GestionRed.dto.RouterRequest;
import com.GestionRed.GestionRed.dto.RouterResponse;
import com.GestionRed.GestionRed.model.Router;
import com.GestionRed.GestionRed.repository.RouterRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.legrange.mikrotik.ApiConnection;
import me.legrange.mikrotik.MikrotikApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class RouterService {
    @Autowired
    private final RouterRepository routerRepository;


    public Optional<Router> getRouter(Long id){
        Optional<Router> optionalRouter = routerRepository.findById(id);
        return optionalRouter;
    }

    public void createRouter(@NonNull RouterRequest routerRequest) {
       // String encryptedPassword = passwordEncoder.encode(routerRequest.getPassword());
        Router router = Router.builder()
                .name(routerRequest.getName())
                .ipAddress((routerRequest.getIpAddress()))
                .userMikrotik(routerRequest.getUserMikrotik())
                .password(routerRequest.getPassword())
                .location(routerRequest.getLocation())
                .security(routerRequest.getSecurity())
                .security_alt(routerRequest.getSecurity_alt())
                .radius_secret(routerRequest.getRadius_secret())
                .radius_nas_ip(routerRequest.getRadius_nas_ip())
                .typeServer(routerRequest.getTypeServer())
                .build();
        routerRepository.save(router);
        log.info("Router {} in saved", router.getId());
    }

    public Optional<Router> getRouterByName(String routerName){
        return routerRepository.findByName(routerName);
    }
    public List<RouterResponse> getAllRouters() {
        List<Router> routers = routerRepository.findAll();

        return routers.stream().map(this::mapToRouterResponse).toList();
    }


    private RouterResponse mapToRouterResponse(@NonNull Router router) {



        String defaultInfoMessage = "Error retrieving system information.";
        List<Map<String,String>> system = null;
        String version = null;
        String cpu = null;

        try {
            system = systemResourcePrint(router.getIpAddress(),
                    router.getUserMikrotik(),
                    router.getPassword(),
                    getCommandForRouter(router.getTypeServer()));

            log.info("Seerves:{}",getCommandForRouter(router.getTypeServer()));


            List<Map<String,String>> system1 = systemResourcePrint(router.getIpAddress(),
                    router.getUserMikrotik(),
                    router.getPassword(),
                    "/system/resource/print");

            Map<String, String> versionAndCpu = extractVersionAndCpu(system1);
            version = versionAndCpu.get("version");
            cpu = versionAndCpu.get("cpu");

        } catch (MikrotikApiException e) {
            system = Collections.singletonList(Collections.singletonMap("message", defaultInfoMessage));
        }

        int systemLength = (system != null) ? system.size() : 0;
        String statusSystem = (systemLength == 0) ? "desactivado" : "activatdo";



        return RouterResponse.builder()
                .id(String.valueOf(router.getId()))
                .name(router.getName())
                .ipAddress(router.getIpAddress())
                .userMikrotik(router.getUserMikrotik())
                .password(router.getPassword())
                .location(router.getLocation())
                .security(router.getSecurity())
                .security_alt(router.getSecurity_alt())
                .radius_secret(router.getRadius_secret())
                .radius_nas_ip(router.getRadius_nas_ip())
                .typeServer(router.getTypeServer())
                //----------------------------------
                //--------------------------------
                .clients(systemLength)
                .status(statusSystem)
                .version(version)
                .cpu(cpu)
                //-------------------------------------
                .build();
    }
    private String getCommandForRouter(String typeServer) {
        log.info("Server:{}",typeServer);
        if(typeServer.equals("DHCP")){
            return "/ip/dhcp-server/lease/print detail";
        }else  {
            return "/ppp/secret/print detail";
        }

    }
    private Map<String, String> extractVersionAndCpu(List<Map<String, String>> system1) {
        Map<String, String> result = new HashMap<>();
        for (Map<String, String> entry : system1) {
            if (entry.containsKey("version")) {
                result.put("version", entry.get("version"));
            }
            if (entry.containsKey("cpu")) {
                result.put("cpu", entry.get("cpu"));
            }
        }
        return result;
    }


    public void updateRouter(Long id, RouterRequest routerRequest) {
        Optional<Router> optionalRouter = routerRepository.findById(id);

        if (optionalRouter.isPresent()) {
            Router existingModels = optionalRouter.get();

            existingModels.setName(routerRequest.getName());
            existingModels.setIpAddress(routerRequest.getIpAddress());
            existingModels.setUserMikrotik(routerRequest.getUserMikrotik());
            existingModels.setPassword(routerRequest.getPassword());
            existingModels.setLocation(routerRequest.getLocation());
            existingModels.setSecurity(routerRequest.getSecurity());
            existingModels.setSecurity_alt((routerRequest.getSecurity_alt()));
            existingModels.setRadius_secret(routerRequest.getRadius_secret());
            existingModels.setRadius_nas_ip(routerRequest.getRadius_nas_ip());
            existingModels.setTypeServer(routerRequest.getTypeServer());

            routerRepository.save(existingModels);

            log.info("Router {} updated", id);
        } else {
            log.warn("Router with ID {} not found", id);
        }

    }
    public void deleteRouter(Long id) {
        Optional<Router> optionalRouter = routerRepository.findById(id);
        if (optionalRouter.isPresent()) {
            routerRepository.deleteById(id);
        } else {

            log.warn("Router with ID {} not found", id);
        }
    }
    //connection for de routers
    public List<Map<String, String>> systemResourcePrint(String ip, String admin, String password,String command ) throws MikrotikApiException {
        ApiConnection connection = null;
        try {
            connection = ApiConnection.connect(ip);
            connection.login(admin, password);
            List<Map<String, String>> rs = connection.execute(command);
            connection.close();
            return rs;
        } catch (MikrotikApiException e) {
            throw new MikrotikApiException("Error Mikrotik", e);
        }finally {
            if (connection != null && connection.isConnected()){
                connection.close();
            }
        }
    }

    public List<Map<String, String>> GetLogs(Long idRouter) throws MikrotikApiException {
        Optional<Router> router = routerRepository.findById(idRouter);

        if(router.isPresent()) {
            Router existingRouter = router.get();
            return  systemResourcePrint(existingRouter.getIpAddress(),
                    existingRouter.getUserMikrotik(),
                    existingRouter.getPassword(),
                    "/log/print detail");

        }
        return null;
    }





    public Map<String,Object> ConfigRouter(Long idRouter) throws MikrotikApiException, IOException {
        Optional<Router> router = routerRepository.findById(idRouter);

        if(router.isPresent()){

            Router existingRouter = router.get();
            //-------------------------------------Router interface query---------------------------------------------
            List<Map<String,String>> interfaces = systemResourcePrint(existingRouter.getIpAddress(),
                    existingRouter.getUserMikrotik(),
                    existingRouter.getPassword(),
                    "/interface/print where type=ether or type=vlan");
            //----The name fields are extracted from the interfaces
            List<String> interfaceName = new ArrayList<>();
            for (Map<String,String>interfaceData:interfaces){
                String name = interfaceData.get("name");
                interfaceName.add(name);
            }
            //-------------------------------------Query system fields---------------------------------------------
            List<Map<String,String>> system = systemResourcePrint(existingRouter.getIpAddress(),
                    existingRouter.getUserMikrotik(),
                    existingRouter.getPassword(),
                    "/system/resource/print");



            Map<String, Object> result = new HashMap<>();


            result.put("interface",interfaceName);
            result.put("system",system);

            try{
                return result;
            }catch (Exception e){

            }


        }
        return null;
    }
    //-------------------------------------Traffic query interfaces---------------------------------------------
    public List<Map<String,String>> trafficMonitoringInterfaces(String interfaceMonitoring,String ipAddress,String userMikrotik,String password) throws MikrotikApiException {
        String command = "/interface/monitor-traffic interface="+interfaceMonitoring+" once";
        return systemResourcePrint(ipAddress, userMikrotik, password, command);
    }
    //-----------------------------------------Query to obtain tha name of a router-----------------------------

}
