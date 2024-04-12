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
    public int registrarUniversidad (String universidad, String pais) throws ErrorDAO {
        int filasAfectadas = 0;

        if (validarCadenas(new String[]{universidad,pais})) {
            if (universidadExiste(universidad,pais)) {
                filasAfectadas = -1;
            }
            else {
                try {
                    Pais paisOrigen = PaisDB.getPaisPorNombre(pais);
                    Universidad nuevaUniversidad = new Universidad(universidad,paisOrigen.getId());
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
                filasAfectadas = -1;
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
        Pais paisEncontrado;

        try {
            universidadEncontrada = UniversidadDB.getUniversidadPorNombre(universidad);
            if (universidadEncontrada.getId() > 0) {
                paisEncontrado = PaisDB.getPaisPorId(universidadEncontrada.getIdPais());
                if (paisEncontrado.getId() > 0) {
                    if (universidad.equals(universidadEncontrada.getNombre()) && paisEncontrado.getNombre().equals(pais)) {
                        existe = true;
                    }
                }
            }
        }
        catch (SQLException error) {
            bitacora.info(error.getMessage());
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
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
