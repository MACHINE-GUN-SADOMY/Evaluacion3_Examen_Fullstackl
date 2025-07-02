package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ComunaJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComunaJpaRepository extends JpaRepository<ComunaJpa, Integer> {
    Optional<ComunaJpa> findByNombreComuna(String nombre);
}
