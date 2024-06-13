package DAO;

import DTO.PaisDTO;
import Utilidades.ErrorDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaisAuxiliar {
    private final PaisDAO PAIS_DAO = new PaisDAO();

    public List<String> getNombresPaisesAlfabeticamente () throws ErrorDAO {
        List<PaisDTO> listaPaises = PAIS_DAO.getPaisesAlfabeticamente();
        List<String> nombresPaises = new ArrayList<>();

        for (PaisDTO paisDTO : listaPaises) {
            nombresPaises.add(paisDTO.getNombre());
        }

        return nombresPaises;
    }

    public Optional<PaisDTO> getPaisPorNombre (String nombre) throws ErrorDAO {
        Optional<PaisDTO> paisDTOOptional;
        PaisDTO paisAConsultar = new PaisDTO(nombre);

        if (paisAConsultar.nombreValido()) {
            paisDTOOptional = PAIS_DAO.getPaisPorNombre(nombre.trim());
        }
        else {
            throw new ErrorDAO("Los nombres no pueden contener caracteres especiales.\nSolo son válidas letras del alfabeto en español y guiones en el medio", ErrorDAO.Tipo.VALIDACION);
        }

        return paisDTOOptional;
    }

    public Optional<PaisDTO> getPaisPorId (int id) throws ErrorDAO {
        Optional<PaisDTO> paisDTOOptional;
        if (id > 0) {
            paisDTOOptional = PAIS_DAO.getPaisPorId(id);
        }
        else {
            throw new ErrorDAO("ID inválido. Debe ser mayor a 0", ErrorDAO.Tipo.VALIDACION);
        }
        return paisDTOOptional;
    }
}
