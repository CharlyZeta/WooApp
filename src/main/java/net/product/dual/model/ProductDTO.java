package net.product.dual.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class ProductDTO {

    @Id
    private Integer id;
    private String sku;
    private String nombre;

    @Column(columnDefinition = "LONGTEXT")
    private String descripcion;

    @Column(columnDefinition = "LONGTEXT")
    private String descriptionCorta;
    private Float precio;
    private Float precioOferta;
    private Integer stock;
    private Boolean manejoStock;
    private String estado;
    private String link;
    private String visibilidad;
    @ManyToMany
    @JoinTable(
            name = "producto_categoria",
            joinColumns = @JoinColumn(name = "producto_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorias;

    @ElementCollection
    @CollectionTable(name = "producto_imagenes", joinColumns = @JoinColumn(name = "producto_id"))
    @Column(name = "url_imagen")
    private List<String> imagenes;



}
