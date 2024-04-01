package Logica.DAO;

import AccesoADatos.UniversidadDB;
import Logica.Bitacora;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import Logica.Interfaces.IUniversidadDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAOUniversidad implements IUniversidadDAO {
    private final UniversidadDB UNIVERSIDAD_DB = new UniversidadDB();
    private static Bitacora bitacora = new Bitacora(Universidad.class.getName());

    @Override
    public int registrarUniversidad(Universidad universidad) throws ErrorDAO {
        int filasAfectadas;

        if (universidadValida(universidad) && !universidadExiste(universidad)) {
            try {
                filasAfectadas = this.UNIVERSIDAD_DB.registrarUniversidad(universidad);
            }
            catch (ErrorDAO error) {
                bitacora.escribirError(error);
                throw error;
            }
        }
        else {
            filasAfectadas = -1;
        }

        return filasAfectadas;
    }

    @Override
    public int editarUniversidad(Universidad universidad) throws ErrorDAO {
        int filasAfectadas;

        if (universidadValida(universidad) && !universidadExiste(universidad)) {
            try {
                filasAfectadas = this.UNIVERSIDAD_DB.editarUniversidad(universidad);
            }
            catch (ErrorDAO error) {
                bitacora.escribirError(error);
                throw error;
            }
        }
        else {
            filasAfectadas = -1;
        }

        return filasAfectadas;
    }

    @Override
    public Optional<Universidad> getUniversidadPorNombre(String nombre) throws ErrorDAO {
        Universidad universidad = null;
        if (!esNulo(nombre) && !nombre.isBlank()) {
            try {
                universidad = this.UNIVERSIDAD_DB.getUniversidadPorNombre(nombre);
            }
            catch (ErrorDAO error) {
                bitacora.escribirError(error);
                throw error;
            }
        }
        return Optional.ofNullable(universidad);
    }

    @Override
    public List<Universidad> getUniversidadesPorPaisOrigen(String paisOrigen) throws ErrorDAO {
        List<Universidad> listaUniversidades = new ArrayList<>();
        if (!esNulo(paisOrigen) && !paisOrigen.isBlank()) {
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

    private boolean esNulo (Object objeto) {
        return Optional.ofNullable(objeto)
                .isEmpty();
    }

    private boolean universidadValida (Universidad universidad) {
        boolean valido = false;

        if (!esNulo(universidad) || !esNulo(universidad.getPaisOrigen())) {
            String nombreUniversidad = universidad.getNombre();
            int idPais = universidad.getPaisOrigen().getId();

            if (!nombreUniversidad.isBlank() || idPais > 0) {
                valido = true;
            }
        }

        return valido;
    }

    private boolean universidadExiste (Universidad universidad) throws ErrorDAO {
        boolean existe = false;
        Universidad universidadEncontrada;

        try {
            universidadEncontrada = UNIVERSIDAD_DB.getUniversidadPorNombre(universidad.getNombre());
        }
        catch (ErrorDAO error) {
            bitacora.escribirError(error);
            throw error;
        }

        if (!esNulo(universidadEncontrada)) {
            String nombreEncontrado = universidadEncontrada.getNombre();
            int idPaisEncontrado = universidadEncontrada.getPaisOrigen().getId();
            String nombre = universidad.getNombre();
            int idPais = universidad.getPaisOrigen().getId();

            if (nombreEncontrado.equals(nombre) && idPaisEncontrado == idPais) {
                existe = true;
            }
        }

        return existe;
    }
}
