package fr.magnolia.dsi.productmanager.repository;

import fr.magnolia.dsi.productmanager.domain.Question;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Question entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {

}
