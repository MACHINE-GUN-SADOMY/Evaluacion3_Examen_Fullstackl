package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ClienteResponse {
    private Integer clienteId;
    private String nombreCliente;
    private String apellidoCliente;
    private Integer telefono;
    private String email;
    private String contraseña;
    private String direccion;
    private LocalDate fechaNacimiento;
    private ComunaResponse comuna;
}
