package pe.edu.pucp.salud360.comunidad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.salud360.comunidad.models.Comunidad;

import java.util.List;

@Repository
public interface ComunidadRepository extends JpaRepository<Comunidad, Integer> {
    @Query(value = """
    SELECT * FROM ingesoft.comunidad c
    WHERE c.id_comunidad NOT IN (
        SELECT cc.id_comunidad
        FROM ingesoft.comunidad_cliente cc
        WHERE cc.id_cliente = :idCliente
    )
    ORDER BY RANDOM()
    LIMIT 1
""", nativeQuery = true)
    Comunidad findComunidadAleatoriaExcluyendoCliente(@Param("idCliente") Integer idCliente);

    @Query(value = """
    SELECT * FROM ingesoft.comunidad c
    WHERE c.id_comunidad NOT IN (
        SELECT cc.id_comunidad
        FROM ingesoft.comunidad_cliente cc
        WHERE cc.id_cliente = :idCliente
    )
""", nativeQuery = true)
    List<Comunidad> findComunidadesExcluyendoCliente(@Param("idCliente") Integer idCliente);

    @Query(value = """
SELECT DISTINCT c.*
FROM ingesoft.comunidad_cliente cc
JOIN ingesoft.comunidad c ON c.id_comunidad = cc.id_comunidad
LEFT JOIN ingesoft.afiliacion a ON a.id_cliente = cc.id_cliente
AND a.id_membresia IN (
    SELECT m.id_membresia FROM ingesoft.membresia m WHERE m.id_comunidad = c.id_comunidad
)
WHERE cc.id_cliente = :idCliente
AND (a.estado IS NULL OR a.estado NOT IN ('Activado'))
""", nativeQuery = true)
    List<Comunidad> findComunidadesInactivasDeCliente(@Param("idCliente") Integer idCliente);


    @Query(value = """
SELECT DISTINCT c.*
FROM ingesoft.comunidad_cliente cc
JOIN ingesoft.comunidad c ON c.id_comunidad = cc.id_comunidad
LEFT JOIN ingesoft.afiliacion a ON a.id_cliente = cc.id_cliente
AND a.id_membresia IN (
    SELECT m.id_membresia FROM ingesoft.membresia m WHERE m.id_comunidad = c.id_comunidad
)
WHERE cc.id_cliente = :idCliente
AND (a.estado IS NULL OR a.estado IN ('Activado'))
""", nativeQuery = true)
    List<Comunidad> findComunidadesActivasDeCliente(@Param("idCliente") Integer idCliente);
}
