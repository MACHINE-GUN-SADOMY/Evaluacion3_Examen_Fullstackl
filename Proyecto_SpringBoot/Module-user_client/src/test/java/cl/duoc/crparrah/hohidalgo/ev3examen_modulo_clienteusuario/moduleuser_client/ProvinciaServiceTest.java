package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.ProvinciaRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.RegionRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.ProvinciaResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ProvinciaJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.RegionJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.ProvinciaJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.RegionJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service.ProvinciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.Assertions; // Importación explícita de Assertions

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ProvinciaServiceTest {
    @Mock
    private ProvinciaJpaRepository provinciaJpaRepository;

    @Mock
    private RegionJpaRepository regionJpaRepository;

    @InjectMocks
    private ProvinciaService provinciaService;

    private RegionJpa regionJpaRM;
    private ProvinciaJpa provinciaJpaSantiago;
    private ProvinciaJpa provinciaJpaCordillera;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        regionJpaRM = new RegionJpa(1, "Región Metropolitana");
        RegionRequest regionRequest = new RegionRequest("Biobio");

        provinciaJpaSantiago = new ProvinciaJpa();
        provinciaJpaSantiago.setIdProvincia(101);
        provinciaJpaSantiago.setNombreProvincia("Santiago");
        provinciaJpaSantiago.setIdRegion(regionJpaRM);

        provinciaJpaCordillera = new ProvinciaJpa();
        provinciaJpaCordillera.setIdProvincia(102);
        provinciaJpaCordillera.setNombreProvincia("Cordillera");
        provinciaJpaCordillera.setIdRegion(regionJpaRM);
    }

    @Test
    void getAllProvincias_returnProvinciasExistentes() {
        List<ProvinciaJpa> provinciasJpa = Arrays.asList(provinciaJpaSantiago, provinciaJpaCordillera);

        when(provinciaJpaRepository.findAll()).thenReturn(provinciasJpa);

        List<ProvinciaJpa> result = provinciaService.getAllProvincias();

        verify(provinciaJpaRepository, times(1)).findAll();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());

        Assertions.assertEquals(provinciaJpaSantiago.getNombreProvincia(), result.get(0).getNombreProvincia());
        Assertions.assertEquals(provinciaJpaSantiago.getIdRegion().getIdRegion(), result.get(0).getIdRegion().getIdRegion());
        Assertions.assertEquals(provinciaJpaCordillera.getNombreProvincia(), result.get(1).getNombreProvincia());
        Assertions.assertEquals(provinciaJpaCordillera.getIdRegion().getIdRegion(), result.get(1).getIdRegion().getIdRegion());
    }

    @Test
    void getProvinciaById_returnProvinciaExistente() {
        ProvinciaJpa expectedProvincia = provinciaJpaSantiago;

        when(provinciaJpaRepository.findById(expectedProvincia.getIdProvincia())).thenReturn(Optional.of(expectedProvincia));

        Optional<ProvinciaJpa> resultOptional = provinciaService.getProvinciaById(expectedProvincia.getIdProvincia());

        Assertions.assertTrue(resultOptional.isPresent()); // Verifica que el Optional no está vacío
        ProvinciaJpa result = resultOptional.get(); // Obtiene la entidad JPA del Optional
        Assertions.assertEquals(expectedProvincia.getIdProvincia(), result.getIdProvincia());
        Assertions.assertEquals(expectedProvincia.getNombreProvincia(), result.getNombreProvincia());
        Assertions.assertEquals(expectedProvincia.getIdRegion().getNombreRegion(), result.getIdRegion().getNombreRegion()); // Verifica la relación
        verify(provinciaJpaRepository, times(1)).findById(expectedProvincia.getIdProvincia()); // Verifica que el método del repositorio fue llamado
    }

    @Test
    void getProvinciaById_returnUnOptionalEmptySinoExiste() {
        when(provinciaJpaRepository.findById(anyInt())).thenReturn(Optional.empty());

        Optional<ProvinciaJpa> result = provinciaService.getProvinciaById(999);

        Assertions.assertTrue(result.isEmpty());
        verify(provinciaJpaRepository, times(1)).findById(999);
    }

    // PROVINCIA BY NAME , NO SE HARA TEST.

    @Test
    void createProvincia_creaUnaProvincia() {
        ProvinciaRequest newProvinciaRequest = new ProvinciaRequest();
        newProvinciaRequest.setNombreProvincia("Osorno");
        newProvinciaRequest.setIdRegion(regionJpaRM.getIdRegion());

        when(regionJpaRepository.findById(regionJpaRM.getIdRegion())).thenReturn(Optional.of(regionJpaRM));

        ProvinciaJpa savedProvinciaJpa = new ProvinciaJpa();
        savedProvinciaJpa.setIdProvincia(4);
        savedProvinciaJpa.setNombreProvincia("Osorno");
        savedProvinciaJpa.setIdRegion(regionJpaRM);
        when(provinciaJpaRepository.save(any(ProvinciaJpa.class))).thenReturn(savedProvinciaJpa);

        ProvinciaResponse result = provinciaService.createProvincia(newProvinciaRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(4, result.getIdProvincia());
        Assertions.assertEquals("Osorno", result.getNombreProvincia());
        Assertions.assertNotNull(result.getRegion());
        Assertions.assertEquals(regionJpaRM.getIdRegion(), result.getRegion().getIdRegion());
        Assertions.assertEquals(regionJpaRM.getNombreRegion(), result.getRegion().getRegionName());

        verify(regionJpaRepository, times(1)).findById(regionJpaRM.getIdRegion()); // Verifica que se buscó la región
        verify(provinciaJpaRepository, times(1)).save(any(ProvinciaJpa.class)); // Verifica que se guardó la provincia
    }

    @Test // arreglar
    void updateProvincia_actualizaUnaProvinciaExistente() {
        ProvinciaJpa existingProvincia = provinciaJpaSantiago;

        ProvinciaRequest updatedProvinciaData = new ProvinciaRequest();
        updatedProvinciaData.setNombreProvincia("Santiago Actualizado");
        updatedProvinciaData.setIdRegion(regionJpaRM.getIdRegion());

        ProvinciaJpa updatedJpa = new ProvinciaJpa();
        updatedJpa.setIdProvincia(existingProvincia.getIdProvincia());
        updatedJpa.setNombreProvincia(updatedProvinciaData.getNombreProvincia());
        updatedJpa.setIdRegion(regionJpaRM);

        when(provinciaJpaRepository.findById(existingProvincia.getIdProvincia())).thenReturn(Optional.of(existingProvincia));
        when(provinciaJpaRepository.save(any(ProvinciaJpa.class))).thenReturn(updatedJpa);

        ProvinciaResponse result = provinciaService.updateProvincia(existingProvincia.getIdProvincia(), updatedProvinciaData);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingProvincia.getIdProvincia(), result.getIdProvincia());
        Assertions.assertEquals("Santiago Actualizado", result.getNombreProvincia());
        Assertions.assertEquals(regionJpaRM.getIdRegion(), result.getRegion().getIdRegion());
        Assertions.assertEquals(regionJpaRM.getNombreRegion(), result.getRegion().getRegionName());
        verify(provinciaJpaRepository, times(1)).findById(existingProvincia.getIdProvincia());
        verify(provinciaJpaRepository, times(1)).save(existingProvincia);
    }

    @Test // arreglar
    void updateProvincia_returnOptionalEmptySinoExistente() {
        ProvinciaRequest updatedProvinciaRequest = new ProvinciaRequest();
        updatedProvinciaRequest.setNombreProvincia("Provincia Ejemplo");
        updatedProvinciaRequest.setIdRegion(999);

        when(provinciaJpaRepository.findById(99)).thenReturn(Optional.empty());

        ProvinciaResponse result = provinciaService.updateProvincia(99, updatedProvinciaRequest);

        Assertions.assertNull(result);
        verify(provinciaJpaRepository, times(1)).findById(99);
        verify(regionJpaRepository, never()).findById(anyInt());
        verify(provinciaJpaRepository, never()).save(any(ProvinciaJpa.class));
    }

    @Test // arreglar
    void deleteProvincia_shouldDeleteExistingProvincia() {
        ProvinciaJpa existingProvincia = provinciaJpaSantiago;
        when(provinciaJpaRepository.findById(existingProvincia.getIdProvincia())).thenReturn(Optional.of(existingProvincia));
        doNothing().when(provinciaJpaRepository).delete(existingProvincia);

        provinciaService.deleteProvinciaById(existingProvincia.getIdProvincia());

        verify(provinciaJpaRepository, times(1)).findById(existingProvincia.getIdProvincia());
        verify(provinciaJpaRepository, times(1)).delete(existingProvincia);
    }

    @Test // arreglar
    void deleteProvincia_noHaceNadaSiLaProvinciaNoExiste() {
        when(provinciaJpaRepository.findById(99)).thenReturn(Optional.empty());

        provinciaService.deleteProvinciaById(99);

        verify(provinciaJpaRepository, times(1)).findById(99);
        verify(provinciaJpaRepository, never()).delete(any(ProvinciaJpa.class));
    }
}