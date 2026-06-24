package io.github.mantasg6.mylo.domain.entities;

import io.github.mantasg6.mylo.core.database.BaseEntity;
import io.github.mantasg6.mylo.shared.enums.WidgetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "widgets")
public class Widget extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type")
    private WidgetType targetType;

    @Column(name = "target_id")
    private Long targetId;

    private int position;

}
