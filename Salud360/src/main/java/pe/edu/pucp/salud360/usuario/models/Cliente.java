package pe.edu.pucp.salud360.usuario.models;

import jakarta.persistence.*;

import lombok.*;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.comunidad.models.Testimonio;
import pe.edu.pucp.salud360.membresia.models.Afiliacion;
import pe.edu.pucp.salud360.membresia.models.MedioDePago;
import pe.edu.pucp.salud360.servicio.models.CitaMedica;
import pe.edu.pucp.salud360.servicio.models.Clase;
import pe.edu.pucp.salud360.servicio.models.Reserva;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCliente", unique = true, nullable = false, updatable = false)
    private Integer idCliente;

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

    @Column(name = "fechaNacimiento", unique = false, nullable = false, updatable = true)
    private LocalDate fechaNacimiento;

    @Column(name = "direccion", unique = false, nullable = false, updatable = true)
    private String direccion;

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

    @ManyToMany(mappedBy = "clientes")
    private List<Comunidad> comunidades;

    @OneToMany(mappedBy = "cliente")
    private List<Afiliacion> afiliaciones;

    @OneToMany(mappedBy = "cliente")
    private List<Reserva> reservas;

    @ManyToMany(mappedBy = "clientes")
    private List<Clase> clases;

    @OneToMany(mappedBy = "cliente")
    private List<CitaMedica> citasMedicas;

    @OneToMany(mappedBy = "cliente")
    private List<Notificacion> notificaciones;

    @OneToMany(mappedBy = "cliente")
    private List<MedioDePago> mediosDePago;

    @OneToMany(mappedBy = "cliente")
    private List<Testimonio> testimonios;
}
