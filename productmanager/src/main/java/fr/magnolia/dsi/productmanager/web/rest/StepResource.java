package fr.magnolia.dsi.productmanager.web.rest;

import fr.magnolia.dsi.productmanager.domain.Step;
import fr.magnolia.dsi.productmanager.repository.StepRepository;
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
 * REST controller for managing {@link fr.magnolia.dsi.productmanager.domain.Step}.
 */
@RestController
@RequestMapping("/api")
public class StepResource {

    private final Logger log = LoggerFactory.getLogger(StepResource.class);

    private static final String ENTITY_NAME = "productmanagerStep";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StepRepository stepRepository;

    public StepResource(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    /**
     * {@code POST  /steps} : Create a new step.
     *
     * @param step the step to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new step, or with status {@code 400 (Bad Request)} if the step has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/steps")
    public ResponseEntity<Step> createStep(@Valid @RequestBody Step step) throws URISyntaxException {
        log.debug("REST request to save Step : {}", step);
        if (step.getId() != null) {
            throw new BadRequestAlertException("A new step cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Step result = stepRepository.save(step);
        return ResponseEntity.created(new URI("/api/steps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /steps} : Updates an existing step.
     *
     * @param step the step to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated step,
     * or with status {@code 400 (Bad Request)} if the step is not valid,
     * or with status {@code 500 (Internal Server Error)} if the step couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/steps")
    public ResponseEntity<Step> updateStep(@Valid @RequestBody Step step) throws URISyntaxException {
        log.debug("REST request to update Step : {}", step);
        if (step.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Step result = stepRepository.save(step);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, step.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /steps} : get all the steps.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of steps in body.
     */
    @GetMapping("/steps")
    public List<Step> getAllSteps() {
        log.debug("REST request to get all Steps");
        return stepRepository.findAll();
    }

    /**
     * {@code GET  /steps/:id} : get the "id" step.
     *
     * @param id the id of the step to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the step, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/steps/{id}")
    public ResponseEntity<Step> getStep(@PathVariable String id) {
        log.debug("REST request to get Step : {}", id);
        Optional<Step> step = stepRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(step);
    }

    /**
     * {@code DELETE  /steps/:id} : delete the "id" step.
     *
     * @param id the id of the step to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/steps/{id}")
    public ResponseEntity<Void> deleteStep(@PathVariable String id) {
        log.debug("REST request to delete Step : {}", id);
        stepRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
