package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa;

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
@Table(name = "detalle_orden")
public class DetalleOrdenJpa {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_detalle_orden")
    private Integer idDetalleOrden;

    @Column (name = "cantidad_productos", nullable = false, updatable = false, unique = true)
    private Integer cantidadProductos;

    @Column (name = "sub_item", nullable = false, updatable = false, unique = true)
    private Integer sub_item;

    @ManyToOne
    @JoinColumn(name = "id_orden", nullable = false)
    private OrdenJpa orden;

    @ManyToOne
    @JoinColumn (name = "id_producto", nullable = false)
    private ProductoJpa producto;
}
