package io.github.mantasg6.mylo.domain.goal;

import java.util.ArrayList;
import java.util.List;

import io.github.mantasg6.mylo.core.database.BaseEntity;
import io.github.mantasg6.mylo.domain.progress.Progress;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Goal entity representing goal database table.
 *
 */
@Setter
@Getter
@Entity
@Table(name = "goals")
public class Goal extends BaseEntity {

    private String name;

    @OneToMany(
        mappedBy = "goal",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Progress> progress = new ArrayList<>();
}
