package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.ProductoJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoJpaRepository extends JpaRepository<ProductoJpa, Integer> {
    Optional<ProductoJpa> findBySku(String sku);
}
