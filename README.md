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
Funcion para la conexion con e router para obtener su configuracion:
```
    
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

```

Metodo para obtener una lista de los routers de la base de datos:
```
  public List<RouterResponse> getAllRouters() {
        List<Router> routers = routerRepository.findAll();

        return routers.stream().map(this::mapToRouterResponse).toList();
    }

```
