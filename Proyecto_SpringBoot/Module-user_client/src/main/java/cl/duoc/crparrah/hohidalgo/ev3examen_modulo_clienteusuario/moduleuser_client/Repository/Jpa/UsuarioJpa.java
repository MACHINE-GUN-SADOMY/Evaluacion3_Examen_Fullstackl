package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@Table (name = "usuarios")
public class UsuarioJpa {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_usuario", nullable = false,updatable = false,unique = true)
    private Integer idUsuario;

    @Column (name = "nombre_usuario", nullable = false, updatable = true, unique = true)
    private String nombreUsuario;

    @Column (name = "contraseña", nullable = false, updatable = true, unique = false)
    private String contraseñaUsuario;

    @Column (name = "fecha_registro", nullable = false, updatable = true, unique = true)
    private Date fechaRegistro;

    @ManyToOne
    @JoinColumn (name = "id_role", nullable = false)
    private RoleJpa role;

    public UsuarioJpa(String nombreUsuario, String contraseñaUsuario, Date fechaRegistro, RoleJpa role) {
        this.nombreUsuario = nombreUsuario;
        this.contraseñaUsuario = contraseñaUsuario;
        this.fechaRegistro = fechaRegistro;
        this.role = role;
    }
}
