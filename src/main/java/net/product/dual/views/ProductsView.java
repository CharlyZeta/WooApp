package net.product.dual.views;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.pro.licensechecker.Product;
import net.product.dual.model.ProductDTO;
import net.product.dual.services.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Route("productos")
public class ProductsView extends VerticalLayout {

    private final ProductService productService;

    public ProductsView(ProductService productService) {
        this.productService = productService;

    }


    private void configurarVista() {
        // Configurar la grilla de productos
        Grid<ProductDTO> gridProductos = new Grid<>(ProductDTO.class);
        gridProductos.setColumns("nombre", "sku", "precio");
        //gridProductos.addColumn(this::crearBotones);   // para agregar botones de compartir por whatsapp y detalles
        gridProductos.setSizeFull();

        // Obtener y agregar los datos de productos a la grilla
        //List<Map<String, Object>> productosMapas = productService.importaProductosDeWoocommerce();
        List<ProductDTO> productosDTO = productService.buscarTodos();
        ListDataProvider<ProductDTO> dataProvider = new ListDataProvider<>(productosDTO);
        gridProductos.setDataProvider(dataProvider);

        add(gridProductos);
    }



    private Component crearBotones(Product producto) {
        // Crear botón para compartir por WhatsApp
        Button botonCompartirWhatsApp = new Button("Compartir por WhatsApp");
        botonCompartirWhatsApp.addClickListener(e -> compartirPorWhatsApp(producto));

        // Crear botón para ver detalles
        Button botonVerDetalles = new Button("Ver Detalles");
        botonVerDetalles.addClickListener(e -> verDetallesProducto(producto));

        // Agregar los botones a un HorizontalLayout
        HorizontalLayout layoutBotones = new HorizontalLayout(botonCompartirWhatsApp, botonVerDetalles);
        return layoutBotones;
    }

    private void compartirPorWhatsApp(Product producto) {
        // Lógica para compartir el producto por WhatsApp
    }

    private void verDetallesProducto(Product producto) {
        // Lógica para ver los detalles del producto
    }
}