package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Controller;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.ProvinciaRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.RoleRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.ProvinciaResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.RoleResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ProvinciaJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.RegionJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.RoleJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/provincias")
public class ProvinciaController {
    @Autowired
    private ProvinciaService provinciaService;

    @GetMapping
    public ResponseEntity<List<ProvinciaJpa>> getAllProvincias() {
        List<ProvinciaJpa> provincias = provinciaService.getAllProvincias();
        return new ResponseEntity<>(provincias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProvinciaJpa> getProvinciaById(@PathVariable Integer id) {
        Optional<ProvinciaJpa> provincia = provinciaService.getProvinciaById(id);
        return provincia.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> createProvincia(@RequestBody ProvinciaRequest provinciaRequest) {
        try {
            ProvinciaResponse createdProvincia = provinciaService.createProvincia(provinciaRequest);
            return new ResponseEntity<>(createdProvincia, HttpStatus.CREATED);
        } catch (RuntimeException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProvincia(@PathVariable Integer id,@RequestBody ProvinciaRequest provinciaRequest) {
        try {
            ProvinciaResponse updatedProvincia = provinciaService.updateProvincia(id, provinciaRequest);
            return new ResponseEntity<>(updatedProvincia, HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrada")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvincia(@PathVariable Integer id) {
        try{
            provinciaService.deleteProvinciaById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (RuntimeException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

