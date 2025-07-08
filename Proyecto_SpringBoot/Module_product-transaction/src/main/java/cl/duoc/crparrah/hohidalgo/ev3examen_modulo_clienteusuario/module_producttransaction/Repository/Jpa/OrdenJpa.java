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
@Table(name = "ordenes")
public class OrdenJpa {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_orden", nullable = false, unique = true, updatable = false)
    private Integer idOrden;

    @Column(name = "id_cliente", nullable = false, unique = false, updatable = false)
    private Integer idCliente;

    @Column(name = "direccion_cliente", nullable = false)
    private String direccionCliente;

    @Column (name = "fecha_orden", nullable = true, unique = false,updatable = false)
    private LocalDate fechaOrden;

    @Column (name = "monto_total", nullable = false, unique = false, updatable = true)
    private Integer montoTotal;

    @Column (name = "estado_orden", nullable = false, unique = false, updatable = true)
    private String estadoOrden;

    @OneToOne
    @JoinColumn(name = "id_carrito", referencedColumnName = "id_carrito")
    private CarritoJpa carrito;
}
