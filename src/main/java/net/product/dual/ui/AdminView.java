package net.product.dual.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import net.product.dual.model.ProductDTO;
import net.product.dual.services.ProductServiceImpl;


import java.util.List;

@Route("admin")
@RolesAllowed("ADMIN")
public class AdminView extends VerticalLayout {
    private final Grid<ProductDTO> grid = new Grid<>(ProductDTO.class, false);
    private final ProductServiceImpl productService;
    private final TextField filterText = new TextField();

    public AdminView(ProductServiceImpl productService) {
        this.productService = productService;

        var grilla = new Grid<>(ProductDTO.class);
        add(
                new H1("Admin View")
        );

        grid.addColumn(ProductDTO::getSku).setHeader("Sku");
        grid.addColumn(ProductDTO::getNombre).setHeader("Nombre").setSortable(true);
        grid.addColumn(ProductDTO::getPrecio).setHeader("Precio").setSortable(true);
        grid.addColumn(ProductDTO::getStock).setHeader("Stock").setSortable(true);
        //grid.addColumn(ProductDTO::getCategorias).setHeader("Categoría").setSortable(true);

        List<ProductDTO> productos = productService.MostrarTodos("");
        grid.setItems(productos);

        add(grid);
    }
}
