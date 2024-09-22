package net.product.dual.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import jakarta.annotation.security.PermitAll;
import net.product.dual.model.ProductDTO;
import net.product.dual.services.ProductService;
import net.product.dual.services.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

@Route("products")
@PermitAll
public class ProductView extends VerticalLayout {

    private final ProductService productService;
    private final Grid<ProductDTO> grid = new Grid<>(ProductDTO.class, false);
    private final TextField filterText = new TextField();

    @Autowired
    public ProductView(ProductService productService) {
        this.productService = productService;
        setSizeFull();
        configureGrid();
        configureFilter();

        add(
                new H1("Tabla de productos"), filterText, grid
        );

        updateList();
    }

    private void configureGrid() {
        grid.setSizeFull();

        /*
        grid.addColumn(new ComponentRenderer<>(product -> {
            if (product.getImagenes() != null && !product.getImagenes().isEmpty()) {
                Image image = new Image(product.getImagenes().get(0), "Product image");
                image.setWidth("100px");
                image.setHeight("100px");
                return image;
            }
            return new Image("images/default-product.png", "Default product image");
        })).setHeader("Imagen").setFlexGrow(0).setWidth("120px");
        */

        grid.addColumn(ProductDTO::getSku).setHeader("SKU");
        grid.addColumn(ProductDTO::getNombre).setHeader("Nombre");
        grid.addColumn(product -> {
            if (product.getPrecioOferta() != null && product.getPrecioOferta() > 0) {
                return String.format("%.0f (Oferta: %.0f)", product.getPrecio(), product.getPrecioOferta());
            }
            return String.format("%.0f", product.getPrecio());
        }).setHeader("Precio");
        grid.addColumn(ProductDTO::getStock).setHeader("Stock");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    }

    private void configureFilter() {
        filterText.setPlaceholder("Filtrar por nombre...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
    }

    private void updateList() {
        grid.setItems(productService.MostrarTodos(filterText.getValue()));
    }
}