package fr.magnolia.dsi.productmanager.repository;

import fr.magnolia.dsi.productmanager.domain.Step;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Step entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StepRepository extends MongoRepository<Step, String> {

}
