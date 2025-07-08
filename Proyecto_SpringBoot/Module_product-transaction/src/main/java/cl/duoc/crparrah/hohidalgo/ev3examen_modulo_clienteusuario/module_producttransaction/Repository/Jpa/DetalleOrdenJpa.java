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

    @Column (name = "monto_total", nullable = false, updatable = true, unique = false)
    private Integer montoTotal;

    @Column (name = "precio", nullable = false, updatable = true, unique = false)
    private Integer precio;

    @Column (name = "total", nullable = false, updatable = true, unique = false)
    private Integer total;

    @OneToOne
    @JoinColumn(name = "id_orden", referencedColumnName = "id_orden")
    private OrdenJpa orden;

    @ManyToOne
    @JoinColumn (name = "id_producto", nullable = false)
    private ProductoJpa producto;
}
