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

    public double generarPagoContado(GeneratePaymentsEntity g){
        if(g.getTipoPago().equals("Pago")){
            return 7500000;
        }
        return 0;
    }

    public double generarPagoCuotas(GeneratePaymentsEntity g){
        StudentEntity s = g.getStudent();

        // descuento por tipo de colegio
        if(s.getNombreEscuela().equals("Comuna")){
            return 1500000 * 0.8;
        }

        return 0;
    }



}
