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
    void testObtenerCuotasPorId() {
        ArrayList<CuotaEntity> c = cuotaService.obtenerCuotasPorGeneratePaymentId((long) 1);
        for (CuotaEntity cuotaEntity : c) {
            assertEquals(1, cuotaEntity.getGeneratePaymentsEntity().getId());
        }

    }

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


}