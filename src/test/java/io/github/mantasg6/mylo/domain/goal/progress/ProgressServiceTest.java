package io.github.mantasg6.mylo.domain.goal.progress;

import static org.assertj.core.api.Assertions.assertThat;
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

@ExtendWith(MockitoExtension.class)
public class ProgressServiceTest {

    @Mock
    private ProgressRepository progressRepository;

    private ProgressService progressService;

    @BeforeEach
    void setUp() {
        ProgressMapper mapper = Mappers.getMapper(ProgressMapper.class);
        progressService = new ProgressService(progressRepository, mapper);
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
        Progress progressInDatabase = new Progress();
        progressInDatabase.setId(id);
        progressInDatabase.setAmount(new BigDecimal("19.23"));
        when(progressRepository.findById(id)).thenReturn(Optional.of(progressInDatabase));

        ProgressResponse actual = progressService.getProgress(id);

        assertThat(actual.id()).isEqualTo(id);
        assertThat(actual.amount()).isEqualTo(new BigDecimal("19.23"));
    }

    @Test
    void updateProgress_shouldCreate_whenProgressNotFound() {
        ProgressId id = new ProgressId(1L, LocalDate.of(2011, 7, 12));
        ProgressRequest request = ProgressRequest.builder().amount(new BigDecimal("10")).build();
        when(progressRepository.findById(id)).thenReturn(Optional.empty());
        Progress newProgressRow = new Progress();
        newProgressRow.setId(id);
        newProgressRow.setAmount(new BigDecimal("10"));
        when(progressRepository.save(any(Progress.class))).thenReturn(newProgressRow);

        ProgressResponse actual = progressService.updateProgress(id, request);

        verify(progressRepository.save(any(Progress.class)));
        assertThat(actual.id()).isEqualTo(id);
        assertThat(actual.amount()).isEqualTo(new BigDecimal("10"));
    }

}
