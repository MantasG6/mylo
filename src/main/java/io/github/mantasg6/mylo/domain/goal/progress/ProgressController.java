package io.github.mantasg6.mylo.domain.goal.progress;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;

/**
 * Progress Controller describes API HTTP contract for progress.
 *
 */
@RestController
@RequestMapping(ProgressController.PROGRESS_API)
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    public static final String PROGRESS_API = "/api/goals/{goalId}/progress";

    /**
     * Get a single progress row.
     *
     * @param goalId ID of the Goal for which the progress is tracked.
     * @param effectiveDate A concrete date to retrieve the progress for.
     * @return HTTP 200 and the amount of progress made in the provided day.
     */
    @GetMapping("/{effectiveDate}")
    public ResponseEntity<ProgressResponse> getById(
            @PathVariable Long goalId,
            @PathVariable LocalDate effectiveDate
    ) {
        return ResponseEntity.ok(progressService.getProgress(new ProgressId(goalId, effectiveDate)));
    }

    /**
     * Update Goal progress.
     * If there are no progress rows for provided date, a new row will be created with the specified amount.
     *
     * @param goalId ID of the Goal for which the progress is tracked.
     * @param effectiveDate A concrete date to update the progress for.
     * @param request A new amount of progress.
     * @return HTTP 200 and the updated amount of progress.
     */
    @PutMapping("/{effectiveDate}")
    public ResponseEntity<ProgressResponse> update(
            @PathVariable Long goalId,
            @PathVariable @PastOrPresent(message = "Effective Date cannot be in the future")
            LocalDate effectiveDate,
            @RequestBody @Valid ProgressRequest request
    ) {
        return ResponseEntity.ok(progressService.updateProgress(new ProgressId(goalId, effectiveDate), request));
    }
}
