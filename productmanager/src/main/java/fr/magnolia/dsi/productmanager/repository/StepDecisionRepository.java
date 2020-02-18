package fr.magnolia.dsi.productmanager.repository;

import fr.magnolia.dsi.productmanager.domain.StepDecision;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the StepDecision entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StepDecisionRepository extends MongoRepository<StepDecision, String> {

}
