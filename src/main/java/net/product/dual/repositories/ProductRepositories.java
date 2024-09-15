package net.product.dual.repositories;


import net.product.dual.model.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepositories extends JpaRepository<ProductDTO, Integer> {
}
