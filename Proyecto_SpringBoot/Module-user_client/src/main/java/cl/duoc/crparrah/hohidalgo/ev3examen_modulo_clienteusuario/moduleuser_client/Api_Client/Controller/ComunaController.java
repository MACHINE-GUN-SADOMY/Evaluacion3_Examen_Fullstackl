package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Controller;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.ComunaRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.ComunaResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ComunaJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service.ComunaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comunas")
public class ComunaController {
    @Autowired
    private ComunaService comunaService;

    @GetMapping
    public ResponseEntity<List<ComunaJpa>> getAllComunas() {
        List<ComunaJpa> comunas = comunaService.getAllComunas();
        return new ResponseEntity<>(comunas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComunaJpa> getComunaById(@PathVariable Integer id) {
        Optional<ComunaJpa> comuna = comunaService.getComunaById(id);
        return comuna.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ComunaResponse> createComuna(@RequestBody ComunaRequest comunaRequest) {
        try {
            ComunaResponse createdComuna = comunaService.createComuna(comunaRequest);
            return new ResponseEntity<>(createdComuna, HttpStatus.CREATED);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComuna(@PathVariable Integer id,@RequestBody ComunaRequest comunaRequest) {
        try {
            ComunaResponse updatedComuna = comunaService.updateComuna(id,comunaRequest);
            return new ResponseEntity<>(updatedComuna, HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrada")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComuna(@PathVariable Integer id) {
        try{
            comunaService.deleteComuna(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (RuntimeException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
