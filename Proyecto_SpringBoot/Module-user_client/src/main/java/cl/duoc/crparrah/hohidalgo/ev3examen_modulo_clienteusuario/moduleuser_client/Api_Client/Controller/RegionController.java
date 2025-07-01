package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Controller;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.RegionJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/regiones")
@RestController
public class RegionController {
    @Autowired
    private RegionService regionService;

    @GetMapping
    public ResponseEntity<List<RegionJpa>> getAllRegions(){
        List<RegionJpa> regiones = regionService.getAllRegion();
        return new ResponseEntity<>(regiones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionJpa> getRegionById(@PathVariable Integer id){
        Optional<RegionJpa> region = regionService.findRegionById(id);
        return region.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // incluir POST y DELETE
}
