package modules;

import modules.entities.StudentEntity;
import modules.repositories.StudentRepository;
import modules.services.StudentService;
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
        studentRepository.save(nuevoEstudiante);
        assertNotNull(studentService.obtenerEstudiantes());
        studentRepository.delete(nuevoEstudiante);
    }


    @Test
    void guardarEstudiante(){
        studentRepository.deleteAll();
        studentService.guardarEstudiante("987654321", "Alex", "Van",
                LocalDate.of(2023, 5, 13), "privado", "Escuela 1", "2023");

        assertNotNull(studentRepository.findAll());
        studentRepository.deleteAll();
    }


    @Test
    void verificarRut(){
        studentService.guardarEstudiante("987654321", "Alex", "Van",
                LocalDate.of(2023, 5, 13), "privado", "Escuela 1", "2023");
        assertEquals("Rut repetido", studentService.verificarRut("987654321"));
        studentRepository.deleteAll();
    }

    @Test
    void encontrarId(){
        studentService.guardarEstudiante("987654321", "Alex", "Van",
                LocalDate.of(2023, 5, 13), "privado", "Escuela 1", "2023");

        StudentEntity s = studentService.encontrarRut("987654321");
        assertEquals("Alex", studentService.encontrarId(s.getId()).getNombre_estudiante());
        studentRepository.deleteAll();
    }


    @Test
    void encontrarRut(){
        studentService.guardarEstudiante("987654321", "Alex", "Van",
                LocalDate.of(2023, 5, 13), "privado", "Escuela 1", "2023");

        StudentEntity estudiante = studentService.encontrarRut("987654321");
        assertEquals("Alex", estudiante.getNombre_estudiante());
        studentRepository.deleteAll();
    }


}