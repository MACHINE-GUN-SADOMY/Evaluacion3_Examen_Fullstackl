package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request;

import lombok.*;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ClienteRequest {
    private String nombreCliente;
    private String apellidoCliente;
    private String email;
    private String contrasenia;
    private String direccion;
    private Integer telefono;
    private Integer idComuna;
}