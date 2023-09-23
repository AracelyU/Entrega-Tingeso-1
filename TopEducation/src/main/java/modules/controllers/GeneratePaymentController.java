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

    /*
    @GetMapping("/seleccionarEstudiante")
    public String seleccionarEstudiante(Model model) {
        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        model.addAttribute("students", estudiantes);
        return "seleccionarEstudiante";
    }

    */


    @GetMapping("/generarCuota")
    public String generarCuota(Model model) {
        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        model.addAttribute("students", estudiantes);
        return "generarCuota";
    }

    @PostMapping("/generarCuota")
    public String generandoCuota(@RequestParam("opcion_pago") String opcionPago,
                                 @RequestParam("alumno") String rut){


        StudentEntity stu = studentService.encontrarRut(rut);

        System.out.println(opcionPago);
        System.out.println(rut);
        System.out.println(stu.getFechaNacimiento());
        return "redirect:/generarCuota";
    }


}
