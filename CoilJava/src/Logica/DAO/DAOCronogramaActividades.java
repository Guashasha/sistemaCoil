package Logica.DAO;

import AccesoADatos.CronogramaActividadDB;
import Logica.Dominio.ActividadVinculada;
import Logica.ErrorDAO;
import Logica.Interfaces.IDAO;
import jdk.jshell.spi.ExecutionControl;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAOCronogramaActividades implements IDAO<ActividadVinculada, Integer> {
    private static final Logger BITACORA = Logger.getLogger(DAOCronogramaActividades.class);

    @Override
    public int agregar (ActividadVinculada actividadVinculada) throws ErrorDAO {
        if (!actividadVinculada.getPeriodo().esCorrecto()) {
            throw new ErrorDAO("El periodo especificado es incorrecto.", ErrorDAO.Tipo.VALIDACION);
        }
        else if (!actividadVinculada.getActividad().esCorrecta()) {
            throw new ErrorDAO("La actividad es incorrecta", ErrorDAO.Tipo.VALIDACION);
        }
        else if (!actividadVinculada.getColaboracion().esValido()) {
            throw new ErrorDAO("La colaboración es incorrecta", ErrorDAO.Tipo.VALIDACION);
        }

        int resultado = -1;

        try {
            resultado = CronogramaActividadDB.agregar(actividadVinculada.getActividad(), actividadVinculada.getColaboracion(), actividadVinculada.getPeriodo());
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Error de sql: " + error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        if (resultado < -1) {
            throw new ErrorDAO("No se pudo registrar la actividad al cronograma", ErrorDAO.Tipo.INSERCION);
        }

        return 1;
    }

    @Override
    public int modificar (ActividadVinculada actividadVinculada) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("La función no está implementada en la clase DAOCronogramaActividades");
    }

    @Override
    public Optional<ActividadVinculada> getPorId (Integer id) throws ErrorDAO {
        return Optional.empty();
    }

    @Override
    public List<ActividadVinculada> getTodos () throws ErrorDAO {
        List<ActividadVinculada> actividades = new ArrayList<>();

        try {
            actividades = CronogramaActividadDB.getTodos();
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Error al recuperar las actividades", ErrorDAO.Tipo.CONEXION);
        }


        return actividades;
    }

    @Override
    public ActividadVinculada resultSetAObjeto (ResultSet resultados) {
        return null;
    }
}
