package pe.edu.pucp.salud360.control.services.servicesImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.salud360.control.dto.ReglasDeNegocioDTO;
import pe.edu.pucp.salud360.control.mappers.ReglasDeNegocioMapper;
import pe.edu.pucp.salud360.control.models.ReglasDeNegocio;
import pe.edu.pucp.salud360.control.repositories.ReglasDeNegocioRepository;
import pe.edu.pucp.salud360.control.services.ReglasDeNegocioService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReglasDeNegocioServiceImp implements ReglasDeNegocioService {

    @Autowired
    ReglasDeNegocioRepository reglasDeNegocioRepository;

    @Autowired
    ReglasDeNegocioMapper reglasDeNegocioMapper;

    @Override
    public ReglasDeNegocioDTO crearReglasDeNegocio(ReglasDeNegocioDTO reglasDTO) {
        ReglasDeNegocio reglasDeNegocio = reglasDeNegocioMapper.mapToModel(reglasDTO);
        return reglasDeNegocioMapper.mapToDTO(reglasDeNegocioRepository.save(reglasDeNegocio));
    }

    @Override
    public ReglasDeNegocioDTO actualizarReglasDeNegocio(Integer idReglas, ReglasDeNegocioDTO reglasDTO) {
        if(reglasDeNegocioRepository.findById(idReglas).isPresent()){
            ReglasDeNegocio reglas = reglasDeNegocioRepository.findById(idReglas).get();
            reglas.setMaxReservas(reglasDTO.getMaxReservas());
            reglas.setMaxCapacidad(reglasDTO.getMaxCapacidad());
            reglas.setMaxDiasSuspension(reglasDTO.getMaxDiasSuspension());
            reglas.setMaxTiempoCancelacion(reglasDTO.getMaxTiempoCancelacion());
            return reglasDeNegocioMapper.mapToDTO(reglasDeNegocioRepository.save(reglas));
        } else {
            return null;
        }
    }

    @Override
    public void eliminarReglasDeNegocio(Integer idReglas) {
        if(reglasDeNegocioRepository.findById(idReglas).isPresent()){
            reglasDeNegocioRepository.deleteById(idReglas);
        }
    }

    @Override
    public List<ReglasDeNegocioDTO> listarReglasDeNegocio() {
        List<ReglasDeNegocio> reglas = reglasDeNegocioRepository.findAll();
        if(!(reglas.isEmpty())){
            return reglas.stream().map(reglasDeNegocioMapper::mapToDTO).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public ReglasDeNegocioDTO buscarReglaDeNegocioPorId(Integer idReglas) {
        if(reglasDeNegocioRepository.findById(idReglas).isPresent()){
            ReglasDeNegocio reglasBuscadas = reglasDeNegocioRepository.findById(idReglas).get();
            return reglasDeNegocioMapper.mapToDTO(reglasBuscadas);
        } else {
            return null;
        }
    }
}
