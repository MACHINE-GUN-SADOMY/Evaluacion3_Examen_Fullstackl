package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Controller;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Request.DetalleOrdenRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.DetalleOrdenResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service.DetalleOrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/detalle")
@RestController
public class DetalleOrdenController {
    @Autowired
    private DetalleOrdenService detalleOrdenService;

    @GetMapping
    public ResponseEntity<List<DetalleOrdenResponse>> getAllDetallesOrden() {
        List<DetalleOrdenResponse> detallesOrden = detalleOrdenService.getAllDetallesOrden();
        return new ResponseEntity<>(detallesOrden, HttpStatus.OK);
    }

    @GetMapping("/{idDetalleOrden}")
    public ResponseEntity<DetalleOrdenResponse> getDetalleOrdenById(@PathVariable Integer idDetalleOrden) {
        DetalleOrdenResponse detalleOrden = detalleOrdenService.getDetalleOrdenById(idDetalleOrden);
        return new ResponseEntity<>(detalleOrden, HttpStatus.OK);
    }

    @DeleteMapping("/{idDetalleOrden}")
    public ResponseEntity<String> deleteDetalleOrden(@PathVariable Integer idDetalleOrden) {
        detalleOrdenService.deleteDetalleOrden(idDetalleOrden);
        return new ResponseEntity<>("Detalle de orden eliminado con éxito", HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<String> agregarDetalleOrden(@RequestBody DetalleOrdenRequest detalleOrdenRequest) {
        String response = detalleOrdenService.agregarDetalleOrden(detalleOrdenRequest);
        return ResponseEntity.ok(response);
    }
}
