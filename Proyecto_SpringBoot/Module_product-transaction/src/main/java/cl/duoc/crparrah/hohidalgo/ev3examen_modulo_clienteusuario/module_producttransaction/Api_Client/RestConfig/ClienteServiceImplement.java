package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.RestConfig;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
public class ClienteServiceImplement{

    private final RestClient restClient;

    public ClienteServiceImplement(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://localhost:8090") // URL del microservicio de clientes
                .build();
    }

    public boolean verificarCliente(Integer idCliente) {
        try {
            ResponseEntity<Void> response = restClient.get()
                    .uri("/clientes/{id}", idCliente)
                    .retrieve()
                    .toBodilessEntity();

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false; // Cliente no existe o error de conexión
        }
    }
}