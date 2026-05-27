package com.chronoaegis.auth_service.controller;

import com.chronoaegis.auth_service.dto.AtualizarUsuarioDTO;
import com.chronoaegis.auth_service.dto.LoginDTO;
import com.chronoaegis.auth_service.dto.RegistroDTO;
import com.chronoaegis.auth_service.dto.UsuarioDTO;
import com.chronoaegis.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@Valid @RequestBody RegistroDTO dto) {
        return ResponseEntity.ok(authService.registrar(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        return ResponseEntity.ok(authService.listarTodos());
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(authService.buscarPorId(id));
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarUsuarioDTO dto) {
        return ResponseEntity.ok(authService.atualizar(id, dto));
    }

    @GetMapping("/usuarios/email/{email}")
    public ResponseEntity<UsuarioDTO> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(authService.buscarPorEmail(email));
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        authService.deletar(id);
        return ResponseEntity.ok("Usuário deletado com sucesso!");
    }
}
