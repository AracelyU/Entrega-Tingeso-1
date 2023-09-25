package modules.services;

import modules.entities.CuotaEntity;
import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import modules.repositories.CuotaRepository;
import modules.repositories.GeneratePaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GeneratePaymentService {

    @Autowired
    GeneratePaymentRepository generatePaymentRepository;

    @Autowired
    CuotaRepository cuotaRepository;

    // obtener estudiantes
    public ArrayList<GeneratePaymentsEntity> obtenerPagos(){
        return (ArrayList<GeneratePaymentsEntity>) generatePaymentRepository.findAll();
    }

    public GeneratePaymentsEntity encontrarPagoPorId(Long id){ return generatePaymentRepository.findByid(id);}

    public int guardarPago(StudentEntity s, Integer numeroCuotas, String tipoPago){
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        if(numeroCuotas <= 0){
            System.out.println("El numero de cuotas es nulo o negativo");
            return -1;
        }
        switch (tipoPago) {
            case "contado":
                if(numeroCuotas != 1){
                    System.out.println("El numero de cuotas para pago contado es distinto de 1");
                    return -1;
                }
                break;
            case "cuotas":
                switch (s.getTipoEscuela()) {
                    case "municipal":
                        if(numeroCuotas > 10){
                            System.out.println("Para tipo de escuela municipal se ingreso más de 10 cuotas");
                            return -1;
                        }
                        break;
                    case "subvencionado":
                        if (numeroCuotas > 7){
                            System.out.println("Para tipo de escuela subvencionado se ingreso más de 7 cuotas");
                            return -1;
                        }
                        break;
                    case "privado":
                        if (numeroCuotas > 4){
                            System.out.println("Para tipo de escuela privado se ingreso más de 4 cuotas");
                            return -1;
                        }
                        break;
                    default:
                        System.out.println("Error en tipo de escuela");
                        return -1;
                }
                break;
            default:
                System.out.println("Error en tipo de pago");
                return -1;
        }
        g.setStudent(s);
        g.setTipoPago(tipoPago);
        g.setNumeroCuota(numeroCuotas);
        g.setMatricula((float) 70000);
        if(g.getTipoPago().equals("contado")){
            g.setMontoPago((float) 750000);
        }else{
            g.setMontoPago((float) (1500000 * (1 - descuentoAnioEgreso(g) + descuentoTipoEscuela(g))));
        }
        generatePaymentRepository.save(g);
        generarCuotas(g);
        return 1;
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
        if(g.getTipoPago().equals("cuotas")) {
            for (int i = 1; i < g.getNumeroCuota() + 1; i++) {
                CuotaEntity c = new CuotaEntity();
                c.setEstadoCuota("pendiente");
                c.setNumeroCuota(i);
                c.setValorCuota(g.getMontoPago());
                c.setGeneratePaymentsEntity(g);
                cuotaRepository.save(c);
            }
        }else{
            CuotaEntity p = new CuotaEntity();
            p.setValorCuota(g.getMontoPago());
            p.setNumeroCuota(1);
            p.setEstadoCuota("pendiente");
            p.setGeneratePaymentsEntity(g);
            cuotaRepository.save(p);
        }
    }











}


