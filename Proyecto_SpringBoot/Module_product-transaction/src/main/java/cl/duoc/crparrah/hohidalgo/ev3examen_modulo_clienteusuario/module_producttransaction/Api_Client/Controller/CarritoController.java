package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Controller;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.CarritoResponse;
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

    // AGREGAR ITEMS AL CARRITO
    @PostMapping("/agregar/{carritoId}/{productoId}/{cantidad}")
    public ResponseEntity<ItemCarritoJpa> agregarItem(
            @PathVariable Integer carritoId,
            @PathVariable Integer productoId,
            @PathVariable Integer cantidad) {
        ItemCarritoJpa item = itemCarritoService.agregarItemCarrito(carritoId, productoId, cantidad);
        return ResponseEntity.ok(item);
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

    @DeleteMapping("/eliminar/{carritoId}/{itemId}")
    public ResponseEntity<String> eliminarItem(@PathVariable Integer carritoId, @PathVariable Integer itemId) {
        itemCarritoService.eliminarItemCarrito(carritoId,itemId);
        return ResponseEntity.ok("Item eliminado correctamente");
    }
}
