package Logica.DAO;

import AccesoADatos.PaisDB;
import AccesoADatos.UniversidadDB;
import Logica.Dominio.Pais;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import Logica.Interfaces.IUniversidadDAO;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAOUniversidad implements IUniversidadDAO {
    private static Logger bitacora = Logger.getLogger(DAOUniversidad.class);

    @Override
    public int registrarUniversidad (Universidad universidad, Pais pais) throws ErrorDAO {
        String nombreUnivesidad = universidad.getNombre();
        String nombrePais = pais.getNombre();
        int filasAfectadas = 0;

        if (validarCadenas(new String[]{nombreUnivesidad,nombrePais})) {
            if (universidadExiste(nombreUnivesidad,nombrePais)) {
                throw new ErrorDAO("Intento de registro de universidad existente", ErrorDAO.Tipo.DUPLICIDAD);
            }
            else {
                try {
                    Pais paisOrigen = PaisDB.getPaisPorNombre(nombrePais);
                    Universidad nuevaUniversidad = new Universidad(nombreUnivesidad,paisOrigen.getId());
                    filasAfectadas = UniversidadDB.registrarUniversidad(nuevaUniversidad);
                }
                catch (SQLException error) {
                    bitacora.info(error.getMessage());
                    throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.INSERCION);
                }
            }
        }

        return filasAfectadas;
    }

    @Override
    public int editarUniversidad (String nombreActual, String nuevoNombre, String nuevoPais) throws ErrorDAO {
        int filasAfectadas = 0;

        if (validarCadenas(new String[]{nombreActual,nuevoNombre,nuevoPais})) {
            if (universidadExiste(nuevoNombre,nuevoPais)) {
                throw new ErrorDAO("Intento de modificación de universidad con datos de universidad existente", ErrorDAO.Tipo.DUPLICIDAD);
            }
            else {
                try {
                    Universidad universidad = UniversidadDB.getUniversidadPorNombre(nombreActual);
                    Pais paisOrigen = PaisDB.getPaisPorNombre(nuevoPais);
                    universidad.setNombre(nuevoNombre);
                    universidad.setIdPais(paisOrigen.getId());

                    filasAfectadas = UniversidadDB.editarUniversidad(universidad);
                }
                catch (SQLException error) {
                    bitacora.info(error.getMessage());
                    throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.MODIFICACION);
                }
            }
        }

        return filasAfectadas;
    }

    @Override
    public Optional<Universidad> getUniversidadPorNombre (String nombre) throws ErrorDAO {
        Universidad universidad = null;
        if (cadenaValida(nombre)) {
            try {
                universidad = UniversidadDB.getUniversidadPorNombre(nombre);
            }
            catch (SQLException error) {
                bitacora.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }
        return Optional.ofNullable(universidad);
    }

    @Override
    public List<Universidad> getUniversidadesPorPaisOrigen (String paisOrigen) throws ErrorDAO {
        List<Universidad> listaUniversidades = new ArrayList<>();
        if (cadenaValida(paisOrigen)) {
            try {
                listaUniversidades = UniversidadDB.getUniversidadesPorPaisOrigen(paisOrigen);
            }
            catch (SQLException error) {
                bitacora.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }
        return listaUniversidades;
    }

    @Override
    public List<Universidad> getTodasAlfabeticamente () throws ErrorDAO {
        try {
            return UniversidadDB.getTodasAlfabeticamente();
        }
        catch (SQLException error) {
            bitacora.info(error.getMessage());
            throw new ErrorDAO(error.getMessage(),ErrorDAO.Tipo.CONSULTA);
        }
    }

    public boolean esNulo (Object objeto) {
        return Optional.ofNullable(objeto)
                .isEmpty();
    }

    public boolean cadenaValida (String cadena) {
        return !esNulo(cadena) && !cadena.isBlank();
    }

    public boolean universidadExiste (String universidad, String pais) throws ErrorDAO {
        boolean existe = false;
        Universidad universidadEncontrada;

        try {
            universidadEncontrada = UniversidadDB.getUniversidadPorNombreYPais(universidad,pais);
        }
        catch (SQLException error) {
            bitacora.info(error.getMessage());
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
        }

        if (universidadEncontrada.getId() > 0) {
            existe = true;
        }

        return existe;
    }

    public boolean validarCadenas (String[] cadenas) {
        boolean validas = true;
        int i = 0;

        while (i < cadenas.length) {
            if (!cadenaValida(cadenas[i])) {
                validas = false;
                break;
            }
            i++;
        }

        return validas;
    }
}
