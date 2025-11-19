package com.ejerciciocopilot.controller;

import com.ejerciciocopilot.dto.RoleResponseDTO;
import com.ejerciciocopilot.model.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * Exposición de roles soportados. Los roles son estáticos (enum),
 * por lo que POST simula un intento de creación y responde 501.
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @GetMapping
    public ResponseEntity<RoleResponseDTO> listAll() {
        return ResponseEntity.ok(
                RoleResponseDTO.builder()
                        .roles(Arrays.stream(Role.values()).map(Enum::name).toList())
                        .build()
        );
    }

    @GetMapping("/{role}")
    public ResponseEntity<Map<String, Object>> getRole(@PathVariable String role) {
        try {
            Role r = Role.valueOf(role.toUpperCase());
            return ResponseEntity.ok(Map.of(
                    "role", r.name(),
                    "valid", true
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "role", role,
                    "valid", false,
                    "message", "Rol inválido"
            ));
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createRole(@RequestBody Map<String, String> body) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(Map.of(
                "message", "Los roles son estáticos y no pueden crearse dinámicamente",
                "provided", body.get("role")
        ));
    }
}
