package Logica.DAO;

import AccesoADatos.PaisDB;
import Logica.Bitacora;
import Logica.Dominio.Pais;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import Logica.Interfaces.IPaisDAO;

import java.util.List;
import java.util.Optional;

public class DAOPais implements IPaisDAO {
    private static final PaisDB PAIS_DB = new PaisDB();
    private static Bitacora bitacora = new Bitacora(Universidad.class.getName());

    @Override
    public List<Pais> paisesAlfabeticamente() throws ErrorDAO {
        try {
            return PAIS_DB.paisesAlfabeticamente();
        } catch (ErrorDAO error) {
            bitacora.escribirError(error);
            throw error;
        }
    }

    @Override
    public Optional<Pais> getPaisPorNombre(String nombre) throws ErrorDAO {
        Pais pais = null;
        if (Optional.ofNullable(nombre).isPresent() && !nombre.isBlank()) {
            try {
                pais = PAIS_DB.getPaisPorNombre(nombre);
            } catch (ErrorDAO error) {
                bitacora.escribirError(error);
                throw error;
            }
        }
        return Optional.ofNullable(pais);
    }

    @Override
    public Optional<Pais> getPaisPorId(int id) throws ErrorDAO {
        Pais pais = null;
        if (id > 0) {
            try {
                pais = PAIS_DB.getPaisPorId(id);
            } catch (ErrorDAO error) {
                bitacora.escribirError(error);
                throw error;
            }
        }
        return Optional.ofNullable(pais);
    }
}
