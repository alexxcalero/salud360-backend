package pe.edu.pucp.salud360.usuario.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario", unique = true, nullable = false, updatable = false)
    private Integer idUsuario;

    @Column(name = "correo", unique = true, nullable = false, updatable = true)
    private String correo;

    @Column(name = "contrasenha", unique = false, nullable = false, updatable = true)
    private String contrasenha;

    @Column(name = "activo", unique = false, nullable = false, updatable = true)
    private Boolean activo;

    @OneToOne(mappedBy = "usuario")
    private Cliente cliente;

    @OneToOne(mappedBy = "usuario")
    private Administrador administrador;

    @ManyToOne
    @JoinColumn(name = "idRol", unique = false, nullable = false, updatable = true)
    private Rol rol;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.getNombre()));
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public String getPassword() {
        return contrasenha;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
        //return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
        //return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
        //return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return activo;
    }
}
