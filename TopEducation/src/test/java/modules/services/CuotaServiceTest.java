package modules.services;

import modules.entities.CuotaEntity;
import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import modules.repositories.CuotaRepository;
import modules.repositories.GeneratePaymentRepository;
import modules.repositories.StudentRepository;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CuotaServiceTest {

    @Autowired
    CuotaService cuotaService;

    @Autowired
    CuotaRepository cuotaRepository;

    @Autowired
    GeneratePaymentRepository generatePaymentRepository;

    @Autowired
    StudentRepository studentRepository;


    /* dudando en hacer este test
    @Test
    void testGenerarCuota(){

    }
     */


    @Test
    void testEncontrarCuotasPorIdEstudiante(){

        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("987654320");
        nuevoEstudiante.setNombre_estudiante("Pedro");
        nuevoEstudiante.setApellido_estudiante("Van");
        nuevoEstudiante.setFecha_nacimiento(LocalDate.of(2003, 5, 13));
        nuevoEstudiante.setTipo_escuela("privado");
        nuevoEstudiante.setNombre_escuela("Escuela 1");
        nuevoEstudiante.setAnio_egreso("2023");
        studentRepository.save(nuevoEstudiante);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setNumero_cuota(1);
        g.setEstudiante(nuevoEstudiante);
        generatePaymentRepository.save(g);

        // generando cuota
        CuotaEntity c = new CuotaEntity();
        c.setValor_cuota(1F);
        c.setEstado_cuota("pendiente");
        c.setNumero_cuota(1);
        c.setPago(g);
        cuotaRepository.save(c);

        ArrayList<CuotaEntity> cuotas = cuotaService.encontrarCuotasPorIdEstudiante(g.getEstudiante().getId());
        assertNotEquals(0, cuotas.size()); // Asegura que la lista tenga elementos

        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);

    }

    @Test
    void testEncontrarCuotasPendientesPorIdEstudiante(){
        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("987654321");
        nuevoEstudiante.setNombre_estudiante("Alex");
        nuevoEstudiante.setApellido_estudiante("Van");
        nuevoEstudiante.setFecha_nacimiento(LocalDate.of(2003, 5, 13));
        nuevoEstudiante.setTipo_escuela("privado");
        nuevoEstudiante.setNombre_escuela("Escuela 1");
        nuevoEstudiante.setAnio_egreso("2023");
        studentRepository.save(nuevoEstudiante);
        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(nuevoEstudiante);
        g.setNumero_cuota(1);
        g.setTipo_pago("contado");
        generatePaymentRepository.save(g);
        // generando cuota
        CuotaEntity c = new CuotaEntity();
        c.setValor_cuota(1F);
        c.setEstado_cuota("pendiente");
        c.setNumero_cuota(1);
        c.setPago(g);
        cuotaRepository.save(c);
        ArrayList<CuotaEntity> cuotas = cuotaService.encontrarCuotasPendientesPorIdEstudiante(g.getEstudiante().getId());
        assertNotEquals(0, cuotas.size()); // Asegura que la lista tenga elementos
        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);
    }

    /* dudando en hacer este test
    @Test
    void testVerificarPagarCuota(){

    }
     */


    @Test
    void testPagarCuota(){
        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("987654321");
        nuevoEstudiante.setNombre_estudiante("Alex");
        nuevoEstudiante.setApellido_estudiante("Van");
        nuevoEstudiante.setFecha_nacimiento(LocalDate.of(2003, 5, 13));
        nuevoEstudiante.setTipo_escuela("privado");
        nuevoEstudiante.setNombre_escuela("Escuela 1");
        nuevoEstudiante.setAnio_egreso("2023");
        studentRepository.save(nuevoEstudiante);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(nuevoEstudiante);
        generatePaymentRepository.save(g);

        // generando cuota
        CuotaEntity c = new CuotaEntity();
        c.setValor_cuota(1F);
        c.setEstado_cuota("pendiente");
        c.setPago(g);
        cuotaRepository.save(c);

        cuotaService.pagarCuota(c.getId());

        c = cuotaService.obtenerCuotaPorId(c.getId());

        assertEquals("pagado", c.getEstado_cuota());

        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);


    }

    @Test
    void testDescuentoPuntajePromedio(){
        assertEquals(0.05F,cuotaService.descuentoPuntajePromedio(930F));
    }


        /* dudando en hacer este test
    @Test
    void testAplicarDescuentoPromedio(){

    }
     */

    @Test
    void testCuotasPagadasPorIdEstudiante(){
        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("987654321");
        nuevoEstudiante.setNombre_estudiante("Alex");
        nuevoEstudiante.setApellido_estudiante("Van");
        nuevoEstudiante.setTipo_escuela("privado");
        nuevoEstudiante.setNombre_escuela("Escuela 1");
        nuevoEstudiante.setAnio_egreso("2023");
        studentRepository.save(nuevoEstudiante);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(nuevoEstudiante);
        generatePaymentRepository.save(g);

        // generando cuota
        CuotaEntity c = new CuotaEntity();
        c.setValor_cuota(1F);
        c.setEstado_cuota("pagado");  // hay una cuota pagada
        c.setPago(g);
        cuotaRepository.save(c);

        assertNotEquals(0, cuotaService.cuotasPagadasPorIdEstudiante(g.getEstudiante().getId())); // Asegura que la lista tenga elementos
        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);

    }

    @Test
    void testSaldoPorPagar(){
        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("987654321");
        nuevoEstudiante.setNombre_escuela("Escuela 1");
        nuevoEstudiante.setAnio_egreso("2023");
        studentRepository.save(nuevoEstudiante);
        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(nuevoEstudiante);
        generatePaymentRepository.save(g);
        // generando cuota
        CuotaEntity c = new CuotaEntity();
        c.setValor_cuota(1F);
        c.setEstado_cuota("pendiente");  // hay una cuota pagada
        c.setNumero_cuota(1);
        c.setPago(g);
        cuotaRepository.save(c);
        assertEquals(1F, cuotaService.saldoPorPagar(g.getEstudiante().getId()));
        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);
    }

    /* dudando en hacer este test
    @Test
    void testDescuentoAtrasoCuotas(){

    }
     */

    /* dudando en hacer este test
    @Test
    void testSplicarInteresAtrasoCuotas(){

    }
     */

    @Test
    void testNumeroCuotasAtrasadas(){
        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("000000AAAAA");
        studentRepository.save(nuevoEstudiante);
        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(nuevoEstudiante);
        generatePaymentRepository.save(g);
        // generando cuota
        CuotaEntity c = new CuotaEntity();
        c.setValor_cuota(1F);
        c.setEstado_cuota("pendiente");
        LocalDateTime fecha = LocalDateTime.of(2023, 10, 3, 0, 0);
        c.setFecha_vencimiento(fecha);
        c.setNumero_cuota(1);
        c.setPago(g);
        cuotaRepository.save(c);
        assertEquals(1, cuotaService.numeroCuotasAtrasadas(nuevoEstudiante.getId()));
        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);

    }








}