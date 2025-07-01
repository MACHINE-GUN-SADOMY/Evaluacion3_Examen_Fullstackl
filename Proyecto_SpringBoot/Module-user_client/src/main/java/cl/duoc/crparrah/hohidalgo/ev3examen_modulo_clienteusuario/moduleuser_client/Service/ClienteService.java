package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.ClienteRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.ClienteJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.ComunaJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ClienteJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ComunaJpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private final ClienteJpaRepository clienteJpaRepository;
    @Autowired
    private final ComunaJpaRepository comunaJpaRepository;

    public ClienteService(ClienteJpaRepository clienteJpaRepository, ComunaJpaRepository comunaJpaRepository) {
        this.clienteJpaRepository = clienteJpaRepository;
        this.comunaJpaRepository = comunaJpaRepository;
    }

    public Optional<Integer> createCliente (ClienteRequest clienteRequest) {
        Optional<ClienteJpa> found = clienteJpaRepository.findByEmail(clienteRequest.getEmail());
        if (found.isPresent()) {
            return Optional.empty();
        }

        ComunaJpa comuna = comunaJpaRepository.findById(clienteRequest.getIdComuna())
                .orElseThrow(() -> new RuntimeException("La comuna no existe"));

        ClienteJpa newCliente = new ClienteJpa();
        newCliente.setNombreCliente(clienteRequest.getNombreCliente());
        newCliente.setApellidoCliente(clienteRequest.getApellidoCliente());
        newCliente.setEmail(clienteRequest.getEmail());
        newCliente.setContrasena(clienteRequest.getContrasenia());
        newCliente.setDireccion(clienteRequest.getDireccion());
        newCliente.setTelefono(clienteRequest.getTelefono());
        newCliente.setComuna(comuna);

        return Optional.of(clienteJpaRepository.save(newCliente).getClienteId());
    }

    public List<ClienteJpa> getAllClientes() {
        return clienteJpaRepository.findAll();
    }

    public boolean deleteClienteById(Integer idCliente) {
        if (!clienteJpaRepository.existsById(idCliente)) {
            return false;
        }clienteJpaRepository.deleteById(idCliente);
        return true;
    }

    public Optional<ClienteJpa> getClienteById(Integer idCliente) {
        return clienteJpaRepository.findById(idCliente);
    }

    // falta el update
}
