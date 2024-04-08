package Logica.DAO;

import AccesoADatos.AcademicoDB;
import Logica.Dominio.Academico;
import Logica.ErrorDAO;
import Logica.Interfaces.IAcademicoDAO;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class DAOAcademico implements IAcademicoDAO {
    //fixme considerar hacer un metodo generico;

    @Override
    public List<Academico> getAcademicosPorFacultad (String nombrefacultad) throws ErrorDAO {
        List<Academico>  listaAcademicos= null;
        if (!cadenaValida(nombrefacultad)) {
            throw new ErrorDAO ("El nombre de la facultad es incorrecto");
        }
        try {
            listaAcademicos = AcademicoDB.getListaAcademicoPorCampos("facultad",nombrefacultad);

        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }

        return listaAcademicos;
    }

    @Override
    public Optional<Academico> getAcademicoPorCedula (String cedula) throws ErrorDAO {
        Academico academico = null;
        if (!cadenaValida(cedula)) {
            throw new ErrorDAO ("La cedula profesional esta incorrecta");
        }
        try {
            academico = AcademicoDB.getAcademicoPorCedula(cedula);

        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }

        return Optional.ofNullable(academico);
    }

    @Override
    public List<Academico> getAcademicosPorUniversidad (String nombreUniversidad) throws ErrorDAO {
        List<Academico> listaAcademicos = null;
        if (!cadenaValida(nombreUniversidad)) {
            throw new ErrorDAO ("El nombre de la univesidad esta incorrecto");
        }
        try {
            listaAcademicos = AcademicoDB.getListaAcademicoPorCampos("universidad", nombreUniversidad);
        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }

        return listaAcademicos;
    }

    @Override
    public List<Academico> getAcademicosPorAreaEstudios (String areaEstudios) throws ErrorDAO {
        List<Academico> listaAcademicos = null;
        if (!cadenaValida(areaEstudios)) {
            throw new ErrorDAO ("El nombre del area de estudios es incorrecto");
        }
        try {
            listaAcademicos = AcademicoDB.getListaAcademicoPorCampos("area", areaEstudios);
        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }

        return listaAcademicos;
    }



    @Override
    public List<Academico> getAcademicosPorCategoriaContratacion (String categoriaContratacion) throws ErrorDAO {
        List<Academico> listaAcademicos = null;
        if (!cadenaValida(categoriaContratacion)) {
            throw new ErrorDAO ("El nombre de la categoria de contratacion es incorrecta");
        }
        try {
            listaAcademicos = AcademicoDB.getListaAcademicoPorCampos("categoria", categoriaContratacion);
        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }

        return listaAcademicos;
    }

    @Override
    public List<Academico> getAcademicosPorRegion (String region) throws ErrorDAO {
        List<Academico> listaAcademicos = null;
        if (!cadenaValida(region)) {
            throw new ErrorDAO ("El nombre de la region es incorrecto");
        }
        try {
            listaAcademicos = AcademicoDB.getListaAcademicoPorCampos("region", region);
        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }

        return listaAcademicos;
    }

    @Override
    public Optional<Academico> getAcademicoPorIdPersona (int idPersona) throws ErrorDAO {
        Academico academico = null;
        if (!idValido(idPersona)) {
            throw new ErrorDAO ("id de la persona invalido");
        }
        try {
            academico = AcademicoDB.getAcademicoPorId(idPersona);
        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }

        return Optional.ofNullable(academico);
    }

    @Override
    public int agregar (Academico academico) throws ErrorDAO {
        int filasAfectadas;

        if (!academico.validarNulos()) {
            throw new ErrorDAO ("Existe al menos un campo vacio en el academico");
        }
        try {
            filasAfectadas = AcademicoDB.agregarAcademico(academico);

        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }

        return filasAfectadas;
    }

    @Override
    public int modificar (Academico academico) throws ErrorDAO {
        int filasAfectadas;

        if (!academico.validarNulos()) {
            throw new ErrorDAO ("Existe al menos un campo vacio en el academico");
        }
        try {
            filasAfectadas = AcademicoDB.editarAcademico(academico);

        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }

        return filasAfectadas;
    }


    //fixme nombre nada descriptivo.
    @Override
    public Optional<Academico> getPorId (String y) throws ErrorDAO {
        return Optional.empty();
    }

    @Override
    public List<Academico> getTodos () throws ErrorDAO {
        return AcademicoDB.getTodos();
    }

    @Override
    public Academico resultSetAObjeto (ResultSet resultados) {
        return null;
    }

    private boolean cadenaValida (String cadena) {
        return cadena != null && !cadena.isBlank();
    }

    private boolean idValido (int id) {
        return id > 0;
    }

}
