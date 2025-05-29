package pe.edu.pucp.salud360.servicio.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "documentoMedico")
public class DocumentoMedico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDocumentoMedico", unique = true, nullable = false, updatable = false)
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
