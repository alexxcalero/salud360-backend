package pe.edu.pucp.salud360.autenticacion.models;


import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pe.edu.pucp.salud360.usuario.models.Usuario;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class UsuarioDetails implements UserDetails {

    private final Usuario usuario;



    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Ajusta si tienes roles
    }

    @Override
    public String getPassword() {
        return usuario.getContrasenha();
    }

    @Override
    public String getUsername() {
        return usuario.getCorreo(); // O lo que uses para login
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return usuario.getActivo(); }
}

