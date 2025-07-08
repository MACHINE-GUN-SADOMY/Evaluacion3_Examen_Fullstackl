package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Controller;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Request.ItemCarritoRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.CarritoResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.ItemCarritoResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.CarritoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.ItemCarritoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service.CarritoService;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service.ItemCarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {
    @Autowired
    private CarritoService carritoService;

    @Autowired
    private ItemCarritoService itemCarritoService;

    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponse> obtenerCarrito(@PathVariable Integer id) {
        CarritoResponse response = carritoService.findByCarritoId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CarritoResponse>> getAllCarritos() {
        List<CarritoResponse> carritos = carritoService.getAllCarritos();
        return ResponseEntity.ok(carritos);
    }

    @PostMapping("/{idCliente}")
    public ResponseEntity<?> crearCarrito(@PathVariable Integer idCliente) {
        try {
            CarritoResponse response = carritoService.crearCarritoParaCliente(idCliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCarrito(@PathVariable Integer id) {
        try {
            carritoService.deleteCarritoBy(id);
            return ResponseEntity.ok("Carrito eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/agregar/{idCliente}/{idCarrito}")
    public ResponseEntity<?> agregarItemCarrito(@PathVariable Integer idCliente,
                @PathVariable Integer idCarrito, @RequestBody ItemCarritoRequest itemRequest) {
        try {
            ItemCarritoResponse response = itemCarritoService.agregarItemCarrito(idCliente, idCarrito, itemRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/sacar-items/{idCliente}/{idCarrito}/{idItemCarrito}")
    public ResponseEntity<?> eliminarItemCarrito(@PathVariable Integer idCliente,
            @PathVariable Integer idCarrito, @PathVariable Integer idItemCarrito) {
        try {
            itemCarritoService.eliminarItemCarrito(idCliente, idCarrito, idItemCarrito);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Producto eliminado del carrito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
