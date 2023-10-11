package modules.controllers;

import modules.entities.StudentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import modules.Service.StudentService;

import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequestMapping
public class StudentController {

    @Autowired
    private StudentService studentService;

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
                                  @RequestParam("fechaNacimiento") LocalDate fechaNacimiento,
                                  @RequestParam("tipoEscuela") String tipoEscuela,
                                  @RequestParam("nombreEscuela") String nombreEscuela,
                                  @RequestParam("anioEgreso") String anioEgreso,
                                  Model model) {

        if(studentService.verificarRut(rut).equals("Rut repetido")){
            model.addAttribute("error", "El estudiante no se pudo registrar, el rut ya esta registrado.");
        }else{
            studentService.guardarEstudiante(rut, nombreEstudiante, apellidoEstudiante, fechaNacimiento, tipoEscuela, nombreEscuela, anioEgreso);
            model.addAttribute("mensaje", "El estudiante se registró con éxito.");
        }

        return "nuevoEstudiante";
    }


}