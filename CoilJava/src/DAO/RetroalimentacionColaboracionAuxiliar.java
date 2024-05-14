package DAO;

import DTO.ColaboracionDTO;
import DTO.RetroalimentacionActividadDTO;
import DTO.RetroalimentacionColaboracionDTO;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
import DAO.Interfaces.IRetroalimentacionColaboracionDAO;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RetroalimentacionColaboracionAuxiliar implements IRetroalimentacionColaboracionDAO {
    private static final Logger BITACORA = Logger.getLogger(RetroalimentacionActividadDTO.class.getName());

    @Override
    public int agregar (RetroalimentacionColaboracionDTO retroalimentacion) throws ErrorDAO {
        if (!retroalimentacion.esCorrecta()) {
            throw new ErrorDAO("los datos de la colaboracion son invalidos", Tipo.VALIDACION);
        }

        if (getPorPersonaYColaboracion(retroalimentacion.getIdUsuario(), retroalimentacion.getColaboracion()).isPresent()) {
            throw new ErrorDAO("La colaboración ya fue calificada por el usuario", Tipo.DUPLICIDAD);
        }

        ColaboracionAuxiliar col = new ColaboracionAuxiliar();
        Optional<ColaboracionDTO> colaboracion = col.getColaboracionPorId(retroalimentacion.getColaboracion());

        if (colaboracion.isEmpty()) {
            throw new ErrorDAO("La colaboración no existe", Tipo.CONSULTA);
        } else if (colaboracion.get()
                .getEstado() != ColaboracionDTO.EstadoColaboracion.enRevision) {
            throw new ErrorDAO("La colaboración no puede ser evaluada aún", Tipo.VALIDACION);
        }

        int resultado = -1;

        try {
            resultado = RetroalimentacionColaboracionDAO.agregarRetroalimentacion(retroalimentacion);
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return resultado;
    }

    @Override
    public int modificar (RetroalimentacionColaboracionDTO obj) throws ErrorDAO {
        throw new ErrorDAO("metodo no disponible para el objeto", Tipo.VALIDACION);
    }

    @Override
    public Optional<RetroalimentacionColaboracionDTO> getPorId (Integer id) throws ErrorDAO {
        if (id < 1) {
            throw new ErrorDAO("la id proporcionada no es correcta", Tipo.VALIDACION);
        }

        ResultSet retroalimentacion = null;

        try {
            retroalimentacion = RetroalimentacionColaboracionDAO.getPorId(id);
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        RetroalimentacionColaboracionDTO retroalimentacionObj = null;

        try {
            if (retroalimentacion != null && retroalimentacion.next()) {
                retroalimentacionObj = resultSetAObjeto(retroalimentacion);

                retroalimentacion.close();
            }
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return Optional.ofNullable(retroalimentacionObj);
    }

    @Override
    public Optional<RetroalimentacionColaboracionDTO> getPorPersonaYColaboracion (int idPersona, int idColaboracion) {
        if (idPersona < 1 || idColaboracion < 1) {
            throw new ErrorDAO("alguna de las id proporcionadas no es correcta", Tipo.VALIDACION);
        }
        ResultSet retroalimentacion = null;

        try {
            retroalimentacion = RetroalimentacionColaboracionDAO.getPorPersonaYColaboracion(idPersona, idColaboracion);
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        RetroalimentacionColaboracionDTO retroalimentacionObj = null;

        try {
            if (retroalimentacion != null && retroalimentacion.next()) {
                retroalimentacionObj = resultSetAObjeto(retroalimentacion);

                retroalimentacion.close();
            }
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return Optional.ofNullable(retroalimentacionObj);
    }

    @Override
    public List<RetroalimentacionColaboracionDTO> getTodos () throws ErrorDAO {
        ResultSet resultados = null;

        try {
            resultados = RetroalimentacionColaboracionDAO.getTodos();
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        List<RetroalimentacionColaboracionDTO> retroalimentaciones = new ArrayList<>();

        if (resultados == null) {
            return retroalimentaciones;
        }

        try {
            while (resultados.next()) {
                RetroalimentacionColaboracionDTO retroalimentacion = resultSetAObjeto(resultados);

                retroalimentaciones.add(retroalimentacion);
            }

            resultados.close();
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return retroalimentaciones;
    }

    @Override
    public RetroalimentacionColaboracionDTO resultSetAObjeto (ResultSet resultados) {
        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();

        try {
            retroalimentacion.setIdRetroalimentacion(resultados.getInt(1));
            retroalimentacion.setInteraccionConPar(resultados.getInt(2));
            retroalimentacion.setComentario(resultados.getString(3));
            retroalimentacion.setIdUsuario(resultados.getInt(4));
            retroalimentacion.setHabilidadesObtenidas(resultados.getInt(5));
            retroalimentacion.setCalificacion(resultados.getInt(6));
            retroalimentacion.setIntercambioCultural(resultados.getInt(7));
            retroalimentacion.setMejoraDelLenguaje(resultados.getInt(8));
            retroalimentacion.setTrabajoColaborativo(resultados.getInt(9));
            retroalimentacion.setMejoraFormacionProfesional(resultados.getInt(10));
            retroalimentacion.setColaboracion(resultados.getInt(11));
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return retroalimentacion;
    }
}
