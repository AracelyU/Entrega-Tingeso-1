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
    public String generandoCuota(@RequestParam("opcion_pago") String opcionPago,
                                 @RequestParam("alumno") String rut,
                                 @RequestParam("numeroCuotas") Integer numeroCuotas,
                                 Model model){
        StudentEntity stu = studentService.encontrarRut(rut);
        int salvar = generatePaymentService.guardarPago(stu, numeroCuotas, opcionPago);

        System.out.println(salvar);

        if(salvar == 1){ // se creo bien el pago
            return "redirect:/generarCuota";
        }

        model.addAttribute("error", "Ha ocurrido un error al generar la cuota.");
        return "redirect:/generarCuota";
    }


}
