package io.github.mantasg6.mylo.domain.workspace;

import java.util.ArrayList;
import java.util.List;

import io.github.mantasg6.mylo.core.database.BaseEntity;
import io.github.mantasg6.mylo.domain.widget.Widget;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "workspaces")
public class Workspace extends BaseEntity {

    private String name;

    @OneToMany(
        mappedBy = "workspace",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Widget> widgets = new ArrayList<>();

}
