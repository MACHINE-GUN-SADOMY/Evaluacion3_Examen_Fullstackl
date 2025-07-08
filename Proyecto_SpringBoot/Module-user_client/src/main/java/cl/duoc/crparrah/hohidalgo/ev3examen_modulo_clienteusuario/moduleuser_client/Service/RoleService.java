package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.RoleRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.RoleResponse;
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

    public Optional<RoleJpa> findRoleById(Integer id) {
        return roleJpaRepository.findById(id);
    }

    public RoleResponse createRole(RoleRequest roleRequest) { // ¡CAMBIADO EL TIPO DE RETORNO AQUÍ!
        if (roleJpaRepository.findByNombreRole(roleRequest.getNombreRole()).isPresent()) {
            throw new RuntimeException("El rol con nombre: " + roleRequest.getNombreRole() + " ya existe.");
        }
        RoleJpa role = new RoleJpa(roleRequest.getNombreRole());
        RoleJpa savedRole = roleJpaRepository.save(role);
        return new RoleResponse(savedRole.getIdRole(), savedRole.getNombreRole());
    }


    public RoleResponse updateRole(Integer id, RoleRequest roleRequest) {
        RoleJpa found = roleJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role con ID " + id + " no encontrado."));

        if (!found.getNombreRole().equals(roleRequest.getNombreRole())) {
            if (roleJpaRepository.findByNombreRole(roleRequest.getNombreRole()).isPresent()) {
                throw new RuntimeException("El nombre de role '" + roleRequest.getNombreRole() + "' ya existe.");
            }
        }
        found.setNombreRole(roleRequest.getNombreRole());
        RoleJpa updatedRole = roleJpaRepository.save(found);
        return new RoleResponse(updatedRole.getIdRole(), updatedRole.getNombreRole());
    }

    public void deleteRole(Integer idRole) {
        if (!roleJpaRepository.existsById(idRole)) {
            throw new RuntimeException("Role con ID " + idRole + " no existe");
        }roleJpaRepository.deleteById(idRole);
    }

    public Optional<RoleJpa> findRoleByNombreRole(String nombreRole) {
        return roleJpaRepository.findByNombreRole(nombreRole);
    }
}