package DAO;

import DTO.PaisDTO;
import Utilidades.ErrorDAO;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaisAuxiliar {
    private final static Logger BITACORA = Logger.getLogger(PaisAuxiliar.class);
    private final PaisDAO PAIS_DAO = new PaisDAO();

    public List<String> getNombresPaisesAlfabeticamente () throws ErrorDAO {
        List<PaisDTO> listaPaises;
        List<String> nombresPaises = new ArrayList<>();

        try {
            listaPaises = PAIS_DAO.getPaisesAlfabeticamente();
        } catch (SQLException error) {
            BITACORA.info(error.getMessage());
            throw new ErrorDAO("Error al establecer conexión con la base de datos", ErrorDAO.Tipo.CONEXION);
        }

        for (PaisDTO paisDTO : listaPaises) {
            nombresPaises.add(paisDTO.getNombre());
        }

        return nombresPaises;
    }

    public Optional<PaisDTO> getPaisPorNombre (String nombre) throws ErrorDAO {
        Optional<PaisDTO> paisDTOOptional;
        if (nombre != null && !nombre.isBlank()) {
            try {
                paisDTOOptional = PAIS_DAO.getPaisPorNombre(nombre.trim());
            } catch (SQLException error) {
                BITACORA.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }
        else {
            throw new ErrorDAO("Nombre vacío", ErrorDAO.Tipo.VALIDACION);
        }
        return paisDTOOptional;
    }

    public Optional<PaisDTO> getPaisPorId (int id) throws ErrorDAO {
        Optional<PaisDTO> paisDTOOptional;
        if (id > 0) {
            try {
                paisDTOOptional = PAIS_DAO.getPaisPorId(id);
            } catch (SQLException error) {
                BITACORA.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }
        else {
            throw new ErrorDAO("ID inválido", ErrorDAO.Tipo.VALIDACION);
        }
        return paisDTOOptional;
    }
}
