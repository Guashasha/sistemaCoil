package DAO;

import DTO.PaisDTO;
import Utilidades.ErrorDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * La clase PaisAuxiliar funciona como intermediario entre el cliente y las clases DAO. Procesa y valida la información de los parámetros antes de mandarla o después de recibirla de las clases DAO.
 * @author pale
 */
public class PaisAuxiliar {
    private final PaisDAO PAIS_DAO = new PaisDAO();

    /**
     * Procesa todos los países que están registrados y obtiene una lista de los nombres ordenados de manera alfabética
     * @return Lista de los nombres de todos los países ordenados de manera alfabética.
     * @throws ErrorDAO si ocurre un error o durante el acceso a la base de datos.
     */
    public List<String> getNombresPaisesAlfabeticamente () throws ErrorDAO {
        List<PaisDTO> listaPaises = PAIS_DAO.getPaisesAlfabeticamente();
        List<String> nombresPaises = new ArrayList<>();

        for (PaisDTO paisDTO : listaPaises) {
            nombresPaises.add(paisDTO.getNombre());
        }

        return nombresPaises;
    }

    /**
     * Valida los parámetros para obtener un país de acuerdo a su nombre, utilizando la clase PaisDAO.
     * @param nombre Nombre del País a buscar.
     * @return Objeto Optional con el Pais inicializado con su id, iso y nombre; o un objeto Optional vacío si no se encuentran resultados.
     * @throws ErrorDAO si ocurre un error en la validación de los parámetros o durante el acceso a la base de datos.
     */
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
}
