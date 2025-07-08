package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.DetalleOrdenJpa;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DetalleOrdenJpaRepository extends JpaRepository<DetalleOrdenJpa, Integer> {
    List<DetalleOrdenJpa> findByOrdenIdOrden(Integer id);

    @EntityGraph(attributePaths = {"producto", "orden", "orden.carrito"})
    Optional<DetalleOrdenJpa> findById(Integer id);
}
