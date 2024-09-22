package net.product.dual.repositories;

import net.product.dual.model.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepositories extends JpaRepository<ProductDTO, Integer> {

    @Query("SELECT p FROM ProductDTO p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<ProductDTO> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    @Query("SELECT p FROM ProductDTO p WHERE " +
            "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.sku) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<ProductDTO> findByNombreOrSkuContainingIgnoreCase(@Param("searchTerm") String searchTerm);
}
