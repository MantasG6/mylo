package io.github.mantasg6.mylo.domain.widget;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Widget Repository for storing Widgets in the database.
 *
 */
public interface WidgetRepository extends JpaRepository<Widget, Long> {}
