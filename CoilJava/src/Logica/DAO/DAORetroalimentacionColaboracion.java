package Logica.DAO;

import AccesoADatos.ConexionBaseDatos;
import AccesoADatos.RetroalimentacionColaboracionDB;
import Logica.Bitacora;
import Logica.Dominio.*;
import Logica.ErrorDAO;
import Logica.Interfaces.IRetroalimentacionColaboracionDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DAORetroalimentacionColaboracion implements IRetroalimentacionColaboracionDAO {
    private static final Bitacora bitacora = new Bitacora(RetroalimentacionActividad.class.getName());

    @Override
    public int agregar (RetroalimentacionColaboracion retroalimentacion) throws ErrorDAO {
        if (!validarRetroalimentacion(retroalimentacion)) {
            throw new ErrorDAO("los datos de la colaboracion son invalidos");
        }

        if (getPorPersonaYColaboracion(retroalimentacion.getIdUsuario(), retroalimentacion.getColaboracion()).isPresent()) {
            throw new Error("La colaboración ya fue calificada por el usuario");
        }

        int resultado = -1;

        try {
            resultado = RetroalimentacionColaboracionDB.agregarRetroalimentacion(retroalimentacion);
        }
        catch (ErrorDAO error) {
            bitacora.escribirError(error);

            throw error;
        }

        return resultado;
    }

    @Override
    public int modificar (RetroalimentacionColaboracion obj) throws ErrorDAO {
        return 0;
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
        return null;
    }

    @Override
    public boolean validarRetroalimentacion (RetroalimentacionColaboracion retroalimentacion) {
        boolean resultado = true;

        if (!calificacionCorrecta(retroalimentacion.getCalificacion())) {
            resultado = false;
        }

        if (!calificacionCorrecta(retroalimentacion.getHabilidadesObtenidas())) {
            resultado = false;
        }

        if (!calificacionCorrecta(retroalimentacion.getIntercambioCultural())) {
            resultado = false;
        }

        if (!calificacionCorrecta(retroalimentacion.getMejoraDelLenguaje())) {
            resultado = false;
        }

        if (!calificacionCorrecta(retroalimentacion.getTrabajoColaborativo())) {
            resultado = false;
        }

        if (!calificacionCorrecta(retroalimentacion.getMejoraFormacionProfesional())) {
            resultado = false;
        }

        if (!calificacionCorrecta(retroalimentacion.getIntercambioCultural())) {
            resultado = false;
        }

        return resultado;
    }

    @Override
    public boolean calificacionCorrecta (int calificacion) {
        return (calificacion >= 1 && calificacion <= 5);
    }
}
