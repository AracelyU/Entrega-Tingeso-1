package modules.services;

import lombok.Generated;
import modules.entities.CuotaEntity;
import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import modules.repositories.GeneratePaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

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

        // FALTA VER QUE SOLO SE PUEDAN GENERAR PAGOS ANTES DEL INICIO DE CLASE

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

        if(g.getTipo_pago().equals("contado")){
            g.setMonto_total_arancel(750000F);
        }else{
            g.setMonto_total_arancel(1500000 * (1 - descuentoAnioEgreso(g) - descuentoTipoEscuela(g)) / numeroCuotas);
        }
        generatePaymentRepository.save(g);
        cuotaService.generarCuotas(g);
    }


    // los descuentos por tipo de escuela
    public float descuentoTipoEscuela(GeneratePaymentsEntity g){
        StudentEntity s = g.getEstudiante();
        switch (s.getTipo_escuela()){
            case "municipal": return 0.2F;
            case "subvencionado": return 0.1F;
            case "privado": return 0F;
            default: return -1F;
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


    @Generated
    // aplicar descuento a todas las cuotas de los estudiantes
    public void aplicarDescuentoPromedio(){
        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        for(StudentEntity s : estudiantes){

            GeneratePaymentsEntity g = obtenerPagoPorIdEstudiante(s.getId());
            if(g != null){ // esto si el estudiante tiene un pago asociado

                ArrayList<CuotaEntity> cuotas = cuotaService.encontrarCuotasPendientesPorIdEstudiante(s.getId());

                // calcular promedio
                Float promedio = testService.obtenerPromedio(s.getRut());

                // si el estudiante tiene pago al contado aún no pagado se le añade lo obtenido al monto devuelto
                if (cuotas.get(0).getPago().getTipo_pago().equals("contado") && cuotas.get(0).getEstado_cuota().equals("pendiente")) {
                    CuotaEntity c_contado = cuotas.get(0);
                    Float monto_nuevo = c_contado.getValor_cuota() * (1 - cuotaService.descuentoPuntajePromedio(promedio)); // cuando hay que devolverle?
                    c_contado.getPago().setSaldo_devuelto(monto_nuevo);
                    cuotaService.guardarCuota(c_contado);
                }

                // si es en cuotas se aplica a todas las cuotas pendientes
                for (CuotaEntity c : cuotas) {
                    // aplicar descuento  a las cuotas
                    Float monto_nuevo = c.getValor_cuota() * (1 - cuotaService.descuentoPuntajePromedio(promedio));
                    c.setValor_cuota(monto_nuevo);
                    cuotaService.guardarCuota(c);
                }
            }
        }
    }


}


