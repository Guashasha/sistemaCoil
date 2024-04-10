package Logica.DAO;

import AccesoADatos.PaisDB;
import Logica.Bitacora;
import Logica.Dominio.Pais;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import Logica.Interfaces.IPaisDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DAOPais implements IPaisDAO {
    private static Bitacora bitacora = new Bitacora(Universidad.class.getName());

    @Override
    public List<Pais> paisesAlfabeticamente() throws ErrorDAO {
        try {
            return PaisDB.paisesAlfabeticamente();
        } catch (SQLException error) {
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
        }
    }

    @Override
    public Optional<Pais> getPaisPorNombre(String nombre) throws ErrorDAO {
        Pais pais = null;
        if (Optional.ofNullable(nombre).isPresent() && !nombre.isBlank()) {
            try {
                pais = PaisDB.getPaisPorNombre(nombre);
            } catch (SQLException error) {

            }
        }
        return Optional.ofNullable(pais);
    }

    @Override
    public Optional<Pais> getPaisPorId(int id) throws ErrorDAO {
        Pais pais = null;
        if (id > 0) {
            try {
                pais = PaisDB.getPaisPorId(id);
            } catch (SQLException error) {

            }
        }
        return Optional.ofNullable(pais);
    }
}
