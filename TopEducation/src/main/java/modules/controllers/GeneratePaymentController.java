package modules.controllers;

import modules.entities.StudentEntity;
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
        int generoPago = generatePaymentService.guardarPago(stu, numeroCuotas, opcionPago);
        if(generoPago == 1){
            model.addAttribute("mensaje", "El pago se generó con éxito.");
        }else{
            model.addAttribute("error", "Ha ocurrido un error al generar el pago");
        }

        // Carga nuevamente la lista de estudiantes y retorna a la vista generarCuota
        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        model.addAttribute("students", estudiantes);
        return "redirect:/generarCuota";
    }


}
