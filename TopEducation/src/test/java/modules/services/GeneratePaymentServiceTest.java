package modules.services;

import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import modules.repositories.GeneratePaymentRepository;
import modules.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/* ACLARACIONES
*  Se recomienda tener la tabla pagos y estudiante vacia
*
* */
@SpringBootTest
class GeneratePaymentServiceTest {

    @Autowired
    GeneratePaymentService generatePaymentService;

    @Autowired
    GeneratePaymentRepository generatePaymentRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    StudentRepository studentRepository;


/*

    @Test
    void testobtenerCuotasPorListaPagos(){
        ArrayList<GeneratePaymentsEntity> pagos = generatePaymentService.obtenerPagos();
        assertNotNull(generatePaymentService.obtenerCuotasPorListaPagos(pagos));
    }


 */





}