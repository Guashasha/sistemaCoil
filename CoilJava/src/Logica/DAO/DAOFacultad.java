package Logica.DAO;

import AccesoADatos.FacultadDB;
import Logica.Dominio.Facultad;
import Logica.ErrorDAO;
import Logica.Interfaces.IFacultadDAO;
import java.util.List;
import java.util.Optional;

public class DAOFacultad implements IFacultadDAO {
    private final FacultadDB FACULTAD_DB = new FacultadDB();

    @Override
    public Optional<Facultad> getFacultadPorNombre(String nombre) throws ErrorDAO {
        return Optional.ofNullable(this.FACULTAD_DB.getFacultadPorNombre(nombre));
    }

    @Override
    public List<Facultad> getFacultadPorRegion(String region) throws ErrorDAO {
        return this.FACULTAD_DB.getFacultadPorRegion(region);
    }

    @Override
    public List<Facultad> getTodasAlfabeticamente() throws ErrorDAO {
        return this.FACULTAD_DB.getTodasAlfabeticamente();
    }
}
