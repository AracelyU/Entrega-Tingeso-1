package modules.controllers;

import modules.entities.TestEntity;
import modules.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;


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
            //redirectAttributes.addFlashAttribute("mensaje", "¡Archivo cargado correctamente!");
            String mensaje = testService.leerCsv(filename);
            model.addAttribute("mensaje", mensaje);
            return "cargarCSV";
        }


        /*
        @GetMapping("/fileInformation")
        public String listar(Model model) {
            ArrayList<TestEntity> datas = testService.obtenerData();
            model.addAttribute("datas", datas);
            return "fileInformation";
        }

         */



}
