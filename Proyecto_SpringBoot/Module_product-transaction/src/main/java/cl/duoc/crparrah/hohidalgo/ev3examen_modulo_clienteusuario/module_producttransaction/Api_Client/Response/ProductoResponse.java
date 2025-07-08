package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor @NoArgsConstructor
public class ProductoResponse {
    private Integer idProducto;
    private String nombreProducto;
    private String descripcionProducto;
    private Integer precio;
    private String sku;
    private Integer stockDisponible;
    private String categoria;
}
