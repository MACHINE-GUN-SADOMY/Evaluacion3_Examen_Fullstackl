package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.Builder;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class OrdenResponse {
    private Integer idOrden;
    private Integer idCliente;
    private String direccionCliente;
    private LocalDate fechaOrden;
    private Integer montoTotal;
    private String estadoOrden;
    private CarritoResponse carrito;
}
