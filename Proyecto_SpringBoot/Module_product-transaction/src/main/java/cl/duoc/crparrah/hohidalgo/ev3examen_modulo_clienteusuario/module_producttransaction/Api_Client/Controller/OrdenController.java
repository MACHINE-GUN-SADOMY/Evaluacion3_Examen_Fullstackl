
package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Controller;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Request.OrdenRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.OrdenResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.OrdenJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service.OrdenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {
    @Autowired
    private OrdenService ordenService;

    @PostMapping
    public ResponseEntity<OrdenResponse> crearOrden(@RequestBody OrdenRequest ordenRequest) {
        try {
            OrdenResponse ordenResponse = ordenService.crearOrden(ordenRequest);
            return new ResponseEntity<>(ordenResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{idOrden}")
    public ResponseEntity<OrdenResponse> getOrdenById(@PathVariable Integer idOrden) {
        try {
            OrdenResponse ordenResponse = ordenService.getOrdenById(idOrden);
            return new ResponseEntity<>(ordenResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<OrdenResponse>> getAllOrdenes() {
        try {
            List<OrdenResponse> ordenes = ordenService.getAllOrdenes();
            return new ResponseEntity<>(ordenes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{idOrden}")
    public ResponseEntity<?> modificarEstadoOrden(@PathVariable Integer idOrden, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        try {
            ordenService.modificarEstadoOrden(idOrden, nuevoEstado);
            return ResponseEntity.ok("Estado de la orden actualizado exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idOrden}")
    public ResponseEntity<?> eliminarOrden(@PathVariable Integer idOrden) {
        try {
            ordenService.deleteOrdenById(idOrden);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}