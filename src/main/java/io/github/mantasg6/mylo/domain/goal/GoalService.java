package io.github.mantasg6.mylo.domain.goal;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Goal management service.
 *
 */
@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository repository;
    private final GoalMapper mapper;

    /**
     * Get all Goals from the database.
     *
     * @return List of all Goals.
     */
    public List<GoalResponse> getAllGoals() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    /**
     * Get Goal by ID.
     *
     * @param id 
     * @return 
     */
    public GoalResponse getGoalById(Long id) {
        Goal goal = repository.findById(id).orElseThrow(() -> new GoalNotFoundException(id));
        return mapper.toDto(goal);
    }

    /**
     * Creates new Goal.
     *
     * @param request Details of the Goal to be created.
     * @return The created Goal details.
     */
    public GoalResponse createGoal(GoalRequest request) {
        return mapper.toDto(repository.save(mapper.toEntity(request)));
    }

    /**
     * Update Goal of the given ID with the given details.
     *
     * @param id ID of the Goal to be updated.
     * @param request New details of the Goal to be updated.
     * @return New Goal details.
     */
    public GoalResponse updateGoal(Long id, GoalRequest request) {
        Goal goal = repository.findById(id).orElseThrow(() -> new GoalNotFoundException(id));

        Optional.ofNullable(request.name()).ifPresent(goal::setName);

        return mapper.toDto(repository.save(goal));
    }

    /**
     * Delete Goal with provided ID.
     *
     * @param id ID of the Goal to be deleted.
     */
    public void deleteGoal(Long id) {
        repository.deleteById(id);
    }
}
