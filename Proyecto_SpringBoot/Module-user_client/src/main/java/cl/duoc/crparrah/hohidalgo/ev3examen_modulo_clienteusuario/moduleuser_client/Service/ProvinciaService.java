package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.ProvinciaRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ProvinciaJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.RegionJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.ProvinciaJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.RegionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import java.util.List;
import java.util.Optional;

@Service
public class ProvinciaService {
    @Autowired
    private final ProvinciaJpaRepository provinciaJpaRepository;
    @Autowired
    private final RegionJpaRepository regionJpaRepository;

    public ProvinciaService(ProvinciaJpaRepository provinciaJpaRepository,
                            RegionJpaRepository regionJpaRepository) {
        this.provinciaJpaRepository = provinciaJpaRepository;
        this.regionJpaRepository = regionJpaRepository;
    }

    // GUARDAR PROVINCIA
    public Optional<Integer> saveProvincia(ProvinciaRequest provincia) {
        Optional<ProvinciaJpa> found = provinciaJpaRepository.findByNombreProvincia(provincia.getNombreProvincia());
        if (found.isPresent()) {
            return Optional.empty();
        }

        RegionJpa region = regionJpaRepository.findById(provincia.getIdProvincia())
                .orElseThrow(() -> new RuntimeException("Provincia no encontrado"));

        ProvinciaJpa newProvincia = new ProvinciaJpa();
        newProvincia.setNombreProvincia(provincia.getNombreProvincia());
        newProvincia.setRegion(region);

        return Optional.of(provinciaJpaRepository.save(newProvincia).getIdProvincia());
    }

    // GET TODAS LAS PROVINCIAS
    public List <ProvinciaJpa> getAllProvincias() {
        return provinciaJpaRepository.findAll();
    }

    // GET PROVINCIA ID
    public Optional<ProvinciaJpa> getProvincia(Integer id) {
        return provinciaJpaRepository.findById(id);
    }

    // UPDATE

    // DELETE
    public boolean deleteClienteById(Integer id) {
        if (!provinciaJpaRepository.existsById(id)) {
            return false;
        }provinciaJpaRepository.deleteById(id);
        return true;
    }

}
