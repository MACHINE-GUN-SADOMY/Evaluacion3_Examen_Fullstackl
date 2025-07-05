package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class UsuarioResponse {
    private Integer idUsuario;
    private String nombreUsuario;
    private String contraseñaUsuario;
    private Date fechaRegistro;
    private RoleResponse role;
}
