package modules;

import modules.entities.StudentEntity;
import modules.entities.TestEntity;
import modules.repositories.StudentRepository;
import modules.repositories.TestRepository;
import modules.services.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestServiceTest {

    @Autowired
    TestService testService;

    @Autowired
    TestRepository testRepository;

    @Autowired
    StudentRepository studentRepository;


    @Test
    void guardarDataDB(){
        testService.guardarDataDB("123", "2023/08/03", "932", "Quimica");
        assertNotNull(testRepository.findAll());
        testRepository.deleteAll();
    }


    @Test
    void obtenerTest(){
        testService.guardarDataDB("123", "2023/08/03", "932", "Quimica");
        assertNotNull(testService.obtenerTest());
        testRepository.deleteAll();
    }


    @Test
    void obtenerPromedio() {
        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("987654321");
        studentRepository.save(nuevoEstudiante);

        // creando dos pruebas
        TestEntity prueba1 = new TestEntity();
        TestEntity prueba2 = new TestEntity();
        prueba1.setRut("987654321");
        prueba2.setRut("987654321");
        prueba1.setNombre_prueba("Matematicas");
        prueba2.setNombre_prueba("Lenguaje");
        prueba1.setFecha_examen("2023/10/04");
        prueba2.setFecha_examen("2023/10/04");
        prueba1.setPuntaje_obtenido(648F);
        prueba2.setPuntaje_obtenido(648F);
        testRepository.save(prueba1);
        testRepository.save(prueba2);
        assertEquals(648, testService.obtenerPromedio("987654321"));
        testRepository.delete(prueba1);
        testRepository.delete(prueba2);
        studentRepository.delete(nuevoEstudiante);

    }

    @Test
    void numeroPruebas(){
        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("987654321");
        studentRepository.save(nuevoEstudiante);

        // creando dos pruebas
        TestEntity prueba1 = new TestEntity();
        TestEntity prueba2 = new TestEntity();
        prueba1.setRut("987654321");
        prueba2.setRut("987654321");
        prueba1.setNombre_prueba("Matematicas");
        prueba2.setNombre_prueba("Lenguaje");
        prueba1.setFecha_examen("2023/10/04");
        prueba2.setFecha_examen("2023/10/04");
        prueba1.setPuntaje_obtenido(648F);
        prueba2.setPuntaje_obtenido(648F);
        testRepository.save(prueba1);
        testRepository.save(prueba2);

        assertEquals(2, testService.numeroPruebas("987654321"));
        testRepository.delete(prueba1);
        testRepository.delete(prueba2);
        studentRepository.delete(nuevoEstudiante);

    }


}