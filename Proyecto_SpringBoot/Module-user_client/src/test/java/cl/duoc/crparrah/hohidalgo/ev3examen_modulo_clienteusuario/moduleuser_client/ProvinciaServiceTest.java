package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.ProvinciaRequest;
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

import java.util.Arrays;
import java.util.Optional;

public class ProvinciaServiceTest {

    @Mock
    private ProvinciaJpaRepository provinciaJpaRepository;

    @Mock
    private RegionJpaRepository regionJpaRepository;

    @InjectMocks
    private ProvinciaService provinciaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void CreateProvincia() {
        ProvinciaRequest request = new ProvinciaRequest();
        request.setNombreProvincia("ProvinciaNueva");
        request.setIdRegion(1);

        RegionJpa region = new RegionJpa();
        region.setIdRegion(1);
        region.setNombreRegion("RegionTest");

        when(provinciaJpaRepository.findByNombreProvincia("ProvinciaNueva")).thenReturn(Optional.empty());
        when(regionJpaRepository.findById(1)).thenReturn(Optional.of(region));
        when(provinciaJpaRepository.save(any(ProvinciaJpa.class))).thenAnswer(invocation -> {
            ProvinciaJpa provincia = invocation.getArgument(0);
            provincia.setIdProvincia(10);
            provincia.setIdRegion(region);
            return provincia;
        });

        ProvinciaResponse response = provinciaService.createProvincia(request);

        assertEquals(10, response.getIdProvincia());
        assertEquals("ProvinciaNueva", response.getNombreProvincia());
        assertNotNull(response.getRegion());
        assertEquals(1, response.getRegion().getIdRegion());
        assertEquals("RegionTest", response.getRegion().getRegionName());
    }

    @Test
    public void Provincia_nombreDUPLICADO() {
        ProvinciaRequest request = new ProvinciaRequest();
        request.setNombreProvincia("ProvinciaExistente");
        request.setIdRegion(1);

        when(provinciaJpaRepository.findByNombreProvincia("ProvinciaExistente")).thenReturn(Optional.of(new ProvinciaJpa()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            provinciaService.createProvincia(request);
        });

        assertTrue(ex.getMessage().contains("ya existe"));
    }

    @Test
    public void updateProvincia() {
        ProvinciaRequest request = new ProvinciaRequest();
        request.setNombreProvincia("ProvinciaModificada");
        request.setIdRegion(2);

        RegionJpa regionNueva = new RegionJpa();
        regionNueva.setIdRegion(2);
        regionNueva.setNombreRegion("RegionNueva");

        RegionJpa regionAntigua = new RegionJpa();
        regionAntigua.setIdRegion(1);
        regionAntigua.setNombreRegion("RegionAntigua");

        ProvinciaJpa provinciaExistente = new ProvinciaJpa();
        provinciaExistente.setIdProvincia(10);
        provinciaExistente.setNombreProvincia("ProvinciaAntigua");
        provinciaExistente.setIdRegion(regionAntigua);

        when(provinciaJpaRepository.findById(10)).thenReturn(Optional.of(provinciaExistente));
        when(provinciaJpaRepository.findByNombreProvincia("ProvinciaModificada")).thenReturn(Optional.empty());
        when(regionJpaRepository.findById(2)).thenReturn(Optional.of(regionNueva));
        when(provinciaJpaRepository.save(any(ProvinciaJpa.class))).thenAnswer(i -> i.getArgument(0));

        ProvinciaResponse response = provinciaService.updateProvincia(10, request);

        assertEquals(10, response.getIdProvincia());
        assertEquals("ProvinciaModificada", response.getNombreProvincia());
        assertNotNull(response.getRegion());
        assertEquals(2, response.getRegion().getIdRegion());
        assertEquals("RegionNueva", response.getRegion().getRegionName());
    }

    @Test
    public void UpdateProvincia_nombreDuplicado() {
        ProvinciaRequest request = new ProvinciaRequest();
        request.setNombreProvincia("ProvinciaDuplicada");
        request.setIdRegion(1);

        ProvinciaJpa provinciaExistente = new ProvinciaJpa();
        provinciaExistente.setIdProvincia(10);
        provinciaExistente.setNombreProvincia("NombreAntiguo");

        when(provinciaJpaRepository.findById(10)).thenReturn(Optional.of(provinciaExistente));
        when(provinciaJpaRepository.findByNombreProvincia("ProvinciaDuplicada")).thenReturn(Optional.of(new ProvinciaJpa()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            provinciaService.updateProvincia(10, request);
        });
        assertTrue(ex.getMessage().contains("ya está registrada"));
    }

    @Test
    public void UpdateProvincia_regionNombreExiste() {
        ProvinciaRequest request = new ProvinciaRequest();
        request.setNombreProvincia("ProvinciaValida");
        request.setIdRegion(99);

        ProvinciaJpa provinciaExistente = new ProvinciaJpa();
        RegionJpa regionActual = new RegionJpa();
        regionActual.setIdRegion(1);
        provinciaExistente.setIdProvincia(10);
        provinciaExistente.setNombreProvincia("NombreAntiguo");
        provinciaExistente.setIdRegion(regionActual);

        when(provinciaJpaRepository.findById(10)).thenReturn(Optional.of(provinciaExistente));
        when(provinciaJpaRepository.findByNombreProvincia("ProvinciaValida")).thenReturn(Optional.empty());
        when(regionJpaRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            provinciaService.updateProvincia(10, request);
        });
        assertTrue(ex.getMessage().contains("no encontrada"));
    }

    @Test
    public void deleteProvinciaById() {
        when(provinciaJpaRepository.existsById(10)).thenReturn(true);
        doNothing().when(provinciaJpaRepository).deleteById(10);

        assertDoesNotThrow(() -> provinciaService.deleteProvinciaById(10));

        verify(provinciaJpaRepository).deleteById(10);
    }

    @Test
    public void DeleteProvinciaById_noExiste() {
        when(provinciaJpaRepository.existsById(99)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            provinciaService.deleteProvinciaById(99);
        });

        assertTrue(ex.getMessage().contains("no existe"));
    }

    @Test
    public void GetProvinciaById_existe() {
        ProvinciaJpa provincia = new ProvinciaJpa();
        provincia.setIdProvincia(10);

        when(provinciaJpaRepository.findById(10)).thenReturn(Optional.of(provincia));

        Optional<ProvinciaJpa> result = provinciaService.getProvinciaById(10);

        assertTrue(result.isPresent());
        assertEquals(10, result.get().getIdProvincia());
    }

    @Test
    public void GetProvinciaById_noExiste() {
        when(provinciaJpaRepository.findById(99)).thenReturn(Optional.empty());

        Optional<ProvinciaJpa> result = provinciaService.getProvinciaById(99);

        assertFalse(result.isPresent());
    }

    @Test
    public void GetAllProvincias() {
        ProvinciaJpa p1 = new ProvinciaJpa();
        p1.setIdProvincia(1);
        ProvinciaJpa p2 = new ProvinciaJpa();
        p2.setIdProvincia(2);

        when(provinciaJpaRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        var result = provinciaService.getAllProvincias();

        assertEquals(2, result.size());
    }
}
