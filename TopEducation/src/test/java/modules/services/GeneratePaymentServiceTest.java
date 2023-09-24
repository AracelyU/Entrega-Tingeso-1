package modules.services;

import modules.entities.StudentEntity;
import modules.repositories.GeneratePaymentRepository;
import modules.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
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
    void guardarPago() {
        studentService.guardarEstudiante("987654321", "Alex", "Van",
                "2003/05/13", "privado", "Escuela 1", "2023");

        StudentEntity nuevoEstudiante = studentService.encontrarRut("987654321");
        int pagoCorrecto = generatePaymentService.guardarPago(nuevoEstudiante, 1, "contado");
        assertEquals(1, pagoCorrecto);
        studentRepository.delete(nuevoEstudiante);

    }
}