package io.github.mantasg6.mylo.domain.goal.progress;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

@DataJpaTest
public class ProgressRepositoryTest {

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void save_shouldNotRoundAmount_whenAmount3Decimals() {
        Progress progress = new Progress();
        ProgressId id = new ProgressId(1L, LocalDate.of(2000, 01, 19));
        progress.setId(id);
        progress.setAmount(new BigDecimal("99.107"));
        progress.setCreatedAt(Instant.now());
        progress.setUpdatedAt(Instant.now());

        progressRepository.save(progress);
        testEntityManager.flush();
        testEntityManager.clear();
        Progress saved = progressRepository.findById(id).orElseThrow();

        assertThat(saved.getAmount()).isEqualTo(new BigDecimal("99.107"));
    }
}
