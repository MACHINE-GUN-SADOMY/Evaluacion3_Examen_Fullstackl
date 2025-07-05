package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.ClienteRestClient;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Request.OrdenRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.CarritoResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.OrdenResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.RestConfig.ClienteServiceImplement;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.CarritoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.CarritoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.OrdenJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.OrdenJpaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrdenService {
    private final OrdenJpaRepository ordenJpaRepository;

    @Autowired
    private CarritoJpaRepository carritoJpaRepository;
    private final CarritoService carritoService;

    public OrdenService(OrdenJpaRepository ordenJpaRepository, CarritoService carritoService) {
        this.ordenJpaRepository = ordenJpaRepository;
        this.carritoService = carritoService;
    }

    private OrdenResponse mapToResponse(OrdenJpa orden) {
        OrdenResponse orderResponse = new OrdenResponse();
        orderResponse.setIdOrden(orden.getIdOrden());
        orderResponse.setIdCliente(orden.getIdCliente());
        orderResponse.setDireccionCliente(orden.getDireccionCliente());
        orderResponse.setFechaOrden(orden.getFechaOrden());
        orderResponse.setMontoTotal(orden.getMontoTotal());
        orderResponse.setEstadoOrden(orden.getEstadoOrden());
        return orderResponse;
    }

    public List<OrdenResponse> getAllOrdenes() {
        return ordenJpaRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Optional<OrdenResponse> getOrdenById(Integer idOrden) {
        return ordenJpaRepository.findById(idOrden).map(this::mapToResponse);
    }

    public Optional<OrdenResponse> getOrdenByClienteId(Integer clienteId) {
        return ordenJpaRepository.findByIdCliente(clienteId).map(this::mapToResponse);
    }

    public OrdenResponse crearOrdenResponse(OrdenRequest ordenRequest) {
        // VERIFICAR SI EXISTE EL CLIENTE CON AYUDA DE MODULO
        if (!carritoService.verificarCliente(ordenRequest.getIdCliente())) {
            throw new RuntimeException("El cliente no existe");
        }

        // VERIFICAR SI EXISTE UN CLIENTE YA CON UNA ORDEN ASOCIADA CON SU CARRITO
        Optional<OrdenJpa> ordenExistente = ordenJpaRepository.findByCarritoId(ordenRequest.getIdCarrito());
        if (ordenExistente.isPresent()) {
            throw new RuntimeException("Ya existe una orden asociada a este carrito");
        }

        // VERIFICAR SI EL CLIENTE TIENE UNA ORDEN REGISTRADA YA
        Optional<OrdenJpa> ordenPorCliente = ordenJpaRepository.findByIdCliente(ordenRequest.getIdCliente());
        if (ordenPorCliente.isPresent()) {
            throw new RuntimeException("Este cliente ya tiene una orden registrada");
        }

        // VERIFICAR SI EL CARRITO EXISTE
        CarritoJpa carrito = carritoJpaRepository.findById(ordenRequest.getIdCarrito())
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        int montoTotal = carrito.getItems().stream()
                .mapToInt(item -> item.getProducto().getPrecio() * item.getCantidad())
                .sum();

        // Crear la orden
        OrdenJpa ordenJpa = new OrdenJpa();
        ordenJpa.setIdCliente(ordenRequest.getIdCliente()); // Usando el idCliente del request
        ordenJpa.setFechaOrden(LocalDate.now());
        ordenJpa.setMontoTotal(montoTotal);
        ordenJpa.setEstadoOrden("Pendiente");
        ordenJpa.setCarrito(carrito);

        OrdenJpa ordenGuardada = ordenJpaRepository.save(ordenJpa);

        return mapToResponse(ordenGuardada);
    }

    public OrdenResponse actualizarEstadoOrden(Integer idOrden, String nuevoEstado) {
        OrdenJpa ordenJpa = ordenJpaRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con id: " + idOrden));

        ordenJpa.setEstadoOrden(nuevoEstado);

        OrdenJpa ordenActualizada = ordenJpaRepository.save(ordenJpa);

        return mapToResponse(ordenActualizada);
    }

    public void deleteOrden (Integer Id) {
        OrdenJpa ordenJpa = ordenJpaRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con id: " + Id));

        ordenJpaRepository.delete(ordenJpa);
    }
}