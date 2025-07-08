package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ProductoRequest {
    private String nombreProducto;
    private String descripcionProducto;
    private Integer precio;
    private String sku;
    private Integer stockDisponible;
    private String categoria;
}
