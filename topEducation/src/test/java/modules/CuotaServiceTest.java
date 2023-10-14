package modules;

import modules.entities.CuotaEntity;
import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import modules.repositories.CuotaRepository;
import modules.repositories.GeneratePaymentRepository;
import modules.repositories.StudentRepository;
import modules.services.CuotaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class CuotaServiceTest {

    @Autowired
    CuotaService cuotaService;

    @Autowired
    CuotaRepository cuotaRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    GeneratePaymentRepository generatePaymentRepository;


    @Test
    void testGuardarCuota(){
        StudentEntity s = new StudentEntity();
        s.setRut("1122334455");
        studentRepository.save(s);
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(s);
        generatePaymentRepository.save(g);
        CuotaEntity c = new CuotaEntity();
        c.setPago(g);
        cuotaService.guardarCuota(c);
        assertNotNull(cuotaRepository.findAll());
        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(s);
    }

    @Test
    void testGenerarCuota(){
        // creando estudiante
        StudentEntity s = new StudentEntity();
        s.setRut("120932401");
        studentRepository.save(s);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setTipo_pago("cuotas");
        g.setNumero_cuota(2);
        g.setEstudiante(s);
        g.setMonto_total_arancel(430000F);
        generatePaymentRepository.save(g);

        cuotaService.generarCuotas(g);
        assertNotNull(cuotaRepository.findAll());

        cuotaRepository.deleteAll();
        generatePaymentRepository.delete(g);
        studentRepository.delete(s);
    }


    @Test
    void testEncontrarCuotasPorIdEstudiante(){
        // creando estudiante
        StudentEntity s = new StudentEntity();
        s.setRut("987654320");
        studentRepository.save(s);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(s);
        generatePaymentRepository.save(g);

        // generando cuota
        CuotaEntity c = new CuotaEntity();
        c.setPago(g);
        cuotaRepository.save(c);

        ArrayList<CuotaEntity> cuotas = cuotaService.encontrarCuotasPorIdEstudiante(s.getId());
        assertNotNull(cuotas);

        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(s);

    }

    @Test
    void testEncontrarCuotasPendientesPorIdEstudiante(){
        // creando estudiante
        StudentEntity s = new StudentEntity();
        s.setRut("987654320");
        studentRepository.save(s);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(s);
        generatePaymentRepository.save(g);

        // generando cuota
        CuotaEntity c = new CuotaEntity();
        c.setPago(g);
        cuotaRepository.save(c);

        ArrayList<CuotaEntity> cuotas = cuotaService.encontrarCuotasPendientesPorIdEstudiante(s.getId());
        assertNotNull(cuotas);

        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(s);
    }

    @Test
    void testVerificarPagarCuota() {   // Este test esta muy influenciado por al fecha, puede fallar de la nada según la fecha
        // creando estudiante
        StudentEntity s = new StudentEntity();
        s.setRut("987654320");
        studentRepository.save(s);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(s);
        generatePaymentRepository.save(g);

        // generando cuota
        CuotaEntity c = new CuotaEntity();
        c.setPago(g);
        cuotaRepository.save(c);

        ArrayList<CuotaEntity> cuotas = cuotaService.encontrarCuotasPorIdEstudiante(s.getId());
        String mensaje = cuotaService.verificarPagarCuota(cuotas.get(0).getId());

        assertEquals("No se puede pagar la cuota, se puede pagar entre el día 5 y 10 de cada mes.", mensaje); // Asegura que la lista tenga elementos

        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(s);

    }

    @Test
    void testPagarCuota(){
        // creando estudiante
        StudentEntity s = new StudentEntity();
        s.setRut("987654321");
        studentRepository.save(s);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(s);
        generatePaymentRepository.save(g);

        // generando cuota
        CuotaEntity c = new CuotaEntity();
        c.setEstado_cuota("pendiente");
        c.setPago(g);
        cuotaRepository.save(c);

        cuotaService.pagarCuota(c.getId());
        c = cuotaService.obtenerCuotaPorId(c.getId());
        assertEquals("pagado", c.getEstado_cuota());

        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(s);

    }

    @Test
    void testDescuentoPuntajePromedio(){

        assertEquals(0.05F,cuotaService.descuentoPuntajePromedio(930F));
    }


    @Test
    void testCuotasPagadasPorIdEstudiante(){
        // creando estudiante
        StudentEntity s = new StudentEntity();
        s.setRut("987654321");
        studentRepository.save(s);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(s);
        generatePaymentRepository.save(g);

        // generando cuota
        CuotaEntity c = new CuotaEntity();
        c.setValor_cuota(1F);
        c.setEstado_cuota("pagado");  // hay una cuota pagada
        c.setPago(g);
        cuotaRepository.save(c);

        assertEquals(1, cuotaService.cuotasPagadasPorIdEstudiante(s.getId()));
        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(s);

    }

    @Test
    void testSaldoPorPagar(){
        // creando estudiante
        StudentEntity s = new StudentEntity();
        s.setRut("987654321");
        studentRepository.save(s);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(s);
        generatePaymentRepository.save(g);

        // generando cuota
        CuotaEntity c = new CuotaEntity();
        c.setValor_cuota(1F);
        c.setEstado_cuota("pendiente");
        c.setPago(g);
        cuotaRepository.save(c);

        assertEquals(1F, cuotaService.saldoPorPagar(s.getId()));

        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(s);
    }

    @Test
    void testSaldoPagado(){
        // creando estudiante
        StudentEntity s = new StudentEntity();
        s.setRut("987654321");
        studentRepository.save(s);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(s);
        generatePaymentRepository.save(g);

        // generando cuota
        CuotaEntity c = new CuotaEntity();
        c.setValor_cuota(1000000F);
        c.setEstado_cuota("pagado");
        c.setPago(g);
        cuotaRepository.save(c);

        assertEquals(1000000F, cuotaService.saldoPagado(g.getEstudiante().getId()));

        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(s);

    }


    @Test
    void testObtenerCuotaPorId(){

        // creando estudiante
        StudentEntity s = new StudentEntity();
        s.setRut("98765432C");
        studentRepository.save(s);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(s);
        generatePaymentRepository.save(g);

        CuotaEntity c = new CuotaEntity();
        c.setValor_cuota(1000000F);
        c.setPago(g);
        cuotaRepository.save(c);

        ArrayList<CuotaEntity> cuotas = cuotaService.encontrarCuotasPorIdEstudiante(s.getId());
        CuotaEntity c2 = cuotaService.obtenerCuotaPorId(cuotas.get(0).getId());

        assertEquals(1000000F, c2.getValor_cuota());

        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(s);

    }

    @Test
    void testDescuentoAtrasoCuotas(){

        // creando estudiante
        StudentEntity s = new StudentEntity();
        s.setRut("98765432C");
        studentRepository.save(s);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(s);
        generatePaymentRepository.save(g);

        CuotaEntity c = new CuotaEntity();
        c.setPago(g);
        c.setFecha_vencimiento(LocalDateTime.of(2021, 6, 10, 0, 0)); // vencio hace años
        cuotaRepository.save(c);

        ArrayList<CuotaEntity> cuotas = cuotaService.encontrarCuotasPorIdEstudiante(s.getId());
        CuotaEntity c2 = cuotas.get(0);

        assertEquals(0.015F,cuotaService.descuentoAtrasoCuotas(c2));

        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(s);

    }



    @Test
    void testSplicarInteresAtrasoCuotas(){

        // creando estudiante
        StudentEntity s = new StudentEntity();
        s.setRut("98765432C");
        studentRepository.save(s);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(s);
        generatePaymentRepository.save(g);

        CuotaEntity c = new CuotaEntity();
        c.setValor_cuota(1000000F);
        c.setEstado_cuota("pendiente");
        c.setPago(g);
        c.setFecha_vencimiento(LocalDateTime.of(2021, 6, 10, 0, 0)); // vencio hace años
        cuotaRepository.save(c);

        cuotaService.aplicarInteresAtrasoCuotas(s.getId());
        ArrayList<CuotaEntity> cuotas = cuotaService.encontrarCuotasPorIdEstudiante(s.getId());
        CuotaEntity c2 = cuotas.get(0);

        assertEquals(1015000F,c2.getValor_cuota());

        cuotaRepository.deleteAll();
        generatePaymentRepository.delete(g);
        studentRepository.delete(s);

    }


    @Test
    void testNumeroCuotasAtrasadas() {
        // creando estudiante
        StudentEntity s = new StudentEntity();
        s.setRut("000000AAAAA");
        studentRepository.save(s);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setEstudiante(s);
        generatePaymentRepository.save(g);

        // generando cuota
        CuotaEntity c = new CuotaEntity();
        c.setEstado_cuota("pendiente");
        LocalDateTime fecha = LocalDateTime.of(2021, 8, 3, 0, 0); // vencio hace años
        c.setFecha_vencimiento(fecha);
        c.setPago(g);
        cuotaRepository.save(c);

        assertEquals(1, cuotaService.numeroCuotasAtrasadas(s.getId()));

        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(s);

    }




}