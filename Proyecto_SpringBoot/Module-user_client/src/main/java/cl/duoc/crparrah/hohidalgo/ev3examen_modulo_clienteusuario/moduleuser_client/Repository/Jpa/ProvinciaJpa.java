package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa;

import lombok.*;
import jakarta.persistence.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table (name = "provincia")
public class ProvinciaJpa {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_provincia", nullable = false,unique = true,updatable = false)
    private Integer idProvincia;

    @Column (name = "nombre_provincia", nullable = false,unique = true,updatable = true)
    private String nombreProvincia;

    @ManyToOne
    @JoinColumn (name = "id_region", nullable = false)
    private RegionJpa region;
}
