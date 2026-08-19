package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*") // ¡ESTO ES VITAL! Evita que el navegador bloquee la conexión
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para RECIBIR al usuario de la web y GUARDARLO en MySQL
    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Método para LEER los usuarios (por si luego los quieres listar)
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
}