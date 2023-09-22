package modules.controllers;

import modules.entities.StudentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import modules.services.StudentService;

import java.util.ArrayList;

@Controller
@RequestMapping
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/listar")
    public String listar(Model model){
        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        model.addAttribute("STUDENTS", estudiantes);
        return "index";
    }


    @GetMapping("/nuevoEstudiante")
    public String proveedor(){
        return "newStudent";
    }
    @PostMapping("/nuevoEstudiante")
    public String nuevoProveedor(@RequestParam("rut") String rut,
                                 @RequestParam("nombreEstudiante") String nombreEstudiante,
                                 @RequestParam("apellidoEstudiante") String apellidoEstudiante,
                                 @RequestParam("tipoEscuela") String tipoEscuela,
                                 @RequestParam("nombreEscuela") String nombreEscuela,
                                 @RequestParam("anioEgreso") String anioEgreso){

        studentService.guardarEstudiante(rut, nombreEstudiante, apellidoEstudiante,
                                        tipoEscuela,nombreEscuela, anioEgreso){
        return "redirect:/nuevoEstudiante";
    }





}
