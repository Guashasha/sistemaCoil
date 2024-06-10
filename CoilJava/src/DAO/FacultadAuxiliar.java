package DAO;

import DTO.FacultadDTO;
import Utilidades.ErrorDAO;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FacultadAuxiliar {
    private final Logger BITACORA = Logger.getLogger(FacultadAuxiliar.class);
    private final FacultadDAO FACULTAD_DAO = new FacultadDAO();

    public Optional<FacultadDTO> getFacultadPorNombre (String nombre) throws ErrorDAO {
        Optional<FacultadDTO> facultad = Optional.empty();

        if (cadenaValida(nombre)) {
            try {
                facultad = FACULTAD_DAO.getFacultadPorNombre(nombre.trim());
            }
            catch (SQLException error) {
                BITACORA.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }

        return facultad;
    }

    public List<FacultadDTO> getFacultadPorRegion (String region) throws ErrorDAO {
        List<FacultadDTO> listaFacultades = new ArrayList<>();
        if (cadenaValida(region)) {
            try {
                listaFacultades = FACULTAD_DAO.getFacultadPorRegion(region.trim());
            } catch (SQLException error) {
                BITACORA.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }
        return listaFacultades;
    }

    private boolean cadenaValida (String cadena) {
        return cadena != null && !cadena.isBlank();
    }
}
