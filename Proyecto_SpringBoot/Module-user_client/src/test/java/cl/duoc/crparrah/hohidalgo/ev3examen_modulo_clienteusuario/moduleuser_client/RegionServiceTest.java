package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.RegionJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.RegionJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service.RegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para RegionService Devolvera directamente entidades JPA")
public class RegionServiceTest {

    @Mock
    private RegionJpaRepository regionJpaRepository;

    @InjectMocks
    private RegionService regionService;

    private RegionJpa regionJpa1;
    private RegionJpa regionJpa2;

    @BeforeEach
    void setUp() {
        regionJpa1 = new RegionJpa(1, "Región Metropolitana");
        regionJpa2 = new RegionJpa(2, "Valparaíso");
    }

    @Test
    @DisplayName("Debe obtener todas las regiones")
    void getAllRegiones_ShouldReturnAllRegionsAsJpaList() {
        when(regionJpaRepository.findAll()).thenReturn(Arrays.asList(regionJpa1, regionJpa2));

        List<RegionJpa> result = regionService.getAllRegion();

        verify(regionJpaRepository, times(1)).findAll();

        assertNotNull(result, "El resultado no debería ser nulo");

        assertEquals(2, result.size(), "La lista debería contener 2 regiones");
        List<RegionJpa> expected = Arrays.asList(regionJpa1, regionJpa2);
        assertEquals(expected, result, "Las listas de regiones JPA no coinciden");
    }

    @Test
    @DisplayName("Debe obtener una region por ID")
    void getRegionById_ShouldReturnRegionJpa_WhenFound() {
        when(regionJpaRepository.findById(1)).thenReturn(Optional.of(regionJpa1));

        Optional<RegionJpa> result = regionService.findRegionById(1);

        verify(regionJpaRepository, times(1)).findById(1);

        assertTrue(result.isPresent(), "La region deberia haber sido encontrada");

        assertEquals(regionJpa1, result.get(), "La region encontrada no coincide con la esperada");
    }

    @Test
    @DisplayName("Debe devolver Optional.empty cuando la region por ID no se encuentra")
    void getRegionById_ShouldReturnEmpty_WhenNotFound() {
        when(regionJpaRepository.findById(anyInt())).thenReturn(Optional.empty());

        Optional<RegionJpa> result = regionService.findRegionById(99);

        verify(regionJpaRepository, times(1)).findById(99);

        assertFalse(result.isPresent(), "La región no debería haber sido encontrada");
    }

    @Test
    @DisplayName("Debe devolver una lista vacía de entidades JPA si no hay regiones")
    void getAllRegiones_ShouldReturnEmptyJpaList_WhenNoRegions() {
        when(regionJpaRepository.findAll()).thenReturn(Collections.emptyList());

        List<RegionJpa> result = regionService.getAllRegion();

        verify(regionJpaRepository, times(1)).findAll();

        assertNotNull(result, "El resultado no debería ser nulo");
        assertTrue(result.isEmpty(), "La lista debería estar vacía");
    }
}
