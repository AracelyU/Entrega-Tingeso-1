package modules.services;

import modules.repositories.TestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestServiceTest {

    @Autowired
    TestRepository testRepository;

    @Test
    void testObtenerPromedio() {
        assertEquals(648, testRepository.findPuntajePromedio("21.090.969-2"));

    }

    @Test
    void testNumeroPruebas(){
        assertEquals(3, testRepository.findNumeroPruebas("21.090.969-2"));

    }
}