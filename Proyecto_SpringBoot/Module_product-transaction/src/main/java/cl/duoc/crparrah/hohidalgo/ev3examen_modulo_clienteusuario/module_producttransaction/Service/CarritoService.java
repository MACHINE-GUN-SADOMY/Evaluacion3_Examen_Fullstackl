package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.ClienteRestClient;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.CarritoResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.ItemCarritoResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.CarritoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.CarritoJpa;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarritoService {
    private final CarritoJpaRepository carritoJpaRepository;
    private final ClienteRestClient clienteRestClient;

    @Lazy
    private OrdenService ordenService;

    @Autowired
    public CarritoService(CarritoJpaRepository carritoJpaRepository,
                          ClienteRestClient clienteRestClient) {
        this.carritoJpaRepository = carritoJpaRepository;
        this.clienteRestClient = clienteRestClient;
    }

    public CarritoResponse mapToResponse(CarritoJpa carritoJpa) {
        List<ItemCarritoResponse> itemsResponse = carritoJpa.getItems().stream()
                .map(item -> new ItemCarritoResponse(
                        item.getIdItemCarrito(),
                        item.getCarrito().getId(),
                        item.getProducto().getIdProducto(),
                        item.getCantidad(),
                        item.getProducto().getNombreProducto(),
                        item.getPrecioUnitario()))
                .collect(Collectors.toList());

        // CALCULAR LA CANTIDAD
        int cantidadTotal = itemsResponse.stream()
                .mapToInt(ItemCarritoResponse::getCantidad)
                .sum();

        // RETURN EL CARRITO con el orden correcto de los parámetros
        return new CarritoResponse(
                carritoJpa.getId(),
                carritoJpa.getFechaCreacion(),
                cantidadTotal,
                carritoJpa.getClienteId(),
                itemsResponse
        );
    }

    public CarritoResponse findByCarritoId(Integer id) {
        CarritoJpa carrito = carritoJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        return mapToResponse(carrito);
    }

    public List<CarritoResponse> getAllCarritos() {
        List<CarritoJpa> carritos = carritoJpaRepository.findAll();

        return carritos.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public boolean verificarCliente(Integer idCliente) {
        try {
            ResponseEntity<Void> response = clienteRestClient.verificarCliente(idCliente);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception exception) {
            return false;
        }
    }

    public CarritoResponse crearCarritoParaCliente(Integer idCliente) {
        // VALIDAR EXISTENCIA DEL CLIENTE
        if (!verificarCliente(idCliente)) {
            throw new RuntimeException("Cliente no existe");
        }

        // VALIDAR QUE EL CLIENTE TIENE UN CARRITO O NO
        Optional<CarritoJpa> carritoExistente = carritoJpaRepository.findByClienteId(idCliente);
        if (carritoExistente.isPresent()) {
            throw new RuntimeException("El cliente ya tiene un carrito");
        }

        CarritoJpa carrito = new CarritoJpa();
        carrito.setClienteId(idCliente);
        carrito.setFechaCreacion(LocalDate.now());

        CarritoJpa carritoGuardado = carritoJpaRepository.save(carrito);

        return mapToResponse(carritoGuardado);
    }

    public void deleteCarritoBy(Integer id) {
        if (!carritoJpaRepository.existsById(id)) {
            throw new RuntimeException("Carrito no existe");
        }
        carritoJpaRepository.deleteById(id);
    }
}
