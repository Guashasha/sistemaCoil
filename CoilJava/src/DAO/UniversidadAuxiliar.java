package DAO;

import DTO.PaisDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * La clase UniversidadAuxiliar funciona como intermediario entre el cliente y las clases DAO. Procesa y valida la información de los parámetros
 * antes de mandarla o después de recibirla de las clases DAO.
 * @author pale
 */
public class UniversidadAuxiliar {
    /**
     *Instancia de la clase UniversidadDAO que se utiliza en los métodos de la clase.
     */
    private final UniversidadDAO UNIVERSIDAD_DAO = new UniversidadDAO();
    /**
     *Instancia de la clase PaisDAO que se utiliza en los métodos de la clase.
     */
    private final PaisDAO PAIS_DAO = new PaisDAO();

    /**
     * Valida los parámetros para registrar una universidad con la clase UniversidadDAO.
     * @param universidad universidad a registrar, inicializada con su nombre.
     * @param pais pais de la universidad a registrar, inicializado con su nombre.
     * @return número de filas afectadas por la sentencia SQL.
     * @throws ErrorDAO si ocurre un error en la validación de la información o durante el acceso a la base de datos.
     */
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
                filasAfectadas = UNIVERSIDAD_DAO.registrarUniversidad(prepararUniversidadNueva(nombreUniversidad,nombrePais));
            }
        }
        else {
            throw new ErrorDAO("Los nombres no pueden contener caracteres especiales.\nSolo son válidas letras del alfabeto en español y guiones en el medio", ErrorDAO.Tipo.VALIDACION);
        }

        return filasAfectadas;
    }

    /**
     *Valida los parámetros para editar una universidad existente con la clase UniversidadDAO
     * @param universidadActual Contiene de la universidad que se quiere editar.
     * @param nuevaUniversidad contiene los datos editados de la universidad.
     * @param nuevoPais Contiene el nombre del país de la universidad editada.
     * @return número de las filas afectadas por la sentencia SQL.
     * @throws ErrorDAO si ocurre un error en la validación de la información o durante el acceso a la base de datos.
     */
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
                filasAfectadas = UNIVERSIDAD_DAO.editarUniversidad(prepararUniversidadEditada(nombreActual,nuevoNombre,nombreNuevoPais));
            }
        }
        else {
            throw new ErrorDAO("Los nombres no pueden contener caracteres especiales.\nSolo son válidas letras del alfabeto en español y guiones en el medio", ErrorDAO.Tipo.VALIDACION);
        }

        return filasAfectadas;
    }

    /**
     *Valida los parámetros para obtener una universidad de acuerdo a su nombre, con la clase UniversidadDAO.
     * @param nombre nombre de la universidad a consulta
     * @return Objeto Optional con una universidad inicializada con su id, nombre e id de país; o un objeto Optional vacío si no se encuentran resultados.
     * @throws ErrorDAO si ocurre un error en la validación de la información o durante el acceso a la base de datos.
     */
    public Optional<UniversidadDTO> getUniversidadPorNombre (String nombre) throws ErrorDAO {
        Optional<UniversidadDTO> universidad;
        UniversidadDTO universidadABuscar = new UniversidadDTO(nombre);
        if (universidadABuscar.nombreValido()) {
            universidad = UNIVERSIDAD_DAO.getUniversidadPorNombre(nombre.trim());
        }
        else {
            throw new ErrorDAO("Los nombres no pueden contener caracteres especiales.\nSolo son válidas letras del alfabeto en español y guiones en el medio", ErrorDAO.Tipo.VALIDACION);
        }
        return universidad;
    }

    /**
     * Valida los parámetros para obtener una universidad de acuerdo a su país asociado, con la clase UniversidadDAO.
     * @param pais nombre del país al que pertenece la universidad.
     * @return Objeto Optional con una universidad inicializada con su id, nombre e id de país; o un objeto Optional vacío si no se encuentran resultados.
     * @throws ErrorDAO si ocurre un error en la validación de la información o durante el acceso a la base de datos.
     */
    public List<UniversidadDTO> getUniversidadesPorPaisOrigen (String pais) throws ErrorDAO {
        List<UniversidadDTO> listaUniversidades = new ArrayList<>();
        PaisDTO paisOrigen = new PaisDTO(pais);
        if (paisOrigen.nombreValido()) {
            listaUniversidades = UNIVERSIDAD_DAO.getUniversidadesPorPaisOrigen(pais.trim());
        }
        return listaUniversidades;
    }

    /**
     * Valida los parámetros para obtener la lista de universidades que tienen el nombre coincidente con una cadena determinada.
     * @param universidad universidad inicializada con el nombre que se desea usar como coincidencia.
     * @return lista con las universidades coincidentes con la cadena especificada o una lista vacía si no se encuentran resultados.
     * @throws ErrorDAO si ocurre un error en la validación de la información o durante el acceso a la base de datos.
     */
    public List<UniversidadDTO> getUniversidadesPorNombre (UniversidadDTO universidad) throws ErrorDAO {
        if (esNulo(universidad)) {
            throw new ErrorDAO("Algo salió mal, inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }
        List<UniversidadDTO> listaUniversidades;

        if (universidad.nombreValido()) {
            listaUniversidades = UNIVERSIDAD_DAO.getUniversidadesPorNombre(universidad.getNombre()
                                                                                      .trim());
        }
        else {
            throw new ErrorDAO("Los nombres no pueden contener caracteres especiales.\nSolo son válidas letras del alfabeto en español y guiones en el medio", ErrorDAO.Tipo.VALIDACION);
        }

        return listaUniversidades;
    }

    /**
     * Valida los parámetros y obtiene una universidad que esté registrada con un id específico.
     * @param id id de la universidad a buscar
     * @return Objeto Optional con una universidad inicializa con su id, nombre e id de país; o un objeto Optional vacío si no se encuentran resultados.
     * @throws ErrorDAO si ocurre un error en la validación de la información o durante el acceso a la base de datos.
     */
    public Optional<UniversidadDTO> getUniversidadPorId (int id) throws ErrorDAO {
        Optional<UniversidadDTO> universidad;
        if (id > 0) {
            universidad = UNIVERSIDAD_DAO.getUniversidadPorId(id);
        }
        else {
            throw new ErrorDAO("ID inválido", ErrorDAO.Tipo.VALIDACION);
        }
        return universidad;
    }

    /**
     * Valida los parámetros y verifica si existe una universidad específica en la base de datos.
     * @param universidad nombre de la universidad a buscar.
     * @param pais nombre del país al que pertenece la universidad a buscar.
     * @return true si existe una universidad con los valores especificados, de lo contrario false.
     * @throws ErrorDAO si ocurre un error en la validación de la información o durante el acceso a la base de datos.
     */
    public boolean universidadExiste (String universidad, String pais) throws ErrorDAO {
        boolean existe = false;
        UniversidadDTO universidadABuscar = new UniversidadDTO(universidad);
        PaisDTO paisOrigen = new PaisDTO(pais);

        if (universidadABuscar.nombreValido() && paisOrigen.nombreValido()) {
            Optional<UniversidadDTO> universidadDTO = UNIVERSIDAD_DAO.getUniversidadPorNombreYPais(universidad,pais);

            if (universidadDTO.isPresent()) {
                existe = true;
            }
        }
        else {
            throw new ErrorDAO("Los nombres no pueden estar vacíos ni contener caracteres especiales.\nSolo son válidas letras del alfabeto en español y guiones en el medio", ErrorDAO.Tipo.VALIDACION);
        }

        return existe;
    }

    private static boolean esNulo (Object objeto) {
        return objeto == null;
    }

    private UniversidadDTO prepararUniversidadEditada (String nombreActual, String nuevoNombre, String nombreNuevoPais) throws ErrorDAO {
        UniversidadDTO universidadEditada;
        Optional<UniversidadDTO> universidadActualOptional = UNIVERSIDAD_DAO.getUniversidadPorNombre(nombreActual);
        Optional<PaisDTO> paisNuevoOptional = PAIS_DAO.getPaisPorNombre(nombreNuevoPais);

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

    private UniversidadDTO prepararUniversidadNueva (String nombre, String pais) throws ErrorDAO {
        UniversidadDTO nuevaUniversidad;
        Optional<PaisDTO> paisOptional = PAIS_DAO.getPaisPorNombre(pais);

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
