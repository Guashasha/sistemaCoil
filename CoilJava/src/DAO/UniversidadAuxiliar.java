package DAO;

import DTO.PaisDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import DAO.Interfaces.IUniversidadDAO;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UniversidadAuxiliar implements IUniversidadDAO {
    private static Logger bitacora = Logger.getLogger(UniversidadAuxiliar.class);

    @Override
    public int registrarUniversidad (UniversidadDTO universidadDTO, PaisDTO paisDTO) throws ErrorDAO {
        if (esNulo(universidadDTO) || esNulo(paisDTO)) {
            throw new ErrorDAO("Algo salió mal, inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }
        int filasAfectadas;

        if (universidadDTO.nombreValido() && paisDTO.nombreValido()) {
            String nombreUnivesidad = universidadDTO.getNombre()
                    .trim();
            String nombrePais = paisDTO.getNombre()
                    .trim();

            if (universidadExiste(nombreUnivesidad,nombrePais)) {
                throw new ErrorDAO("La universidadDTO que intentas registrar ya ha sido registrada anteriormente", ErrorDAO.Tipo.DUPLICIDAD);
            }
            else {
                try {
                    PaisDTO paisDTOOrigen = PaisDAO.getPaisPorNombre(nombrePais);
                    UniversidadDTO nuevaUniversidadDTO = new UniversidadDTO(nombreUnivesidad, paisDTOOrigen.getId());
                    filasAfectadas = UniversidadDAO.registrarUniversidad(nuevaUniversidadDTO);
                }
                catch (SQLException error) {
                    bitacora.info(error.getMessage());
                    throw new ErrorDAO("Error al establecer conexión con la base de datos", ErrorDAO.Tipo.CONEXION);
                }
            }
        }
        else {
            throw new ErrorDAO("Campos vacíos", ErrorDAO.Tipo.VALIDACION);
        }

        return filasAfectadas;
    }

    @Override
    public int editarUniversidad (UniversidadDTO universidadDTOActual, UniversidadDTO nuevaUniversidadDTO, PaisDTO nuevoPaisDTO) throws ErrorDAO {
        if (esNulo(universidadDTOActual) || esNulo(nuevaUniversidadDTO) || esNulo(nuevoPaisDTO)) {
            throw new ErrorDAO("Algo salió mal, inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }
        int filasAfectadas;

        if (universidadDTOActual.nombreValido() && nuevaUniversidadDTO.nombreValido() && nuevoPaisDTO.nombreValido()) {
            String nombreActual = universidadDTOActual.getNombre()
                    .trim();
            String nuevoNombre = nuevaUniversidadDTO.getNombre()
                    .trim();
            String nombreNuevoPais = nuevoPaisDTO.getNombre()
                    .trim();

            if (universidadExiste(nuevoNombre,nombreNuevoPais)) {
                throw new ErrorDAO("La institución " + nuevoNombre + " ya existe", ErrorDAO.Tipo.DUPLICIDAD);
            }
            else {
                try {
                    UniversidadDTO universidadDTO = UniversidadDAO.getUniversidadPorNombre(nombreActual);
                    PaisDTO paisDTOOrigen = PaisDAO.getPaisPorNombre(nombreNuevoPais);
                    universidadDTO.setNombre(nuevoNombre);
                    universidadDTO.setIdPais(paisDTOOrigen.getId());

                    filasAfectadas = UniversidadDAO.editarUniversidad(universidadDTO);
                }
                catch (SQLException error) {
                    bitacora.info(error.getMessage());
                    throw new ErrorDAO("Error al establecer conexión con la base de datos", ErrorDAO.Tipo.CONEXION);
                }
            }
        }
        else {
            throw new ErrorDAO("Campos vacíos", ErrorDAO.Tipo.VALIDACION);
        }

        return filasAfectadas;
    }

    @Override
    public Optional<UniversidadDTO> getUniversidadPorNombre (String nombre) throws ErrorDAO {
        UniversidadDTO universidadDTO = null;
        if (cadenaValida(nombre)) {
            try {
                universidadDTO = UniversidadDAO.getUniversidadPorNombre(nombre.trim());
            }
            catch (SQLException error) {
                bitacora.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }
        return Optional.ofNullable(universidadDTO);
    }

    @Override
    public List<UniversidadDTO> getUniversidadesPorPaisOrigen (String paisOrigen) throws ErrorDAO {
        List<UniversidadDTO> listaUniversidades = new ArrayList<>();
        if (cadenaValida(paisOrigen)) {
            try {
                listaUniversidades = UniversidadDAO.getUniversidadesPorPaisOrigen(paisOrigen.trim());
            }
            catch (SQLException error) {
                bitacora.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
            }
        }
        return listaUniversidades;
    }

    @Override
    public List<UniversidadDTO> getUniversidadesPorNombre (UniversidadDTO universidadDTO) throws ErrorDAO {
        if (esNulo(universidadDTO)) {
            throw new ErrorDAO("Algo salió mal, inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }
        List<UniversidadDTO> listaUniversidades;

        if (universidadDTO.nombreValido()) {
            String nombre = universidadDTO.getNombre().
                    trim();
            try{
                listaUniversidades = UniversidadDAO.getUniversidadesPorNombre(nombre);
            }
            catch (SQLException error) {
                bitacora.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
            }
        }
        else {
            throw new ErrorDAO("Campos vacíos", ErrorDAO.Tipo.VALIDACION);
        }

        return listaUniversidades;
    }

    @Override
    public List<UniversidadDTO> getTodasAlfabeticamente () throws ErrorDAO {
        try {
            return UniversidadDAO.getTodasAlfabeticamente();
        }
        catch (SQLException error) {
            bitacora.fatal(error.getMessage());
            throw new ErrorDAO("Error al establecer conexión con la base de datos", ErrorDAO.Tipo.CONEXION);
        }
    }

    @Override
    public Optional<UniversidadDTO> getUniversidadPorId (int id) throws ErrorDAO {
        UniversidadDTO universidadDTO = null;
        if (id > 0) {
            try {
                universidadDTO = UniversidadDAO.getUniversidadPorId(id);
            }
            catch (SQLException error) {
                bitacora.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }
        return Optional.ofNullable(universidadDTO);
    }

    public boolean universidadExiste (String universidad, String pais) throws ErrorDAO {
        boolean existe = false;
        UniversidadDTO universidadDTOEncontrada;

        try {
            universidadDTOEncontrada = UniversidadDAO.getUniversidadPorNombreYPais(universidad,pais);
        }
        catch (SQLException error) {
            bitacora.info(error.getMessage());
            throw new ErrorDAO("Error al establecer conexión con la base de datos", ErrorDAO.Tipo.CONEXION);
        }

        if (universidadDTOEncontrada.getId() > 0) {
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
}
