package DAO;

import DTO.FacultadDTO;
import Utilidades.ErrorDAO;
import DAO.Interfaces.IFacultadDAO;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FacultadAuxiliar implements IFacultadDAO {
    private static Logger bitacora = Logger.getLogger(FacultadAuxiliar.class);

    @Override
    public Optional<FacultadDTO> getFacultadPorNombre (String nombre) throws ErrorDAO {
        FacultadDTO facultadDTO = null;

        if (cadenaValida(nombre)) {
            try {
                facultadDTO = FacultadDAO.getFacultadPorNombre(nombre.trim());
            }
            catch (SQLException error) {
                bitacora.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }

        return Optional.ofNullable(facultadDTO);
    }

    @Override
    public List<FacultadDTO> getFacultadPorRegion (String region) throws ErrorDAO {
        List<FacultadDTO> listaFacultades = new ArrayList<>();
        if (cadenaValida(region)) {
            try {
                listaFacultades = FacultadDAO.getFacultadPorRegion(region.trim());
            } catch (SQLException error) {
                bitacora.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }
        return listaFacultades;
    }

    @Override
    public List<FacultadDTO> getTodasAlfabeticamente () throws ErrorDAO {
        try {
            return FacultadDAO.getTodasAlfabeticamente();
        } catch (SQLException error) {
            bitacora.info(error.getMessage());
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
        }
    }

    private boolean cadenaValida (String cadena) {
        return cadena != null && !cadena.isBlank();
    }
}
