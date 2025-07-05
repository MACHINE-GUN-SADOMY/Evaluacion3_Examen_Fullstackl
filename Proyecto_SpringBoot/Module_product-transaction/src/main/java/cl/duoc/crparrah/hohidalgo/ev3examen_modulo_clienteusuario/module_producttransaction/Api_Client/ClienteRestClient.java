package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ClienteRestClient {
    @GetMapping("/clientes/{id}")
    ResponseEntity<Void> verificarCliente(@PathVariable("id") Integer idCliente);
}
