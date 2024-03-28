package Logica.DAO;

import AccesoADatos.FacultadDB;
import Logica.Bitacora;
import Logica.Dominio.Facultad;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import Logica.Interfaces.IFacultadDAO;
import java.util.List;
import java.util.Optional;

public class DAOFacultad implements IFacultadDAO {
    private final FacultadDB FACULTAD_DB = new FacultadDB();
    private static Bitacora bitacora = new Bitacora(Universidad.class.getName());

    @Override
    public Optional<Facultad> getFacultadPorNombre(String nombre) throws ErrorDAO {
        Facultad facultad;
        try {
             facultad = this.FACULTAD_DB.getFacultadPorNombre(nombre);
        }
        catch (ErrorDAO error) {
            bitacora.escribirError(error);
            throw error;
        }
        return Optional.ofNullable(facultad);
    }

    @Override
    public List<Facultad> getFacultadPorRegion(String region) throws ErrorDAO {
        try {
            return this.FACULTAD_DB.getFacultadPorRegion(region);
        } catch (ErrorDAO error) {
            bitacora.escribirError(error);
            throw error;
        }
    }

    @Override
    public List<Facultad> getTodasAlfabeticamente() throws ErrorDAO {
        try {
            return this.FACULTAD_DB.getTodasAlfabeticamente();
        } catch (ErrorDAO error) {
            bitacora.escribirError(error);
            throw error;
        }
    }
}
