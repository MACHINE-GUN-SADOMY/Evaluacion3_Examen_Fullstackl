package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.CarritoJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoJpaRepository extends JpaRepository<CarritoJpa, Integer> {
    Optional<CarritoJpa> findByClienteId(Integer clienteId);
}
