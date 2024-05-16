package DAO;

import DTO.AcademicoDTO;
import DTO.CuentaDTO;
import Utilidades.ErrorDAO;
import DAO.Interfaces.IAcademicoDAO;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class AcademicoAuxiliar implements IAcademicoDAO {
    @Override
    public List<AcademicoDTO> getAcademicosPorFacultad (String nombrefacultad) throws ErrorDAO {
        if (!cadenaValida(nombrefacultad)) {
            throw new ErrorDAO("El nombre de la facultad es incorrecto", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return AcademicoDAO.getListaAcademicoPorCampos("facultad", nombrefacultad);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public Optional<AcademicoDTO> getAcademicoPorCedula (String cedula) throws ErrorDAO {
        try {
            probarCedula(cedula);
            return Optional.ofNullable(AcademicoDAO.getAcademicoPorCedula(cedula));
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<AcademicoDTO> getAcademicosPorUniversidad (String nombreUniversidad) throws ErrorDAO {
        if (!cadenaValida(nombreUniversidad)) {
            throw new ErrorDAO("El nombre de la univesidad esta incorrecto", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return AcademicoDAO.getListaAcademicoPorCampos("universidad", nombreUniversidad);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<AcademicoDTO> getAcademicosPorAreaEstudios (String areaEstudios) throws ErrorDAO {
        try {
            probarAreaEstudios(areaEstudios);
            return AcademicoDAO.getListaAcademicoPorCampos("area", areaEstudios);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<AcademicoDTO> getAcademicosPorCategoriaContratacion (String categoriaContratacion) throws ErrorDAO {
        try {
            probarCategoria(categoriaContratacion);
            return AcademicoDAO.getListaAcademicoPorCampos("categoria", categoriaContratacion);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<AcademicoDTO> getAcademicosPorRegion (String region) throws ErrorDAO {
        if (!cadenaValida(region)) {
            throw new ErrorDAO("El nombre de la region es incorrecto", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return AcademicoDAO.getListaAcademicoPorCampos("region", region);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO("Error en la base de datos", error.getTipo());
        }
    }

    @Override
    public Optional<AcademicoDTO> getAcademicoPorIdPersona (int idPersona) throws ErrorDAO {
        if (!idValido(idPersona)) {
            throw new ErrorDAO("id de la persona invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return Optional.ofNullable(AcademicoDAO.getAcademicoPorId(idPersona));
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int agregarAcademicoConCuenta (AcademicoDTO academicoDTO, CuentaDTO cuentaDTO) throws ErrorDAO {
        try {
            CuentaAuxiliar cuentaAuxiliar = new CuentaAuxiliar();
            cuentaAuxiliar.usuarioExistente(cuentaDTO);
            return AcademicoDAO.agregarAcademicoConCuenta(academicoDTO, cuentaDTO);
        }
        catch (ErrorDAO errorDAO) {
            throw new ErrorDAO(errorDAO.getMessage(), errorDAO.getTipo());
        }
    }

    @Override
    public int agregar (AcademicoDTO academicoDTO) throws ErrorDAO {
        try {
            existe(academicoDTO);
            return AcademicoDAO.agregarAcademico(academicoDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int modificar (AcademicoDTO academicoDTO) throws ErrorDAO {
        try {
            return AcademicoDAO.editarAcademico(academicoDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public Optional<AcademicoDTO> getPorId (String y) {
        // throw new ExecutionControl.NotImplementedException("Metodo no implementado");
        return Optional.empty();
    }

    @Override
    public List<AcademicoDTO> getTodos () throws ErrorDAO {
        try {
            return AcademicoDAO.getTodos();
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
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