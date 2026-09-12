package io.github.mantasg6.mylo.domain.widget;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.github.mantasg6.mylo.domain.widget.WidgetRequest.OnCreate;
import io.github.mantasg6.mylo.domain.widget.WidgetRequest.OnUpdate;
import lombok.RequiredArgsConstructor;

/**
 * Widget controller describes API HTTP contract for Widgets.
 *
 */
@RestController
@RequestMapping(WidgetController.WIDGETS_API)
@RequiredArgsConstructor
public class WidgetController {

    public static final String WIDGETS_API = "/api/widgets";

    private final WidgetService widgetService;

    /**
     * Retrieve all Widgets available in the database.
     *
     * @return HTTP 200 and the list of Widgets.
     */
    @GetMapping
    public ResponseEntity<List<WidgetResponse>> getAll() {
        return ResponseEntity.ok(widgetService.getAllWidgets());
    }

    /**
     * Retrieve single Widget.
     *
     * @param id ID of the Widget to retrieve.
     * @return HTTP 200 and the details of the retrieved Widget.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WidgetResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(widgetService.getWidgetById(id));
    }
    
    /**
     * Create new Widget.
     *
     * @param request Request with the details about the Widget to create.
     * @return HTTP 201 and the details of the new Widget.
     */
    @PostMapping
    public ResponseEntity<WidgetResponse> createWidget(
        @RequestBody @Validated(OnCreate.class) WidgetRequest request
    ) {
        WidgetResponse created = widgetService.createWidget(request);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    /**
     * Update an existing Widget.
     *
     * @param id ID of the Widget to update.
     * @param request Request with the update details.
     * @return HTTP 200 and the details of the updated Widget.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WidgetResponse> updateWidget(
        @PathVariable Long id,
        @RequestBody @Validated(OnUpdate.class) WidgetRequest request
    ) {
        return ResponseEntity.ok(widgetService.updateWidget(id, request));
    }

    /**
     * Delete a Widget.
     *
     * @param id ID of the Widget to delete.
     * @return HTTP 204.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWidget(@PathVariable Long id) {
        widgetService.deleteWidget(id);
        return ResponseEntity.noContent().build();
    }

}
