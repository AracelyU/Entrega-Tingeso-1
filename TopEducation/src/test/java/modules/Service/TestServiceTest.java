package modules.Service;

import modules.entities.StudentEntity;
import modules.entities.TestEntity;
import modules.repositories.StudentRepository;
import modules.repositories.TestRepository;
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
        testRepository.deleteAll();
        testService.guardarDataDB("123", "2023/08/03", "932", "Quimica");
        assertNotNull(studentRepository.findAll());
        testRepository.deleteAll();
    }



    @Test
    void obtenerPromedio() {
        // creando estudiante
        StudentEntity nuevoEstudiante = new StudentEntity();
        nuevoEstudiante.setRut("987654321");
        nuevoEstudiante.setNombre_estudiante("Alex");
        nuevoEstudiante.setApellido_estudiante("Van");
        nuevoEstudiante.setFecha_nacimiento(LocalDate.of(2003, 5, 13));
        nuevoEstudiante.setTipo_escuela("privado");
        nuevoEstudiante.setNombre_escuela("Escuela 1");
        nuevoEstudiante.setAnio_egreso("2023");
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
        nuevoEstudiante.setNombre_estudiante("Alex");
        nuevoEstudiante.setApellido_estudiante("Van");
        nuevoEstudiante.setFecha_nacimiento(LocalDate.of(2003, 5, 13));
        nuevoEstudiante.setTipo_escuela("privado");
        nuevoEstudiante.setNombre_escuela("Escuela 1");
        nuevoEstudiante.setAnio_egreso("2023");
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