Microservicio para la para la gestion de Cajas nap, Resdes Ipv4, Routers Mikrotik

Uso del MVC.

Cada campo de Routers: Metodo para crear un Router

```
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
``` 
