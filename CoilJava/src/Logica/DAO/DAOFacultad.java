package Logica.DAO;

import AccesoADatos.ConexionBaseDatos;
import Logica.Dominio.Facultad;
import Logica.ErrorDAO;
import Logica.Interfaces.IFacultadDAO;
import java.util.ArrayList;

public class DAOFacultad implements IFacultadDAO {
    private final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    @Override
    public Facultad getFacultadPorNombre(String nombre) throws ErrorDAO {
        return null;
    }

    @Override
    public ArrayList<Facultad> getFacultadPorRegion(String region) throws ErrorDAO {
        return null;
    }

    @Override
    public ArrayList<Facultad> getTodasAlfabeticamente() throws ErrorDAO {
        return null;
    }
}
