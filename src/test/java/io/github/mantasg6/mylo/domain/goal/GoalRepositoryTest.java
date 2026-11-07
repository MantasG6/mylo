package io.github.mantasg6.mylo.domain.goal;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import io.github.mantasg6.mylo.core.database.JpaConfig;
import io.github.mantasg6.mylo.domain.goal.progress.Progress;
import io.github.mantasg6.mylo.domain.goal.progress.ProgressId;
import io.github.mantasg6.mylo.domain.goal.progress.ProgressRepository;

@DataJpaTest
@Import(JpaConfig.class)
public class GoalRepositoryTest {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void cleanUp() {
        goalRepository.deleteAll();
    }

    @Test
    void save_shouldNotRoundTarget_whenTarget3Decimals() {
        Goal goal = new Goal();
        goal.setName("Test Goal");
        goal.setTarget(new BigDecimal("812.908"));

        Long id = goalRepository.save(goal).getId();
        testEntityManager.flush();
        testEntityManager.clear();
        Goal saved = goalRepository.findById(id).orElseThrow();

        assertThat(saved.getTarget()).isEqualTo(new BigDecimal("812.908"));
    }

    @Test
    void delete_shouldDeleteAllProgress_whenGoalHasProgress() {
        Goal goal = new Goal();
        goal.setName("Test Goal");
        goal.setTarget(new BigDecimal("100"));
        Goal saved = goalRepository.save(goal);
        Progress progress1 = new Progress(new ProgressId(saved.getId(), LocalDate.now()));
        progress1.setAmount(new BigDecimal("30"));
        Progress progress2 = new Progress(new ProgressId(saved.getId(), LocalDate.now().minusDays(1)));
        progress2.setAmount(new BigDecimal("40"));
        progressRepository.save(progress1);
        progressRepository.save(progress2);

        goalRepository.delete(goal);
        testEntityManager.flush();
        testEntityManager.clear();

        assertThat(progressRepository.count()).isZero();
    }
}
