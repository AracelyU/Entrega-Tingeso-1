package modules.services;

import modules.entities.CuotaEntity;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CuotaServiceTest {

    @Autowired
    CuotaService cuotaService;




    @Test
    void testEncontrarCuotasPorIdEstudiante(){
        ArrayList<CuotaEntity> c = cuotaService.encontrarCuotasPorIdEstudiante((long) 1);
        assertTrue(c.isEmpty());

    }

    @Test
    void testEncontrarCuotasPendientesPorIdEstudiante(){
        ArrayList<CuotaEntity> c = cuotaService.encontrarCuotasPendientesPorIdEstudiante((long) 1);
        assertNotNull(c); // Asegura que la lista no sea nula
        //assertNotEquals(0, c.size()); // Asegura que la lista tenga elementos
        assertEquals(4, c.size());

    }

    /*

     @Test
    void testLocalDateTime() {

        // Obtener la fecha y hora actual
         LocalDateTime fechaActual = LocalDateTime.now();

         // Imprimir la fecha actual
         System.out.println("Fecha actual: " + fechaActual);

         // Calcular y imprimir los siguientes 7 meses
         for (int i = 1; i <= 13; i++) {
             LocalDateTime fechaSiguiente = fechaActual.plusMonths(i).withDayOfMonth(10);
             System.out.println("fecha siguiente: " + fechaSiguiente);
         }


     }

     */


}