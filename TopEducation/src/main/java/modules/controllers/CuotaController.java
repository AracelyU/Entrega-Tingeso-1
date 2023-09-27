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
public class CuotaController {

    @Autowired
    CuotaService cuotaService;

    @Autowired
    GeneratePaymentService generatePaymentService;

    @Autowired
    StudentService studentService;

    @GetMapping("/mostrarCuota")
    public String elegirPago(Model model){
        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        ArrayList<GeneratePaymentsEntity> pagos = generatePaymentService.obtenerPagos();
        model.addAttribute("pagos", pagos);
        model.addAttribute("students", estudiantes);
        return "mostrarCuota";
    }

    @PostMapping("/mostrarCuota")
    public String mostrandoCuota(@RequestParam("id_estudiante") Long id, Model model){
        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        ArrayList<GeneratePaymentsEntity> pagos = generatePaymentService.encontrarPagoPorStudentId(id);
        ArrayList<CuotaEntity> cuotas = cuotaService.obtenerCuotasPorGeneratePaymentArray(pagos);
        model.addAttribute("cuotas", cuotas);
        model.addAttribute("students", estudiantes);
        return "mostrarCuota";
    }

    @PostMapping("/registrarCuota")
    public String registrarCuota(@RequestParam("cuota_id") Long id, Model model){

        // hacer condicionales para ver si la cuota se paga en el tiempo correcto

        // cambiar estado de cuota a pagado

        System.out.println(id);
        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        model.addAttribute("students", estudiantes);
        return "mostrarCuota";
    }

}
