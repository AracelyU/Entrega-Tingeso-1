package modules.Service;

import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import modules.repositories.CuotaRepository;
import modules.repositories.GeneratePaymentRepository;
import modules.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GeneratePaymentServiceTest {

    @Autowired
    GeneratePaymentService generatePaymentService;

    @Autowired
    GeneratePaymentRepository generatePaymentRepository;

    @Autowired
    CuotaRepository cuotaRepository;

    @Autowired
    StudentRepository studentRepository;



    @Test
    void verificarGuardarPago(){
        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("987654321");
        nuevoEstudiante.setFecha_nacimiento(LocalDate.of(2003, 5, 13));
        nuevoEstudiante.setTipo_escuela("municipal");
        nuevoEstudiante.setNombre_escuela("Escuela 1");
        nuevoEstudiante.setAnio_egreso("2023");
        studentRepository.save(nuevoEstudiante); // no tiene un pago

        String mensaje = generatePaymentService.verificarGuardarPago(nuevoEstudiante.getId(), 4, "cuota");
        assertEquals("El pago se generó con éxito.",mensaje);

        studentRepository.delete(nuevoEstudiante);

    }


    @Test
    void guardarPago(){

        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("987654321");
        nuevoEstudiante.setFecha_nacimiento(LocalDate.of(2003, 5, 13));
        nuevoEstudiante.setTipo_escuela("municipal");
        nuevoEstudiante.setNombre_escuela("Escuela 1");
        nuevoEstudiante.setAnio_egreso("2023");
        studentRepository.save(nuevoEstudiante);

        generatePaymentService.guardarPago(nuevoEstudiante.getId(), 4, "cuota");
        GeneratePaymentsEntity pago = generatePaymentRepository.findByStudentId(nuevoEstudiante.getId());
        assertEquals(4, pago.getNumero_cuota());

        cuotaRepository.deleteAll();
        generatePaymentRepository.delete(pago);
        studentRepository.delete(nuevoEstudiante);
    }


    @Test
    void descuentoTipoEscuela(){
        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("987654321");
        nuevoEstudiante.setNombre_estudiante("Alex");
        nuevoEstudiante.setApellido_estudiante("Van");
        nuevoEstudiante.setFecha_nacimiento(LocalDate.of(2003, 5, 13));
        nuevoEstudiante.setTipo_escuela("municipal");
        nuevoEstudiante.setNombre_escuela("Escuela 1");
        nuevoEstudiante.setAnio_egreso("2023");
        studentRepository.save(nuevoEstudiante);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(nuevoEstudiante);
        generatePaymentRepository.save(g);

        assertEquals(0.2F, generatePaymentService.descuentoTipoEscuela(g));
        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);
    }

    @Test
    void descuentoAnioEgreso(){
        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("987654321");
        nuevoEstudiante.setNombre_estudiante("Alex");
        nuevoEstudiante.setApellido_estudiante("Van");
        nuevoEstudiante.setFecha_nacimiento(LocalDate.of(2003, 5, 13));
        nuevoEstudiante.setTipo_escuela("municipal");
        nuevoEstudiante.setNombre_escuela("Escuela 1");
        nuevoEstudiante.setAnio_egreso("2023");
        studentRepository.save(nuevoEstudiante);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(nuevoEstudiante);
        generatePaymentRepository.save(g);

        assertEquals(0.15F, generatePaymentService.descuentoAnioEgreso(g));
        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);
    }


    @Test
    void obtenerPagoPorIdEstudiante(){
        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("987654321");
        nuevoEstudiante.setNombre_estudiante("Alex");
        nuevoEstudiante.setApellido_estudiante("Van");
        nuevoEstudiante.setFecha_nacimiento(LocalDate.of(2003, 5, 13));
        nuevoEstudiante.setTipo_escuela("municipal");
        nuevoEstudiante.setNombre_escuela("Escuela 1");
        nuevoEstudiante.setAnio_egreso("2023");
        studentRepository.save(nuevoEstudiante);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(nuevoEstudiante);
        g.setTipo_pago("contado");
        generatePaymentRepository.save(g);

        GeneratePaymentsEntity pago = generatePaymentService.obtenerPagoPorIdEstudiante(nuevoEstudiante.getId());

        assertEquals("contado", pago.getTipo_pago());

        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);
    }



}