package com.chronoaegis.auth_service.service;

import com.chronoaegis.auth_service.dto.LoginDTO;
import com.chronoaegis.auth_service.dto.RegistroDTO;
import com.chronoaegis.auth_service.model.Usuario;
import com.chronoaegis.auth_service.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

}
