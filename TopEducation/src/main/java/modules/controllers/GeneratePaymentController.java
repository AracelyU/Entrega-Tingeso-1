package modules.controllers;

import modules.entities.CuotaEntity;
import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import modules.services.CuotaService;
import modules.services.GeneratePaymentService;
import modules.services.StudentService;
import modules.services.TestService;
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

    @GetMapping("/generarCuota")
    public String generarCuota(Model model) {
        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        model.addAttribute("students", estudiantes);
        return "generarCuota";
    }

    @PostMapping("/generarCuota")
    public String generandoCuota(@RequestParam("opcionPago") String opcionPago,
                                 @RequestParam("alumno") Long id,
                                 @RequestParam("numeroCuotas") Integer numeroCuotas,
                                 Model model) {
        StudentEntity stu = studentService.encontrarId(id);
        String mensaje = generatePaymentService.verificarGuardarPago(stu, numeroCuotas, opcionPago);
        if (mensaje.equals("El pago se generó con éxito.")) {
            generatePaymentService.guardarPago(stu, numeroCuotas, opcionPago);
        }
        model.addAttribute("mensaje", mensaje);
        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        model.addAttribute("students", estudiantes);
        return "generarCuota";
    }



    @GetMapping("/generarReporte")
    public String generandoReporte(Model model){
        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        model.addAttribute("students", estudiantes);
        return "generarReporte";

    }

    @PostMapping("/generarReporte")
    public String mostrandoReporte(@RequestParam("id_estudiante") Long id, Model model){
        StudentEntity s = studentService.encontrarId(id); // obtener estudiante
        Integer nro_examenes_rendidos = testService.numeroPruebas(s.getRut());
        Float puntaje_promedio = testService.obtenerTodoPromedio(s.getRut());

        Float falta_pagar; // falta hacer una función que vea al atributo pagado de pagos


        return "mostrarReporte";
    }

    @PostMapping("/aplicarPuntaje")
    public String aplicandoPuntaje(){
        generatePaymentService.aplicarDescuentoPrueba();
        return "cargarCSV";
    }


}