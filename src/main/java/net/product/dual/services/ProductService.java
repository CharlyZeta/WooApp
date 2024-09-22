package net.product.dual.services;

import com.vaadin.flow.data.provider.DataProvider;
import net.product.dual.model.ProductDTO;

import java.util.List;
import java.util.Optional;


public interface ProductService {


    //List<Map<String, Object>> importaProductosDeWoocommerce();

    void guardarProductos(List<ProductDTO> productos);

    ProductDTO guardarProducto(ProductDTO producto);

    List<ProductDTO> MostrarTodos(String filterText);
    List<ProductDTO> buscarProductosPorCategoria();
    ProductDTO buscarProductoDTOPorId(Long id);





}
