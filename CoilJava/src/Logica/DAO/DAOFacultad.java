package Logica.DAO;

import AccesoADatos.FacultadDB;
import Logica.Dominio.Facultad;
import Utilidades.ErrorDAO;
import Logica.Interfaces.IFacultadDAO;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAOFacultad implements IFacultadDAO {
    private static Logger bitacora = Logger.getLogger(DAOFacultad.class);

    @Override
    public Optional<Facultad> getFacultadPorNombre(String nombre) throws ErrorDAO {
        Facultad facultad = null;

        if (cadenaValida(nombre)) {
            try {
                facultad = FacultadDB.getFacultadPorNombre(nombre);
            }
            catch (SQLException error) {
                bitacora.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }

        return Optional.ofNullable(facultad);
    }

    @Override
    public List<Facultad> getFacultadPorRegion(String region) throws ErrorDAO {
        List<Facultad> listaFacultades = new ArrayList<>();
        if ((cadenaValida(region))) {
            try {
                listaFacultades = FacultadDB.getFacultadPorRegion(region);
            } catch (SQLException error) {
                bitacora.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }
        return listaFacultades;
    }

    @Override
    public List<Facultad> getTodasAlfabeticamente() throws ErrorDAO {
        try {
            return FacultadDB.getTodasAlfabeticamente();
        } catch (SQLException error) {
            bitacora.info(error.getMessage());
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
        }
    }

    private boolean cadenaValida (String cadena) {
        return Optional.ofNullable(cadena).isPresent() && !cadena.isBlank();
    }
}
