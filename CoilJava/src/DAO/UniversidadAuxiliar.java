package DAO;

import DTO.PaisDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UniversidadAuxiliar {
    private final static Logger BITACORA = Logger.getLogger(UniversidadAuxiliar.class);
    private final UniversidadDAO UNIVERSIDAD_DAO = new UniversidadDAO();
    private final PaisDAO PAIS_DAO = new PaisDAO();

    public int registrarUniversidad (UniversidadDTO universidad, PaisDTO pais) throws ErrorDAO {
        if (esNulo(universidad) || esNulo(pais)) {
            throw new ErrorDAO("Algo salió mal, inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }
        int filasAfectadas;

        if (universidad.nombreValido() && pais.nombreValido()) {
            String nombreUniversidad = universidad.getNombre()
                    .trim();
            String nombrePais = pais.getNombre()
                    .trim();

            if (universidadExiste(nombreUniversidad,nombrePais)) {
                throw new ErrorDAO("La universidad que intentas registrar ya ha sido registrada anteriormente", ErrorDAO.Tipo.DUPLICIDAD);
            }
            else {
                try {
                    filasAfectadas = UNIVERSIDAD_DAO.registrarUniversidad(prepararUniversidadNueva(nombreUniversidad,nombrePais));
                }
                catch (SQLException error) {
                    BITACORA.info(error.getMessage());
                    throw new ErrorDAO("Error al establecer conexión con la base de datos", ErrorDAO.Tipo.CONEXION);
                }
            }
        }
        else {
            throw new ErrorDAO("Campos vacíos", ErrorDAO.Tipo.VALIDACION);
        }

        return filasAfectadas;
    }

    public int editarUniversidad (UniversidadDTO universidadActual, UniversidadDTO nuevaUniversidad, PaisDTO nuevoPais) throws ErrorDAO {
        if (esNulo(universidadActual) || esNulo(nuevaUniversidad) || esNulo(nuevoPais)) {
            throw new ErrorDAO("Algo salió mal, inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }
        int filasAfectadas;

        if (universidadActual.nombreValido() && nuevaUniversidad.nombreValido() && nuevoPais.nombreValido()) {
            String nombreActual = universidadActual.getNombre()
                    .trim();
            String nuevoNombre = nuevaUniversidad.getNombre()
                    .trim();
            String nombreNuevoPais = nuevoPais.getNombre()
                    .trim();

            if (universidadExiste(nuevoNombre,nombreNuevoPais)) {
                throw new ErrorDAO("La institución " + nuevoNombre + " ya existe", ErrorDAO.Tipo.DUPLICIDAD);
            }
            else {
                try {
                    filasAfectadas = UNIVERSIDAD_DAO.editarUniversidad(prepararUniversidadEditada(nombreActual,nuevoNombre,nombreNuevoPais));
                }
                catch (SQLException error) {
                    BITACORA.info(error.getMessage());
                    throw new ErrorDAO("Error al establecer conexión con la base de datos", ErrorDAO.Tipo.CONEXION);
                }
            }
        }
        else {
            throw new ErrorDAO("Campos vacíos", ErrorDAO.Tipo.VALIDACION);
        }

        return filasAfectadas;
    }

    public Optional<UniversidadDTO> getUniversidadPorNombre (String nombre) throws ErrorDAO {
        Optional<UniversidadDTO> universidad;
        if (cadenaValida(nombre)) {
            try {
                universidad = UNIVERSIDAD_DAO.getUniversidadPorNombre(nombre.trim());
            }
            catch (SQLException error) {
                BITACORA.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }
        else {
            throw new ErrorDAO("Nombre vacío", ErrorDAO.Tipo.VALIDACION);
        }
        return universidad;
    }

    public List<UniversidadDTO> getUniversidadesPorPaisOrigen (String paisOrigen) throws ErrorDAO {
        List<UniversidadDTO> listaUniversidades = new ArrayList<>();
        if (cadenaValida(paisOrigen)) {
            try {
                listaUniversidades = UNIVERSIDAD_DAO.getUniversidadesPorPaisOrigen(paisOrigen.trim());
            }
            catch (SQLException error) {
                BITACORA.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
            }
        }
        return listaUniversidades;
    }

    public List<UniversidadDTO> getUniversidadesPorNombre (UniversidadDTO universidad) throws ErrorDAO {
        if (esNulo(universidad)) {
            throw new ErrorDAO("Algo salió mal, inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }
        List<UniversidadDTO> listaUniversidades;

        if (universidad.nombreValido()) {
            String nombre = universidad.getNombre().
                    trim();
            try{
                listaUniversidades = UNIVERSIDAD_DAO.getUniversidadesPorNombre(nombre);
            }
            catch (SQLException error) {
                BITACORA.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
            }
        }
        else {
            throw new ErrorDAO("Campos vacíos", ErrorDAO.Tipo.VALIDACION);
        }

        return listaUniversidades;
    }

    public List<UniversidadDTO> getTodasAlfabeticamente () throws ErrorDAO {
        try {
            return UNIVERSIDAD_DAO.getTodasAlfabeticamente();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al establecer conexión con la base de datos", ErrorDAO.Tipo.CONEXION);
        }
    }

    public Optional<UniversidadDTO> getUniversidadPorId (int id) throws ErrorDAO {
        Optional<UniversidadDTO> universidad;
        if (id > 0) {
            try {
                universidad = UNIVERSIDAD_DAO.getUniversidadPorId(id);
            }
            catch (SQLException error) {
                BITACORA.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }
        else {
            throw new ErrorDAO("ID inválido", ErrorDAO.Tipo.VALIDACION);
        }
        return universidad;
    }

    public boolean universidadExiste (String universidad, String pais) throws ErrorDAO {
        boolean existe = false;
        Optional<UniversidadDTO> universidadDTO;

        try {
            universidadDTO = UNIVERSIDAD_DAO.getUniversidadPorNombreYPais(universidad,pais);
        }
        catch (SQLException error) {
            BITACORA.info(error.getMessage());
            throw new ErrorDAO("Error al establecer conexión con la base de datos", ErrorDAO.Tipo.CONEXION);
        }

        if (universidadDTO.isPresent()) {
            existe = true;
        }

        return existe;
    }

    public static boolean esNulo (Object objeto) {
        return objeto == null;
    }

    public static boolean cadenaValida (String cadena) {
        return !esNulo(cadena) && !cadena.isBlank();
    }

    private UniversidadDTO prepararUniversidadEditada (String nombreActual, String nuevoNombre, String nombreNuevoPais) throws ErrorDAO {
        UniversidadDTO universidadEditada;
        Optional<UniversidadDTO> universidadActualOptional;
        Optional<PaisDTO> paisNuevoOptional;

        try {
            universidadActualOptional = UNIVERSIDAD_DAO.getUniversidadPorNombre(nombreActual);
            paisNuevoOptional = PAIS_DAO.getPaisPorNombre(nombreNuevoPais);
        }
        catch (SQLException error) {
            BITACORA.info(error.getMessage());
            throw new ErrorDAO("Error al establecer conexión con la base de datos", ErrorDAO.Tipo.CONEXION);
        }

        if (universidadActualOptional.isPresent() && paisNuevoOptional.isPresent()){
            PaisDTO paisOrigen = paisNuevoOptional.get();
            universidadEditada = universidadActualOptional.get();
            universidadEditada.setNombre(nuevoNombre);
            universidadEditada.setIdPais(paisOrigen.getId());
        }
        else {
            throw new ErrorDAO("Ocurrió un error. Inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }

        return universidadEditada;
    }

    private UniversidadDTO prepararUniversidadNueva (String nombre, String pais) throws ErrorDAO{
        UniversidadDTO nuevaUniversidad;
        Optional<PaisDTO> paisOptional;

        try {
            paisOptional = PAIS_DAO.getPaisPorNombre(pais);
        }
        catch (SQLException error) {
            BITACORA.info(error.getMessage());
            throw new ErrorDAO("Error al establecer conexión con la base de datos", ErrorDAO.Tipo.CONEXION);
        }

        if (paisOptional.isPresent()) {
            PaisDTO paisOrigen = paisOptional.get();
            nuevaUniversidad = new UniversidadDTO(nombre, paisOrigen.getId());
        }
        else {
            throw new ErrorDAO("Ocurrió un error. Inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }

        return nuevaUniversidad;
    }
}
