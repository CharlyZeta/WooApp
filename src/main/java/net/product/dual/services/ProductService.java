package net.product.dual.services;

import net.product.dual.model.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface ProductService {

    //List<Map<String, Object>> importaProductosDeWoocommerce();

    void guardarProductos(List<ProductDTO> productos);

    void guardarProducto(ProductDTO producto);

    List<ProductDTO> buscarTodos();
    List<ProductDTO> buscarProductosPorCategoria();
    Optional<ProductDTO> buscarProductoDTOPorId();

    Optional<List<ProductDTO>> buscarProductosPorNombre();

    Optional<ProductDTO> buscarProductoPorSku();


}
