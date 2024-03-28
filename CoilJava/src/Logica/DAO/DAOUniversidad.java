package Logica.DAO;

import AccesoADatos.UniversidadDB;
import Logica.Bitacora;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import Logica.Interfaces.IUniversidadDAO;
import java.util.List;
import java.util.Optional;

public class DAOUniversidad implements IUniversidadDAO {
    private final UniversidadDB UNIVERSIDAD_DB = new UniversidadDB();
    private static Bitacora bitacora = new Bitacora(Universidad.class.getName());

    @Override
    public int registrarUniversidad(Universidad universidad) throws ErrorDAO {
        int filasAfectadas = 0;
        if (Optional.ofNullable(universidad).isPresent()) {
            try {
                filasAfectadas = this.UNIVERSIDAD_DB.registrarUniversidad(universidad);
            } catch (ErrorDAO error) {
                bitacora.escribirError(error);
                throw error;
            }
        }
        return filasAfectadas;
    }

    @Override
    public int editarUniversidad(Universidad universidad) throws ErrorDAO {
        int filasAfectadas = 0;
        if (Optional.ofNullable(universidad).isPresent()) {
            try {
                filasAfectadas = this.UNIVERSIDAD_DB.editarUniversidad(universidad);
            } catch (ErrorDAO error) {
                bitacora.escribirError(error);
                throw error;
            }
        }
        return filasAfectadas;
    }

    @Override
    public Optional<Universidad> getUniversidadPorNombre(String nombre) throws ErrorDAO {
        Universidad universidad;
        try {
            universidad = this.UNIVERSIDAD_DB.getUniversidadPorNombre(nombre);
        }
        catch (ErrorDAO error) {
            bitacora.escribirError(error);
            throw error;
        }
        return Optional.ofNullable(universidad);
    }

    @Override
    public List<Universidad> getUniversidadesPorPaisOrigen(String paisOrigen) throws ErrorDAO {
        try {
            return this.UNIVERSIDAD_DB.getUniversidadesPorPaisOrigen(paisOrigen);
        } catch (ErrorDAO error) {
            bitacora.escribirError(error);
            throw error;
        }
    }

    @Override
    public List<Universidad> getTodasAlfabeticamente() throws ErrorDAO {
        try {
            return this.UNIVERSIDAD_DB.getTodasAlfabeticamente();
        } catch (ErrorDAO error) {
            bitacora.escribirError(error);
            throw error;
        }
    }
}
