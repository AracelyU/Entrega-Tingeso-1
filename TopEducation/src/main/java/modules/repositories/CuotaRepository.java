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


    // encontrar cuota por id_cuota
    CuotaEntity findCuotaEntitiesById(Long id);

    // seleccionar cuotas por id de pago
    @Query("SELECT c FROM CuotaEntity c WHERE c.generatePaymentsEntity.id = :generatePaymentId")
    ArrayList<CuotaEntity> findCuotasByGeneratePaymentId(@Param("generatePaymentId") Long generatePaymentId);


    // seleccionar cuotas por id de pago y que esten pendientes
    @Query("SELECT c FROM CuotaEntity c WHERE c.estadoCuota = 'pendiente' AND c.generatePaymentsEntity.id = :generatePaymentId")
    ArrayList<CuotaEntity> findCuotasPendientesByGeneratePaymentId(@Param("generatePaymentId") Long generatePaymentId);

}
