package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Request;

import lombok.*;

@Data
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ItemCarritoRequest {
    private Integer idProducto;
    private Integer cantidad;
}
