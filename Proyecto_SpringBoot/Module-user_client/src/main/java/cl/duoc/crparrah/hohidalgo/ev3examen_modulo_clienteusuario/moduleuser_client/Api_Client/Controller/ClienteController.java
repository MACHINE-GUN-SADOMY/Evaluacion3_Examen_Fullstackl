package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Controller;

import ch.qos.logback.core.net.server.Client;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.ClienteRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.ComunaRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.ClienteResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.ComunaResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.ClienteJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ClienteJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ComunaJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/clientes")
@RestController
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteJpaRepository clienteJpaRepository;

    @GetMapping
    public ResponseEntity<List<ClienteJpa>> getAllClientes() {
        List<ClienteJpa> clientes = clienteService.getAllClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteJpa> getClienteById(@PathVariable Integer id) {
        Optional<ClienteJpa> cliente = clienteService.getClienteById(id);
        return cliente.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> createUsuario(@RequestBody ClienteRequest clienteRequest) {
        try {
            ClienteResponse newCliente = clienteService.createCliente(clienteRequest);
            return new ResponseEntity<>(newCliente, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Captura excepciones de Runtime (como las de email/teléfono duplicado o comuna no encontrada)
            // y devuelve un estado HTTP 400 Bad Request con el mensaje de error.
            // Puedes refinar esto con excepciones más específicas si lo deseas.
            System.err.println("Error al crear cliente: " + e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Captura cualquier otra excepción inesperada
            System.err.println("Error interno del servidor al crear cliente: " + e.getMessage());
            return new ResponseEntity("Error interno del servidor al crear el cliente.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> updateCliente(@PathVariable Integer id, @RequestBody ClienteRequest clienteRequest) {
        try {
            ClienteResponse updatedCliente = clienteService.updateCliente(id, clienteRequest);
            return new ResponseEntity<>(updatedCliente, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Integer id) {
        try{
            clienteService.deleteClienteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (RuntimeException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
