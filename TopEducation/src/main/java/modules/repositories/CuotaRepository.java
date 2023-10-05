package modules.repositories;

import modules.entities.CuotaEntity;
import modules.entities.StudentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface CuotaRepository extends CrudRepository<CuotaEntity, Long> {


    // encontrar cuotas por id_estudiante
    @Query("SELECT c FROM CuotaEntity c, GeneratePaymentsEntity g WHERE g.estudiante.id = :id_estudiante AND c.pago.id = g.id")
    ArrayList<CuotaEntity> findCuotaEntitiesByIdStudent(@Param("id_estudiante") Long id_estudiante);


    // encontrar cuotas pendientes por id_estudiante
    @Query("SELECT c FROM CuotaEntity c, GeneratePaymentsEntity g WHERE g.estudiante.id = :id_estudiante AND c.pago.id = g.id AND c.estado_cuota = 'pendiente'")
    ArrayList<CuotaEntity> findCuotaEntitiesPendientesByIdStudent(@Param("id_estudiante") Long id_estudiante);

    // numero de cuotas pagadas
    @Query("SELECT COUNT(*) FROM CuotaEntity c WHERE c.estado_cuota = 'pagado' AND c.pago.estudiante.id = :id_estudiante")
    Integer findCuotasPagadas(@Param("id_estudiante") Long id_estudiante);

    // monto pagado
    @Query("SELECT SUM(c.valor_cuota) FROM CuotaEntity c WHERE c.estado_cuota = 'pagado' AND c.pago.estudiante.id = :id_estudiante")
    Float findSaldoPagado(@Param("id_estudiante") Long id_estudiante);

    // monto total que aún queda por pagar
    @Query("SELECT SUM(c.valor_cuota) FROM CuotaEntity c WHERE c.estado_cuota = 'pendiente' AND c.pago.estudiante.id = :id_estudiante")
    Float findSaldoPorPagar(@Param("id_estudiante") Long id_estudiante);

    //--------

    // encontrar cuota por id_cuota
    CuotaEntity findCuotaEntitiesById(Long id);

    // seleccionar cuotas por id de pago
    @Query("SELECT c FROM CuotaEntity c WHERE c.pago.id = :generatePaymentId")
    ArrayList<CuotaEntity> findCuotasByGeneratePaymentId(@Param("generatePaymentId") Long generatePaymentId);

    // seleccionar cuotas por id de pago y que esten pendientes
    @Query("SELECT c FROM CuotaEntity c WHERE c.estado_cuota = 'pendiente' AND c.pago.id = :generatePaymentId")
    ArrayList<CuotaEntity> findCuotasPendientesByGeneratePaymentId(@Param("generatePaymentId") Long generatePaymentId);

    // seleccionar el ultimo pago (por revisar)
    @Query("SELECT MAX(c.fecha_pago) FROM CuotaEntity c")
    LocalDateTime findLastPayment();

}
