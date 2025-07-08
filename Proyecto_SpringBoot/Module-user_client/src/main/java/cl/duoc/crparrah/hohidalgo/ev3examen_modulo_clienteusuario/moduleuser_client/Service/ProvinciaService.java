package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.ProvinciaRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.ProvinciaResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.RegionResponse;
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


    public ProvinciaResponse createProvincia(ProvinciaRequest provinciaRequest) {
        if (provinciaJpaRepository.findByNombreProvincia(provinciaRequest.getNombreProvincia()).isPresent()) {
            throw new RuntimeException("La provincia con nombre: " + provinciaRequest.getNombreProvincia() + " ya existe.");
        }

        RegionJpa region = regionJpaRepository.findById(provinciaRequest.getIdRegion())
                .orElseThrow(() -> new RuntimeException("Región con ID " + provinciaRequest.getIdRegion() + " no encontrada."));

        ProvinciaJpa provincia = new ProvinciaJpa();
        provincia.setNombreProvincia(provinciaRequest.getNombreProvincia());
        provincia.setIdRegion(region);

        ProvinciaJpa savedProvincia = provinciaJpaRepository.save(provincia);

        RegionResponse regionResponse = new RegionResponse(savedProvincia.getIdRegion().getIdRegion(),
                savedProvincia.getIdRegion().getNombreRegion());

        return new ProvinciaResponse(
                savedProvincia.getIdProvincia(),
                savedProvincia.getNombreProvincia(),
                regionResponse);
    }

    public List <ProvinciaJpa> getAllProvincias() {
        return provinciaJpaRepository.findAll();
    }

    public Optional<ProvinciaJpa> getProvinciaById(Integer id) {
        return provinciaJpaRepository.findById(id);
    }


    public ProvinciaResponse updateProvincia(Integer id, ProvinciaRequest provinciaRequest) {
        ProvinciaJpa existingProvincia = provinciaJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Provincia con ID " + id + " no encontrada."));

        if (!existingProvincia.getNombreProvincia().equals(provinciaRequest.getNombreProvincia())) {
            if (provinciaJpaRepository.findByNombreProvincia(provinciaRequest.getNombreProvincia()).isPresent()) {
                throw new RuntimeException("La provincia '" + provinciaRequest.getNombreProvincia() + "' ya está registrada.");
            }
        }

        if (!existingProvincia.getIdRegion().getIdRegion().equals(provinciaRequest.getIdRegion())) {
            RegionJpa newRegion = regionJpaRepository.findById(provinciaRequest.getIdRegion())
                    .orElseThrow(() -> new RuntimeException("Región con ID " + provinciaRequest.getIdRegion() + " no encontrada."));
            existingProvincia.setIdRegion(newRegion);
        }

        existingProvincia.setNombreProvincia(provinciaRequest.getNombreProvincia());
        ProvinciaJpa updatedProvincia = provinciaJpaRepository.save(existingProvincia);

        RegionResponse regionResponse = new RegionResponse(updatedProvincia.getIdRegion().getIdRegion(), updatedProvincia.getIdRegion().getNombreRegion());

        return new ProvinciaResponse(
                updatedProvincia.getIdProvincia(),
                updatedProvincia.getNombreProvincia(),
                regionResponse // ¡CORREGIDO! Pasando el objeto RegionResponse
        );
    }

    public void deleteProvinciaById(Integer id) {
        if (!provinciaJpaRepository.existsById(id)) {
            throw new RuntimeException("Provincia con ID " + id + " no existe");
        }provinciaJpaRepository.deleteById(id);
    }
}