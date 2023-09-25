package modules.repositories;

import modules.entities.GeneratePaymentsEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeneratePaymentRepository extends CrudRepository<GeneratePaymentsEntity, Long> {

    @Query("select g from GeneratePaymentsEntity g where g.id = :id")
    GeneratePaymentsEntity findByid(@Param("id")Long id);


}
