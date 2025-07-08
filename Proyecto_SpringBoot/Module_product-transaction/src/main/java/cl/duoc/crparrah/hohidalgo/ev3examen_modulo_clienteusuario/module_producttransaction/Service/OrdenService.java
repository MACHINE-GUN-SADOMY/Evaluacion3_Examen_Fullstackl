package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Request.OrdenRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.CarritoResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.OrdenResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.CarritoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.ItemCarritoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.CarritoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.ItemCarritoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.OrdenJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.OrdenJpaRepository;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.ProductoJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenService {
    @Autowired
    private CarritoJpaRepository carritoJpaRepository;

    @Autowired
    private OrdenJpaRepository ordenJpaRepository;

    @Autowired
    private ProductoJpaRepository productoJpaRepository;

    @Autowired
    private ItemCarritoJpaRepository itemCarritoJpaRepository;

    // MAPEA A RESPONSE
    private OrdenResponse mapToResponse(OrdenJpa ordenJpa) {
        CarritoResponse carritoResponse = new CarritoResponse(
                ordenJpa.getCarrito().getId(),
                ordenJpa.getCarrito().getClienteId(),
                ordenJpa.getCarrito().getFechaCreacion(),
                ordenJpa.getCarrito().getCantidadTotal()
        );
        return new OrdenResponse(
                ordenJpa.getIdOrden(),
                ordenJpa.getIdCliente(),
                ordenJpa.getDireccionCliente(),
                ordenJpa.getFechaOrden(),
                ordenJpa.getMontoTotal(),
                ordenJpa.getEstadoOrden(),
                carritoResponse
        );
    }

    public OrdenResponse crearOrden(OrdenRequest ordenRequest) {
        // VVERIFICAR SI EXISTE EL CARRO
        CarritoJpa carrito = carritoJpaRepository.findById(ordenRequest.getIdCarrito())
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // CALCULAR MONTO TOTAL
        int montoTotal = carrito.getItems().stream()
                .mapToInt(item -> item.getProducto().getPrecio() * item.getCantidad())
                .sum();

        OrdenJpa ordenJpa = new OrdenJpa();
        ordenJpa.setIdCliente(ordenRequest.getIdCliente());
        ordenJpa.setDireccionCliente(ordenRequest.getDireccionCliente());
        ordenJpa.setFechaOrden(LocalDate.now());
        ordenJpa.setMontoTotal(montoTotal);
        ordenJpa.setEstadoOrden("Pendiente");
        ordenJpa.setCarrito(carrito);
        OrdenJpa ordenGuardada = ordenJpaRepository.save(ordenJpa);

        // USAR METODO MAP TO RESPONSE
        return mapToResponse(ordenGuardada);
    }

    public OrdenResponse getOrdenById(Integer idOrden) {
        Optional<OrdenJpa> ordenJpa = ordenJpaRepository.findById(idOrden);

        if (ordenJpa.isPresent()) {
            OrdenJpa orden = ordenJpa.get();
            CarritoResponse carritoResponse = new CarritoResponse(
                    orden.getCarrito().getId(),
                    orden.getCarrito().getClienteId(),
                    orden.getCarrito().getFechaCreacion(),
                    calcularTotalCarrito(orden.getCarrito()) // Suponiendo que este método calcula el total del carrito
            );
            return new OrdenResponse(
                    orden.getIdOrden(),
                    orden.getIdCliente(),
                    orden.getDireccionCliente(),
                    orden.getFechaOrden(),
                    orden.getMontoTotal(),
                    orden.getEstadoOrden(),
                    carritoResponse
            );
        } else {
            throw new RuntimeException("Orden no encontrada");
        }
    }

    public List<OrdenResponse> getAllOrdenes() {
        List<OrdenJpa> ordenes = ordenJpaRepository.findAll();
        List<OrdenResponse> ordenResponses = new ArrayList<>();

        for (OrdenJpa orden : ordenes) {
            CarritoResponse carritoResponse = new CarritoResponse(
                    orden.getCarrito().getId(),
                    orden.getCarrito().getClienteId(),
                    orden.getCarrito().getFechaCreacion(),
                    calcularTotalCarrito(orden.getCarrito())
            );
            ordenResponses.add(new OrdenResponse(
                    orden.getIdOrden(),
                    orden.getIdCliente(),
                    orden.getDireccionCliente(),
                    orden.getFechaOrden(),
                    orden.getMontoTotal(),
                    orden.getEstadoOrden(),
                    carritoResponse
            ));
        }

        return ordenResponses;
    }

    private Integer calcularTotalCarrito(CarritoJpa carrito) {
        int total = 0;
        for (ItemCarritoJpa item : carrito.getItems()) {
            total += item.getCantidad() * item.getPrecioUnitario();
        }
        return total;
    }

    public void deleteOrdenById(Integer idOrden) {
        OrdenJpa orden = ordenJpaRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con id " + idOrden));

        CarritoJpa carrito = orden.getCarrito();


        ordenJpaRepository.delete(orden);
    }

    public void modificarEstadoOrden(Integer idOrden, String nuevoEstado) {
        List<String> estadosValidos = Arrays.asList("Pendiente", "Pagado", "Cancelado");

        if (!estadosValidos.contains(nuevoEstado)) {
            throw new RuntimeException("Estado de la orden no válido");
        }


        OrdenJpa orden = ordenJpaRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        orden.setEstadoOrden(nuevoEstado);

        ordenJpaRepository.save(orden);
    }
}