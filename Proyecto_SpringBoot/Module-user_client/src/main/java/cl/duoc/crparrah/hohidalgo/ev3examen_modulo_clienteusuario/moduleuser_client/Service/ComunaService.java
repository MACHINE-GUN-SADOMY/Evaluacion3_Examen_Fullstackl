package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.ComunaRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.ComunaResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.ProvinciaResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.RegionResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.ComunaJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ComunaJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ProvinciaJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.ProvinciaJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComunaService {
    @Autowired
    ComunaJpaRepository comunaJpaRepository;
    @Autowired
    ProvinciaJpaRepository provinciaJpaRepository;

    public ComunaService(ComunaJpaRepository comunaJpaRepository, ProvinciaJpaRepository provinciaJpaRepository) {
        this.comunaJpaRepository = comunaJpaRepository;
        this.provinciaJpaRepository = provinciaJpaRepository;
    }

    public ComunaResponse createComuna(ComunaRequest comunaRequest) {
        if (comunaJpaRepository.findByNombreComuna(comunaRequest.getComunaNombre()).isPresent()) {
            throw new RuntimeException("La comuna con nombre: " + comunaRequest.getComunaNombre() + " ya existe.");
        }

        ProvinciaJpa provincia = provinciaJpaRepository.findById(comunaRequest.getIdProvincia())
                .orElseThrow(() -> new RuntimeException("Provincia con ID " + comunaRequest.getIdProvincia() + " no encontrada."));

        ComunaJpa comuna = new ComunaJpa();
        comuna.setNombreComuna(comunaRequest.getComunaNombre());
        comuna.setIdProvincia(provincia);

        ComunaJpa savedComuna = comunaJpaRepository.save(comuna);

        RegionResponse regionResponse = null;
        if (savedComuna.getIdProvincia() != null && savedComuna.getIdProvincia().getIdRegion() != null) {
            regionResponse = new RegionResponse(
                    savedComuna.getIdProvincia().getIdRegion().getIdRegion(),
                    savedComuna.getIdProvincia().getIdRegion().getNombreRegion()
            );
        }

        ProvinciaResponse provinciaResponse = null;
        if (savedComuna.getIdProvincia() != null) {
            provinciaResponse = new ProvinciaResponse(
                    savedComuna.getIdProvincia().getIdProvincia(),
                    savedComuna.getIdProvincia().getNombreProvincia(),
                    regionResponse
            );
        }

        return new ComunaResponse(
                savedComuna.getIdComuna()
                ,savedComuna.getNombreComuna(),
                provinciaResponse
        );
    }

    public ComunaResponse updateComuna(Integer id,ComunaRequest comunaRequest) {
        ComunaJpa existingComuna = comunaJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comuna con ID " + id + " no encontrada."));

        if (!existingComuna.getNombreComuna().equals(comunaRequest.getComunaNombre())) {
            if (comunaJpaRepository.findByNombreComuna(comunaRequest.getComunaNombre()).isPresent()) {
                throw new RuntimeException("La comuna '" + comunaRequest.getComunaNombre() + "' ya está registrada.");
            }
        }

        if (!existingComuna.getIdProvincia().getIdProvincia().equals(comunaRequest.getIdProvincia())) {
            ProvinciaJpa newProvincia = provinciaJpaRepository.findById(comunaRequest.getIdProvincia())
                    .orElseThrow(() -> new RuntimeException("Provincia con ID " + comunaRequest.getIdProvincia() + " no encontrada."));
            existingComuna.setIdProvincia(newProvincia);
        }

        existingComuna.setNombreComuna(comunaRequest.getComunaNombre());
        ComunaJpa updatedComuna = comunaJpaRepository.save(existingComuna);

        ProvinciaJpa provinciaJpa = updatedComuna.getIdProvincia();

        RegionResponse regionResponse = null;
        if (provinciaJpa != null && provinciaJpa.getIdRegion().getIdRegion() != null) {
            regionResponse = new RegionResponse(
                    provinciaJpa.getIdRegion().getIdRegion(),
                    provinciaJpa.getIdRegion().getNombreRegion()
            );
        }

        ProvinciaResponse provinciaResponse = null;
        if (provinciaJpa != null) {
            provinciaResponse = new ProvinciaResponse(
                    provinciaJpa.getIdProvincia(),
                    provinciaJpa.getNombreProvincia(),
                    regionResponse);
        }

        return new ComunaResponse(
                updatedComuna.getIdComuna(),
                updatedComuna.getNombreComuna(),
                provinciaResponse);
    }

    public boolean deleteComuna(Integer id) {
        if(!comunaJpaRepository.existsById(id)) {
            return false;
        }comunaJpaRepository.deleteById(id);
        return true;
    }

    public Optional<ComunaJpa> getComunaById(Integer id) {
        return comunaJpaRepository.findById(id);
    }

    public List<ComunaJpa> getAllComunas() {
        return comunaJpaRepository.findAll();
    }
}