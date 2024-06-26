package com.GestionRed.GestionRed.services.ConfigRoutersServers;

import com.GestionRed.GestionRed.dto.dtoQueriesFromOtherMicroservices.QueriesFromOtherMicroservicesRequest;
import com.GestionRed.GestionRed.model.Router;
import com.GestionRed.GestionRed.repository.RouterRepository;
import com.GestionRed.GestionRed.services.RouterService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import me.legrange.mikrotik.MikrotikApiException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class QueriesFromOtherMicroservicesServicePPPoE {

    private final RouterService routerService;
    private final RouterRepository routerRepository;


    public QueriesFromOtherMicroservicesServicePPPoE(RouterService routerService, RouterRepository routerRepository ) {
        this.routerService = routerService;
        this.routerRepository = routerRepository;
    }

    //-----------------------------------------------Create client PPPoE-------------------------------------------------------------
    public Object InteractionWithTheSwitch(
            int caseAction,
            Long idRouter,
            QueriesFromOtherMicroservicesRequest.AssignPromotionRequest assignPromotionRequest,
            QueriesFromOtherMicroservicesRequest.ClientPPPoERequest clientPPPoERequest,
            QueriesFromOtherMicroservicesRequest.DeleteClientInListPromotion deleteClientInListPromotion,
            QueriesFromOtherMicroservicesRequest.CutServiceClientRequest cutServiceClientRequest,
            QueriesFromOtherMicroservicesRequest.CreateProfilePPP createProfilePPP,
            QueriesFromOtherMicroservicesRequest.CutServicePPPoEClient cutServicePPPoEClient
    ) throws MikrotikApiException, IllegalArgumentException {
        //consult the router data through the id
        Optional<Router> optionalRouter = routerRepository.findById(idRouter);
        //check if the router exists
        if(optionalRouter.isEmpty()){
            log.info("Router not found with ID:{}", idRouter);
            throw new RuntimeException("Router not found with ID:" + idRouter);
        }
        Router router = optionalRouter.get();
        //response switch === [tag=null, data={ret=*22}]
        //List<Map<String,String>> switchCaseResults;
        switch (caseAction){
            case 1:
                //assign promotion client
                String commandAssignPromotion = String.format("ip/firewall/address-list/ add list=%s address=%s",
                        assignPromotionRequest.getListPromotion(),
                        assignPromotionRequest.getAddress());
                //connection from router
                return routerService.systemResourcePrint(
                        router.getIpAddress(),
                        router.getUserMikrotik(),
                        router.getPassword(), commandAssignPromotion);


            case 2:
                //create client PPPoE
                String nameGenerate = String.format("%010d",clientPPPoERequest.getIdUser());
                String commandCreateClientPPPoE = String.format("/ppp/secret/add name=%s password=%s comment=\"%s\" service=pppoe profile=default remote-address=%s",
                        nameGenerate,clientPPPoERequest.getUserPassword(),
                        clientPPPoERequest.getUserName(),
                        clientPPPoERequest.getAddress());

                //connection from router
                List<Map<String,String>> response= routerService.systemResourcePrint(
                        router.getIpAddress(),
                        router.getUserMikrotik(),
                        router.getPassword(),
                        commandCreateClientPPPoE);

               return isResponseSuccessful(response);


            case 3:

                String commandDeleteClientInListPromotion = String.format("ip/firewall/address-list/remove [find where address=\"%s\" list=%s]",
                        deleteClientInListPromotion.getAddress(),
                        deleteClientInListPromotion.getNamePromotion());

                //command to assign profile PPPoE
                String commandAssignProfileClient = String.format("/ppp secret set [find where address=\"%s\"] profile=%s",
                        deleteClientInListPromotion.getAddress(),
                        deleteClientInListPromotion.getProfile());
                //connection from router
                List<Map<String,String>> switchCaseResults =  routerService.systemResourcePrint(
                        router.getIpAddress(),
                        router.getUserMikrotik(),
                        router.getPassword(),
                        commandDeleteClientInListPromotion);


                if(isResponseSuccessful(switchCaseResults)){
                    List<Map<String,String>> switchCaseResults2 =routerService.systemResourcePrint(
                            router.getIpAddress(),
                            router.getUserMikrotik(),
                            router.getPassword(),
                            commandAssignProfileClient) ;

                    return isResponseSuccessful(switchCaseResults2);
                }

            case 4:
                //cut off the client's internet
                String commandCutServiceClient = String.format("/ip firewall address-list add list=delinquentClientPPPoE address=%s comment=\"%s delinquent client\"",
                        cutServiceClientRequest.getAddress(),
                        cutServiceClientRequest.getNameClient());
                List<Map<String,String>> switchCaseResultsCommandCutServiceClient =routerService.systemResourcePrint(
                        router.getIpAddress(),
                        router.getUserMikrotik(),
                        router.getPassword(),
                        commandCutServiceClient);
                return isResponseSuccessful(switchCaseResultsCommandCutServiceClient);

            case 5:
                String commandCutServicePPoEClient = String.format("/ppp/secret/disable [find remote-address=\"$s\"]",cutServicePPPoEClient.getRemoteAddress());

                List<Map<String,String>> switchCaseResultsCommandCutServicePPPoEClient =routerService.systemResourcePrint(
                        router.getIpAddress(),
                        router.getUserMikrotik(),
                        router.getPassword(),
                        commandCutServicePPoEClient);
                return isResponseSuccessful(switchCaseResultsCommandCutServicePPPoEClient);

            case 6:
                String commandReactivateServiceClientPPPoE = String.format("/ppp/secret/enable [find remote-address=\"$s\"]",cutServicePPPoEClient.getRemoteAddress());

                List<Map<String,String>> ReactivateServiceClientPPPoE =routerService.systemResourcePrint(
                        router.getIpAddress(),
                        router.getUserMikrotik(),
                        router.getPassword(),
                        commandReactivateServiceClientPPPoE);
                return isResponseSuccessful(ReactivateServiceClientPPPoE);


                /*
                * -----------Create list delinquent client: /ip firewall address-list add list=morosos address=192.168.1.100 comment="Cliente moroso"
                * -----------assign rules to list --->/ip firewall filter add chain=forward src-address-list=morosos action=drop
                 * */

//
//                case 5:
//                    String commandCreateProfilePPP = String.format("/ppp profile add name=%s rate-limit=%s/%s",
//                            createProfilePPP.getName(),
//                            createProfilePPP.getLowSpeed(),
//                            createProfilePPP.getUploadSpeed());
//                    List<Map<String,String>> createProfilePPPR =routerService.systemResourcePrint(
//                            router.getIpAddress(),
//                            router.getUserMikrotik(),
//                            router.getPassword(),
//                            commandCreateProfilePPP);
//                    return isResponseSuccessful(createProfilePPPR);

            default:
                throw new IllegalArgumentException("Invalid case action:"+caseAction);


        }


    }




    public boolean isResponseSuccessful(@NonNull List<Map<String, String>> response) {
        if (!response.isEmpty()) {
            Map<String, String> responseData = response.get(0);
            if (responseData.containsKey("data") && responseData.get("data").contains("ret=*22")) {
                return true;
            }
        }
        return false;
    }






}
