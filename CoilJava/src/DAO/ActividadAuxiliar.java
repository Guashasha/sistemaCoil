package DAO;

import DTO.ActividadDTO;
import DTO.RetroalimentacionActividadDTO;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
import DAO.Interfaces.IActividadDAO;
import jdk.jshell.spi.ExecutionControl;
import org.apache.log4j.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActividadAuxiliar implements IActividadDAO {
    private static final Logger BITACORA = Logger.getLogger(RetroalimentacionActividadDTO.class);

    @Override
    public int agregar (ActividadDTO actividadDTO) throws ErrorDAO {
        if (!actividadDTO.esCorrecta()) {
            throw new ErrorDAO("La actividadDTO es incorrecta", Tipo.VALIDACION);
        }

        if (getPorTitulo(actividadDTO.getTitulo()).isPresent()) {
            throw new ErrorDAO("la actividadDTO ya existe", Tipo.DUPLICIDAD);
        }

        int resultado = -1;

        try {
            resultado = ActividadDAO.agregarActividad(actividadDTO);
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Ocurrió un error con la base de datos: " + error.getMessage(), Tipo.CONEXION);
        }

        return resultado;
    }

    @Override
    public int modificar (ActividadDTO actividadDTO) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Metodo no implementado");
    }

    @Override
    public Optional<ActividadDTO> getPorId (Integer idActividad) throws ErrorDAO {
        ResultSet resultado = null;

        try {
            resultado = ActividadDAO.getPorId(idActividad);
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Ocurrió un error con la base de datos: " + error.getMessage(), Tipo.CONEXION);
        }

        ActividadDTO actividadDTO = null;

        try {
            if (resultado != null && resultado.next()) {
                actividadDTO = resultSetAObjeto(resultado);

                resultado.close();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return Optional.ofNullable(actividadDTO);
    }

    @Override
    public Optional<ActividadDTO> getPorTitulo (String titulo) {
        ResultSet resultado = null;

        try {
            resultado = ActividadDAO.getPorTitulo(titulo);
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Ocurrió un error con la base de datos: " + error.getMessage(), Tipo.CONEXION);
        }

        ActividadDTO actividadDTO = null;

        try {
            if (resultado != null && resultado.next()) {
                actividadDTO = resultSetAObjeto(resultado);

                resultado.close();
            }
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Ocurrió un error con la base de datos: " + error.getMessage(), Tipo.CONEXION);
        }

        return Optional.ofNullable(actividadDTO);
    }

    public List<ActividadDTO> getPorIdColaboracion (int idColaboracion) throws ErrorDAO {
        ResultSet resultado = null;

        try {
            resultado = ActividadDAO.getPorIdColaboracion(idColaboracion);
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Ocurrió un error al recuperar la información de la actividad", Tipo.CONSULTA);
        }

        List<ActividadDTO> actividades = new ArrayList<>();

        if (resultado == null) {
            return actividades;
        }

        try {
            while (resultado.next()) {
                ActividadDTO actividadDTO = resultSetAObjeto(resultado);

                if (actividadDTO.esCorrecta()) {
                    actividades.add(actividadDTO);
                }
            }

            resultado.close();
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Ocurrió un error con la base de datos: " + error.getMessage(), Tipo.CONEXION);
        }

        return actividades;
    }

    @Override
    public List<ActividadDTO> getTodos () throws ErrorDAO {
        ResultSet resultados = null;

        try {
            resultados = ActividadDAO.getTodos();
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Ocurrió un error con la base de datos: " + error.getMessage(), Tipo.CONEXION);
        }

        List<ActividadDTO> actividades = new ArrayList<>();

        if (resultados == null) {
            return actividades;
        }

        try {
            while (resultados.next()) {
                ActividadDTO actividadDTO = resultSetAObjeto(resultados);

                if (actividadDTO.esCorrecta()) {
                    actividades.add(actividadDTO);
                }
            }

            resultados.close();
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Ocurrió un error con la base de datos: " + error.getMessage(), Tipo.CONEXION);
        }

        return actividades;
    }

    @Override
    public ActividadDTO resultSetAObjeto (ResultSet resultados) {
        ActividadDTO actividadDTO = null;

        try {
            actividadDTO = new ActividadDTO();
            actividadDTO.setIdActividad(resultados.getInt(1));
            actividadDTO.setTitulo(resultados.getString(2));
            actividadDTO.setDescripcion(resultados.getString(3));
            actividadDTO.setTipo(ActividadDTO.TipoActividad.valueOf(resultados.getString(4)));
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Ocurrió un error con la base de datos: " + error.getMessage(), Tipo.CONEXION);
        }

        return actividadDTO;
    }
}
