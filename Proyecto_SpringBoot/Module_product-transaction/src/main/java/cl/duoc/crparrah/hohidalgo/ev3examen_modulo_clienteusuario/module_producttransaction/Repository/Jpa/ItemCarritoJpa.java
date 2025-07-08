package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "items_carrito")
public class ItemCarritoJpa {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_item")
    private Integer idItemCarrito;

    @Column (name = "cantidad", nullable = false, updatable = true, unique = false)
    private Integer cantidad;

    @Column (name = "precio_unitario", nullable = false,updatable = true, unique = false)
    private Integer precioUnitario;

    @ManyToOne
    @JoinColumn (name = "id_producto", nullable = false)
    private ProductoJpa producto;

    @ManyToOne
    @JoinColumn (name = "id_carrito", nullable = false)
    @JsonBackReference
    private CarritoJpa carrito;
}
