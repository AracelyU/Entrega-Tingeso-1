package modules.controllers;

import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import modules.CuotaService;
import modules.GeneratePaymentService;
import modules.StudentService;
import modules.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class GeneratePaymentController {

    @Autowired
    private GeneratePaymentService generatePaymentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CuotaService cuotaService;


    @Autowired
    private TestService testService;

    @GetMapping("/generarPago")
    public String generarCuota(Model model) {
        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        model.addAttribute("students", estudiantes);
        return "generarPago";
    }

    @PostMapping("/generarPago")
    public String generandoCuota(@RequestParam("opcionPago") String opcionPago,
                                 @RequestParam("alumno") Long id,
                                 @RequestParam("numeroCuotas") Integer numeroCuotas,
                                 Model model) {
        String mensaje = generatePaymentService.verificarGuardarPago(id, numeroCuotas, opcionPago);
        if (mensaje.equals("El pago se generó con éxito.")) {
            generatePaymentService.guardarPago(id, numeroCuotas, opcionPago);
        }
        model.addAttribute("mensaje", mensaje);
        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        model.addAttribute("students", estudiantes);
        return "generarPago";
    }


    @GetMapping("/generarReporte")
    public String generandoReporte(Model model){
        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        model.addAttribute("students", estudiantes);
        return "generarReporte";

    }

    @PostMapping("/generarReporte")
    public String mostrandoReporte(@RequestParam("id_estudiante") Long id, Model model){
        GeneratePaymentsEntity g = generatePaymentService.obtenerPagoPorIdEstudiante(id);
        StudentEntity s = g.getEstudiante();
        Integer nro_pruebas = testService.numeroPruebas(s.getRut());
        Float puntaje_promedio = testService.obtenerPromedio(s.getRut());
        Integer cuotas_pagadas = cuotaService.cuotasPagadasPorIdEstudiante(s.getId());
        Float saldo_pagar = cuotaService.saldoPorPagar(s.getId());
        Float saldo_pagado = cuotaService.saldoPagado(s.getId());
        Float total_arancel = saldo_pagado + saldo_pagar;
        Integer cuotas_atraso = cuotaService.numeroCuotasAtrasadas(s.getId());

        // faltan la cantidad de cuotas por retraso

        model.addAttribute("pagos", g);
        model.addAttribute("nro_pruebas", nro_pruebas);
        model.addAttribute("puntaje_promedio", puntaje_promedio);
        model.addAttribute("cuotas_pagadas", cuotas_pagadas);
        model.addAttribute("saldo_pagar", saldo_pagar);
        model.addAttribute("saldo_pagado", saldo_pagado);
        model.addAttribute("total_arancel", total_arancel);
        model.addAttribute("cuotas_atraso", cuotas_atraso);

        return "mostrarReporte";
    }

    /*
    @PostMapping("/aplicarPuntaje")
    public String aplicandoPuntaje(){
        generatePaymentService.aplicarDescuentoPrueba();
        return "cargarCSV";
    }

     */


}