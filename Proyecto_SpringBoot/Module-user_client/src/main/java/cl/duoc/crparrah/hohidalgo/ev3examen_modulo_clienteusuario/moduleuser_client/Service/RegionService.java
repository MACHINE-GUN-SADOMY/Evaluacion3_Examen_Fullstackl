package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.RegionRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.RegionJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.RegionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegionService {
    @Autowired
    private final RegionJpaRepository regionJpaRepository;

    public RegionService(RegionJpaRepository regionJpaRepository) {
        this.regionJpaRepository = regionJpaRepository;
    }

    public Optional<Integer> createRegion(RegionRequest regionRequest) {
        Optional <RegionJpa> found = regionJpaRepository.findByNombreRegion(regionRequest.getNombreRegion());
        if (found.isPresent()) {
            return Optional.empty();
        }

        RegionJpa newRegion = new RegionJpa();
        newRegion.setNombreRegion(regionRequest.getNombreRegion());

        return Optional.of(regionJpaRepository.save(newRegion).getIdRegion());
    }

    public Optional <RegionJpa> findRegionById(Integer idRegion) {
        return regionJpaRepository.findById(idRegion);
    }

    public List<RegionJpa> getAllRegion () {
        return regionJpaRepository.findAll();
    }

    // falta delete , put
}
