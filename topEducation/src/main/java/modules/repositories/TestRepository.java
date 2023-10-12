package modules.repositories;

import modules.entities.TestEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends CrudRepository<TestEntity, Long> {

    // obtener el promedio de los ultimos examenes de un estudiante
    @Query(value = "SELECT avg(e.puntaje_obtenido) FROM examenes e WHERE e.rut = :rut AND e.fecha_examen = (SELECT MAX(e.fecha_examen) FROM examenes e WHERE e.rut = :rut)", nativeQuery = true)
    Float findPuntajePromedio(@Param("rut") String rut);

    // obtener numero de pruebas que dio un estudiante durante todo el preuniversitario
    @Query(value = "SELECT COUNT(*) FROM examenes e WHERE e.rut = :rut", nativeQuery = true)
    Integer findNumeroPruebas(@Param("rut") String rut);



}
