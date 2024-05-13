package Logica.DAO;

import AccesoADatos.AcademicoDB;
import Logica.Dominio.Academico;
import Logica.Dominio.Cuenta;
import Utilidades.ErrorDAO;
import Logica.Interfaces.IAcademicoDAO;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class DAOAcademico implements IAcademicoDAO {
    @Override
    public List<Academico> getAcademicosPorFacultad (String nombrefacultad) throws ErrorDAO {
        if (!cadenaValida(nombrefacultad)) {
            throw new ErrorDAO("El nombre de la facultad es incorrecto", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return AcademicoDB.getListaAcademicoPorCampos("facultad", nombrefacultad);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public Optional<Academico> getAcademicoPorCedula (String cedula) throws ErrorDAO {
        try {
            probarCedula(cedula);
            return Optional.ofNullable(AcademicoDB.getAcademicoPorCedula(cedula));
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<Academico> getAcademicosPorUniversidad (String nombreUniversidad) throws ErrorDAO {
        if (!cadenaValida(nombreUniversidad)) {
            throw new ErrorDAO("El nombre de la univesidad esta incorrecto", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return AcademicoDB.getListaAcademicoPorCampos("universidad", nombreUniversidad);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<Academico> getAcademicosPorAreaEstudios (String areaEstudios) throws ErrorDAO {
        try {
            probarAreaEstudios(areaEstudios);
            return AcademicoDB.getListaAcademicoPorCampos("area", areaEstudios);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<Academico> getAcademicosPorCategoriaContratacion (String categoriaContratacion) throws ErrorDAO {
        try {
            probarCategoria(categoriaContratacion);
            return AcademicoDB.getListaAcademicoPorCampos("categoria", categoriaContratacion);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<Academico> getAcademicosPorRegion (String region) throws ErrorDAO {
        if (!cadenaValida(region)) {
            throw new ErrorDAO("El nombre de la region es incorrecto", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return AcademicoDB.getListaAcademicoPorCampos("region", region);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO("Error en la base de datos", error.getTipo());
        }
    }

    @Override
    public Optional<Academico> getAcademicoPorIdPersona (int idPersona) throws ErrorDAO {
        if (!idValido(idPersona)) {
            throw new ErrorDAO("id de la persona invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return Optional.ofNullable(AcademicoDB.getAcademicoPorId(idPersona));
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int agregarAcademicoConCuenta (Academico academico, Cuenta cuenta) throws ErrorDAO {
        try {
            DAOCuenta daoCuenta = new DAOCuenta();
            daoCuenta.usuarioExistente(cuenta);
            existe(academico);
            return AcademicoDB.agregarAcademicoConCuenta(academico, cuenta);
        }
        catch (ErrorDAO errorDAO) {
            throw new ErrorDAO(errorDAO.getMessage(), errorDAO.getTipo());
        }
    }

    @Override
    public int agregar (Academico academico) throws ErrorDAO {
        try {
            existe(academico);
            return AcademicoDB.agregarAcademico(academico);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int modificar (Academico academico) throws ErrorDAO {
        try {
            return AcademicoDB.editarAcademico(academico);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public Optional<Academico> getPorId (String y) {
        // throw new ExecutionControl.NotImplementedException("Metodo no implementado");
        return Optional.empty();
    }

    @Override
    public List<Academico> getTodos () throws ErrorDAO {
        try {
            return AcademicoDB.getTodos();
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public Academico resultSetAObjeto (ResultSet resultados) {
        return null;
    }

    public static boolean cadenaValida (String cadena) {
        return cadena != null && !cadena.isBlank();
    }

    private static boolean idValido (int id) {
        return id > 0;
    }

    private void existe (Academico academico) throws ErrorDAO {
        Optional<Academico> optionalAcademico = getAcademicoPorCedula(academico.getCedulaProfesional());
        if (optionalAcademico.isPresent()) {
            throw new ErrorDAO("La cedula profesional " + academico.getCedulaProfesional() + " se encuentra registrada en el sistema", ErrorDAO.Tipo.VALIDACION);
        }
    }

    private void probarCedula (String cedula) {
        Academico academico = new Academico();
        academico.setCedulaProfesional(cedula);
    }

    private void probarAreaEstudios (String area) {
        Academico academico = new Academico();
        academico.setAreaEstudios(area);
    }

    private void probarCategoria (String categoria) {
        Academico academico = new Academico();
        academico.setCategoriaContratacion(categoria);
    }
}