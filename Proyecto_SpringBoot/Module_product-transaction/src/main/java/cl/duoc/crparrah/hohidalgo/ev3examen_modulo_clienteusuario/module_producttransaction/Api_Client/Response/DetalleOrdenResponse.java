package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.OrdenJpa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class DetalleOrdenResponse {
    private Integer id;
    private Integer cantidadProductos;
    private Integer sub_item;
    private Integer montoTotal;
    private OrdenJpa orden;
    private ProductoResponse producto;
}
