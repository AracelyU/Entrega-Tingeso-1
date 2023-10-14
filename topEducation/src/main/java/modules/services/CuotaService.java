package modules.services;

import lombok.Generated;
import modules.entities.CuotaEntity;
import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import modules.repositories.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Service
public class CuotaService {

    @Autowired
    CuotaRepository cuotaRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    TestService testService;


    // guardar cuota
    public void guardarCuota(CuotaEntity c){
        cuotaRepository.save(c);
    }


    // generar el numero de cuotas para un estudiante
    public void generarCuotas(GeneratePaymentsEntity g){  // fecha de pago es null
        LocalDateTime fechaActual = LocalDateTime.now();
        if(g.getTipo_pago().equals("cuotas")) {
            for (int i = 1; i < g.getNumero_cuota() + 1; i++) {
                CuotaEntity c = new CuotaEntity();
                c.setEstado_cuota("pendiente");
                c.setNumero_cuota(i);
                c.setValor_cuota(g.getMonto_total_arancel());
                c.setPago(g);
                c.setFecha_vencimiento(fechaActual.plusMonths(i).withDayOfMonth(10).truncatedTo(ChronoUnit.DAYS)); // trunca a días
                cuotaRepository.save(c);
            }

        }else{  // para contado
            CuotaEntity p = new CuotaEntity();
            p.setValor_cuota(g.getMonto_total_arancel());
            p.setNumero_cuota(1);
            p.setFecha_vencimiento(fechaActual.plusMonths(1).withDayOfMonth(10).truncatedTo(ChronoUnit.DAYS));
            p.setEstado_cuota("pendiente");
            p.setPago(g);
            cuotaRepository.save(p);
        }
    }


    // a partir de un id de pago encuentra todas las cuotas
    public ArrayList<CuotaEntity> encontrarCuotasPorIdEstudiante(Long id_estudiante){
        return cuotaRepository.findCuotaEntitiesByIdStudent(id_estudiante);
    }

    // a partir de un id de pago encuentra todas las cuotas pendientes
    public ArrayList<CuotaEntity> encontrarCuotasPendientesPorIdEstudiante(Long id_estudiante){
        return cuotaRepository.findCuotaEntitiesPendientesByIdStudent(id_estudiante);
    }

    // verifica si la cuota se puede pagar
    public String verificarPagarCuota(Long id){
        LocalDateTime fecha_Actual = LocalDateTime.now();
        CuotaEntity c = cuotaRepository.findCuotaEntitiesById(id);

        // verificar si el día no esta entre el día 5 y 10
        if(fecha_Actual.getDayOfMonth() < 5 || fecha_Actual.getDayOfMonth() > 10){
            return "No se puede pagar la cuota, se puede pagar entre el día 5 y 10 de cada mes.";
        }

        // verificar si ha pagado la cuota anteriores antes de elegir pagar esta cuota
        ArrayList<CuotaEntity> cuotas = encontrarCuotasPorIdEstudiante(c.getPago().getEstudiante().getId());
        for(CuotaEntity cuota : cuotas) {
            if(cuota.getId().equals(c.getId())){ // es la misma cuota
                return "Se ha registrado el pago de la cuota con exito.";
            }else{
                if(!cuota.getEstado_cuota().equals("pagado")){
                    return "Las cuotas se debe pagar en orden, pague las cuotas pendientes anteriores";

                }
            }
        }
        return "Error al pagar la cuota.";
    }

    // cambia el estado de la cuota a un estado ya pagado
    public void pagarCuota(Long id){
        LocalDateTime fechaActual = LocalDateTime.now();
        CuotaEntity c = cuotaRepository.findCuotaEntitiesById(id);
        c.setEstado_cuota("pagado");
        c.setFecha_pago(fechaActual);
        c.getPago().setUltimo_pago(fechaActual);
        cuotaRepository.save(c);
    }

    // descuento por promedio de los ultimos examenes
    public Float descuentoPuntajePromedio(Float puntaje){
        if(puntaje < 850){
            return 0F;
        }else if (puntaje < 899){
            return 0.02F;
        }else if (puntaje < 949){
            return 0.05F;
        }else{
            return 0.1F;
        }
    }


    // numero de cuotas pagadas
    public Integer cuotasPagadasPorIdEstudiante(Long id_estudiante){
        return cuotaRepository.findCuotasPagadas(id_estudiante);
    }

    // saldo que aún queda por pagar
    public Float saldoPorPagar(Long id_estudiante){
        if(cuotaRepository.findSaldoPorPagar(id_estudiante) != null){
            return cuotaRepository.findSaldoPorPagar(id_estudiante);
        }else{
            return 0F;
        }
    }

    // saldo pagado
    public Float saldoPagado(Long id_estudiante){
        if(cuotaRepository.findSaldoPagado(id_estudiante) != null){
            return cuotaRepository.findSaldoPagado(id_estudiante);
        }else{
            return 0F;
        }
    }

    // obtener cuota por su id
    public CuotaEntity obtenerCuotaPorId(Long id){
        return cuotaRepository.findCuotaEntitiesById(id);
    }

    // descuento por atraso en cuotas
    public Float descuentoAtrasoCuotas(CuotaEntity c){
        LocalDateTime fecha_actual = LocalDateTime.now();
        int diferencia_anio = c.getFecha_vencimiento().getYear()- fecha_actual.getYear();

        if(diferencia_anio < 0){ // no se esta pagando dentro del año
            return 0.15F;
        }

        if(fecha_actual.isAfter(c.getFecha_vencimiento())){// la cuota esta vencida
            // obtener la diferencia de tiempo entre las fechas
            Period meses_diferencia = Period.between(c.getFecha_vencimiento().toLocalDate(), fecha_actual.toLocalDate());
            int meses = meses_diferencia.getMonths();
            if(meses == 0){
                return 0F;

            }else if(meses == 1){
                return 0.03F;

            } else if (meses == 2) {
                return 0.06F;

            } else if (meses == 3){
                return 0.09F;

            }else{
                return 0.15F;
            }
        }
        // sino esta vencida no hay porque aplicar descuento
        return 0F;
    }

    public void aplicarInteresAtrasoCuotas(Long id_estudiante){
        ArrayList<CuotaEntity> cuotas = cuotaRepository.findCuotaEntitiesPendientesByIdStudent(id_estudiante);
        for(CuotaEntity c : cuotas){
            Float monto = c.getValor_cuota() * (1 + descuentoAtrasoCuotas(c));
            c.setValor_cuota(monto);
            cuotaRepository.save(c);
        }
    }

    public Integer numeroCuotasAtrasadas(Long id_estudiante){
        ArrayList<CuotaEntity> cuotas = encontrarCuotasPendientesPorIdEstudiante(id_estudiante);
        Integer atrasos = 0;
        for(CuotaEntity c : cuotas){
            if(descuentoAtrasoCuotas(c) != 0F){ // si el valor era cero no tenía atrados
                atrasos++;
            }
        }
        return atrasos;
    }


}
