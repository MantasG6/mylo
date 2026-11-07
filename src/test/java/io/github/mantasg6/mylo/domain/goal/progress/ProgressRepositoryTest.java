package io.github.mantasg6.mylo.domain.goal.progress;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import io.github.mantasg6.mylo.core.database.JpaConfig;
import io.github.mantasg6.mylo.domain.goal.Goal;
import io.github.mantasg6.mylo.domain.goal.GoalRepository;

@DataJpaTest
@Import(JpaConfig.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ProgressRepositoryTest {

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private GoalRepository goalRepository;

    private Goal goal;

    @BeforeAll
    void setUpGoal() {
        goal = new Goal();
        goal.setName("Test Goal");
        goal.setTarget(new BigDecimal("1000"));
        goalRepository.save(goal);
    }

    @AfterAll
    void cleanUpGoal() {
        goalRepository.deleteAll();
    }

    @BeforeEach
    void cleanUpProgress() {
        progressRepository.deleteAll();
    }

    @Test
    void save_shouldNotRoundAmount_whenAmount3Decimals() {
        Progress progress = new Progress();
        ProgressId id = new ProgressId(goal.getId(), LocalDate.of(2000, 01, 19));
        progress.setId(id);
        progress.setAmount(new BigDecimal("99.107"));

        progressRepository.save(progress);
        testEntityManager.flush();
        testEntityManager.clear();
        Progress saved = progressRepository.findById(id).orElseThrow();

        assertThat(saved.getAmount()).isEqualTo(new BigDecimal("99.107"));
    }
}
