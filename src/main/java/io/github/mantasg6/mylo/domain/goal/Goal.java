package io.github.mantasg6.mylo.domain.goal;

import java.math.BigDecimal;

import io.github.mantasg6.mylo.core.database.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal target;
}
