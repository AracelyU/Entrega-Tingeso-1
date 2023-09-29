package modules.repositories;

import modules.entities.TestEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TestRepository extends CrudRepository<TestEntity, Long> {

    @Query(value = "select e from examenes e where e.rut = :rut and e.fecha =:fecha limit 1", nativeQuery = true)
    TestEntity findExamen(@Param("rut") String rut, @Param("fecha") String fecha);

}
