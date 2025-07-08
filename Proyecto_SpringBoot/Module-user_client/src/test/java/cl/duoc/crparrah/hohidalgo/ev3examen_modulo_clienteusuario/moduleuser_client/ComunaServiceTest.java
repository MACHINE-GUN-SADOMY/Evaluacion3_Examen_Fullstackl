package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.ComunaRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.ComunaResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.ComunaJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.RegionJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.ProvinciaJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ComunaJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ProvinciaJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service.ComunaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

public class ComunaServiceTest {

    @Mock
    private ComunaJpaRepository comunaJpaRepository;

    @Mock
    private ProvinciaJpaRepository provinciaJpaRepository;

    @InjectMocks
    private ComunaService comunaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createComuna() {
        ComunaRequest request = new ComunaRequest();
        request.setComunaNombre("ComunaNueva");
        request.setIdProvincia(1);

        ProvinciaJpa provincia = new ProvinciaJpa();
        provincia.setIdProvincia(1);
        provincia.setNombreProvincia("ProvinciaTest");

        when(comunaJpaRepository.findByNombreComuna("ComunaNueva")).thenReturn(Optional.empty());
        when(provinciaJpaRepository.findById(1)).thenReturn(Optional.of(provincia));
        when(comunaJpaRepository.save(any(ComunaJpa.class))).thenAnswer(invocation -> {
            ComunaJpa comuna = invocation.getArgument(0);
            comuna.setIdComuna(10);
            comuna.setIdProvincia(provincia);
            return comuna;
        });

        ComunaResponse response = comunaService.createComuna(request);

        assertEquals(10, response.getIdComuna());
        assertEquals("ComunaNueva", response.getNombreComuna());
        assertNotNull(response.getProvincia());
        assertEquals(1, response.getProvincia().getIdProvincia());
        assertEquals("ProvinciaTest", response.getProvincia().getNombreProvincia());
    }

    @Test
    public void createComuna_nombreDuplicado() {
        ComunaRequest request = new ComunaRequest();
        request.setComunaNombre("ComunaExistente");
        request.setIdProvincia(1);

        when(comunaJpaRepository.findByNombreComuna("ComunaExistente")).thenReturn(Optional.of(new ComunaJpa()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            comunaService.createComuna(request);
        });

        assertTrue(ex.getMessage().contains("ya existe"));
    }

    @Test
    public void updateComuna() {
        ComunaRequest request = new ComunaRequest();
        request.setComunaNombre("ComunaModificada");
        request.setIdProvincia(2);


        ProvinciaJpa provinciaNueva = new ProvinciaJpa();
        provinciaNueva.setIdProvincia(2);
        provinciaNueva.setNombreProvincia("ProvinciaNueva");

        ProvinciaJpa provinciaAntigua = new ProvinciaJpa();
        provinciaAntigua.setIdProvincia(1);
        provinciaAntigua.setNombreProvincia("ProvinciaAntigua");

        RegionJpa regionNueva = new RegionJpa();
        regionNueva.setIdRegion(1);
        regionNueva.setNombreRegion("RegionNueva");
        provinciaNueva.setIdRegion(regionNueva);

        ComunaJpa comunaExistente = new ComunaJpa();
        comunaExistente.setIdComuna(10);
        comunaExistente.setNombreComuna("ComunaAntigua");
        comunaExistente.setIdProvincia(provinciaAntigua);

        when(comunaJpaRepository.findById(10)).thenReturn(Optional.of(comunaExistente));
        when(comunaJpaRepository.findByNombreComuna("ComunaModificada")).thenReturn(Optional.empty());
        when(provinciaJpaRepository.findById(2)).thenReturn(Optional.of(provinciaNueva));
        when(comunaJpaRepository.save(any(ComunaJpa.class))).thenAnswer(i -> i.getArgument(0));

        ComunaResponse response = comunaService.updateComuna(10, request);

        assertEquals(10, response.getIdComuna());
        assertEquals("ComunaModificada", response.getNombreComuna());
        assertNotNull(response.getProvincia());
        assertEquals(2, response.getProvincia().getIdProvincia());
        assertEquals("ProvinciaNueva", response.getProvincia().getNombreProvincia());
    }

    @Test
    public void updateComuna_nombreDuplicado() {
        ComunaRequest request = new ComunaRequest();
        request.setComunaNombre("ComunaDuplicada");
        request.setIdProvincia(1);

        ComunaJpa comunaExistente = new ComunaJpa();
        comunaExistente.setIdComuna(10);
        comunaExistente.setNombreComuna("NombreAntiguo");

        when(comunaJpaRepository.findById(10)).thenReturn(Optional.of(comunaExistente));
        when(comunaJpaRepository.findByNombreComuna("ComunaDuplicada")).thenReturn(Optional.of(new ComunaJpa()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            comunaService.updateComuna(10, request);
        });
        assertTrue(ex.getMessage().contains("ya está registrada"));
    }

    @Test
    public void updateComuna_provinciaNoExiste() {
        ComunaRequest request = new ComunaRequest();
        request.setComunaNombre("ComunaValida");
        request.setIdProvincia(99);

        ComunaJpa comunaExistente = new ComunaJpa();
        ProvinciaJpa provinciaActual = new ProvinciaJpa();
        provinciaActual.setIdProvincia(1);
        comunaExistente.setIdComuna(10);
        comunaExistente.setNombreComuna("NombreAntiguo");
        comunaExistente.setIdProvincia(provinciaActual);

        when(comunaJpaRepository.findById(10)).thenReturn(Optional.of(comunaExistente));
        when(comunaJpaRepository.findByNombreComuna("ComunaValida")).thenReturn(Optional.empty());
        when(provinciaJpaRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            comunaService.updateComuna(10, request);
        });
        assertTrue(ex.getMessage().contains("no encontrada"));
    }

    @Test
    public void deleteComuna() {
        when(comunaJpaRepository.existsById(10)).thenReturn(true);
        doNothing().when(comunaJpaRepository).deleteById(10);

        boolean result = comunaService.deleteComuna(10);

        assertTrue(result);
        verify(comunaJpaRepository).deleteById(10);
    }

    @Test
    public void deleteComuna_comunaNoExiste() {
        when(comunaJpaRepository.existsById(99)).thenReturn(false);

        boolean result = comunaService.deleteComuna(99);

        assertFalse(result);
        verify(comunaJpaRepository, never()).deleteById(any());
    }

    @Test
    public void getComunaById() {
        ComunaJpa comuna = new ComunaJpa();
        comuna.setIdComuna(10);

        when(comunaJpaRepository.findById(10)).thenReturn(Optional.of(comuna));

        Optional<ComunaJpa> result = comunaService.getComunaById(10);

        assertTrue(result.isPresent());
        assertEquals(10, result.get().getIdComuna());
    }

    @Test
    public void getComunaById_comunaNoExiste() {
        when(comunaJpaRepository.findById(99)).thenReturn(Optional.empty());

        Optional<ComunaJpa> result = comunaService.getComunaById(99);

        assertFalse(result.isPresent());
    }

    @Test
    public void GetAllComunas() {
        ComunaJpa c1 = new ComunaJpa();
        c1.setIdComuna(1);
        ComunaJpa c2 = new ComunaJpa();
        c2.setIdComuna(2);

        when(comunaJpaRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        var result = comunaService.getAllComunas();

        assertEquals(2, result.size());
    }
}
