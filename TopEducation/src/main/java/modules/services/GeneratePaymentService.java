package modules.services;

import modules.entities.CuotaEntity;
import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import modules.repositories.CuotaRepository;
import modules.repositories.GeneratePaymentRepository;
import modules.repositories.StudentRepository;
import modules.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class GeneratePaymentService {

    @Autowired
    GeneratePaymentRepository generatePaymentRepository;

    @Autowired
    CuotaRepository cuotaRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TestRepository testRepository;


    // obtener todos los pagos
    public ArrayList<GeneratePaymentsEntity> obtenerPagos(){
        return (ArrayList<GeneratePaymentsEntity>) generatePaymentRepository.findAll();
    }

    // obtener las cuotas asociadas a una lista de pagos
    public ArrayList<CuotaEntity> obtenerCuotasPorListaPagos(ArrayList<GeneratePaymentsEntity> g){
        ArrayList<CuotaEntity> c = new ArrayList<>();
        for(int i=0; i < g.size(); i++){
            c.addAll(cuotaRepository.findCuotasByGeneratePaymentId(g.get(i).getId()));
        }
        return c;
    }

    public GeneratePaymentsEntity encontrarPagoPorId(Long id){ return generatePaymentRepository.findByid(id);}
    public ArrayList<GeneratePaymentsEntity> encontrarPagoPorStudentId(Long id){ return generatePaymentRepository.findByStudentId(id);}


    public String verificarGuardarPago(StudentEntity s, Integer numeroCuotas, String tipoPago){
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        if(numeroCuotas <= 0){
            return "El número de cuotas debe de ser positivo";
        }

        if(tipoPago.equals("contado") && numeroCuotas != 1){
            return "En pagos al contado no deben de hacer más de 1 cuota";
        }

        switch (s.getTipoEscuela()){
            case "municipal":
                if(numeroCuotas > 10){
                    return "Para tipo de escuela municipal se ingreso más de 10 cuotas";
                }
                break;

            case "subvencionado":
                if(numeroCuotas > 7){
                    return "Para tipo de escuela subvencionado se ingreso más de 7 cuotas";
                }
                break;

            case "privado":
                if(numeroCuotas > 4){
                    return "Para tipo de escuela privado se ingreso más de 4 cuotas";
                }
                break;

            default:
                return "Error en tipo de escuela";
        }

        return "El pago se generó con éxito.";
    }

    public void guardarPago(StudentEntity s, Integer numeroCuotas, String tipoPago){
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setStudent(s);
        g.setTipoPago(tipoPago);
        g.setNumeroCuota(numeroCuotas);
        g.setMatricula((float) 70000);
        g.setPagado(0F);  // comienza con cero
        if(g.getTipoPago().equals("contado")){
            g.setMontoPago((float) 750000);
        }else{
            g.setMontoPago(1500000 * (1 - descuentoAnioEgreso(g) - descuentoTipoEscuela(g))/ numeroCuotas);
        }
        generatePaymentRepository.save(g);
        generarCuotas(g);
    }

    // los descuentos por tipo de escuela
    private float descuentoTipoEscuela(GeneratePaymentsEntity g){
        StudentEntity s = g.getStudent();
        switch (s.getTipoEscuela()){
            case "municipal": return (float) 0.2;
            case "subvencionado": return (float) 0.1;
            case "privado": return 0;
            default: return -1;
        }
    }

    // los descuentos por año de egreso
    private float descuentoAnioEgreso(GeneratePaymentsEntity g){
        StudentEntity s = g.getStudent();
        int diferenciaAnios = Integer.parseInt("2023") - Integer.parseInt(s.getAnioEgreso());
        if(diferenciaAnios < 1){
            return (float) 0.15;
        }else if(diferenciaAnios <= 2){
            return (float) 0.08;
        }else if(diferenciaAnios <= 4){
            return (float) 0.04;
        }else{
            return 0;
        }
    }

    // generar el numero de cuotas para un estudiante /* todavia no se configura la fecha*/
    private void generarCuotas(GeneratePaymentsEntity g){
        LocalDateTime fechaActual = LocalDateTime.now();
        if(g.getTipoPago().equals("cuotas")) {
            for (int i = 1; i < g.getNumeroCuota() + 1; i++) {
                CuotaEntity c = new CuotaEntity();
                c.setEstadoCuota("pendiente");
                c.setNumeroCuota(i);
                c.setValorCuota(g.getMontoPago());
                c.setGeneratePaymentsEntity(g);
                c.setFechaVencimiento(fechaActual.plusMonths(i).withDayOfMonth(10));
                cuotaRepository.save(c);
            }

        }else{  // para contado
            CuotaEntity p = new CuotaEntity();
            p.setValorCuota(g.getMontoPago());
            p.setNumeroCuota(1);
            p.setFechaVencimiento(fechaActual.plusMonths(1).withDayOfMonth(10));
            p.setEstadoCuota("pendiente");
            p.setGeneratePaymentsEntity(g);
            cuotaRepository.save(p);
        }
    }

    public Float faltaPagar(GeneratePaymentsEntity g){
        ArrayList<CuotaEntity> c = cuotaRepository.findCuotasPendientesByGeneratePaymentId(g.getId());
        Float monto = (float) 0;
        for(CuotaEntity cuotas : c){
            monto+= cuotas.getValorCuota();  // lo que ya estan pagados suman 0
        }
        return monto;
    }

    public Float descuentoPuntajePromedio(Float puntaje){
        if(puntaje < 850){
            return (float) 0;
        }else if (puntaje < 899){
            return 0.02F;
        }else if (puntaje < 949){
            return 0.05F;
        }else{
            return 0.1F;
        }
    }


    public void aplicarDescuentoPrueba(){
        ArrayList<StudentEntity> estudiantes = (ArrayList<StudentEntity>) studentRepository.findAll();
        for(StudentEntity s : estudiantes){
            ArrayList<GeneratePaymentsEntity> pagos = generatePaymentRepository.findByStudentId(s.getId());
            for(GeneratePaymentsEntity g : pagos){
                ArrayList<CuotaEntity> cuotas = cuotaRepository.findCuotasPendientesByGeneratePaymentId(g.getId());
                Float puntaje = testRepository.findPuntajePromedio(s.getRut());

                for(CuotaEntity c : cuotas){
                    c.setValorCuota( c.getValorCuota() * (1 - descuentoPuntajePromedio(puntaje)));
                    cuotaRepository.save(c);
                }
            }
        }
    }




    // para generar reporte
    public void generarReporte(){



    }











}


