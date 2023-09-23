package modules.services;

import modules.entities.StudentEntity;
import modules.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

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
                                  String fechaNacimiento, String tipoEscuela, String nombreEscuela, String anioEgreso){
        StudentEntity student = new StudentEntity();
        student.setRut(rut);
        student.setNombreEstudiante(nombreEstudiante);
        student.setApellidoEstudiante(apellidoEstudiante);
        student.setFechaNacimiento(fechaNacimiento);
        student.setTipoEscuela(tipoEscuela);
        student.setNombreEscuela(nombreEscuela);
        student.setAnioEgreso(anioEgreso);
        studentRepository.save(student);
    }


    // encontrar estudiante por rut
    public StudentEntity encontrarRut(String rut){
        return studentRepository.findByRut(rut);
    }



}
