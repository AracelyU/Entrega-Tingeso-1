package modules.Service;

import modules.entities.CuotaEntity;
import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import modules.repositories.CuotaRepository;
import modules.repositories.GeneratePaymentRepository;
import modules.repositories.StudentRepository;
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
    GeneratePaymentRepository generatePaymentRepository;

    @Autowired
    StudentRepository studentRepository;



    @Test
    void testGenerarCuota(){
        cuotaRepository.deleteAll();
        generatePaymentRepository.deleteAll();
        studentRepository.deleteAll();
        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("120932401");
        nuevoEstudiante.setFecha_nacimiento(LocalDate.of(2003, 5, 13));
        nuevoEstudiante.setTipo_escuela("privado");
        nuevoEstudiante.setNombre_escuela("Escuela 1");
        nuevoEstudiante.setAnio_egreso("2023");
        studentRepository.save(nuevoEstudiante);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setTipo_pago("cuotas");
        g.setNumero_cuota(2);
        g.setEstudiante(nuevoEstudiante);
        g.setMonto_total_arancel(430000F);
        generatePaymentRepository.save(g);

        cuotaService.generarCuotas(g);
        ArrayList<CuotaEntity> cuotas = cuotaRepository.findCuotaEntitiesByIdStudent(nuevoEstudiante.getId());
        assertNotNull(cuotas);

        cuotaRepository.deleteAll();
        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);

    }


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
        assertNotNull(cuotas);

        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);

    }

    @Test
    void testEncontrarCuotasPendientesPorIdEstudiante(){
        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("987654331B");
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
        assertNotNull(cuotas); // Asegura que la lista tenga elementos
        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);
    }

    @Test
    void testVerificarPagarCuota() {   // Este test esta muy influenciado por al fecha, puede fallar de la nada según la fecha
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
        String mensaje = cuotaService.verificarPagarCuota(cuotas.get(0).getId());

        assertEquals("No se puede pagar la cuota, se puede pagar entre el día 5 y 10 de cada mes.", mensaje); // Asegura que la lista tenga elementos

        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);

    }

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

    @Test
    void testSaldoPagado(){
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
        c.setValor_cuota(1000000F);
        c.setEstado_cuota("pagado");  // hay una cuota pagada
        c.setNumero_cuota(1);
        c.setPago(g);
        cuotaRepository.save(c);
        assertEquals(1000000F, cuotaService.saldoPagado(g.getEstudiante().getId()));
        cuotaRepository.delete(c);
        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);

    }


    @Test
    void testObtenerCuotaPorId(){

        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("98765432C");
        nuevoEstudiante.setFecha_nacimiento(LocalDate.of(2003, 5, 13));
        nuevoEstudiante.setTipo_escuela("privado");
        nuevoEstudiante.setAnio_egreso("2023");
        studentRepository.save(nuevoEstudiante);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setTipo_pago("cuotas");
        g.setNumero_cuota(1);
        g.setMonto_total_arancel(430000F);
        g.setEstudiante(nuevoEstudiante);
        generatePaymentRepository.save(g);

        CuotaEntity c = new CuotaEntity();
        c.setValor_cuota(1000000F);
        c.setEstado_cuota("pagado");  // hay una cuota pagada
        c.setNumero_cuota(1);
        c.setPago(g);
        cuotaRepository.save(c);

        ArrayList<CuotaEntity> cuotas = cuotaService.encontrarCuotasPorIdEstudiante(nuevoEstudiante.getId());
        CuotaEntity c2 = cuotaService.obtenerCuotaPorId(cuotas.get(0).getId());

        assertEquals(1000000F, c2.getValor_cuota());

        cuotaRepository.deleteAll();
        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);

    }

    @Test
    void testDescuentoAtrasoCuotas(){

        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("98765432C");
        nuevoEstudiante.setFecha_nacimiento(LocalDate.of(2003, 5, 13));
        nuevoEstudiante.setTipo_escuela("privado");
        nuevoEstudiante.setAnio_egreso("2023");
        studentRepository.save(nuevoEstudiante);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setTipo_pago("cuotas");
        g.setEstudiante(nuevoEstudiante);
        generatePaymentRepository.save(g);

        cuotaRepository.deleteAll();
        CuotaEntity c = new CuotaEntity();
        c.setValor_cuota(1000000F);
        c.setEstado_cuota("pendiente");  // hay una cuota pagada
        c.setNumero_cuota(1);
        c.setPago(g);
        c.setFecha_vencimiento(LocalDateTime.of(2021, 6, 10, 0, 0));
        cuotaRepository.save(c);

        ArrayList<CuotaEntity> cuotas = cuotaService.encontrarCuotasPorIdEstudiante(nuevoEstudiante.getId());
        CuotaEntity c2 = cuotas.get(0);

        assertEquals(0.015F,cuotaService.descuentoAtrasoCuotas(c2));

        cuotaRepository.deleteAll();
        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);

    }



    @Test
    void testSplicarInteresAtrasoCuotas(){

        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("98765432C");
        nuevoEstudiante.setFecha_nacimiento(LocalDate.of(2003, 5, 13));
        nuevoEstudiante.setTipo_escuela("privado");
        nuevoEstudiante.setAnio_egreso("2023");
        studentRepository.save(nuevoEstudiante);

        // generando pago
        GeneratePaymentsEntity g = new GeneratePaymentsEntity();
        g.setTipo_pago("cuotas");
        g.setEstudiante(nuevoEstudiante);
        generatePaymentRepository.save(g);

        CuotaEntity c = new CuotaEntity();
        c.setValor_cuota(1000000F);
        c.setEstado_cuota("pendiente");  // hay una cuota pagada
        c.setNumero_cuota(1);
        c.setPago(g);
        c.setFecha_vencimiento(LocalDateTime.of(2021, 6, 10, 0, 0));
        cuotaRepository.save(c);

        cuotaService.aplicarInteresAtrasoCuotas(nuevoEstudiante.getId());
        ArrayList<CuotaEntity> cuotas = cuotaService.encontrarCuotasPorIdEstudiante(nuevoEstudiante.getId());
        CuotaEntity c2 = cuotas.get(0);

        assertEquals(1015000F,c2.getValor_cuota());

        cuotaRepository.deleteAll();
        generatePaymentRepository.delete(g);
        studentRepository.delete(nuevoEstudiante);

    }


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
        c.setEstado_cuota("pendiente"); // se define una fecha tal que hay meses de diferencia
        LocalDateTime fecha = LocalDateTime.of(2023, 8, 3, 0, 0);
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