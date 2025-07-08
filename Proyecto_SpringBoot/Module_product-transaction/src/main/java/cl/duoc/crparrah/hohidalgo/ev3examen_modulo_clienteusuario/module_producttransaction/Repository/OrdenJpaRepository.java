package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.OrdenJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdenJpaRepository extends JpaRepository<OrdenJpa, Integer> {
    Optional<OrdenJpa> findByCarritoId(Integer idCarrito);
    Optional<OrdenJpa> findByIdCliente(Integer idCliente);
    Optional<OrdenJpa> findByIdOrden(Integer idOrden);
}