package DAO;

import DTO.AcademicoDTO;
import DTO.CuentaDTO;
import Utilidades.ErrorDAO;

import java.util.List;
import java.util.Optional;

/**
 * La clase AcademicoAuxiliar se encarga de obtener información de los academicos, asi como registros y cambios de estos en la base de datos y mandarlos a capas superiores mediante Transfer Objects.
 *
 * @author FerRMZ
 */
public class AcademicoAuxiliar {
    private final AcademicoDAO ACADEMICO_DAO = new AcademicoDAO();

    /**
     * Obtiene una lista de objetos AcademicoDTO basado en el nombre de la facultad.
     *
     * @param nombrefacultad el nombre de la facultad por el cual se desea filtrar los académicos.
     * @return una lista de objetos AcademicoDTO que cumplen con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public List<AcademicoDTO> getAcademicosPorFacultad (String nombrefacultad) throws ErrorDAO {
        if (!cadenaValida(nombrefacultad)) {
            throw new ErrorDAO("El nombre de la facultad es incorrecto", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ACADEMICO_DAO.getAcademicosPorFacultad(nombrefacultad);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Obtiene un objeto AcademicoDTO basado en la cédula del académico.
     *
     * @param cedula la cédula por la cual se desea filtrar el académico.
     * @return un objeto Optional que contiene el AcademicoDTO que cumple con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public Optional<AcademicoDTO> getAcademicoPorCedula (String cedula) throws ErrorDAO {
        try {
            probarCedula(cedula);
            return ACADEMICO_DAO.getAcademicoPorCedula(cedula);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Obtiene una lista de objetos AcademicoDTO basado en el nombre de la universidad.
     *
     * @param nombreUniversidad el nombre de la universidad por el cual se desea filtrar los académicos.
     * @return una lista de objetos AcademicoDTO que cumplen con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public List<AcademicoDTO> getAcademicosPorUniversidad (String nombreUniversidad) throws ErrorDAO {
        if (!cadenaValida(nombreUniversidad)) {
            throw new ErrorDAO("El nombre de la univesidad esta incorrecto", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ACADEMICO_DAO.getAcademicosPorUniversidad(nombreUniversidad);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Obtiene una lista de objetos AcademicoDTO basado en el área de estudios.
     *
     * @param areaEstudios el área de estudios por la cual se desea filtrar los académicos.
     * @return una lista de objetos AcademicoDTO que cumplen con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public List<AcademicoDTO> getAcademicosPorAreaEstudios (String areaEstudios) throws ErrorDAO {
        try {
            probarAreaEstudios(areaEstudios);
            return ACADEMICO_DAO.getAcademicosPorAreaEstudios(areaEstudios);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Obtiene una lista de objetos AcademicoDTO basado en la categoría de contratación.
     *
     * @param categoriaContratacion la categoría de contratación por la cual se desea filtrar los académicos.
     * @return una lista de objetos AcademicoDTO que cumplen con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public List<AcademicoDTO> getAcademicosPorCategoriaContratacion (String categoriaContratacion) throws ErrorDAO {
        try {
            probarCategoria(categoriaContratacion);
            return ACADEMICO_DAO.getAcademicosPorCategoriaContratacion(categoriaContratacion);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }


    /**
     * Obtiene una lista de objetos AcademicoDTO basado en la región.
     *
     * @param region la región por la cual se desea filtrar los académicos.
     * @return una lista de objetos AcademicoDTO que cumplen con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public List<AcademicoDTO> getAcademicosPorRegion (String region) throws ErrorDAO {
        if (!cadenaValida(region)) {
            throw new ErrorDAO("El nombre de la region es incorrecto", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ACADEMICO_DAO.getAcademicosPorRegion(region);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO("Error en la base de datos", error.getTipo());
        }
    }

    /**
     * Obtiene un objeto AcademicoDTO basado en el ID de la persona.
     *
     * @param idPersona el ID de la persona por el cual se desea filtrar el académico.
     * @return un objeto Optional que contiene el AcademicoDTO que cumple con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public Optional<AcademicoDTO> getPorId (int idPersona) throws ErrorDAO {
        if (!idValido(idPersona)) {
            throw new ErrorDAO("id de la persona invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ACADEMICO_DAO.getPorId(idPersona);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Agrega un objeto AcademicoDTO junto con un objeto CuentaDTO.
     *
     * @param academicoDTO el objeto AcademicoDTO que se desea agregar.
     * @param cuentaDTO el objeto CuentaDTO asociado al académico.
     * @return el ID del académico agregado.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public int agregarAcademicoConCuenta (AcademicoDTO academicoDTO, CuentaDTO cuentaDTO) throws ErrorDAO {
        try {
            if (ACADEMICO_DAO.getAcademicoPorCorreo(academicoDTO.getCorreoElectronico()).isPresent()) {
                throw new ErrorDAO ("El correo electrónico ya se encuentra registrado", ErrorDAO.Tipo.VALIDACION);
            }
            existe(academicoDTO);
            CuentaAuxiliar cuentaAuxiliar = new CuentaAuxiliar();
            cuentaAuxiliar.usuarioExistente(cuentaDTO);
            return ACADEMICO_DAO.agregarAcademicoConCuenta(academicoDTO, cuentaDTO);
        }
        catch (ErrorDAO errorDAO) {
            throw new ErrorDAO(errorDAO.getMessage(), errorDAO.getTipo());
        }
    }

    /**
     * Agrega un objeto AcademicoDTO.
     *
     * @param academicoDTO el objeto AcademicoDTO que se desea agregar.
     * @return el ID del académico agregado.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public int agregar (AcademicoDTO academicoDTO) throws ErrorDAO {
        try {
            existe(academicoDTO);
            return ACADEMICO_DAO.agregar(academicoDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Modifica un objeto AcademicoDTO.
     *
     * @param academicoDTO el objeto AcademicoDTO que se desea modificar.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public int modificar (AcademicoDTO academicoDTO) throws ErrorDAO {
        try {
            return ACADEMICO_DAO.modificar(academicoDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Obtiene una lista de todos los objetos AcademicoDTO.
     *
     * @return una lista de todos los objetos AcademicoDTO.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public List<AcademicoDTO> getTodos () throws ErrorDAO {
        try {
            return ACADEMICO_DAO.getTodos();
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public static boolean cadenaValida (String cadena) {
        return cadena != null && !cadena.isBlank();
    }

    private static boolean idValido (int id) {
        return id > 0;
    }

    private void existe (AcademicoDTO academicoDTO) throws ErrorDAO {
        Optional<AcademicoDTO> optionalAcademico = getAcademicoPorCedula(academicoDTO.getCedulaProfesional());
        if (optionalAcademico.isPresent()) {
            throw new ErrorDAO("La cedula profesional " + academicoDTO.getCedulaProfesional() + " se encuentra registrada en el sistema", ErrorDAO.Tipo.VALIDACION);
        }
    }

    private void probarCedula (String cedula) {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setCedulaProfesional(cedula);
    }

    private void probarAreaEstudios (String area) {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setAreaEstudios(area);
    }

    private void probarCategoria (String categoria) {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setCategoriaContratacion(categoria);
    }
}