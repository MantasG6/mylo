package io.github.mantasg6.mylo.domain.goal;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Goal controller describes API HTTP contract for Goals.
 *
 */
@RestController
@RequestMapping(GoalController.GOALS_API)
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    public static final String GOALS_API = "/api/goals";

    /**
     * GET all Goals.
     *
     * @return HTTP 200 and details response of all Goals.
     */
    @GetMapping
    public ResponseEntity<List<GoalResponse>> getAll() {
        return ResponseEntity.ok(goalService.getAllGoals());
    }

    /**
     * Get a single Goal by ID.
     *
     * @param id ID of the Goal to retrieve.
     * @return HTTP 200 and the details of the Goal retrieved.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GoalResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(goalService.getGoalById(id));
    }

    /**
     * Create a new Goal.
     *
     * @param request Details of the Goal to be created.
     * @return HTTP 201 and details of the new Goal.
     */
    @PostMapping
    public ResponseEntity<GoalResponse> createGoal(@RequestBody @Valid GoalRequest request) {
        GoalResponse created = goalService.createGoal(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    /**
     * Update and existing Goal.
     *
     * @param id ID of the Goal to update.
     * @param request Details to update.
     * @return HTTP 200 and updated Goal.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GoalResponse> updateGoal(
        @PathVariable Long id,
        @RequestBody @Valid GoalRequest request
    ) {
        return ResponseEntity.ok(goalService.updateGoal(id, request));
    }

    /**
     * Delete Goal.
     *
     * @param id ID of the Goal to delete.
     * @return HTTP 204.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }
}
