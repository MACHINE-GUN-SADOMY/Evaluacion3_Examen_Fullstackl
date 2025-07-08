package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.RestConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
        @Bean
        public RestClient restClient() {
            return RestClient.builder().build(); // Configuración mínima
        }
}
