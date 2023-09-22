package modules.repositories;

import modules.entities.StudentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, Long> {

    public StudentEntity findByRut (String rut);


}
