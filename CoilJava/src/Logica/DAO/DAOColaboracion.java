package Logica.DAO;

import AccesoADatos.ColaboracionDB;
import Logica.Dominio.Academico;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.Estudiante;
import Logica.Dominio.Periodo;
import Utilidades.ErrorDAO;
import Logica.Interfaces.IColaboracionDAO;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DAOColaboracion implements IColaboracionDAO {
    private static final Logger BITACORA = Logger.getLogger(DAOColaboracion.class);

    @Override
    public Optional<Colaboracion> getColaboracionPorAcademicosParticipantes (Academico academico1, Academico academico2) throws ErrorDAO {
        Colaboracion colaboracion = null;
        if (!cadenaValida(academico1.getCedulaProfesional()) && !cadenaValida(academico2.getCedulaProfesional())) {
            throw new ErrorDAO("Error en los academicos de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            colaboracion = ColaboracionDB.getColaboracionPorAcademicosParticipantes(academico1, academico2);
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return Optional.ofNullable(colaboracion);
    }

    @Override
    public Optional<Colaboracion> getColaboracionPorId (int idColaboracion) throws ErrorDAO {
        Colaboracion colaboracion = null;
        if (!idValido(idColaboracion)) {
            throw new ErrorDAO("Error en el identificador de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            colaboracion = ColaboracionDB.getColaboracionPorId(idColaboracion);
        }
        catch (SQLException error) {
            BITACORA.error(error);

        }

        return Optional.ofNullable(colaboracion);
    }

    @Override
    public List<Estudiante> getListaDeEstudiantes (Colaboracion colaboracion) throws ErrorDAO {
        List<Estudiante> listaEstudiantes = null;
        if (!idValido(colaboracion.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            listaEstudiantes = ColaboracionDB.getListaDeEstudiantes(colaboracion);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }

        return listaEstudiantes;
    }

    @Override
    public List<Academico> getAcademicosParticipantes (Colaboracion colaboracion) throws ErrorDAO {
        List<Academico> listaAcademicos = null;
        if (!idValido(colaboracion.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            listaAcademicos = ColaboracionDB.getAcademicosParticipantes(colaboracion);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return listaAcademicos;
    }

    @Override
    public List<Colaboracion> getColaboracionPorPeriodo (Periodo periodo) {
        List<Colaboracion> listaColaboracion = null;
        if (!periodo.validarNulo()) {
            throw new ErrorDAO("Periodo con fecha de inicio o de fin vacia", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            listaColaboracion = ColaboracionDB.getColaboracionPorPeriodo(periodo);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return listaColaboracion;

    }

    @Override
    public List<Colaboracion> getColaboracionPorIdioma (String idioma) {
        List<Colaboracion> listaColaboracion = null;
        if (!cadenaValida(idioma)) {
            throw new ErrorDAO("Idioma invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            listaColaboracion = ColaboracionDB.getColaboracionPorIdioma(idioma);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return listaColaboracion;
    }

    @Override
    public List<Colaboracion> getColaboracionPorEstado (String estado) throws ErrorDAO {
        List<Colaboracion> listaColaboracion = null;
        if (!cadenaValida(estado)) {
            throw new ErrorDAO("Pais invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            listaColaboracion = ColaboracionDB.getColaboracionPorEstado(estado);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return listaColaboracion;
    }

    @Override
    public int cambiarEstadoColaboracion (Colaboracion colaboracion) {
        int filasAfectadas = 0;
        if (!idValido(colaboracion.getIdColaboracion())) {
            throw new ErrorDAO("id de la colaboracion invalido", ErrorDAO.Tipo.VALIDACION);
        }
        if (!cadenaValida(colaboracion.getEstado().toString())) {
            throw new ErrorDAO("Error en el estado de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            filasAfectadas = ColaboracionDB.cambiarEstadoColaboracion(colaboracion);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return filasAfectadas;
    }

    @Override
    public int agregarEstudianteAColaboracion (Colaboracion colaboracion, Estudiante estudiante) throws ErrorDAO {
        if (!idValido(colaboracion.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        if (!idValido(estudiante.getIdEstudiante())) {
            throw new ErrorDAO("Error en el id del estudiante", ErrorDAO.Tipo.VALIDACION);
        }
        int filasAfectadas = 0;
        try {
            filasAfectadas = ColaboracionDB.agregarEstudianteAColaboracion(colaboracion, estudiante);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return filasAfectadas;
    }


    @Override
    public int agregarAcademicoAColaboracion (Colaboracion colaboracion, Academico academico) throws ErrorDAO {
        if (!idValido(colaboracion.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        if (!cadenaValida(academico.getCedulaProfesional())) {
            throw new ErrorDAO("Error en la cedula del academico", ErrorDAO.Tipo.VALIDACION);
        }
        int filasAfectadas = 0;
        try {
            filasAfectadas = ColaboracionDB.agregarAcademicoAColaboracion(colaboracion, academico);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return filasAfectadas;
    }

    @Override
    public Optional<Colaboracion> getActivaPorAcademico (Academico academico) throws ErrorDAO {
        return Optional.empty();
    }

    @Override
    public int agregar (Colaboracion colaboracion) throws ErrorDAO {
        if (!colaboracion.esValido()) {
            throw new ErrorDAO("Al menos un dato de la colaboracion esta vacia", ErrorDAO.Tipo.VALIDACION);
        }
        int filasAfectadas = 0;
        try {
            filasAfectadas = ColaboracionDB.registrarColaboracion(colaboracion);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return filasAfectadas;
    }

    @Override
    public int modificar (Colaboracion colaboracion) throws ErrorDAO {
        if (!colaboracion.esValido()) {
            throw new ErrorDAO("Al menos un dato de la colaboracion esta vacio", ErrorDAO.Tipo.VALIDACION);
        }
        int filasAfectadas = 0;
        try {
            filasAfectadas = ColaboracionDB.actualizarColaboracion(colaboracion);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return filasAfectadas;
    }

    @Override
    public Optional<Colaboracion> getPorId (Integer id) throws ErrorDAO {
        if (!idValido(id)) {
            throw new ErrorDAO("Id invalido", ErrorDAO.Tipo.VALIDACION);
        }
        Colaboracion colaboracion = null;
        try {
            colaboracion = ColaboracionDB.getPorId(id);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return Optional.ofNullable(colaboracion);
    }

    @Override
    public List<Colaboracion> getTodos () throws ErrorDAO {
        List<Colaboracion> listaColaboracion = null;
        try {
            listaColaboracion = ColaboracionDB.getTodos();
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return listaColaboracion;
    }

    @Override
    public Colaboracion resultSetAObjeto (ResultSet resultados) {
        return null;
    }

    private boolean cadenaValida (String cadena) {
        return cadena != null && !cadena.isBlank();
    }

    private boolean idValido (int id) {
        return id > 0;
    }
}
