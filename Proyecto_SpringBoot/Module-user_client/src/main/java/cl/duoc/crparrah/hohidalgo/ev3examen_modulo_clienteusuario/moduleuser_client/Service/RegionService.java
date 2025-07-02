package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.RegionRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.RegionResponse;
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

    public RegionResponse createRegion(RegionRequest regionRequest) {
        if (regionJpaRepository.findByNombreRegion(regionRequest.getNombreRegion()).isPresent()) {
            throw new RuntimeException("La región '" + regionRequest.getNombreRegion() + "' ya existe.");
        }
        RegionJpa region = new RegionJpa(regionRequest.getNombreRegion());
        RegionJpa savedRegion = regionJpaRepository.save(region);
        return new RegionResponse(savedRegion.getIdRegion(), savedRegion.getNombreRegion());
    }

    public Optional <RegionJpa> findRegionById(Integer idRegion) {
        return regionJpaRepository.findById(idRegion);
    }

    public List<RegionJpa> getAllRegion () {
        return regionJpaRepository.findAll();
    }

    public void deleteRegion(Integer idRegion) {
        if (!regionJpaRepository.existsById(idRegion)) {
            throw new RuntimeException("Region con ID " + idRegion + " no existe");
        }regionJpaRepository.deleteById(idRegion);
    }

    public RegionResponse updateRegion(Integer id, RegionRequest regionRequest) {
        RegionJpa found = regionJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Región con ID " + id + " no encontrada."));

        if (!found.getNombreRegion().equals(regionRequest.getNombreRegion())) {
            if (regionJpaRepository.findByNombreRegion(regionRequest.getNombreRegion()).isPresent()) {
                throw new RuntimeException("El nombre de región '" + regionRequest.getNombreRegion() + "' ya existe.");
            }
        }
        found.setNombreRegion(regionRequest.getNombreRegion());
        RegionJpa updatedRegion = regionJpaRepository.save(found);
        return new RegionResponse(updatedRegion.getIdRegion(), updatedRegion.getNombreRegion());
    }
}
