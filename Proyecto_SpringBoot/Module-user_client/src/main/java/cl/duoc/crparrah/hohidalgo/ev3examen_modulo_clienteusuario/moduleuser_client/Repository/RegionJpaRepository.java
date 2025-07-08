package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.RegionJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionJpaRepository
extends JpaRepository<RegionJpa, Integer> {
   Optional<RegionJpa> findByNombreRegion(String nombreRegion);
}
