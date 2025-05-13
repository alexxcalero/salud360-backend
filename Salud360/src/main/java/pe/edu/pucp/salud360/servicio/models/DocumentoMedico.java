package pe.edu.pucp.salud360.servicio.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "documentoMedico")
public class DocumentoMedico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDocumentoMedico;

    @Column(name = "documento", unique = false, nullable = false, updatable = true)
    private String documento;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "fechaCreacion", unique = false, nullable = false, updatable = false)
    private LocalDate fechaCreacion;

    @Column(name = "fechaDesactivacion", unique = false, nullable = true, updatable = true)
    private LocalDateTime fechaDesactivacion;

    @ManyToOne
    @JoinColumn(name = "idCitaMedica")
    private CitaMedica citaMedica;
}
