package modules.repositories;

import modules.entities.GeneratePaymentsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneratePaymentRepository extends CrudRepository<GeneratePaymentsEntity, Long> {



}
