package Logica.DAO;

import AccesoADatos.RetroalimentacionColaboracionDB;
import Logica.Dominio.*;
import Logica.ErrorDAO;
import Logica.ErrorDAO.Tipo;
import Logica.Interfaces.IRetroalimentacionColaboracionDAO;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAORetroalimentacionColaboracion implements IRetroalimentacionColaboracionDAO {
    private static final Logger BITACORA = Logger.getLogger(RetroalimentacionActividad.class.getName());

    @Override
    public int agregar (RetroalimentacionColaboracion retroalimentacion) throws ErrorDAO {
        if (!retroalimentacion.esCorrecta()) {
            throw new ErrorDAO("los datos de la colaboracion son invalidos", Tipo.VALIDACION);
        }

        if (getPorPersonaYColaboracion(retroalimentacion.getIdUsuario(), retroalimentacion.getColaboracion()).isPresent()) {
            throw new ErrorDAO("La colaboración ya fue calificada por el usuario", Tipo.DUPLICIDAD);
        }

        DAOColaboracion col = new DAOColaboracion();
        Optional<Colaboracion> colaboracion = col.getColaboracionPorId(retroalimentacion.getColaboracion());

        if (colaboracion.isEmpty()) {
            throw new ErrorDAO("La colaboración no existe", Tipo.CONSULTA);
        }
        else if (colaboracion.get().getEstado() != Colaboracion.EstadoColaboracion.en_revision) {
            throw new ErrorDAO("La colaboración no puede ser evaluada aún", Tipo.VALIDACION);
        }


        int resultado = -1;

        try {
            resultado = RetroalimentacionColaboracionDB.agregarRetroalimentacion(retroalimentacion);
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return resultado;
    }

    @Override
    public int modificar (RetroalimentacionColaboracion obj) throws ErrorDAO {
        throw new ErrorDAO("metodo no disponible para el objeto", Tipo.VALIDACION);
    }

    @Override
    public Optional<RetroalimentacionColaboracion> getPorId (Integer id) throws ErrorDAO {
        if (id < 1) {
            throw new ErrorDAO("la id proporcionada no es correcta", Tipo.VALIDACION);
        }

        ResultSet retroalimentacion = null;

        try {
            retroalimentacion = RetroalimentacionColaboracionDB.getPorId(id);
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        RetroalimentacionColaboracion retroalimentacionObj = null;

        try {
            if (retroalimentacion.next()) {
                retroalimentacionObj = resultSetAObjeto(retroalimentacion);
            }
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return Optional.ofNullable(retroalimentacionObj);
    }

    @Override
    public Optional<RetroalimentacionColaboracion> getPorPersonaYColaboracion (int idPersona, int idColaboracion) {
        if (idPersona < 1 || idColaboracion < 1) {
            throw new ErrorDAO("alguna de las id proporcionadas no es correcta", Tipo.VALIDACION);
        }
        ResultSet retroalimentacion = null;

        try {
            retroalimentacion = RetroalimentacionColaboracionDB.getPorPersonaYColaboracion(idPersona, idColaboracion);
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        RetroalimentacionColaboracion retroalimentacionObj = null;

        try {
            if (retroalimentacion.next()) {
                retroalimentacionObj = resultSetAObjeto(retroalimentacion);
            }
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return Optional.ofNullable(retroalimentacionObj);
    }

    @Override
    public List<RetroalimentacionColaboracion> getTodos () throws ErrorDAO {
        ResultSet resultados = null;

        try {
            resultados = RetroalimentacionColaboracionDB.getTodos();
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        List<RetroalimentacionColaboracion> retroalimentaciones = new ArrayList<>();

        try {
            while (resultados.next()) {
                RetroalimentacionColaboracion retroalimentacion = resultSetAObjeto(resultados);

                retroalimentaciones.add(retroalimentacion);
            }
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return retroalimentaciones;
    }

    @Override
    public RetroalimentacionColaboracion resultSetAObjeto (ResultSet resultados) {
        RetroalimentacionColaboracion retroalimentacion = new RetroalimentacionColaboracion();

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
