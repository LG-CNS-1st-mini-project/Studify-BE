package com.lgcns.studify_be.user;

import com.lgcns.studify_be.user.dto.UserDtos;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studify/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public ResponseEntity<UserDtos.Response> create(@Valid @RequestBody UserDtos.Create req) {
        User u = service.create(req);
        return ResponseEntity
                .created(java.net.URI.create("/api/users/" + u.getId()))
                .body(toRes(u));
    }

    @GetMapping("/{id}")
    public UserDtos.Response get(@PathVariable Long id) { return toRes(service.get(id)); }

    @PutMapping("/{id}")
    public UserDtos.Response update(@PathVariable Long id, @Valid @RequestBody UserDtos.Update req) {
        return toRes(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }

    @GetMapping
    public Page<UserDtos.Response> list(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return service.list(PageRequest.of(page, size)).map(this::toRes);
    }

    private UserDtos.Response toRes(User u) {
        return new UserDtos.Response(
                u.getId(), u.getEmail(), u.getNickname(), u.getStatus(),
                u.getLastLoginAt(), u.getCreatedAt(), u.getUpdatedAt()
        );
    }
}
