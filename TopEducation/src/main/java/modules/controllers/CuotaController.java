package modules.controllers;

import modules.entities.CuotaEntity;
import modules.entities.GeneratePaymentsEntity;
import modules.services.CuotaService;
import modules.services.GeneratePaymentService;
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

    @GetMapping("/mostrarCuota")
    public String elegirPago(Model model){
        ArrayList<GeneratePaymentsEntity> pagos = generatePaymentService.obtenerPagos();
        model.addAttribute("pagos", pagos);
        return "mostrarCuota";
    }

    @PostMapping("/mostrarCuota")
    public String mostrandoCuota(@RequestParam("pago") Long id, Model model){
        ArrayList<CuotaEntity> cuotas = cuotaService.obtenerCuotasPorId(id);
        model.addAttribute("cuotas", cuotas);
        ArrayList<GeneratePaymentsEntity> pagos = generatePaymentService.obtenerPagos();
        model.addAttribute("pagos", pagos);
        System.out.println("2");
        return "mostrarCuota";
    }



}
