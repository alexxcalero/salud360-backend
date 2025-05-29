package pe.edu.pucp.salud360.usuario.models;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.pucp.salud360.servicio.models.CitaMedica;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medico")
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMedico", unique = true, nullable = false, updatable = false)
    private Integer idMedico;

    @Column(name = "nombres", unique = false, nullable = false, updatable = true)
    private String nombres;

    @Column(name = "apellidos", unique = false, nullable = false, updatable = true)
    private String apellidos;

    @Column(name = "numeroDocumento", unique = true, nullable = false, updatable = true)
    private String numeroDocumento;

    @Column(name = "sexo", unique = false, nullable = false, updatable = true)
    private String sexo;

    @Column(name = "especialidad", unique = false, nullable = false, updatable = true)
    private String especialidad;

    @Column(name = "descripcion", unique = false, nullable = false, updatable = true)
    private String descripcion;

    @Column(name = "fotoPerfil", unique = false, nullable = true, updatable = true)
    private String fotoPerfil;

    @Column(name = "activo", unique = false, nullable = false, updatable = true)
    private Boolean activo;

    @Column(name = "fechaCreacion", unique = false, nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fechaDesactivacion", unique = false, nullable = true, updatable = true)
    private LocalDateTime fechaDesactivacion;

    @ManyToOne
    @JoinColumn(name = "idTipoDocumento", unique = false, nullable = false, updatable = true)
    private TipoDocumento tipoDocumento;

    @OneToMany(mappedBy = "medico")
    private List<CitaMedica> citasMedicas;
}
