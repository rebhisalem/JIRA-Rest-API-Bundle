package fr.magnolia.dsi.productmanager.repository;

import fr.magnolia.dsi.productmanager.domain.Proposition;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Proposition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PropositionRepository extends MongoRepository<Proposition, String> {

}
