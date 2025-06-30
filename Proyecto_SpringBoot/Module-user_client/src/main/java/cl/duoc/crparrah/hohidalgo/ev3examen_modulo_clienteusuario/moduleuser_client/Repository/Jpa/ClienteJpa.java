package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table (name = "cliente")
public class ClienteJpa {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_cliente", nullable = false, updatable = false, unique = true)
    private Integer clienteId;

    @Column (name = "nombre_cliente", nullable = false, updatable = true,unique = false)
    private String nombreCliente;

    @Column (name = "apellido_cliente", nullable = false,updatable = true,unique = false)
    private String apellidoCliente;

    // el cliente no puede tener mas de una cuenta registrada con el mismo email
    @Column (name = "email", nullable = false,updatable = true,unique = true)
    private String email;

    @Column (name = "contraseña", nullable = false,updatable = true, unique = false)
    private String contrasena;

    @Column (name = "direccion", nullable = false,updatable = true, unique = false)
    private String direccion;

    @Column (name = "telefono", nullable = true,updatable = true,unique = true)
    private Integer telefono;

    @Column (name = "fecha_nacimiento", nullable = true, updatable = true, unique = false)
    private String fechaNacimiento;

    @ManyToOne
    @JoinColumn (name = "id_comuna", nullable = false)
    private ComunaJpa comuna;
}
