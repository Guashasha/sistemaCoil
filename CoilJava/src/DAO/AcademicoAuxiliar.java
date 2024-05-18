package DAO;

import DTO.AcademicoDTO;
import DTO.CuentaDTO;
import Utilidades.ErrorDAO;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class AcademicoAuxiliar {
    private final AcademicoDAO ACADEMICO_DAO = new AcademicoDAO();

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

    public Optional<AcademicoDTO> getAcademicoPorCedula (String cedula) throws ErrorDAO {
        try {
            probarCedula(cedula);
            return ACADEMICO_DAO.getAcademicoPorCedula(cedula);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

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

    public List<AcademicoDTO> getAcademicosPorAreaEstudios (String areaEstudios) throws ErrorDAO {
        try {
            probarAreaEstudios(areaEstudios);
            return ACADEMICO_DAO.getAcademicosPorAreaEstudios(areaEstudios);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public List<AcademicoDTO> getAcademicosPorCategoriaContratacion (String categoriaContratacion) throws ErrorDAO {
        try {
            probarCategoria(categoriaContratacion);
            return ACADEMICO_DAO.getAcademicosPorCategoriaContratacion(categoriaContratacion);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

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

    public int agregarAcademicoConCuenta (AcademicoDTO academicoDTO, CuentaDTO cuentaDTO) throws ErrorDAO {
        try {
            CuentaAuxiliar cuentaAuxiliar = new CuentaAuxiliar();
            cuentaAuxiliar.usuarioExistente(cuentaDTO);
            return ACADEMICO_DAO.agregarAcademicoConCuenta(academicoDTO, cuentaDTO);
        }
        catch (ErrorDAO errorDAO) {
            throw new ErrorDAO(errorDAO.getMessage(), errorDAO.getTipo());
        }
    }

    public int agregar (AcademicoDTO academicoDTO) throws ErrorDAO {
        try {
            existe(academicoDTO);
            return ACADEMICO_DAO.agregar(academicoDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public int modificar (AcademicoDTO academicoDTO) throws ErrorDAO {
        try {
            return ACADEMICO_DAO.modificar(academicoDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public List<AcademicoDTO> getTodos () throws ErrorDAO {
        try {
            return ACADEMICO_DAO.getTodos();
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public AcademicoDTO resultSetAObjeto (ResultSet resultados) {
        return null;
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