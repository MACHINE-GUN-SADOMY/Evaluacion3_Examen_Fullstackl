package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.RestConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    private static final String SERVICIO_INVENTARIO_URL = "http://localhost:8091/api";

    @Bean
    public RestClient.Builder inventoryServiceClientBuilder() {
        return RestClient.builder()
                .baseUrl(SERVICIO_INVENTARIO_URL);
    }

    @Bean
    public RestClient inventoryServiceClient(RestClient.Builder builder) {
        return builder.build();
    }
}
