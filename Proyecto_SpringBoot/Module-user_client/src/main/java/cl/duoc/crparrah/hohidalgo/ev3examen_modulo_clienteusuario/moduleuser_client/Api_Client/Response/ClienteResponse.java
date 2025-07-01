package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ComunaJpa;
import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ClienteResponse {
    private Integer idCliente;
    private String nombreCliente;
    private String apellidoCliente;
    private String email;
    private String telefono;
    private String contrasenia;
    private String direccionCliente;
    private Data fechaNacimiento;
    private Integer idComuna;
}
