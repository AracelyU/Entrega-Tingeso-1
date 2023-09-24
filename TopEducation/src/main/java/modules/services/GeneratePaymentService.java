package modules.services;

import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import modules.repositories.GeneratePaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneratePaymentService {

    @Autowired
    GeneratePaymentRepository generatePaymentRepository;
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

        System.out.println("El pago se realizo bien");

        g.setStudent(s);
        g.setTipoPago(tipoPago);
        g.setNumeroCuota(numeroCuotas);

        if(g.getTipoPago().equals("contado")){
            g.setMontoPago((float) (1500000 * (generarPagoContado(g))));
        }else{
            g.setMontoPago((float) (1500000 * (descuentoAnioEgreso(g) + descuentoTipoEscuela(g))));
        }

        generatePaymentRepository.save(g);
        return 1;
    }

    private double generarPagoContado(GeneratePaymentsEntity g){
        if(g.getTipoPago().equals("contado")){
            return 7500000;
        }
        return 0;
    }

    // los descuentos por tipo de escuela
    private double descuentoTipoEscuela(GeneratePaymentsEntity g){
        StudentEntity s = g.getStudent();
        switch (s.getTipoEscuela()){
            case "municipal": return 0.2;
            case "subvencionado": return 0.1;
            case "privado": return 0;
            default: return -1;
        }
    }

    // los descuentos por año de egreso
    private double descuentoAnioEgreso(GeneratePaymentsEntity g){
        StudentEntity s = g.getStudent();
        int diferenciaAnios = Integer.parseInt("2023") - Integer.parseInt(s.getAnioEgreso());
        if(diferenciaAnios < 1){
            return 0.15;
        }else if(diferenciaAnios <= 2){
            return 0.08;
        }else if(diferenciaAnios <= 4){
            return 0.04;
        }else{
            return 0;
        }
    }











}


