package modules;

import modules.entities.CuotaEntity;
import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import modules.entities.TestEntity;
import modules.repositories.CuotaRepository;
import modules.repositories.GeneratePaymentRepository;
import modules.repositories.StudentRepository;
import modules.repositories.TestRepository;
import modules.services.GeneratePaymentService;
import modules.services.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Autowired
    TestService testService;

    @Autowired
    TestRepository testRepository;


    @Test
    void verificarGuardarPago(){
        // creando estudiante
        StudentEntity s = new StudentEntity();
        s.setRut("987654321");
        s.setTipo_escuela("municipal");
        studentRepository.save(s);

        String mensaje = generatePaymentService.verificarGuardarPago(s.getId(), 2, "cuota");
        assertEquals("El pago se generó con éxito.",mensaje);

        studentRepository.delete(s);
    }


    @Test
    void guardarPago(){

        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("987654321");
        nuevoEstudiante.setTipo_escuela("municipal");
        nuevoEstudiante.setAnio_egreso("2022");
        studentRepository.save(nuevoEstudiante);

        generatePaymentService.guardarPago(nuevoEstudiante.getId(), 2, "cuota");
        GeneratePaymentsEntity pago = generatePaymentRepository.findByStudentId(nuevoEstudiante.getId());
        assertEquals(2, pago.getNumero_cuota());

        cuotaRepository.deleteAll();
        generatePaymentRepository.delete(pago);
        studentRepository.delete(nuevoEstudiante);
    }


    @Test
    void descuentoTipoEscuela(){
        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("987654321");
        nuevoEstudiante.setTipo_escuela("municipal");
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

    @Test
    void aplicarDescuentoPromedio(){

        // creando estudiantes
        StudentEntity s1 = new StudentEntity();
        s1.setRut("123");
        studentRepository.save(s1);

        StudentEntity s2 = new StudentEntity();
        s2.setRut("456");
        studentRepository.save(s2);

        // generando pagos
        GeneratePaymentsEntity g1 = new GeneratePaymentsEntity();
        g1.setTipo_pago("cuota");
        g1.setEstudiante(s1);
        generatePaymentRepository.save(g1);

        GeneratePaymentsEntity g2 = new GeneratePaymentsEntity();
        g2.setTipo_pago("cuota");
        g2.setEstudiante(s2);
        generatePaymentRepository.save(g2);

        // generando cuotas
        CuotaEntity c1 = new CuotaEntity();
        c1.setEstado_cuota("pendiente");
        c1.setValor_cuota(100F);
        c1.setPago(g1);
        cuotaRepository.save(c1);

        CuotaEntity c2 = new CuotaEntity();
        c2.setEstado_cuota("pendiente");
        c2.setValor_cuota(100F);
        c2.setPago(g2);
        cuotaRepository.save(c2);

        testService.guardarDataDB("123", "13-10-2023", "920", "lenguaje");
        testService.guardarDataDB("456", "13-10-2023", "900", "lenguaje");
        testService.guardarDataDB("123", "13-10-2023", "920", "fisica");
        testService.guardarDataDB("456", "13-10-2023", "900", "fisica");

        generatePaymentService.aplicarDescuentoPromedio();

        CuotaEntity nuevaCuota1 = cuotaRepository.findCuotaEntitiesById(c1.getId());
        CuotaEntity nuevaCuota2 = cuotaRepository.findCuotaEntitiesById(c2.getId());

        assertEquals(95F, nuevaCuota1.getValor_cuota());
        assertEquals(95F, nuevaCuota2.getValor_cuota());

        testRepository.deleteAll();
        cuotaRepository.deleteAll();
        generatePaymentRepository.deleteAll();
        studentRepository.deleteAll();



    }



}