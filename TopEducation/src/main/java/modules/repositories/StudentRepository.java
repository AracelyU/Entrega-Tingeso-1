package modules.repositories;

import jakarta.persistence.Table;
import lombok.Data;
import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Table(name = "estudiante")

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, Long> {

    // encontrar estudiante por su rut
    StudentEntity findByRut(String rut);

    // encontrar estudiante por su id
    @Query("select e from StudentEntity e where e.id = :id")
    StudentEntity findByid(@Param("id")Long id);


}
