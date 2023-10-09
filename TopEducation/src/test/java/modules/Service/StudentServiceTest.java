package modules.Service;

import modules.entities.StudentEntity;
import modules.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentServiceTest {

    @Autowired
    StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Test
    void obtenerEstudiantes(){
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("987654321");
        nuevoEstudiante.setNombre_estudiante("Alex");
        nuevoEstudiante.setApellido_estudiante("Van");
        nuevoEstudiante.setFecha_nacimiento(LocalDate.of(2003, 5, 13));
        nuevoEstudiante.setTipo_escuela("privado");
        nuevoEstudiante.setNombre_escuela("Escuela 1");
        nuevoEstudiante.setAnio_egreso("2023");
        studentRepository.save(nuevoEstudiante);
        assertNotNull(studentService.obtenerEstudiantes());
        studentRepository.delete(nuevoEstudiante);
    }


    @Test
    void guardarEstudiante(){
        studentRepository.deleteAll();
        studentService.guardarEstudiante("987654321", "Alex", "Van",
                LocalDate.of(2023, 5, 13), "privado", "Escuela 1", "2023");

        ArrayList<StudentEntity> estudiantes = studentService.obtenerEstudiantes();
        assertNotNull(estudiantes);
        studentRepository.deleteAll();
    }


    @Test
    void encontrarId(){
        studentRepository.deleteAll();
        studentService.guardarEstudiante("987654321", "Alex", "Van",
                LocalDate.of(2023, 5, 13), "privado", "Escuela 1", "2023");

        StudentEntity estudiante = studentRepository.findByRut("987654321");
        assertEquals("Alex", studentService.encontrarId(estudiante.getId()).getNombre_estudiante());
        studentRepository.deleteAll();
    }


}