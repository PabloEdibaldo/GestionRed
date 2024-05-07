package com.GestionRed.GestionRed.services.ConfigRoutersServers;

import com.GestionRed.GestionRed.dto.dtoQueriesFromOtherMicroservices.dtoQueriesFromOtherMicroservicesDHCP;
import com.GestionRed.GestionRed.model.Router;
import com.GestionRed.GestionRed.repository.RouterRepository;
import com.GestionRed.GestionRed.services.RouterService;
import lombok.extern.slf4j.Slf4j;
import me.legrange.mikrotik.MikrotikApiException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class QueriesFromOtherMicroservicesServiceDCHP {

    private final RouterService routerService;
    private final RouterRepository routerRepository;

    public QueriesFromOtherMicroservicesServiceDCHP(RouterService routerService, RouterRepository routerRepository) {
        this.routerService = routerService;
        this.routerRepository = routerRepository;
    }

    public Object InteractionWithTheSwitchDHCP(int caseAction,
                                               Long idRouter,
                                               dtoQueriesFromOtherMicroservicesDHCP.ClientDHCPRequest clientDHCPRequest,
                                               dtoQueriesFromOtherMicroservicesDHCP.ClientDHCPDeleteRequest deleteDCHPClient
    ) throws MikrotikApiException {
        Optional<Router> optionalRouter = routerRepository.findById(idRouter);
        //check if the router exists
        if (optionalRouter.isEmpty()) {
            log.info("Router not found with ID:{}", idRouter);
            throw new RuntimeException("Router not found with ID:" + idRouter);
        }
        Router router = optionalRouter.get();

        return switch (caseAction) {
            case 1 -> {
                String createLeaseInSwitch = String.format(
                        "/ip/dhcp-server/lease/add address=%s mac-address=%s comment=\"%s\"",
                        clientDHCPRequest.getAddress(),
                        clientDHCPRequest.getMacAddress(),
                        clientDHCPRequest.getUserName());

                yield routerService.systemResourcePrint(
                        router.getIpAddress(),
                        router.getUserMikrotik(),
                        router.getPassword(), createLeaseInSwitch);
            }
            case 2 -> {
                String deleteLeaseInSwitch = String.format(
                        "/ip/dhcp-server/lease/remove [find address=%s]",
                        deleteDCHPClient.getAddress()
                );
                yield routerService.systemResourcePrint(
                        router.getIpAddress(),
                        router.getUserMikrotik(),
                        router.getPassword(), deleteLeaseInSwitch);

            }
            default -> throw new IllegalArgumentException("Invalid case action:" + caseAction);
        };
    }
}