package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Request.DetalleOrdenRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.DetalleOrdenResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.OrdenResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.ProductoResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.DetalleOrdenJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.DetalleOrdenJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.OrdenJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.ProductoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.OrdenJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.ProductoJpaRepository;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalleOrden {

    @Autowired
    private ProductoJpaRepository productoJpaRepository;

    @Autowired
    private OrdenJpaRepository ordenJpaRepository;




    /*
    public DetalleOrdenResponse crearDetalleOrden(DetalleOrdenRequest detalleOrdenRequest) {
        DetalleOrdenJpa detalleOrdenJpa = new DetalleOrdenJpa();
        detalleOrdenJpa.setCantidadProductos(detalleOrdenRequest.getCantidadProductos());
        detalleOrdenJpa.setSub_item(detalleOrdenRequest.getSub_item());
        detalleOrdenJpa.setMontoTotal(detalleOrdenRequest.getMontoTotal());

        OrdenJpa orden = ordenJpaRepository.findById(detalleOrdenRequest.getOrden().getIdOrden())
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        detalleOrdenJpa.setOrden(orden);

        ProductoJpa producto = productoJpaRepository.findById(detalleOrdenRequest.getProducto().getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        detalleOrdenJpa.setProducto(producto);

        detalleOrdenJpa = detalleOrdenJpaRepository.save(detalleOrdenJpa);

        return new DetalleOrdenResponse(
                detalleOrdenJpa.getIdDetalleOrden(),
                detalleOrdenJpa.getCantidadProductos(),
                detalleOrdenJpa.getSub_item(),
                detalleOrdenJpa.getOrden().getIdOrden(),
                detalleOrdenJpa.getOrden(),
                new ProductoResponse(
                        detalleOrdenJpa.getProducto().getIdProducto(),
                        detalleOrdenJpa.getProducto().getNombreProducto(),
                        detalleOrdenJpa.getProducto().getDescripcionProducto(),
                        detalleOrdenJpa.getProducto().getPrecio(),
                        detalleOrdenJpa.getProducto().getSku(),
                        detalleOrdenJpa.getProducto().getStockDisponible(),
                        detalleOrdenJpa.getProducto().getCategoria())
        );
    }

    public DetalleOrdenResponse verDetalleOrdenById(Integer idDetalleOrden) {
        DetalleOrdenJpa detalleOrdenJpa = detalleOrdenJpaRepository.findById(idDetalleOrden)
                .orElseThrow(() -> new RuntimeException("Detalle de orden no encontrado"));

        ProductoJpa producto = detalleOrdenJpa.getProducto();

        ProductoResponse productoResponse = new ProductoResponse(
                producto.getIdProducto(),
                producto.getNombreProducto(),
                producto.getDescripcionProducto(),
                producto.getPrecio(),
                producto.getSku(),
                producto.getStockDisponible(),
                producto.getCategoria()
        );

        DetalleOrdenResponse response = new DetalleOrdenResponse();
        response.setId(detalleOrdenJpa.getIdDetalleOrden());
        response.setCantidadProductos(detalleOrdenJpa.getCantidadProductos());
        response.setSub_item(detalleOrdenJpa.getSub_item());
        response.setMontoTotal(detalleOrdenJpa.getMontoTotal());
        response.setOrden(detalleOrdenJpa.getOrden()); // Esto retorna una entidad
        response.setProducto(productoResponse);

        return response;
    }*/
}
