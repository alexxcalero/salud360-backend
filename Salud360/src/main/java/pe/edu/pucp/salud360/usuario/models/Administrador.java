package pe.edu.pucp.salud360.usuario.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "administrador")
public class Administrador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAdministrador", unique = true, nullable = false, updatable = false)
    private Integer idAdministrador;

    @Column(name = "nombres", unique = false, nullable = false, updatable = true)
    private String nombres;

    @Column(name = "apellidos", unique = false, nullable = false, updatable = true)
    private String apellidos;

    @Column(name = "numeroDocumento", unique = true, nullable = false, updatable = true)
    private String numeroDocumento;

    @Column(name = "sexo", unique = false, nullable = false, updatable = true)
    private String sexo;

    @Column(name = "telefono", unique = true, nullable = false, updatable = true)
    private String telefono;

    @Column(name = "fotoPerfil", unique = false, nullable = true, updatable = true)
    private String fotoPerfil;

    @Column(name = "notificacionPorCorreo", unique = false, nullable = false, updatable = true)
    private Boolean notificacionPorCorreo;

    @Column(name = "notificacionPorSMS", unique = false, nullable = false, updatable = true)
    private Boolean notificacionPorSMS;

    @Column(name = "notificacionPorWhatsApp", unique = false, nullable = false, updatable = true)
    private Boolean notificacionPorWhatsApp;

    @Column(name = "activo", unique = false, nullable = false, updatable = true)
    private Boolean activo;

    @Column(name = "fechaCreacion", unique = false, nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fechaDesactivacion", unique = false, nullable = true, updatable = true)
    private LocalDateTime fechaDesactivacion;

    @OneToOne
    @JoinColumn(name = "idUsuario", unique = true, nullable = false, updatable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idTipoDocumento", unique = false, nullable = false, updatable = true)
    private TipoDocumento tipoDocumento;
}
