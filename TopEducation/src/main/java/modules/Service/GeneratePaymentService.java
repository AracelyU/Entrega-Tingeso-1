package modules.Service;

import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import modules.repositories.GeneratePaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GeneratePaymentService {

    @Autowired
    GeneratePaymentRepository generatePaymentRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    CuotaService cuotaService;

    @Autowired
    TestService testService;



    // verifica si se cumplen las condiciones para generar un pago
    public String verificarGuardarPago(Long id_estudiante, Integer numeroCuotas, String tipoPago){
        StudentEntity s = studentService.encontrarId(id_estudiante);

        GeneratePaymentsEntity g = generatePaymentRepository.findByStudentId(id_estudiante);
        if(g != null){
            return "Este estudiante ya tiene un pago registrado";
        }

        if(numeroCuotas <= 0){
            return "El número de cuotas debe de ser positivo";
        }

        if(tipoPago.equals("contado") && numeroCuotas != 1){
            return "En pagos al contado no deben de hacer más de 1 cuota";
        }

        switch (s.getTipo_escuela()){
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

    // genera un pago
    public void guardarPago(Long id_estudiante, Integer numeroCuotas, String tipoPago){
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();  // la ultima fecha de pago es null
        StudentEntity s = studentService.encontrarId(id_estudiante);
        g.setEstudiante(s);
        g.setTipo_pago(tipoPago);
        g.setNumero_cuota(numeroCuotas);
        g.setMatricula(70000F);
        g.setMonto_pagado(0F);  // comienza con cero

        if(g.getTipo_pago().equals("contado")){
            g.setMonto_total_aracel((float) 750000);
        }else{
            g.setMonto_total_aracel(1500000 * (1 - descuentoAnioEgreso(g) - descuentoTipoEscuela(g))/ numeroCuotas);
        }

        generatePaymentRepository.save(g);
        cuotaService.generarCuotas(g);
    }


    // los descuentos por tipo de escuela
    public float descuentoTipoEscuela(GeneratePaymentsEntity g){
        StudentEntity s = g.getEstudiante();
        switch (s.getTipo_escuela()){
            case "municipal": return (float) 0.2;
            case "subvencionado": return (float) 0.1;
            case "privado": return 0;
            default: return -1;
        }
    }

    // los descuentos por año de egreso
    public float descuentoAnioEgreso(GeneratePaymentsEntity g){
        StudentEntity s = g.getEstudiante();
        LocalDateTime fecha_Actual = LocalDateTime.now();
        int diferenciaAnios = fecha_Actual.getYear() - Integer.parseInt(s.getAnio_egreso());
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

    // obtener pago por id estudiante
    public GeneratePaymentsEntity obtenerPagoPorIdEstudiante(Long id_estudiante){
        return generatePaymentRepository.findByStudentId(id_estudiante);
    }


}


