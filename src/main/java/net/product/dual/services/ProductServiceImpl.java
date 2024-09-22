package net.product.dual.services;

import com.vaadin.flow.data.provider.DataProvider;
import net.product.dual.model.ProductDTO;
import net.product.dual.repositories.ProductRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepositories productRepository;
    private ProductRepositories repoProduct;

    @Autowired
    public ProductServiceImpl(ProductRepositories productRepository) {
        this.productRepository = productRepository;
    }


    public List<ProductDTO> convertirMapasAProductosDTOs(List<Map<String, Object>> productosMapas) {
        List<ProductDTO> productosDTO = new ArrayList<>();
        for (Map<String, Object> productoMapa : productosMapas) {
            ProductDTO producto = new ProductDTO();
            producto.setId((Integer) productoMapa.get("id"));
            producto.setNombre((String) productoMapa.get("name"));
            producto.setPrecio((Float) productoMapa.get("regular_price"));
            // Asignar otros campos del ProductDTO según sea necesario
            productosDTO.add(producto);
        }
        return productosDTO;
    }

    @Override
    public void guardarProductos(List<ProductDTO> productos) {

    }

    @Override
    public ProductDTO guardarProducto(ProductDTO producto) {
        return null;
    }

    @Override
    public List<ProductDTO> MostrarTodos(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return productRepository.findAll();
        } else {
            return productRepository.findByNombreOrSkuContainingIgnoreCase(filterText);
        }
    }

    @Override
    public List<ProductDTO> buscarProductosPorCategoria() {
        return null;
    }

    @Override
    public ProductDTO buscarProductoDTOPorId(Long id) {
        return productRepository.findById(Math.toIntExact(id)).orElse(null);
    }


}