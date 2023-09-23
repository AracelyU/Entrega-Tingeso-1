package modules.repositories;

import jakarta.persistence.Table;
import lombok.Data;
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


    StudentEntity findByRut(String rut);


}
