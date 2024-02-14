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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouterService {
    @Autowired
    private final RouterRepository routerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    public void createRouter(@NonNull RouterRequest routerRequest) {
        String encryptedPassword = passwordEncoder.encode(routerRequest.getPassword());
        Router router = Router.builder()
                .name(routerRequest.getName())
                .ipAddress((routerRequest.getIpAddress()))
                .userMikrotik(routerRequest.getUserMikrotik())
                .password(encryptedPassword)
                .location(routerRequest.getLocation())
                .security(routerRequest.getSecurity())
                .security_alt(routerRequest.getSecurity_alt())
                .radius_secret(routerRequest.getRadius_secret())
                .radius_nas_ip(routerRequest.getRadius_nas_ip())
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
                .build();
    }
    public void updateRouter(Long id, RouterRequest routerRequest) {
        Optional<Router> optionalRouter = routerRepository.findById(id);

        if (optionalRouter.isPresent()) {
            Router existingModels = optionalRouter.get();

            existingModels.setName(routerRequest.getName());
            existingModels.setIpAddress(routerRequest.getIpAddress());
            existingModels.setUserMikrotik(routerRequest.getUserMikrotik());

            if (routerRequest.getPassword() != null) {
                String encryptedPassword = passwordEncoder.encode(routerRequest.getPassword());
                existingModels.setPassword(encryptedPassword);
            }

            existingModels.setLocation(routerRequest.getLocation());
            existingModels.setSecurity(routerRequest.getSecurity());
            existingModels.setSecurity_alt((routerRequest.getSecurity_alt()));
            existingModels.setRadius_secret(routerRequest.getRadius_secret());
            existingModels.setRadius_nas_ip(routerRequest.getRadius_nas_ip());

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
}
