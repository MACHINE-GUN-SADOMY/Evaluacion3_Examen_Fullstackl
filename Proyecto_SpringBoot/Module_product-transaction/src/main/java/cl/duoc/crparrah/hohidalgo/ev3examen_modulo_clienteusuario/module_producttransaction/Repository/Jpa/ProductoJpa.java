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
@Table(name = "productos")
public class ProductoJpa {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id_producto", nullable = false, updatable = false,unique = true)
    private Integer idProducto;

    @Column (name = "nombre_producto", nullable = false, updatable = true,unique = true)
    private String nombreProducto;

    @Column (name = "descripcion", nullable = false, updatable = true, unique = true)
    private String descripcionProducto;

    @Column (name = "precio", nullable = false, updatable = true, unique = true)
    private Integer precio;

    @Column (name = "sku", nullable = false, updatable = true, unique = true)
    private String sku;

    @Column (name = "stock_disponible", nullable = false, updatable = true, unique = false)
    private Integer stockDisponible;

    @Column (name = "categoria", nullable = false, updatable = true, unique = false)
    private String categoria;
}
