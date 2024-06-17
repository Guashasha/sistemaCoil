package DAO;

import DTO.FacultadDTO;
import DTO.RegionDTO;
import Utilidades.ErrorDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FacultadAuxiliar {
    private final FacultadDAO FACULTAD_DAO = new FacultadDAO();

    public Optional<FacultadDTO> getFacultadPorNombre (String nombre) throws ErrorDAO {
        Optional<FacultadDTO> facultad = Optional.empty();
        FacultadDTO facultadABuscar = new FacultadDTO(nombre);

        if (facultadABuscar.nombreValido()) {
            facultad = FACULTAD_DAO.getFacultadPorNombre(nombre.trim());
        }

        return facultad;
    }

    public List<FacultadDTO> getFacultadPorRegion (String region) throws ErrorDAO {
        List<FacultadDTO> listaFacultades = new ArrayList<>();
        RegionDTO regionAsociada = new RegionDTO(region);

        if (regionAsociada.nombreValido()) {
            listaFacultades = FACULTAD_DAO.getFacultadPorRegion(region.trim());
        }
        return listaFacultades;
    }

}
