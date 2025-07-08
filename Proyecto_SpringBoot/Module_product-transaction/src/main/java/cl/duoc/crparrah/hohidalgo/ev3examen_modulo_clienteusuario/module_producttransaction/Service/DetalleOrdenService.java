package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Request.DetalleOrdenRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.CarritoResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.DetalleOrdenResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.OrdenResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.ProductoResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.CarritoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.DetalleOrdenJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.*;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.OrdenJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.ProductoJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DetalleOrdenService {

    @Autowired
    private ProductoJpaRepository productoJpaRepository;

    @Autowired
    private CarritoJpaRepository carritoJpaRepository;

    @Autowired
    private OrdenJpaRepository ordenJpaRepository;
    @Autowired
    private DetalleOrdenJpaRepository detalleOrdenJpaRepository;
    @Autowired
    private CarritoService carritoService;

    public DetalleOrdenResponse mapToResponse(DetalleOrdenJpa detalle) {
        ProductoJpa producto = detalle.getProducto();
        OrdenJpa orden = detalle.getOrden();

        ProductoResponse productoResponse = ProductoResponse.builder()
                .idProducto(producto.getIdProducto())
                .nombreProducto(producto.getNombreProducto())
                .descripcionProducto(producto.getDescripcionProducto())
                .precio(producto.getPrecio())
                .sku(producto.getSku())
                .stockDisponible(producto.getStockDisponible())
                .categoria(producto.getCategoria())
                .build();

        CarritoJpa carrito = orden.getCarrito();
        CarritoResponse carritoResponse = null;
        if (carrito != null) {
            carritoService.mapToResponse(carrito);
        }

        OrdenResponse ordenResponse = OrdenResponse.builder()
                .idOrden(orden.getIdOrden())
                .idCliente(orden.getIdCliente())
                .direccionCliente(orden.getDireccionCliente())
                .fechaOrden(orden.getFechaOrden())
                .montoTotal(orden.getMontoTotal())
                .estadoOrden(orden.getEstadoOrden())
                .carrito(carritoResponse)
                .build();

        return DetalleOrdenResponse.builder()
                .id(detalle.getIdDetalleOrden())
                .cantidadProductos(detalle.getCantidadProductos())
                .montoTotal(detalle.getMontoTotal())
                .precio(detalle.getPrecio())
                .producto(productoResponse)
                .orden(ordenResponse)
                .build();

    }

    public List<DetalleOrdenResponse> getAllDetallesOrden() {
        List<DetalleOrdenJpa> detallesOrdenJpa = detalleOrdenJpaRepository.findAll();

        return detallesOrdenJpa.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DetalleOrdenResponse getDetalleOrdenById(Integer idDetalleOrden) {
        Optional<DetalleOrdenJpa> detalleOrdenJpaOptional = detalleOrdenJpaRepository.findById(idDetalleOrden);
        if (detalleOrdenJpaOptional.isPresent()) {
            return mapToResponse(detalleOrdenJpaOptional.get());
        } else {
            throw new RuntimeException("Detalle de orden no encontrado");
        }
    }

    public String agregarDetalleOrden(DetalleOrdenRequest detalleOrdenRequest) {
        OrdenJpa orden = ordenJpaRepository.findById(detalleOrdenRequest.getIdOrden())
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        if ("Pagado".equals(orden.getEstadoOrden()) || "Cancelado".equals(orden.getEstadoOrden())) {
            return "No se puede agregar detalles a esta orden, ya que su estado es: " + orden.getEstadoOrden();
        }

        CarritoJpa carrito = orden.getCarrito();
        if (carrito == null || carrito.getItems() == null || carrito.getItems().isEmpty()) {
            return "El carrito asociado no tiene productos.";
        }

        for (ItemCarritoJpa item : carrito.getItems()) {
            ProductoJpa producto = item.getProducto();

            if (producto.getStockDisponible() < item.getCantidad()) {
                return "Stock insuficiente para el producto: " + producto.getNombreProducto();
            }

            DetalleOrdenJpa detalleOrdenJpa = new DetalleOrdenJpa();
            detalleOrdenJpa.setOrden(orden);
            detalleOrdenJpa.setProducto(producto);
            detalleOrdenJpa.setCantidadProductos(item.getCantidad());
            detalleOrdenJpa.setPrecio(producto.getPrecio());
            detalleOrdenJpa.setTotal(producto.getPrecio() * item.getCantidad());

            detalleOrdenJpa.setMontoTotal(producto.getPrecio() * item.getCantidad());

            detalleOrdenJpaRepository.save(detalleOrdenJpa);
        }

        return "Detalles de la orden agregados correctamente";
    }

    public void deleteDetalleOrden(Integer idDetalleOrden) {
        Optional<DetalleOrdenJpa> detalleOrdenJpaOptional = detalleOrdenJpaRepository.findById(idDetalleOrden);
        if (detalleOrdenJpaOptional.isPresent()) {
            DetalleOrdenJpa detalleOrdenJpa = detalleOrdenJpaOptional.get();
            String estadoOrden = detalleOrdenJpa.getOrden().getEstadoOrden();

            if ("Pagado".equals(estadoOrden) || "Cancelado".equals(estadoOrden)) {
                throw new RuntimeException("No se puede eliminar el detalle de la orden, ya que está pagado o cancelado");
            }

            detalleOrdenJpaRepository.delete(detalleOrdenJpa);
        } else {
            throw new RuntimeException("Detalle de orden no encontrado");
        }
    }
}
