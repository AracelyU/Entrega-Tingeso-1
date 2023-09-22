package modules.services;

import modules.entities.StudentEntity;
import modules.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    // obtener estudiantes
    public ArrayList<StudentEntity> obtenerEstudiantes(){
        return (ArrayList<StudentEntity>) studentRepository.findAll();
    }

    // guardar estudiante
    public void guardarEstudiante(String rut, String nombreEstudiante, String apellidoEstudiante,
                                  String tipoEscuela, String nombreEscuela, String anioEgreso){
        StudentEntity student = new StudentEntity();
        student.setRut(rut);
        student.setnombreEstudiante(nombreEstudiante);
        student.setnombreEstudiante(apellidoEstudiante);
        student.settipoEscuela(tipoEscuela);
        student.setnombreEscuela(nombreEscuela);
        student.setanioEgreso(anioEgreso);
        studentRepository.save(student);
    }






}
