package Logica.DAO;

import AccesoADatos.FacultadDB;
import Logica.Bitacora;
import Logica.Dominio.Facultad;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import Logica.Interfaces.IFacultadDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAOFacultad implements IFacultadDAO {
    private static Bitacora bitacora = new Bitacora(Universidad.class.getName());

    @Override
    public Optional<Facultad> getFacultadPorNombre(String nombre) throws ErrorDAO {
        Facultad facultad = null;

        if (cadenaValida(nombre)) {
            try {
                facultad = FacultadDB.getFacultadPorNombre(nombre);
            }
            catch (SQLException error) {

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

            }
        }
        return listaFacultades;
    }

    @Override
    public List<Facultad> getTodasAlfabeticamente() throws ErrorDAO {
        try {
            return FacultadDB.getTodasAlfabeticamente();
        } catch (SQLException error) {
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
        }
    }

    private boolean cadenaValida (String cadena) {
        return Optional.ofNullable(cadena).isPresent() && !cadena.isBlank();
    }
}
