package modules.services;

import modules.entities.CuotaEntity;
import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import modules.repositories.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CuotaService {

    @Autowired
    CuotaRepository cuotaRepository;

    // obtener cuota por su id
    public CuotaEntity obtenerCuotaPorId(Long id){
        return cuotaRepository.findCuotaEntitiesById(id);}

    // envia un array de cuotas por id de pago
    public ArrayList<CuotaEntity> obtenerCuotasPorGeneratePaymentId(Long id){
        return cuotaRepository.findCuotasByGeneratePaymentId(id);
    }

    // envia un array de cuotas pendientes por id de pago
    public ArrayList<CuotaEntity> obtenerCuotasPendientesPorGeneratePaymentId(Long id){
        return cuotaRepository.findCuotasPendientesByGeneratePaymentId(id);
    }

    // retorna cuotas por una array de pagos

    public ArrayList<CuotaEntity> obtenerCuotasPorGeneratePaymentArray(ArrayList<GeneratePaymentsEntity> pagos){
        ArrayList<CuotaEntity> c = new ArrayList<>();
        for (GeneratePaymentsEntity pago : pagos) {
            c.addAll(obtenerCuotasPorGeneratePaymentId(pago.getId()));
        }
        return c;
    }

    // retorna cuotas pendientes por un array de pagos
    public ArrayList<CuotaEntity> obtenerCuotasPendientesPorGeneratePaymentArray(ArrayList<GeneratePaymentsEntity> pagos){
        ArrayList<CuotaEntity> c = new ArrayList<>();
        for (GeneratePaymentsEntity pago : pagos) {
            c.addAll(obtenerCuotasPendientesPorGeneratePaymentId(pago.getId()));
        }
        return c;
    }



    // verifica si la cuota se puede pagar
    public String verificarPagarCuota(Long id){
        LocalDateTime fecha_Actual = LocalDateTime.now();
        CuotaEntity c = cuotaRepository.findCuotaEntitiesById(id);

        // verificar si el día esta entre el día 5 y 10
        if(fecha_Actual.getDayOfMonth() < 5 || fecha_Actual.getDayOfMonth() > 29){
            return "No se puede pagar la cuota, se puede pagar entre el día 5 y 10 de cada mes.";
        }

        // verificar si ha pagado la cuota anteriores antes de elegir pagar esta cuota
        // obtener todas las cuotas
        ArrayList<CuotaEntity> cuotas = obtenerCuotasPorGeneratePaymentId(c.getGeneratePaymentsEntity().getId());
        for(CuotaEntity cuota : cuotas) {
            if(cuota.getId().equals(c.getId())){ // es la misma cuota
                return "Se ha pagado la cuota con exito.";
            }else{
                if(!cuota.getEstadoCuota().equals("pagado")){
                    return "Las cuotas se debe pagar en orden, pague las cuotas pendientes anteriores";

                }
            }
        }
        return "Error al pagar la cuota.";
    }

    // cambia el estado de la cuota a un estado ya pagado
    public void pagarCuota(Long id){
        CuotaEntity c = cuotaRepository.findCuotaEntitiesById(id);
        c.setEstadoCuota("pagado");
        c.setValorCuota((float) 0);
        cuotaRepository.save(c);
    }


}
