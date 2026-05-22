package com.chronoaegis.auth_service.service;
import com.chronoaegis.auth_service.dto.AtualizarUsuarioDTO;
import com.chronoaegis.auth_service.dto.UsuarioDTO;
import com.chronoaegis.auth_service.dto.LoginDTO;
import com.chronoaegis.auth_service.dto.RegistroDTO;
import com.chronoaegis.auth_service.model.Usuario;
import com.chronoaegis.auth_service.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registrar(RegistroDTO dto){
        if(usuarioRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNomeJogador(dto.getNomeJogador());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setRole("Jogador");

        usuarioRepository.save(usuario);
        return "Usuário criado com sucesso";
    }

    public String login(LoginDTO dto){
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if(!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }

        return "Login feito com sucesso! Seja bem vindo, " + usuario.getNomeJogador();
    }

    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNomeJogador(usuario.getNomeJogador());
        dto.setEmail(usuario.getEmail());
        dto.setRole(usuario.getRole());
        return dto;
    }

    public UsuarioDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNomeJogador(usuario.getNomeJogador());
        dto.setEmail(usuario.getEmail());
        dto.setRole(usuario.getRole());
        return dto;
    }

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(u -> {
                    UsuarioDTO dto = new UsuarioDTO();
                    dto.setId(u.getId());
                    dto.setNomeJogador(u.getNomeJogador());
                    dto.setEmail(u.getEmail());
                    dto.setRole(u.getRole());
                    return dto;
                })
                .toList();
    }

    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    public UsuarioDTO atualizar(Long id, AtualizarUsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!usuario.getEmail().equals(dto.getEmail())
                && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado por outro usuário");
        }

        usuario.setNomeJogador(dto.getNomeJogador());
        usuario.setEmail(dto.getEmail());
        usuarioRepository.save(usuario);

        UsuarioDTO response = new UsuarioDTO();
        response.setId(usuario.getId());
        response.setNomeJogador(usuario.getNomeJogador());
        response.setEmail(usuario.getEmail());
        response.setRole(usuario.getRole());
        return response;
    }

}
