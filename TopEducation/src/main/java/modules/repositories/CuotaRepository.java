package modules.repositories;

import modules.entities.CuotaEntity;
import modules.entities.StudentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CuotaRepository extends CrudRepository<CuotaEntity, Long> {

    ArrayList<CuotaEntity> findCuotaEntitiesById(Long id);


}
