package Logica.DAO;

import AccesoADatos.PaisDB;
import Logica.Dominio.Pais;
import Logica.ErrorDAO;
import Logica.Interfaces.IPaisDAO;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAOPais implements IPaisDAO {
    private static Logger bitacora = Logger.getLogger(DAOPais.class);

    @Override
    public List<String> getNombresPaisesAlfabeticamente () throws ErrorDAO {
        List<Pais> listaPaises;
        List<String> nombresPaises = new ArrayList<>();

        try {
            listaPaises = PaisDB.paisesAlfabeticamente();
        } catch (SQLException error) {
            bitacora.info(error.getMessage());
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
        }

        for (Pais pais : listaPaises) {
            nombresPaises.add(pais.getNombre());
        }

        return nombresPaises;
    }

    @Override
    public Optional<Pais> getPaisPorNombre (String nombre) throws ErrorDAO {
        Pais pais = null;
        if (Optional.ofNullable(nombre).isPresent() && !nombre.isBlank()) {
            try {
                pais = PaisDB.getPaisPorNombre(nombre);
            } catch (SQLException error) {
                bitacora.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
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
                bitacora.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }
        return Optional.ofNullable(pais);
    }
}
