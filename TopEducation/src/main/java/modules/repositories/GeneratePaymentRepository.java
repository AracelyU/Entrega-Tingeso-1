package modules.repositories;

import modules.entities.GeneratePaymentsEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface GeneratePaymentRepository extends CrudRepository<GeneratePaymentsEntity, Long> {


    // encontrar pago por id de estudiante
    @Query("select g from GeneratePaymentsEntity g where g.estudiante.id = :id")
    GeneratePaymentsEntity findByStudentId(@Param("id")Long id);




}
