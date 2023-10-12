package modules.services;

import modules.entities.StudentEntity;
import modules.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    // obtener estudiantes
    public ArrayList<StudentEntity> obtenerEstudiantes() {
        return (ArrayList<StudentEntity>) studentRepository.findAll();
    }

    // guardar estudiante en BD
    public void guardarEstudiante(String rut, String nombreEstudiante, String apellidoEstudiante,
                                  LocalDate fechaNacimiento, String tipoEscuela, String nombreEscuela, String anioEgreso) {
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

    public String verificarRut(String rut) {
        ArrayList<StudentEntity> student = obtenerEstudiantes();
        for (StudentEntity s : student) {

            if (s.getRut().equals(rut)) {
                return "Rut repetido";
            }

        }
        return "rut no repetido";
    }

    public StudentEntity encontrarId(Long id_estudiante){
        return studentRepository.findByid(id_estudiante);
    }

}
