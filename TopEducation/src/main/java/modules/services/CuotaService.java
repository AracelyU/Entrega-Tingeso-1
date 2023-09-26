package modules.services;

import modules.entities.CuotaEntity;
import modules.entities.StudentEntity;
import modules.repositories.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CuotaService {

    @Autowired
    CuotaRepository cuotaRepository;

    public ArrayList<CuotaEntity> obtenerCuotasPorGeneratePaymentId(Long id){
        return cuotaRepository.findCuotasByGeneratePaymentId(id);
    }

}
