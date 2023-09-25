package modules.services;

import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import modules.repositories.GeneratePaymentRepository;
import modules.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Test
    void testGuardarPago(){   // también se prueba encontrarPagoPorId
        studentService.guardarEstudiante("987654321", "Alex", "Van",
                "2003/05/13", "privado", "Escuela 1", "2023");
        StudentEntity nuevoEstudiante = studentService.encontrarId((long) 1);
        generatePaymentService.guardarPago(nuevoEstudiante, 1, "contado");
        GeneratePaymentsEntity g = generatePaymentService.encontrarPagoPorId((long) 1);
        assertEquals(1, g.getNumeroCuota());
        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);
    }

}