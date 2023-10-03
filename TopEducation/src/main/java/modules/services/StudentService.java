package modules.services;

import modules.entities.StudentEntity;
import modules.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    // obtener estudiantes
    public ArrayList<StudentEntity> obtenerEstudiantes(){
        return (ArrayList<StudentEntity>) studentRepository.findAll();
    }

    // guardar estudiante en BD
    public void guardarEstudiante(String rut, String nombreEstudiante, String apellidoEstudiante,
                                  LocalDate fechaNacimiento, String tipoEscuela, String nombreEscuela, String anioEgreso){
        StudentEntity student = new StudentEntity();
        student.setRut(rut);
        student.setNombre_estudiante(nombreEstudiante);
        student.setApellido_estudiante(apellidoEstudiante);
        student.setFecha_nacimiento(fechaNacimiento);
        student.setTipo_escuela(tipoEscuela);
        student.setNombre_escuela(nombreEscuela);
        student.setAnio_egreso(anioEgreso);
        studentRepository.save(student);
    }


    // encontrar estudiante por rut
    public StudentEntity encontrarRut(String rut){
        return studentRepository.findByRut(rut);
    }

    // encontrar estudiante por su id
    public StudentEntity encontrarId(Long id){ return studentRepository.findByid(id); }



}
