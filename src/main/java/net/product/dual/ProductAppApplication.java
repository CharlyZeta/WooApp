package net.product.dual;

import com.icoderman.woocommerce.ApiVersionType;
import com.icoderman.woocommerce.EndpointBaseType;
import com.icoderman.woocommerce.WooCommerce;
import com.icoderman.woocommerce.WooCommerceAPI;
import com.icoderman.woocommerce.oauth.OAuthConfig;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import net.product.dual.model.Categoria;
import net.product.dual.model.ProductDTO;
import net.product.dual.repositories.ProductRepositories;
import net.product.dual.services.CategoriaServiceImpl;
import net.product.dual.services.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ProductAppApplication {

    private final ProductServiceImpl productService;

    private final CategoriaServiceImpl categoriaService;

    private ProductRepositories productRepositories;

    private final OAuthConfig oAuthConfig;

    final static Logger logger = LoggerFactory.getLogger(ProductAppApplication.class);

    public ProductAppApplication(ProductServiceImpl productService, CategoriaServiceImpl categoriaService, OAuthConfig oAuthConfig) {
        this.productService = productService;
        this.categoriaService = categoriaService;
        this.oAuthConfig = oAuthConfig;
    }

    public static void main(String[] args) {

        SpringApplication.run(ProductAppApplication.class, args);
    }
    private static Map<Long, Categoria> categoriasUnicas = new HashMap<>();

    @PostConstruct
    public void logEnvironmentVariables() {
        logger.info("WOOCOMMERCE_URL: {}", System.getenv("WOOCOMMERCE_URL"));
        logger.info("WOOCOMMERCE_CONSUMER_KEY: {}",
                System.getenv("WOOCOMMERCE_CONSUMER_KEY") != null ? "SET" : "NOT SET");
        logger.info("WOOCOMMERCE_CONSUMER_SECRET: {}",
                System.getenv("WOOCOMMERCE_CONSUMER_SECRET") != null ? "SET" : "NOT SET");
    }

    @PostConstruct
    public void guardarRegistros(){
        importaProductos();
    }

    public void importaProductos() {
        // Configurar cliente
        WooCommerce wooCommerce = new WooCommerceAPI(oAuthConfig, ApiVersionType.V3);

        // Leer todos los productos
        List<Map<String, Object>> allProducts = new ArrayList<>();
        int perPage = 100;
        int page = 1;
        List<Map<String, Object>> products;

        do {
            Map<String, String> params = new HashMap<>();
            params.put("per_page", String.valueOf(perPage));
            params.put("page", String.valueOf(page));
            products = wooCommerce.getAll(EndpointBaseType.PRODUCTS.getValue(), params);
            allProducts.addAll(products);
            page++;
        } while (products.size() == perPage);

        Map<Long, Categoria> categoriasGuardadas = procesarCategorias(allProducts);
        // si hay productos me muestra los atributos del objeto producto
        if (!products.isEmpty()) {
            Map<String, Object> product = products.get(0); // Obtener el primer producto
            System.out.println("Atributos del producto:");
            for (Map.Entry<String, Object> entry : product.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } else {
            System.out.println("No se encontraron productos.");
        }

        // Mostrar los productos (opcional)
        for (Map<String, Object> product : allProducts) {
            // Mostrar imágenes
            List<Map<String, Object>> images = (List<Map<String, Object>>) product.get("images");

            // Mostrar categoría/s
            List<Map<String, Object>> categories = (List<Map<String, Object>>) product.get("categories");

//            if (categories != null) {
//                System.out.println("Categorías:");
//                for (Map<String, Object> category : categories) {
//                    System.out.println("Categoría: " + category.get("name"));
//                }
//            }
//            System.out.println("--------------------------------------");

        }
        List<ProductDTO> productos = new ArrayList<>(convertirMapasAProductosDTOs(allProducts, categoriasGuardadas)); //agrega a la lista de productoDTO los productos convertidos de Maps a DTO
        for (ProductDTO producto : productos){
            System.out.println("id: " + producto.getId() + " - " + " Nombre: " + producto.getNombre());
            try{
                productService.guardarProducto(producto);
            }catch(Exception exception){
                System.out.println("Error grabando producto: " + exception);
            }

        }
//        System.out.println("\nProductos: " + allProducts.size());
    }


    @Transactional
    public void procesarProductos(List<Map<String, Object>> productosMapas) {
        // Paso 1: Procesar y guardar categorías
        Map<Long, Categoria> categoriasGuardadas = procesarCategorias(productosMapas);

        // Paso 2: Procesar y guardar productos
        List<ProductDTO> productos = convertirMapasAProductosDTOs(productosMapas, categoriasGuardadas);

        for (ProductDTO producto : productos) {
            try {
                productService.guardarProducto(producto);
            } catch (Exception e) {
                // Manejo de errores
                System.err.println("Error grabando producto: " + e.getMessage());
            }
        }
    }

    private Map<Long, Categoria> procesarCategorias(List<Map<String, Object>> productosMapas) {
        Map<Long, Categoria> categoriasUnicas = new HashMap<>();

        for (Map<String, Object> productoMapa : productosMapas) {
            List<Map<String, Object>> categoriasMapas = (List<Map<String, Object>>) productoMapa.get("categories");
            if (categoriasMapas != null) {
                for (Map<String, Object> categoriaMapa : categoriasMapas) {
                    Long categoriaId = ((Number) categoriaMapa.get("id")).longValue();
                    categoriasUnicas.computeIfAbsent(categoriaId, k -> {
                        Categoria categoria = new Categoria();
                        categoria.setId(categoriaId);
                        categoria.setName((String) categoriaMapa.get("name"));
                        categoria.setSlug((String) categoriaMapa.get("slug"));
                        return categoria;
                    });
                }
            }
        }

        // Guardar o actualizar todas las categorías
        for (Categoria categoria : categoriasUnicas.values()) {
            categoriaService.guardarCategoria(categoria);
        }

        return categoriasUnicas;
    }

    public static List<ProductDTO> convertirMapasAProductosDTOs(List<Map<String, Object>> productosMapas, Map<Long, Categoria> categoriasGuardadas) {
        List<ProductDTO> productosDTO = new ArrayList<>();

        for (Map<String, Object> productoMapa : productosMapas) {
            ProductDTO producto = new ProductDTO();
            producto.setId( (Integer) productoMapa.get("id"));
            producto.setSku((String) productoMapa.get("sku"));
            producto.setNombre((String) productoMapa.get("name"));
            producto.setPrecio(Float.parseFloat((String) productoMapa.get("regular_price")));
            if(productoMapa.get("sale_price").equals("")){
                producto.setPrecioOferta(0.0f);
            }else{
                producto.setPrecioOferta(Float.parseFloat((String) productoMapa.get("sale_price")));
            }

            producto.setDescripcion((String) productoMapa.get("description"));
            producto.setDescriptionCorta((String) productoMapa.get("short_description"));
            producto.setEstado((String) productoMapa.get("status"));
            producto.setStock((Integer) productoMapa.get("stock_quantity"));
            producto.setManejoStock(!productoMapa.get("manage_stock").equals("true"));

            producto.setVisibilidad((String) productoMapa.get("catalog_visibility"));
            producto.setLink((String) productoMapa.get("permalink"));

            // Extraer y asignar categorías
            List<Map<String, Object>> categoriasMapas = (List<Map<String, Object>>) productoMapa.get("categories");
            if (categoriasMapas != null) {
                List<Categoria> categorias = new ArrayList<>();
                for (Map<String, Object> categoriaMapa : categoriasMapas) {
                    Long categoriaId = ((Number) categoriaMapa.get("id")).longValue();
                    Categoria categoria = categoriasGuardadas.get(categoriaId);
                    if (categoria != null) {
                        categorias.add(categoria);
                    }
                }
                producto.setCategorias(categorias);
            }

            productosDTO.add(producto);
        }
        return productosDTO;
    }

}
