package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "comuna")
public class ComunaJpa {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_comuna", nullable = false, updatable = false, unique = true)
    private Integer comuna;

    @Column (name = "nombre_comuna", nullable = false, unique = true)
    private String nombreComuna;

    @ManyToOne
    @JoinColumn (name = "id_provincia", nullable = false)
    private ProvinciaJpa provinciaJpa;
}
