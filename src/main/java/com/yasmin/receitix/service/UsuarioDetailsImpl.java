package com.yasmin.receitix.service;

import com.yasmin.receitix.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UsuarioDetailsImpl implements UserDetails {

    private Usuario usuario;

    public UsuarioDetailsImpl(Usuario usuario){
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return usuario.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }
    @Override
    public String getPassword(){
        return usuario.getSenha();
    }
    @Override
    public String getUsername(){
        return usuario.getEmail();
    }
}
