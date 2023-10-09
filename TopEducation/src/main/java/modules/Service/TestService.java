package modules.Service;

import lombok.Generated;
import modules.entities.TestEntity;
import modules.repositories.TestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class TestService {

    @Autowired
    TestRepository testRepository;

    private final Logger logg = LoggerFactory.getLogger(TestService.class);

    // la función guardar es para traer a la carpeta src los archivos seleccionados
    @Generated
    public String guardar(MultipartFile file){
        String filename = file.getOriginalFilename();
        if(!filename.toLowerCase().contains(".csv")){
            return "No ingreso un archivo csv";
        }
        if(filename != null){
            if(!file.isEmpty()){
                try{
                    byte [] bytes = file.getBytes();
                    Path path  = Paths.get(file.getOriginalFilename());
                    Files.write(path, bytes);
                    logg.info("Archivo guardado");
                }
                catch (IOException e){
                    logg.error("ERROR", e);
                }
            }
            return "Archivo guardado con exito!";
        }
        else{
            return "No se pudo guardar el archivo";
        }
    }

    // leer el csv ingresado y guardarlo en BD
    @Generated
    public String leerCsv(String direccion){
        String texto = "";
        BufferedReader bf = null;
        //testRepository.deleteAll();
        if(!direccion.toLowerCase().contains(".csv")){
            return "No ingreso un archivo csv";
        }
        try{
            bf = new BufferedReader(new FileReader(direccion));
            String temp = "";
            String bfRead;
            int count = 1;
            while((bfRead = bf.readLine()) != null){
                if (count == 1){
                    count = 0;
                }
                else{
                    guardarDataDB(bfRead.split(";")[0], bfRead.split(";")[1], bfRead.split(";")[2], direccion);
                    temp = temp + "\n" + bfRead;
                }
            }
            texto = temp;
            return "Archivo leido exitosamente";
        }catch(Exception e){
            return "No se encontro el archivo";
        }finally{
            if(bf != null){
                try{
                    bf.close();
                }catch(IOException e){
                    logg.error("ERROR", e);
                }
            }
        }
    }


    // guardar en BD lo obtenido del csv
    public void guardarDataDB(String rut, String fechaExamen, String puntajeObtenido, String direccion){
        TestEntity newData = new TestEntity();
        newData.setRut(rut);
        newData.setFecha_examen(fechaExamen);
        newData.setPuntaje_obtenido(Float.valueOf(puntajeObtenido));
        newData.setNombre_prueba(direccion.replaceAll("\\.csv$", "")); // se quita la extensión del string
        testRepository.save(newData);
    }


    // aplica promedio a los ultimos examenes de un estudiante
    public Float obtenerPromedio(String rut){
        return testRepository.findPuntajePromedio(rut);
    }

    // obtiene el numero de examenes que rindio un estudiante
    public int numeroPruebas(String rut){
        return testRepository.findNumeroPruebas(rut);
    }




}


