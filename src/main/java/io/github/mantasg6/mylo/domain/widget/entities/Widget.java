package io.github.mantasg6.mylo.domain.widget.entities;

import io.github.mantasg6.mylo.core.database.BaseEntity;
import io.github.mantasg6.mylo.domain.workspace.entities.Workspace;
import io.github.mantasg6.mylo.shared.enums.WidgetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "widgets")
public class Widget extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type")
    private WidgetType targetType;

    @Column(name = "target_id")
    private Long targetId;

    private int position;

}
