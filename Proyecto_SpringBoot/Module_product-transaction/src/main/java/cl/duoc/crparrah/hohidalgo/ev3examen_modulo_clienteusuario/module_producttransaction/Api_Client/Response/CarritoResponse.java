package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarritoResponse {
    private Integer id;
    private LocalDate fechaCreacion;
    private Integer cantidadTotal;
    private Integer idCliente;
    private List<ItemCarritoResponse> items;
}