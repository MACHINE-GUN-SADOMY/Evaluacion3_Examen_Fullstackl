package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.RoleRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.RoleJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.RoleJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private final RoleJpaRepository roleJpaRepository;

    public RoleService(RoleJpaRepository roleJpaRepository) {
        this.roleJpaRepository = roleJpaRepository;
    }

    public List<RoleJpa> findAll() {
        return roleJpaRepository.findAll();
    }

    public Optional<RoleJpa> findById(Integer id) {
        return roleJpaRepository.findById(id);
    }

    public Optional<Integer> saveRole(RoleRequest roleRequest) {
        Optional<RoleJpa> found = roleJpaRepository.findByNombreRole(roleRequest.getRoleNombre());

        if (found.isPresent()) {
            return Optional.empty();
        }

        RoleJpa newRole = new RoleJpa();
        newRole.setNombreRole(roleRequest.getRoleNombre());

        return Optional.of(roleJpaRepository.save(newRole).getIdRole());
    }

    // falta put delete
}
