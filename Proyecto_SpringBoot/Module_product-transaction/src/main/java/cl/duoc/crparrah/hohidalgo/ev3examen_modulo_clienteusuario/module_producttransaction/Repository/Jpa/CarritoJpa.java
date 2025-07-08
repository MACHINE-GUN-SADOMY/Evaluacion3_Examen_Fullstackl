package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ItemCarritoJpa> items = new ArrayList<>();

    @OneToOne(mappedBy = "carrito", cascade = CascadeType.ALL)
    private OrdenJpa orden;

    @Column(name = "id_cliente", nullable = false, unique = true)
    private Integer clienteId;

    public int getCantidadTotal() {
        return items.stream()
                .mapToInt(ItemCarritoJpa::getCantidad)
                .sum();
    }
}
