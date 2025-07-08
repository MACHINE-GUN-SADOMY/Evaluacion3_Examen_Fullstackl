package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor @NoArgsConstructor
public class DetalleOrdenResponse {
    private Integer id;
    private Integer cantidadProductos;
    private Integer montoTotal;
    private Integer precio;
    private ProductoResponse producto;
    private OrdenResponse orden;
}