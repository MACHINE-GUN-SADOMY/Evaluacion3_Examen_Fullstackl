package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.UsuarioRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.*;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.RoleJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.UsuarioJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.RoleJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.UsuarioJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private final UsuarioJpaRepository usuarioJpaRepository;

    @Autowired
    private final RoleJpaRepository roleJpaRepository;

    public UsuarioService(UsuarioJpaRepository usuarioJpaRepository, RoleJpaRepository roleJpaRepository) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.roleJpaRepository = roleJpaRepository;
    }

    public UsuarioResponse createUsuario(UsuarioRequest usuarioRequest) {
        if (usuarioJpaRepository.findByNombreUsuario(usuarioRequest.getNombreUsuario()).isPresent()) {
            throw new RuntimeException("El Usuario con nombre: " + usuarioRequest.getNombreUsuario() + " ya existe.");
        }

        RoleJpa role = roleJpaRepository.findById(usuarioRequest.getIdRole())
                .orElseThrow(() -> new RuntimeException("Usuario con Role Id" + usuarioRequest.getIdRole() + " no encontrada."));

        UsuarioJpa usuario = new UsuarioJpa();
        usuario.setNombreUsuario(usuarioRequest.getNombreUsuario());
        usuario.setContraseñaUsuario(usuarioRequest.getNombreUsuario());
        usuario.setFechaRegistro(usuarioRequest.getFecaRegistro());
        usuario.setRole(role);

        UsuarioJpa savedUsuario = usuarioJpaRepository.save(usuario);

        RoleResponse roleResponse = null;
        if (savedUsuario.getRole().getIdRole() != null) {
            roleResponse = new RoleResponse(
                    savedUsuario.getRole().getIdRole(),
                    savedUsuario.getRole().getNombreRole());
        }

        return new UsuarioResponse(
                savedUsuario.getIdUsuario()
                , savedUsuario.getNombreUsuario()
                , savedUsuario.getContraseñaUsuario()
                , savedUsuario.getFechaRegistro()
                , roleResponse
        );
    }

    public UsuarioResponse updateUsuario(Integer id, UsuarioRequest usuarioRequest) {
        UsuarioJpa existingUsuario = usuarioJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado."));

        if (!existingUsuario.getNombreUsuario().equals(usuarioRequest.getNombreUsuario())) {
            if (usuarioJpaRepository.findByNombreUsuario(usuarioRequest.getNombreUsuario()).isPresent()) {
                throw new RuntimeException("El usuario '" + usuarioRequest.getNombreUsuario() + "' ya está registrada.");
            }
        }

        if (!existingUsuario.getRole().getIdRole().equals(usuarioRequest.getIdRole())) {
            RoleJpa newRole = roleJpaRepository.findById(usuarioRequest.getIdRole())
                    .orElseThrow(() -> new RuntimeException("Role con ID " + usuarioRequest.getIdRole() + " no encontrada."));
            existingUsuario.setRole(newRole);
        }

        existingUsuario.setNombreUsuario(usuarioRequest.getNombreUsuario());
        UsuarioJpa updatedUsuario = usuarioJpaRepository.save(existingUsuario);

        RoleResponse roleResponse = null;

        if (updatedUsuario.getRole() != null) {
            roleResponse = new RoleResponse(
                    updatedUsuario.getRole().getIdRole(),
                    updatedUsuario.getRole().getNombreRole()
            );
        }
        return new UsuarioResponse(
                updatedUsuario.getIdUsuario(),
                updatedUsuario.getNombreUsuario(),
                updatedUsuario.getContraseñaUsuario(),
                updatedUsuario.getFechaRegistro(),
                roleResponse);
    }

    public Optional<UsuarioJpa> getUsuarioById(Integer id) {
        return usuarioJpaRepository.findById(id);
    }

    public List<UsuarioJpa> getAllusuarios() {
        return usuarioJpaRepository.findAll();
    }

    public boolean deleteUsuario(Integer id) {
        if (!usuarioJpaRepository.existsById(id)) {
            return false;
        }usuarioJpaRepository.deleteById(id);
        return true;
    }
}