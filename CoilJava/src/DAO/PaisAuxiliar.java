package DAO;

import DTO.PaisDTO;
import Utilidades.ErrorDAO;
import DAO.Interfaces.IPaisDAO;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaisAuxiliar implements IPaisDAO {
    private static Logger bitacora = Logger.getLogger(PaisAuxiliar.class);

    @Override
    public List<String> getNombresPaisesAlfabeticamente () throws ErrorDAO {
        List<PaisDTO> listaPaises;
        List<String> nombresPaises = new ArrayList<>();

        try {
            listaPaises = PaisDAO.paisesAlfabeticamente();
        } catch (SQLException error) {
            bitacora.info(error.getMessage());
            throw new ErrorDAO("Error al establecer conexión con la base de datos", ErrorDAO.Tipo.CONEXION);
        }

        for (PaisDTO paisDTO : listaPaises) {
            nombresPaises.add(paisDTO.getNombre());
        }

        return nombresPaises;
    }

    @Override
    public Optional<PaisDTO> getPaisPorNombre (String nombre) throws ErrorDAO {
        PaisDTO paisDTO = null;
        if (nombre != null && !nombre.isBlank()) {
            try {
                paisDTO = PaisDAO.getPaisPorNombre(nombre.trim());
            } catch (SQLException error) {
                bitacora.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }
        return Optional.ofNullable(paisDTO);
    }

    @Override
    public Optional<PaisDTO> getPaisPorId (int id) throws ErrorDAO {
        PaisDTO paisDTO = null;
        if (id > 0) {
            try {
                paisDTO = PaisDAO.getPaisPorId(id);
            } catch (SQLException error) {
                bitacora.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }
        return Optional.ofNullable(paisDTO);
    }
}
