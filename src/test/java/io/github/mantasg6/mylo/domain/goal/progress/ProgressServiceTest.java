package io.github.mantasg6.mylo.domain.goal.progress;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.mantasg6.mylo.domain.goal.GoalNotFoundException;
import io.github.mantasg6.mylo.domain.goal.GoalRepository;

@ExtendWith(MockitoExtension.class)
public class ProgressServiceTest {

    @Mock
    private ProgressRepository progressRepository;

    @Mock
    private GoalRepository goalRepository;

    private ProgressService progressService;

    @BeforeEach
    void setUp() {
        ProgressMapper mapper = Mappers.getMapper(ProgressMapper.class);
        progressService = new ProgressService(progressRepository, mapper, goalRepository);
    }

    @Test
    void getProgress_shouldReturnAmount0_whenProgressNotFound() {
        ProgressId id = new ProgressId(1L, LocalDate.of(2000, 3, 2));
        when(progressRepository.findById(id)).thenReturn(Optional.empty());

        ProgressResponse actual = progressService.getProgress(id);

        assertThat(actual.id()).isNull();
        assertThat(actual.amount()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void getProgress_shouldReturnCorrectAmount_whenProgressExists() {
        ProgressId id = new ProgressId(1L, LocalDate.of(2009, 1, 9));
        Progress progress = new Progress(id);
        progress.setAmount(new BigDecimal("19.23"));
        when(progressRepository.findById(id)).thenReturn(Optional.of(progress));

        ProgressResponse actual = progressService.getProgress(id);

        assertThat(actual.id()).isEqualTo(id);
        assertThat(actual.amount()).isEqualTo(new BigDecimal("19.23"));
    }

    @Test
    void updateProgress_shouldCreate_whenProgressWithDateNotFound() {
        ProgressId id = new ProgressId(1L, LocalDate.of(2011, 7, 12));
        ProgressRequest request = ProgressRequest.builder().amount(new BigDecimal("10")).build();
        when(progressRepository.findById(id)).thenReturn(Optional.empty());
        Progress newProgressRow = new Progress(id);
        newProgressRow.setAmount(new BigDecimal("10"));
        when(progressRepository.save(any(Progress.class))).thenReturn(newProgressRow);
        when(goalRepository.existsById(1L)).thenReturn(true);

        ProgressResponse actual = progressService.updateProgress(id, request);

        verify(progressRepository).save(any(Progress.class));
        assertThat(actual.id()).isEqualTo(id);
        assertThat(actual.amount()).isEqualTo(new BigDecimal("10"));
    }

    @Test 
    void updateProgress_shouldThrow_whenGoalNotFound() {
        ProgressId id = new ProgressId(1L, LocalDate.of(2011, 9, 9));
        ProgressRequest request = ProgressRequest.builder().amount(new BigDecimal("8")).build();
        when(progressRepository.findById(id)).thenReturn(Optional.empty());
        when(goalRepository.existsById(1L)).thenReturn(false);

        GoalNotFoundException actual = assertThrows(
                GoalNotFoundException.class,
                () -> progressService.updateProgress(id, request)
        );

        assertThat(actual.getMessage()).isEqualTo("Goal with id 1 not found!");
    }

    @Test
    void updateProgress_shouldDeleteProgressRow_whenAmount0() {
        ProgressId id = new ProgressId(1L, LocalDate.of(2009, 4, 23));
        ProgressRequest request = ProgressRequest.builder().amount(BigDecimal.ZERO).build();
        Progress progress = new Progress(id);
        progress.setAmount(new BigDecimal("71"));
        when(progressRepository.findById(id)).thenReturn(Optional.of(progress));

        ProgressResponse actual = progressService.updateProgress(id, request);

        verify(progressRepository).deleteById(id);
        assertThat(actual.amount()).isEqualTo(BigDecimal.ZERO);
    }
}
