package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response;

import lombok.*;

@Data
@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ItemCarritoResponse {
        private Integer idItemCarrito;
        private Integer idCarrito;
        private Integer idProducto;
        private Integer cantidad;
        private String nombreProducto;
        private Integer precioUnitario;
}
