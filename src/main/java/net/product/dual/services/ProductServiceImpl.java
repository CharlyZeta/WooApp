package net.product.dual.services;

import com.icoderman.woocommerce.ApiVersionType;
import com.icoderman.woocommerce.EndpointBaseType;
import com.icoderman.woocommerce.WooCommerce;
import com.icoderman.woocommerce.WooCommerceAPI;
import com.icoderman.woocommerce.oauth.OAuthConfig;
import net.product.dual.model.ProductDTO;
import net.product.dual.repositories.ProductRepositories;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService{

    private List<ProductDTO> productos;
    private final ProductRepositories productRepository;

    public ProductServiceImpl(ProductRepositories productRepository) {
        this.productRepository = productRepository;
    }

    @Override
     public List<ProductDTO> buscarProductosPorCategoria() {
          return null;
     }

     @Override
     public Optional<ProductDTO> buscarProductoDTOPorId() {
          return Optional.empty();
     }

     @Override
     public Optional<List<ProductDTO>> buscarProductosPorNombre() {
          return Optional.empty();
     }

     @Override
     public Optional<ProductDTO> buscarProductoPorSku() {
          return Optional.empty();
     }

    @Override
    public void guardarProductos(List<ProductDTO> productos) {
        productRepository.saveAll(productos);
    }

    @Override
    public void guardarProducto(ProductDTO producto) {
        productRepository.save(producto);
    }

    @Override
    public List<ProductDTO> buscarTodos() {
        return productRepository.findAll();
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
}
