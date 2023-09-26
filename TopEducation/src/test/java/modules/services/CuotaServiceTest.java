package modules.services;

import modules.entities.CuotaEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}