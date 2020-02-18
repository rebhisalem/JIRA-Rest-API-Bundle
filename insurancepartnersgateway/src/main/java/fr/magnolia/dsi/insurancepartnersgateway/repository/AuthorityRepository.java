package fr.magnolia.dsi.insurancepartnersgateway.repository;

import fr.magnolia.dsi.insurancepartnersgateway.domain.Authority;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends MongoRepository<Authority, String> {
}
