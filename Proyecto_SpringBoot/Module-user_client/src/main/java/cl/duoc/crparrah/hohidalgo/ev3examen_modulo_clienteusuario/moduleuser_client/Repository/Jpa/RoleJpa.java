package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@Table (name = "role")
public class RoleJpa {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_role", nullable = false, updatable = false, unique = true)
    private Integer idRole;

    @Column (name = "nombre_role", nullable = false,unique = true,updatable = true)
    private String nombreRole;
}
