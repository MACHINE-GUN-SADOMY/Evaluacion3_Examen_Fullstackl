package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ProvinciaJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProvinciaJpaRepository
    extends JpaRepository<ProvinciaJpa, Integer> {
    Optional<ProvinciaJpa> findByNombreProvincia(String nombre);
}
