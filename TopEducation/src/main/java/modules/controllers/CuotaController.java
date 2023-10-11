package modules.controllers;

import modules.entities.CuotaEntity;
import modules.Service.CuotaService;
import modules.Service.GeneratePaymentService;
import modules.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
public class CuotaController {

    @Autowired
    CuotaService cuotaService;

    @GetMapping("/mostrarPago")
    public String monstrandoPago(){
        return "mostrarPago";
    }

    @PostMapping("/mostrarPago")
    public String mostrandoCuota(@RequestParam("id_estudiante") Long id, Model model){
        cuotaService.aplicarInteresAtrasoCuotas(id);  // se analiza si hay cuotas atrasadas
        ArrayList<CuotaEntity> cuotas = cuotaService.encontrarCuotasPorIdEstudiante(id);
        model.addAttribute("cuotas", cuotas);
        return "mostrarPago";
    }

    @PostMapping("/registrarPago")
    public String mostrarCuotasRegistrar(@RequestParam("id_estudiante") Long id, Model model){
        cuotaService.aplicarInteresAtrasoCuotas(id);  // se analiza si hay cuotas atrasadas
        ArrayList<CuotaEntity> cuotas = cuotaService.encontrarCuotasPendientesPorIdEstudiante(id);
        model.addAttribute("cuotas", cuotas);
        return "registrarPago";
    }

    @PostMapping("/pagarCuota")
    public String registrarCuota(@RequestParam("cuota_id") Long id, Model model){

        String mensaje = cuotaService.verificarPagarCuota(id);
        if (mensaje.equals("Se ha registrado el pago de la cuota con exito.")) {
            cuotaService.pagarCuota(id); // cambiar estado de cuota a pagado
            model.addAttribute("mensaje", mensaje);
        }else {
            model.addAttribute("error", mensaje);
        }

        // devolver información anterior
        Long id_estudiante = cuotaService.obtenerCuotaPorId(id).getPago().getEstudiante().getId();
        ArrayList<CuotaEntity> cuotas = cuotaService.encontrarCuotasPendientesPorIdEstudiante(id_estudiante);
        model.addAttribute("cuotas", cuotas);

        return "registrarPago";
    }

    @PostMapping("/aplicarDescuentoPromedio")
    public String aplicarDescuentoBD(Model model){

        // Verifica si la fecha actual no está entre el 5 y el 10 del mes
        int dayOfMonth = LocalDateTime.now().getDayOfMonth();
        if (dayOfMonth >= 5 && dayOfMonth <= 10) {
            model.addAttribute("error", "No se puede aplicar descuentos mientras se tramitan los pagos");
        }

        cuotaService.aplicarDescuentoPromedio();
        model.addAttribute("mensaje", "Se ha aplicado el descuento por puntaje a toda la BD");

        return "cargarCSV";
    }


}
