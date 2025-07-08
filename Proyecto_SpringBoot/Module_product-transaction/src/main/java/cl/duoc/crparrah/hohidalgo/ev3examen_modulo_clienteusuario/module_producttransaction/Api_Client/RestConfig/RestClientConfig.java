package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.RestConfig;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.ClienteRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8090") // URL del microservicio Cliente
                .build();
    }

    @Bean
    public ClienteRestClient clienteRestClient(RestClient restClient) {
        return idCliente -> restClient.get()
                .uri("/clientes/{id}", idCliente)
                .retrieve()
                .toBodilessEntity(); // solo status, sin body
    }
}