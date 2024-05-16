package DAO;

import DTO.ActividadDTO;
import DTO.RetroalimentacionActividadDTO;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
import DAO.Interfaces.IRetroalimentacionActividadDAO;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class RetroalimentacionActividadAuxiliar implements IRetroalimentacionActividadDAO {
    private static final Logger BITACORA = Logger.getLogger(RetroalimentacionActividadDTO.class.getName());

    @Override
    public int agregar (RetroalimentacionActividadDTO retroalimentacion) throws ErrorDAO {
        if (!retroalimentacion.esCorrecto()) {
            throw new ErrorDAO("La retroalimentacion es incorrecta", Tipo.VALIDACION);
        }

        if (getPorPersonaYActividad(retroalimentacion.getIdUsuario(), retroalimentacion.getIdActividad()).isPresent()) {
            throw new ErrorDAO("La actividad ya fue calificada por el usuario", Tipo.DUPLICIDAD);
        }

        ActividadAuxiliar act = new ActividadAuxiliar();
        Optional<ActividadDTO> actividad = act.getPorId(retroalimentacion.getIdActividad());

        if (actividad.isEmpty()) {
            throw new ErrorDAO("La actividad no existe", Tipo.CONSULTA);
        }

        int resultado = -1;

        try {
            resultado = RetroalimentacionActividadDAO.agregarRetroalimentacion(retroalimentacion);
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return resultado;
    }

    @Override
    public int modificar (RetroalimentacionActividadDTO retroalimentacion) throws ErrorDAO {
        throw new ErrorDAO("metodo no disponible para el objeto", Tipo.VALIDACION);
    }

    @Override
    public Optional<RetroalimentacionActividadDTO> getPorId (Integer id) throws ErrorDAO {
        if (id < 1) {
            throw new ErrorDAO("El id es invalido" + id, Tipo.VALIDACION);
        }

        ResultSet rsRetroalimentacion = null;

        try {
            rsRetroalimentacion = RetroalimentacionActividadDAO.getPorId(id);
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }


        RetroalimentacionActividadDTO objRetroalimentacion = null;

        try {
            if (rsRetroalimentacion != null && rsRetroalimentacion.next()) {
                objRetroalimentacion = resultSetAObjeto(rsRetroalimentacion);

                rsRetroalimentacion.close();
            }
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return Optional.ofNullable(objRetroalimentacion);
    }

    @Override
    public List<RetroalimentacionActividadDTO> getTodos () throws ErrorDAO {
        ResultSet resultsRetroalimentaciones = null;

        try {
            resultsRetroalimentaciones = RetroalimentacionActividadDAO.getTodos();
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        ArrayList<RetroalimentacionActividadDTO> retroalimentaciones = new ArrayList<>();

        if (resultsRetroalimentaciones == null) {
            return retroalimentaciones;
        }

        try {
            while (resultsRetroalimentaciones.next()) {
                RetroalimentacionActividadDTO retroalimentacion = resultSetAObjeto(resultsRetroalimentaciones);

                if (retroalimentacion.esCorrecto()) {
                    retroalimentaciones.add(retroalimentacion);
                }
            }

            resultsRetroalimentaciones.close();
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return retroalimentaciones;
    }

    @Override
    public Optional<RetroalimentacionActividadDTO> getPorPersonaYActividad (int idPersona, int idActividad) throws ErrorDAO {
        if (idPersona < 1 || idActividad < 1) {
            throw new ErrorDAO("Las id's ingresadas son incorrectas", Tipo.VALIDACION);
        }

        ResultSet resultados = null;

        try {
            resultados = RetroalimentacionActividadDAO.getPorPersonaYActividad(idPersona, idActividad);
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        RetroalimentacionActividadDTO retroalimentacion = null;

        try {
            if (resultados != null && resultados.next()) {
                retroalimentacion = resultSetAObjeto(resultados);

                resultados.close();
            }
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return Optional.ofNullable(retroalimentacion);
    }

    @Override
    public RetroalimentacionActividadDTO resultSetAObjeto (ResultSet resultados) {
        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();

        try {
            retroalimentacion.setIdRetroalimentacion(resultados.getInt(1));
            retroalimentacion.setInteraccionConPar(resultados.getInt(2));
            retroalimentacion.setComentario(resultados.getString(3));
            retroalimentacion.setDificultad(resultados.getInt(4));
            retroalimentacion.setInteres(resultados.getInt(5));
            retroalimentacion.setIdUsuario(resultados.getInt(6));
            retroalimentacion.setIdActividad(resultados.getInt(7));
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return retroalimentacion;
    }
}
