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
        int filasAfectadas;

        if (validarCadenas(new String[]{universidad.getNombre(), pais.getNombre()})) {
            String nombreUnivesidad = universidad.getNombre()
                    .trim();
            String nombrePais = pais.getNombre()
                    .trim();

            if (universidadExiste(nombreUnivesidad,nombrePais)) {
                throw new ErrorDAO("La universidad que intentas registrar ya ha sido registrada anteriormente", ErrorDAO.Tipo.DUPLICIDAD);
            }
            else {
                try {
                    Pais paisOrigen = PaisDB.getPaisPorNombre(nombrePais);
                    Universidad nuevaUniversidad = new Universidad(nombreUnivesidad,paisOrigen.getId());
                    filasAfectadas = UniversidadDB.registrarUniversidad(nuevaUniversidad);
                }
                catch (SQLException error) {
                    bitacora.info(error.getMessage());
                    throw new ErrorDAO("Error al establecer conexión con la base de datos", ErrorDAO.Tipo.CONEXION);
                }
            }
        }
        else {
            throw new ErrorDAO("Campos vacíos", ErrorDAO.Tipo.VALIDACION);
        }

        return filasAfectadas;
    }

    @Override
    public int editarUniversidad (Universidad universidadActual, Universidad nuevaUniversidad, Pais nuevoPais) throws ErrorDAO {
        int filasAfectadas;

        if (validarCadenas(new String[]{universidadActual.getNombre(),nuevaUniversidad.getNombre(),nuevoPais.getNombre()})) {
            String nombreActual = universidadActual.getNombre()
                    .trim();
            String nuevoNombre = nuevaUniversidad.getNombre()
                    .trim();
            String nombreNuevoPais = nuevoPais.getNombre()
                    .trim();

            if (universidadExiste(nuevoNombre,nombreNuevoPais)) {
                throw new ErrorDAO("La institución " + nuevoNombre + " ya existe", ErrorDAO.Tipo.DUPLICIDAD);
            }
            else {
                try {
                    Universidad universidad = UniversidadDB.getUniversidadPorNombre(nombreActual);
                    Pais paisOrigen = PaisDB.getPaisPorNombre(nombreNuevoPais);
                    universidad.setNombre(nuevoNombre);
                    universidad.setIdPais(paisOrigen.getId());

                    filasAfectadas = UniversidadDB.editarUniversidad(universidad);
                }
                catch (SQLException error) {
                    bitacora.info(error.getMessage());
                    throw new ErrorDAO("Error al establecer conexión con la base de datos", ErrorDAO.Tipo.CONEXION);
                }
            }
        }
        else {
            throw new ErrorDAO("Campos vacíos", ErrorDAO.Tipo.VALIDACION);
        }

        return filasAfectadas;
    }

    @Override
    public Optional<Universidad> getUniversidadPorNombre (String nombre) throws ErrorDAO {
        Universidad universidad = null;
        if (cadenaValida(nombre.trim())) {
            try {
                universidad = UniversidadDB.getUniversidadPorNombre(nombre.trim());
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
        if (cadenaValida(paisOrigen.trim())) {
            try {
                listaUniversidades = UniversidadDB.getUniversidadesPorPaisOrigen(paisOrigen.trim());
            }
            catch (SQLException error) {
                bitacora.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
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
            throw new ErrorDAO(error.getMessage(),ErrorDAO.Tipo.CONEXION);
        }
    }

    @Override
    public Optional<Universidad> getUniversidadPorId (int id) throws ErrorDAO {
        Universidad universidad = null;
        if (id > 0) {
            try {
                universidad = UniversidadDB.getUniversidadPorId(id);
            }
            catch (SQLException error) {
                bitacora.info(error.getMessage());
                throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
            }
        }
        return Optional.ofNullable(universidad);
    }
    
    public boolean universidadExiste (String universidad, String pais) throws ErrorDAO {
        boolean existe = false;
        Universidad universidadEncontrada;

        try {
            universidadEncontrada = UniversidadDB.getUniversidadPorNombreYPais(universidad,pais);
        }
        catch (SQLException error) {
            bitacora.info(error.getMessage());
            throw new ErrorDAO("Error al establecer conexión con la base de datos", ErrorDAO.Tipo.CONEXION);
        }

        if (universidadEncontrada.getId() > 0) {
            existe = true;
        }

        return existe;
    }

    public static boolean esNulo (Object objeto) {
        return Optional.ofNullable(objeto)
                .isEmpty();
    }

    public static boolean cadenaValida (String cadena) {
        return !esNulo(cadena) && !cadena.isBlank();
    }

    public static boolean validarCadenas (String[] cadenas) {
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
