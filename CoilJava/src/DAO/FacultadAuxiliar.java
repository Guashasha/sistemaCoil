package DAO;

import DTO.FacultadDTO;
import DTO.RegionDTO;
import Utilidades.ErrorDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * La clase FacultadAuxiliar funciona como intermediario entre el cliente y las clases DAO. Procesa y valida la información de los parámetros antes de mandarla o después de recibirla de las clases DAO.
 * @author pale
 */
public class FacultadAuxiliar {
    /**
     *Instancia de la clase FacultadDAO que se utiliza en los métodos de la clase.
     */
    private final FacultadDAO FACULTAD_DAO = new FacultadDAO();

    /**
     * Valida los parámetros para obtener una facultad de acuerdo a su nombre, utilizando la clase FacultadDAO.
     * @param nombre Nombre de la facultad a buscar.
     * @return Objeto Optional con la facultad inicializada con su id, nombre e id de región; o un objeto Optional vacío si no se encuentran resultados.
     * @throws ErrorDAO si ocurre un error en la validación de los parámetros o durante el acceso a la base de datos.
     */
    public Optional<FacultadDTO> getFacultadPorNombre (String nombre) throws ErrorDAO {
        Optional<FacultadDTO> facultad = Optional.empty();
        FacultadDTO facultadABuscar = new FacultadDTO(nombre);

        if (facultadABuscar.nombreValido()) {
            facultad = FACULTAD_DAO.getFacultadPorNombre(nombre.trim());
        }

        return facultad;
    }

    /**
     * Valida los parámetros para obtener las facultades que están asociadas a una región específica.
     * @param region Nombre de la región asociada a las facultades
     * @return Lista de las facultades asociadas a la región especificada.
     * @throws ErrorDAO si ocurre un error en la validación de los parámetros o durante el acceso a la base de datos.
     */
    public List<FacultadDTO> getFacultadesPorRegion (String region) throws ErrorDAO {
        List<FacultadDTO> listaFacultades = new ArrayList<>();
        RegionDTO regionAsociada = new RegionDTO(region);

        if (regionAsociada.nombreValido()) {
            listaFacultades = FACULTAD_DAO.getFacultadPorRegion(region.trim());
        }
        return listaFacultades;
    }

}
