package Logica.DAO;

import AccesoADatos.RetroalimentacionColaboracionDB;
import Logica.Bitacora;
import Logica.Dominio.*;
import Logica.ErrorDAO;
import Logica.ErrorDAO.Tipo;
import Logica.Interfaces.IRetroalimentacionColaboracionDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DAORetroalimentacionColaboracion implements IRetroalimentacionColaboracionDAO {
    private static final Bitacora bitacora = new Bitacora(RetroalimentacionActividad.class.getName());

    @Override
    public int agregar (RetroalimentacionColaboracion retroalimentacion) throws ErrorDAO {
        if (!retroalimentacion.esCorrecta()) {
            throw new ErrorDAO("los datos de la colaboracion son invalidos", Tipo.VALIDACION);
        }

        if (getPorPersonaYColaboracion(retroalimentacion.getIdUsuario(), retroalimentacion.getColaboracion()).isPresent()) {
            throw new ErrorDAO("La colaboración ya fue calificada por el usuario", Tipo.DUPLICIDAD);
        }

        int resultado = -1;

        try {
            resultado = RetroalimentacionColaboracionDB.agregarRetroalimentacion(retroalimentacion);
        }
        catch (SQLException error) {
            bitacora.escribirError(error);
        }

        return resultado;
    }

    @Override
    public int modificar (RetroalimentacionColaboracion obj) throws ErrorDAO {
        throw new ErrorDAO("metodo no disponible para el objeto", Tipo.VALIDACION);
    }

    @Override
    public Optional<RetroalimentacionColaboracion> getPorId (Integer y) throws ErrorDAO {
        return Optional.empty();
    }

    @Override
    public Optional<RetroalimentacionColaboracion> getPorPersonaYColaboracion (int idPersona, int idColaboracion) {
        return Optional.empty();
    }

    @Override
    public List<RetroalimentacionColaboracion> getTodos () throws ErrorDAO {
        return null;
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
            bitacora.escribirError(error);
        }

        return retroalimentacion;
    }
}
