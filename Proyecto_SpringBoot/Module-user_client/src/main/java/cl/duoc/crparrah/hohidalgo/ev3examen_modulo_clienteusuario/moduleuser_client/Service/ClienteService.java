package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.ClienteRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.ClienteResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.ComunaResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.ProvinciaResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.RegionResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.ClienteJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.ComunaJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ClienteJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ComunaJpa;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ProvinciaJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.RegionJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;


@Service
public class ClienteService {
    @Autowired
    private final ClienteJpaRepository clienteJpaRepository;
    @Autowired
    private final ComunaJpaRepository comunaJpaRepository;

    private ComunaResponse buildComunaResponse(ComunaJpa comunaJpa) {
        if (comunaJpa == null) {
            return null;
        }

        ProvinciaResponse provinciaResponse = null;
        if (comunaJpa.getIdProvincia() != null) {
            ProvinciaJpa provinciaJpa = comunaJpa.getIdProvincia();
            RegionResponse regionResponse = null;
            if (provinciaJpa.getIdRegion() != null) {
                RegionJpa regionJpa = provinciaJpa.getIdRegion();
                regionResponse = new RegionResponse(regionJpa.getIdRegion(), regionJpa.getNombreRegion());
            }
            provinciaResponse = new ProvinciaResponse(
                    provinciaJpa.getIdProvincia(),
                    provinciaJpa.getNombreProvincia(),
                    regionResponse
            );
        }

        return new ComunaResponse(
                comunaJpa.getIdComuna(),
                comunaJpa.getNombreComuna(),
                provinciaResponse
        );
    }

    private ClienteResponse mapToClienteResponse(ClienteJpa clienteJpa) {
        if (clienteJpa == null) {
            return null;
        }
        return new ClienteResponse(
                clienteJpa.getClienteId(),
                clienteJpa.getNombreCliente(),
                clienteJpa.getApellidoCliente(),
                clienteJpa.getTelefono(),
                clienteJpa.getEmail(),
                clienteJpa.getContraseña(),
                clienteJpa.getDireccion(),
                clienteJpa.getFechaNacimiento(),
                buildComunaResponse(clienteJpa.getComuna())
        );
    }

    public ClienteService(ClienteJpaRepository clienteJpaRepository, ComunaJpaRepository comunaJpaRepository, RestClient restClient) {
        this.clienteJpaRepository = clienteJpaRepository;
        this.comunaJpaRepository = comunaJpaRepository;
    }

    public ClienteResponse createCliente(ClienteRequest clienteRequest) {
        if (clienteJpaRepository.findByEmail(clienteRequest.getEmail()).isPresent()) {
            throw new RuntimeException("El cliente con email: " + clienteRequest.getEmail() + " ya existe.");
        }
        if (clienteRequest.getTelefono() != null && clienteJpaRepository.findByTelefono(clienteRequest.getTelefono()).isPresent()) {
            throw new RuntimeException("El teléfono '" + clienteRequest.getTelefono() + "' ya está registrado para otro cliente.");
        }

        ComunaJpa comuna = comunaJpaRepository.findById(clienteRequest.getIdComuna())
                .orElseThrow(() -> new RuntimeException("Comuna con ID " + clienteRequest.getIdComuna() + " no encontrada."));

        ClienteJpa cliente = new ClienteJpa();
        cliente.setNombreCliente(clienteRequest.getNombreCliente());
        cliente.setApellidoCliente(clienteRequest.getApellidoCliente());
        cliente.setEmail(clienteRequest.getEmail());
        cliente.setContraseña(clienteRequest.getContraseña());
        cliente.setDireccion(clienteRequest.getDireccion());
        cliente.setTelefono(clienteRequest.getTelefono());
        cliente.setFechaNacimiento(clienteRequest.getFechaNacimiento());
        cliente.setComuna(comuna);

        ClienteJpa savedCliente = clienteJpaRepository.save(cliente);

        // Mapear la entidad guardada a un DTO de respuesta
        return mapToClienteResponse(savedCliente);
    }


    public List<ClienteJpa> getAllClientes() {
        return clienteJpaRepository.findAll();
    }

    public boolean deleteClienteById(Integer idCliente) {
        if (!clienteJpaRepository.existsById(idCliente)) {
            return false;
        }
        clienteJpaRepository.deleteById(idCliente);
        return true;
    }


    public Optional<ClienteJpa> getClienteById(Integer idCliente) {
        return clienteJpaRepository.findById(idCliente);
    }

    public ClienteResponse updateCliente(Integer id, ClienteRequest clienteRequest) {
        ClienteJpa existingCliente = clienteJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente con ID " + id + " no encontrado."));

        if (!existingCliente.getEmail().equals(clienteRequest.getEmail())) {
            Optional<ClienteJpa> duplicateEmail = clienteJpaRepository.findByEmail(clienteRequest.getEmail());
            if (duplicateEmail.isPresent() && !duplicateEmail.get().getClienteId().equals(id)) {
                throw new RuntimeException("El email '" + clienteRequest.getEmail() + "' ya está registrado para otro cliente.");
            }
        }

        if (clienteRequest.getTelefono() != null &&
                (existingCliente.getTelefono() == null || !existingCliente.getTelefono().equals(clienteRequest.getTelefono()))) {
            Optional<ClienteJpa> duplicateTelefono = clienteJpaRepository.findByTelefono(clienteRequest.getTelefono());
            if (duplicateTelefono.isPresent() && !duplicateTelefono.get().getClienteId().equals(id)) {
                throw new RuntimeException("El teléfono '" + clienteRequest.getTelefono() + "' ya está registrado para otro cliente.");
            }
        }
        ComunaJpa comuna = comunaJpaRepository.findById(clienteRequest.getIdComuna())
                .orElseThrow(() -> new RuntimeException("Comuna con ID " + clienteRequest.getIdComuna() + " no encontrada."));

        existingCliente.setNombreCliente(clienteRequest.getNombreCliente());
        existingCliente.setApellidoCliente(clienteRequest.getApellidoCliente());
        existingCliente.setEmail(clienteRequest.getEmail());

        if (clienteRequest.getContraseña() != null && !clienteRequest.getContraseña().isEmpty()) {
            existingCliente.setContraseña(clienteRequest.getContraseña());
        }
        existingCliente.setDireccion(clienteRequest.getDireccion());
        existingCliente.setTelefono(clienteRequest.getTelefono());
        existingCliente.setFechaNacimiento(clienteRequest.getFechaNacimiento());
        existingCliente.setComuna(comuna);

        ClienteJpa updatedCliente = clienteJpaRepository.save(existingCliente);

        return mapToClienteResponse(updatedCliente);
    }

}