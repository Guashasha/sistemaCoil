package Logica.DAO;

import AccesoADatos.PaisDB;
import AccesoADatos.UniversidadDB;
import Logica.Bitacora;
import Logica.Dominio.Pais;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import Logica.Interfaces.IUniversidadDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAOUniversidad implements IUniversidadDAO {
    private final UniversidadDB UNIVERSIDAD_DB = new UniversidadDB();
    private final PaisDB PAIS_DB = new PaisDB();
    //private static Bitacora bitacora = new Bitacora(Universidad.class.getName());

    @Override
    public int registrarUniversidad (String universidad, String pais) throws ErrorDAO {
        int filasAfectadas = 0;

        if (validarCadenas(new String[]{universidad,pais})) {
            if (universidadExiste(universidad,pais)) {
                filasAfectadas = -1;
            }
            else {
                try {
                    Pais paisOrigen = this.PAIS_DB.getPaisPorNombre(pais);
                    Universidad nuevaUniversidad = new Universidad(universidad,paisOrigen.getId());
                    filasAfectadas = this.UNIVERSIDAD_DB.registrarUniversidad(nuevaUniversidad);
                }
                catch (ErrorDAO error) {
                    //bitacora.escribirError(error);
                    throw error;
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
                    Universidad universidad = this.UNIVERSIDAD_DB.getUniversidadPorNombre(nombreActual);
                    Pais paisOrigen = this.PAIS_DB.getPaisPorNombre(nuevoPais);
                    universidad.setNombre(nuevoNombre);
                    universidad.setIdPais(paisOrigen.getId());

                    filasAfectadas = this.UNIVERSIDAD_DB.editarUniversidad(universidad);
                }
                catch (ErrorDAO error) {
                    //bitacora.escribirError(error);
                    throw error;
                }
            }
        }

        return filasAfectadas;
    }

    @Override
    public Universidad getUniversidadPorNombre (String nombre) throws ErrorDAO {
        Universidad universidad = new Universidad(0);
        if (cadenaValida(nombre)) {
            try {
                universidad = this.UNIVERSIDAD_DB.getUniversidadPorNombre(nombre);
            }
            catch (ErrorDAO error) {
                //bitacora.escribirError(error);
                throw error;
            }
        }
        return universidad;
    }

    @Override
    public List<Universidad> getUniversidadesPorPaisOrigen(String paisOrigen) throws ErrorDAO {
        List<Universidad> listaUniversidades = new ArrayList<>();
        if (cadenaValida(paisOrigen)) {
            try {
                listaUniversidades = this.UNIVERSIDAD_DB.getUniversidadesPorPaisOrigen(paisOrigen);
            }
            catch (ErrorDAO error) {
                //bitacora.escribirError(error);
                throw error;
            }
        }
        return listaUniversidades;
    }

    @Override
    public List<Universidad> getTodasAlfabeticamente() throws ErrorDAO {
        try {
            return this.UNIVERSIDAD_DB.getTodasAlfabeticamente();
        }
        catch (ErrorDAO error) {
            //bitacora.escribirError(error);
            throw error;
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
            universidadEncontrada = UNIVERSIDAD_DB.getUniversidadPorNombre(universidad);
            if (universidadEncontrada.getId() > 0) {
                paisEncontrado = PAIS_DB.getPaisPorId(universidadEncontrada.getIdPais());
                if (paisEncontrado.getId() > 0) {
                    if (universidad.equals(universidadEncontrada.getNombre()) && paisEncontrado.getNombre().equals(pais)) {
                        existe = true;
                    }
                }
            }
        }
        catch (ErrorDAO error) {
            //bitacora.escribirError(error);
            throw error;
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
