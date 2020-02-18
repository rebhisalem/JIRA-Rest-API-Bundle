package fr.magnolia.dsi.productmanager.repository;

import fr.magnolia.dsi.productmanager.domain.ProductStep;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ProductStep entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductStepRepository extends MongoRepository<ProductStep, String> {

}
