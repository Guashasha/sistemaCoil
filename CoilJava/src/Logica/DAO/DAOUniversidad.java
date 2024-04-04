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
    private static Bitacora bitacora = new Bitacora(Universidad.class.getName());

    @Override
    public int registrarUniversidad (String universidad, String pais) throws ErrorDAO {
        int filasAfectadas;

        if (!cadenaValida(universidad) || !cadenaValida(pais)) {
            filasAfectadas = -1;
        }
        else if (universidadExiste(universidad,pais)) {
            filasAfectadas = -2;
        }
        //else if (!paisExiste(pais)) {
        //    filasAfectadas = -3;
        //}
        else {
            try {
                Pais paisOrigen = this.PAIS_DB.getPaisPorNombre(pais);
                Universidad nuevaUniversidad = new Universidad(universidad,paisOrigen.getId());
                filasAfectadas = this.UNIVERSIDAD_DB.registrarUniversidad(nuevaUniversidad);
            }
            catch (ErrorDAO error) {
                bitacora.escribirError(error);
                throw error;
            }
        }

        return filasAfectadas;
    }

    @Override
    public int editarUniversidad (String universidad, String nuevoNombre, String nuevoPais) throws ErrorDAO {
        int filasAfectadas;

        if (!cadenaValida(universidad) || !cadenaValida(nuevoNombre) || !cadenaValida(nuevoPais)) {
            filasAfectadas = -1;
        }
        else if (universidadExiste(nuevoNombre,nuevoPais)) {
            filasAfectadas = -2;
        }
        //else if (!paisExiste(pais)) {
        //    filasAfectadas = -3;
        //}
        else {
            try {
                Pais paisOrigen = this.PAIS_DB.getPaisPorNombre(nuevoPais);
                Universidad universidadActual = this.UNIVERSIDAD_DB.getUniversidadPorNombre(universidad);
                
                Universidad nuevaUniversidad = new Universidad(universidad,paisOrigen.getId(),universidadActual.);
                filasAfectadas = this.UNIVERSIDAD_DB.editarUniversidad(nuevaUniversidad);
            }
            catch (ErrorDAO error) {
                bitacora.escribirError(error);
                throw error;
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
                bitacora.escribirError(error);
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
                bitacora.escribirError(error);
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
            bitacora.escribirError(error);
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
    public boolean universidadValida (Universidad universidad) {
        boolean valido = false;

        if (!esNulo(universidad)) {
            String nombreUniversidad = universidad.getNombre();
            int idPais = universidad.getIdPais();

            if (cadenaValida(nombreUniversidad) || idPais > 0) {
                valido = true;
            }
        }

        return valido;
    }

    public boolean universidadExiste (String universidad, String pais) throws ErrorDAO {
        boolean existe = false;
        Universidad universidadEncontrada;
        Pais paisEncontrado;

        try {
            universidadEncontrada = UNIVERSIDAD_DB.getUniversidadPorNombre(universidad);
            if (universidadEncontrada.getId() != 0) {
                paisEncontrado = PAIS_DB.getPaisPorId(universidadEncontrada.getIdPais());
                if (paisEncontrado.getId() != 0) {
                    if (universidad.equals(universidadEncontrada.getNombre()) && pais.equals(paisEncontrado.getNombre())) {
                        existe = true;
                    }
                }
            }
        }
        catch (ErrorDAO error) {
            bitacora.escribirError(error);
            throw error;
        }

        return existe;
    }

    private boolean paisExiste (String pais) throws ErrorDAO {
        boolean existe = false;
        Pais paisEnconstrado;

        try {
            paisEnconstrado = PAIS_DB.getPaisPorNombre(pais);
        }
        catch (ErrorDAO error) {
            bitacora.escribirError(error);
            throw error;
        }

        if (paisEnconstrado.getId() != 0) {
            if (pais.equals(paisEnconstrado.getNombre())) {
                existe = true;
            }
        }

        return existe;
    }
}
