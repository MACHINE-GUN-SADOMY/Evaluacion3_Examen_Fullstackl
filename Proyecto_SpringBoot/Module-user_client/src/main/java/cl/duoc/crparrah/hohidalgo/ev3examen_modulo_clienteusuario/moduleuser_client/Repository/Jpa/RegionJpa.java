package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.stereotype.Service;

@Entity
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@Table (name = "region")
public class RegionJpa {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_region", nullable = false, unique = true, updatable = false)
    private Integer idRegion;

    @Column (name = "nombre_region", nullable = false, unique = true)
    private String nombreRegion;
}