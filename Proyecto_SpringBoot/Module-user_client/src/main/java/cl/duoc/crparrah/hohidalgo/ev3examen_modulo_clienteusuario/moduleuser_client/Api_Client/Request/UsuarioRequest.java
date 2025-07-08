package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request;

import java.util.Date;
import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class UsuarioRequest {
    private String nombreUsuario;
    private String contraseñaUsuario;
    private Date fecaRegistro;
    private Integer idRole;
}
