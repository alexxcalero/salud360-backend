package pe.edu.pucp.salud360.usuario.models;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.salud360.comunidad.models.Comentario;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;
import pe.edu.pucp.salud360.comunidad.models.Publicacion;
import pe.edu.pucp.salud360.comunidad.models.Testimonio;
import pe.edu.pucp.salud360.membresia.models.Afiliacion;
import pe.edu.pucp.salud360.membresia.models.MedioDePago;
import pe.edu.pucp.salud360.servicio.models.CitaMedica;
import pe.edu.pucp.salud360.servicio.models.Clase;
import pe.edu.pucp.salud360.servicio.models.Reserva;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "persona")
@PrimaryKeyJoinColumn(name = "idUsuario")
public class Persona extends Usuario {
    @Column(name = "direccion", unique = false, nullable = false, updatable = true)
    private String direccion;

    @ManyToMany(mappedBy = "persona")
    private List<Comunidad> comunidades;

    @ManyToMany(mappedBy = "personas")
    private List<Clase> clases;

    @OneToMany(mappedBy = "persona")
    private List<Afiliacion> afiliaciones;

    @OneToMany(mappedBy = "persona")
    private List<MedioDePago> mediosDePago;

    @OneToMany(mappedBy = "persona")
    private List<Publicacion> publicaciones;

    @OneToMany(mappedBy = "persona")
    private List<Comentario> comentarios;

    @OneToMany(mappedBy = "persona")
    private List<Testimonio> testimonios;

    @OneToMany(mappedBy = "usuario")
    private List<Notificacion> notificaciones;

    @OneToMany(mappedBy = "persona")
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "paciente")
    private List<CitaMedica> citasMedicas;
}
