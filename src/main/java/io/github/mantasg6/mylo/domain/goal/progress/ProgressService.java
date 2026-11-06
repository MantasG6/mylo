package io.github.mantasg6.mylo.domain.goal.progress;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.mantasg6.mylo.domain.goal.GoalNotFoundException;
import io.github.mantasg6.mylo.domain.goal.GoalRepository;
import lombok.RequiredArgsConstructor;

/**
 * Progress management service.
 *
 */
@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final ProgressMapper progressMapper;
    private final GoalRepository goalRepository;

    /**
     * Get goal progress for a single provided effective date.
     * If progress for provided goal and day doesn't exist, returns result with amount 0.
     *
     * @param id Goal id and effective date.
     * @return Progress Response with amount of progress made in that day.
     */
    public ProgressResponse getProgress(ProgressId id) {
        Progress progress = progressRepository.findById(id).orElse(null);
        
        if (progress == null) {
            return ProgressResponse.builder().amount(BigDecimal.ZERO).build();
        }

        return progressMapper.toDto(progress);
    }

    /**
     * Update Goal Progress.
     * Creates new progress row if one doesn't exist yet for the given date.
     * Deletes an existing progress row if updating the amount to 0.
     *
     * @param id Goal id and effective date.
     * @param request Progress Request with the amount to update to.
     * @return Progress Response with the updated amount.
     * @throws GoalNotFoundException if Goal with provided goalId not found. 
     */
    public ProgressResponse updateProgress(ProgressId id, ProgressRequest request) {
        Optional<Progress> persistedProgress = progressRepository.findById(id);

        // If amount == 0 and progress exists, delete
        if (request.amount().equals(BigDecimal.ZERO) && persistedProgress.isPresent()) {
            progressRepository.deleteById(id);
            return ProgressResponse.builder().amount(BigDecimal.ZERO).build();
        }

        // If progress doesn't exist, create new
        Progress progress = persistedProgress.orElseGet(() -> {
            // If goal also doesn't exist, throw
            if (!goalRepository.existsById(id.goalId())) {
                throw new GoalNotFoundException(id.goalId());
            }
            return new Progress(id);
        });

        // Finally update the progress
        progress.setAmount(request.amount());
        Progress updated = progressRepository.save(progress);

        return progressMapper.toDto(updated);
    }

    /**
     * Delete all progress associated with a Goal.
     *
     * @param goalId Goal id of the Goal progress to be deleted.
     */
    public void deleteAll(Long goalId) {
        progressRepository.deleteAllByGoalId(goalId);
    }
}
