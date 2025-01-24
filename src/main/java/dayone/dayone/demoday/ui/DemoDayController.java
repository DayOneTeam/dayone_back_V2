package dayone.dayone.demoday.ui;

import dayone.dayone.auth.ui.argumentresolver.AuthUser;
import dayone.dayone.demoday.service.DemoDayService;
import dayone.dayone.demoday.service.dto.DemoDayCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/demo-days")
@RestController
public class DemoDayController {

    private final DemoDayService demoDayService;

    @PostMapping
    public ResponseEntity<Void> create(@AuthUser final Long userId, @RequestBody final DemoDayCreateRequest request) {
        final Long savedId = demoDayService.create(userId, request);
        return ResponseEntity.created(URI.create(String.format("/api/v1/demo-days/%d", savedId))).build();
    }
}
