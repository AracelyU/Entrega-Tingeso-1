package modules.services;

import modules.entities.StudentEntity;
import modules.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


/* ACLARACIONES
* Para usar los test se recomienda tener las base de datos estudiante vacia
* */
@SpringBootTest
class StudentServiceTest {

    @Autowired
    StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Test
    void testObtenerEstudiantes(){
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
    void testGuardarEstudiante(){   // también se prueba encontrarRut
        studentService.guardarEstudiante("987654321", "Alex", "Van",
                LocalDate.of(2023, 5, 13), "privado", "Escuela 1", "2023");

        StudentEntity nuevoEstudiante = studentService.encontrarRut("987654321");
        assertEquals("987654321", nuevoEstudiante.getRut());
        studentRepository.delete(nuevoEstudiante);
    }

    @Test
    void testEncontrarId(){
        StudentEntity nuevoEstudiante = studentService.encontrarId((long) 1);
        assertEquals("municipal", nuevoEstudiante.getTipo_escuela());
    }


}