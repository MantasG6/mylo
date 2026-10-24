package io.github.mantasg6.mylo.domain.goal.progress;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

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

    /**
     * Get goal progress for a single provided effective date.
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

    public ProgressResponse updateProgress(ProgressId id, ProgressRequest request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void deleteSingle(ProgressId id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void deleteAll(Long goalId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
