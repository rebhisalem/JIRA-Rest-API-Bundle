package fr.magnolia.dsi.productmanager.repository;

import fr.magnolia.dsi.productmanager.domain.SurveyQuestion;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the SurveyQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SurveyQuestionRepository extends MongoRepository<SurveyQuestion, String> {

}
