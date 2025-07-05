package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "carritos")
public class CarritoJpa {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_carrito", nullable = false, updatable = false, unique = false)
    private Integer id;

    @Column (name = "fecha_creacion", nullable = false, updatable = false, unique = false)
    private LocalDate fechaCreacion;
}
