package modules.controllers;

import modules.entities.StudentEntity;
import modules.services.GeneratePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import modules.services.StudentService;

import java.util.ArrayList;

@Controller
@RequestMapping
public class StudentController {

    @Autowired
    private StudentService studentService;
    private GeneratePaymentService generatePaymentService;

    @GetMapping("/listaEstudiante")
    public String listar(Model model) {
        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        model.addAttribute("students", estudiantes);
        return "listaEstudiante";
    }

    @GetMapping("/nuevoEstudiante")
    public String estudiante() {
        return "nuevoEstudiante";
    }

    @PostMapping("/nuevoEstudiante")
    public String nuevoEstudiante(@RequestParam("rut") String rut,
                                  @RequestParam("nombreEstudiante") String nombreEstudiante,
                                  @RequestParam("apellidoEstudiante") String apellidoEstudiante,
                                  @RequestParam("fechaNacimiento") String fechaNacimiento,
                                  @RequestParam("tipoEscuela") String tipoEscuela,
                                  @RequestParam("nombreEscuela") String nombreEscuela,
                                  @RequestParam("anioEgreso") String anioEgreso) {
        studentService.guardarEstudiante(rut, nombreEstudiante, apellidoEstudiante, fechaNacimiento, tipoEscuela, nombreEscuela, anioEgreso);
        return "redirect:/nuevoEstudiante";
    }


}