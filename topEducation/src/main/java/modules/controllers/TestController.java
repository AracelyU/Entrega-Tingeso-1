package modules.controllers;

import modules.services.GeneratePaymentService;
import modules.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;


@Controller
public class TestController{
        @Autowired
        private TestService testService;

        @Autowired
        private GeneratePaymentService generatePaymentService;

        @GetMapping("/cargarCSV")
        public String main(Model model) {
            model.addAttribute("mensaje", "Seleccione archivo a cargar");
            return "cargarCSV";
        }

        @PostMapping("/cargarCSV")
        public String upload(@RequestParam("file") MultipartFile file, Model model) {
            testService.guardar(file);
            String filename = file.getOriginalFilename();
            String mensaje = testService.leerCsv(filename);

            if(mensaje.equals("Archivo leido exitosamente")){
                model.addAttribute("mensaje", mensaje);
            }else{
                model.addAttribute("error", mensaje);
            }

            return "cargarCSV";
        }


    @PostMapping("/aplicarDescuentoPromedio")
    public String aplicarDescuentoBD(Model model){

        // Verifica si la fecha actual no está entre el 5 y el 10 del mes
        int dayOfMonth = LocalDateTime.now().getDayOfMonth();
        if (dayOfMonth >= 5 && dayOfMonth <= 10) {
            model.addAttribute("error2", "No se puede aplicar descuentos mientras se tramitan los pagos");
        }else {
            if (testService.obtenerTest().isEmpty()) {
                model.addAttribute("error2", "No hay ningún examen cargado, no se puede aplicar descuento");
            } else {
                generatePaymentService.aplicarDescuentoPromedio();
                model.addAttribute("mensaje2", "Se ha aplicado el descuento por puntaje a toda la BD");
            }
        }

        return "cargarCSV";
    }


}
