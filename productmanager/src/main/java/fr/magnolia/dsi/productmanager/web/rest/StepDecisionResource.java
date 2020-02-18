package fr.magnolia.dsi.productmanager.web.rest;

import fr.magnolia.dsi.productmanager.domain.StepDecision;
import fr.magnolia.dsi.productmanager.repository.StepDecisionRepository;
import fr.magnolia.dsi.productmanager.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.magnolia.dsi.productmanager.domain.StepDecision}.
 */
@RestController
@RequestMapping("/api")
public class StepDecisionResource {

    private final Logger log = LoggerFactory.getLogger(StepDecisionResource.class);

    private static final String ENTITY_NAME = "productmanagerStepDecision";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StepDecisionRepository stepDecisionRepository;

    public StepDecisionResource(StepDecisionRepository stepDecisionRepository) {
        this.stepDecisionRepository = stepDecisionRepository;
    }

    /**
     * {@code POST  /step-decisions} : Create a new stepDecision.
     *
     * @param stepDecision the stepDecision to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stepDecision, or with status {@code 400 (Bad Request)} if the stepDecision has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/step-decisions")
    public ResponseEntity<StepDecision> createStepDecision(@Valid @RequestBody StepDecision stepDecision) throws URISyntaxException {
        log.debug("REST request to save StepDecision : {}", stepDecision);
        if (stepDecision.getId() != null) {
            throw new BadRequestAlertException("A new stepDecision cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StepDecision result = stepDecisionRepository.save(stepDecision);
        return ResponseEntity.created(new URI("/api/step-decisions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /step-decisions} : Updates an existing stepDecision.
     *
     * @param stepDecision the stepDecision to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stepDecision,
     * or with status {@code 400 (Bad Request)} if the stepDecision is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stepDecision couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/step-decisions")
    public ResponseEntity<StepDecision> updateStepDecision(@Valid @RequestBody StepDecision stepDecision) throws URISyntaxException {
        log.debug("REST request to update StepDecision : {}", stepDecision);
        if (stepDecision.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StepDecision result = stepDecisionRepository.save(stepDecision);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stepDecision.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /step-decisions} : get all the stepDecisions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stepDecisions in body.
     */
    @GetMapping("/step-decisions")
    public List<StepDecision> getAllStepDecisions() {
        log.debug("REST request to get all StepDecisions");
        return stepDecisionRepository.findAll();
    }

    /**
     * {@code GET  /step-decisions/:id} : get the "id" stepDecision.
     *
     * @param id the id of the stepDecision to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stepDecision, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/step-decisions/{id}")
    public ResponseEntity<StepDecision> getStepDecision(@PathVariable String id) {
        log.debug("REST request to get StepDecision : {}", id);
        Optional<StepDecision> stepDecision = stepDecisionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(stepDecision);
    }

    /**
     * {@code DELETE  /step-decisions/:id} : delete the "id" stepDecision.
     *
     * @param id the id of the stepDecision to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/step-decisions/{id}")
    public ResponseEntity<Void> deleteStepDecision(@PathVariable String id) {
        log.debug("REST request to delete StepDecision : {}", id);
        stepDecisionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
