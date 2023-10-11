package modules.controllers;

import modules.Service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class TestController{
        @Autowired
        private TestService testService;

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


}
