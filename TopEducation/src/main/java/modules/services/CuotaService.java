package modules.services;

import modules.entities.CuotaEntity;
import modules.entities.StudentEntity;
import modules.repositories.CuotaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CuotaService {

    CuotaRepository cuotaRepository;

    public ArrayList<CuotaEntity> obtenerCuotasPorId(Long id){
        return cuotaRepository.findCuotaEntitiesById(id);
    }

}
