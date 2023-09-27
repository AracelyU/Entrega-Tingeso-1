package modules.controllers;

import modules.entities.CuotaEntity;
import modules.entities.GeneratePaymentsEntity;
import modules.entities.StudentEntity;
import modules.services.CuotaService;
import modules.services.GeneratePaymentService;
import modules.services.StudentService;
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
                                 Model model){
        StudentEntity stu = studentService.encontrarId(id);
        String mensaje = generatePaymentService.verificarGuardarPago(stu, numeroCuotas, opcionPago);
        if(mensaje.equals("El pago se generó con éxito.")){
            generatePaymentService.guardarPago(stu, numeroCuotas, opcionPago);
        }
        model.addAttribute("mensaje", mensaje);
        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        model.addAttribute("students", estudiantes);
        return "generarCuota";
    }

    @GetMapping("/registrarPago")
    public String paginaRegistrarPago(Model model){
        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        model.addAttribute("estudiantes", estudiantes);
        return "registrarPago";
    }

    @PostMapping("/registrarPago")
    public String recibirParaRegistrarPago(@RequestParam("alumno") Long id, Model model){
        ArrayList<GeneratePaymentsEntity> pagos = generatePaymentService.encontrarPagoPorStudentId(id);
        ArrayList<CuotaEntity> cuotas = generatePaymentService.obtenerCuotasPorListaPagos(pagos);
        model.addAttribute("cuotas", cuotas);
        return "registrarCuota";
    }


}
