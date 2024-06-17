package DAO;

import DTO.PaisDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UniversidadAuxiliar {
    private final UniversidadDAO UNIVERSIDAD_DAO = new UniversidadDAO();
    private final PaisDAO PAIS_DAO = new PaisDAO();

    public int registrarUniversidad (UniversidadDTO universidad, PaisDTO pais) throws ErrorDAO {
        if (esNulo(universidad) || esNulo(pais)) {
            throw new ErrorDAO("Algo salió mal, inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }
        int filasAfectadas;

        if (universidad.nombreValido() && pais.nombreValido()) {
            String nombreUniversidad = universidad.getNombre()
                    .trim();
            String nombrePais = pais.getNombre()
                    .trim();

            if (universidadExiste(nombreUniversidad,nombrePais)) {
                throw new ErrorDAO("La universidad que intentas registrar ya ha sido registrada anteriormente", ErrorDAO.Tipo.DUPLICIDAD);
            }
            else {
                filasAfectadas = UNIVERSIDAD_DAO.registrarUniversidad(prepararUniversidadNueva(nombreUniversidad,nombrePais));
            }
        }
        else {
            throw new ErrorDAO("Los nombres no pueden contener caracteres especiales.\nSolo son válidas letras del alfabeto en español y guiones en el medio", ErrorDAO.Tipo.VALIDACION);
        }

        return filasAfectadas;
    }

    public int editarUniversidad (UniversidadDTO universidadActual, UniversidadDTO nuevaUniversidad, PaisDTO nuevoPais) throws ErrorDAO {
        if (esNulo(universidadActual) || esNulo(nuevaUniversidad) || esNulo(nuevoPais)) {
            throw new ErrorDAO("Algo salió mal, inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }
        int filasAfectadas;

        if (universidadActual.nombreValido() && nuevaUniversidad.nombreValido() && nuevoPais.nombreValido()) {
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
                filasAfectadas = UNIVERSIDAD_DAO.editarUniversidad(prepararUniversidadEditada(nombreActual,nuevoNombre,nombreNuevoPais));
            }
        }
        else {
            throw new ErrorDAO("Los nombres no pueden contener caracteres especiales.\nSolo son válidas letras del alfabeto en español y guiones en el medio", ErrorDAO.Tipo.VALIDACION);
        }

        return filasAfectadas;
    }

    public Optional<UniversidadDTO> getUniversidadPorNombre (String nombre) throws ErrorDAO {
        Optional<UniversidadDTO> universidad;
        UniversidadDTO universidadABuscar = new UniversidadDTO(nombre);
        if (universidadABuscar.nombreValido()) {
            universidad = UNIVERSIDAD_DAO.getUniversidadPorNombre(nombre.trim());
        }
        else {
            throw new ErrorDAO("Los nombres no pueden contener caracteres especiales.\nSolo son válidas letras del alfabeto en español y guiones en el medio", ErrorDAO.Tipo.VALIDACION);
        }
        return universidad;
    }

    public List<UniversidadDTO> getUniversidadesPorPaisOrigen (String pais) throws ErrorDAO {
        List<UniversidadDTO> listaUniversidades = new ArrayList<>();
        PaisDTO paisOrigen = new PaisDTO(pais);
        if (paisOrigen.nombreValido()) {
            listaUniversidades = UNIVERSIDAD_DAO.getUniversidadesPorPaisOrigen(pais.trim());
        }
        return listaUniversidades;
    }

    public List<UniversidadDTO> getUniversidadesPorNombre (UniversidadDTO universidad) throws ErrorDAO {
        if (esNulo(universidad)) {
            throw new ErrorDAO("Algo salió mal, inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }
        List<UniversidadDTO> listaUniversidades;

        if (universidad.nombreValido()) {
            listaUniversidades = UNIVERSIDAD_DAO.getUniversidadesPorNombre(universidad.getNombre()
                                                                                      .trim());
        }
        else {
            throw new ErrorDAO("Los nombres no pueden contener caracteres especiales.\nSolo son válidas letras del alfabeto en español y guiones en el medio", ErrorDAO.Tipo.VALIDACION);
        }

        return listaUniversidades;
    }

    public Optional<UniversidadDTO> getUniversidadPorId (int id) throws ErrorDAO {
        Optional<UniversidadDTO> universidad;
        if (id > 0) {
            universidad = UNIVERSIDAD_DAO.getUniversidadPorId(id);
        }
        else {
            throw new ErrorDAO("ID inválido", ErrorDAO.Tipo.VALIDACION);
        }
        return universidad;
    }

    public boolean universidadExiste (String universidad, String pais) throws ErrorDAO {
        boolean existe = false;
        UniversidadDTO universidadABuscar = new UniversidadDTO(universidad);
        PaisDTO paisOrigen = new PaisDTO(pais);

        if (universidadABuscar.nombreValido() && paisOrigen.nombreValido()) {
            Optional<UniversidadDTO> universidadDTO = UNIVERSIDAD_DAO.getUniversidadPorNombreYPais(universidad,pais);

            if (universidadDTO.isPresent()) {
                existe = true;
            }
        }
        else {
            throw new ErrorDAO("Los nombres no pueden estar vacíos ni contener caracteres especiales.\nSolo son válidas letras del alfabeto en español y guiones en el medio", ErrorDAO.Tipo.VALIDACION);
        }

        return existe;
    }

    private static boolean esNulo (Object objeto) {
        return objeto == null;
    }

    private UniversidadDTO prepararUniversidadEditada (String nombreActual, String nuevoNombre, String nombreNuevoPais) throws ErrorDAO {
        UniversidadDTO universidadEditada;
        Optional<UniversidadDTO> universidadActualOptional = UNIVERSIDAD_DAO.getUniversidadPorNombre(nombreActual);
        Optional<PaisDTO> paisNuevoOptional = PAIS_DAO.getPaisPorNombre(nombreNuevoPais);

        if (universidadActualOptional.isPresent() && paisNuevoOptional.isPresent()){
            PaisDTO paisOrigen = paisNuevoOptional.get();
            universidadEditada = universidadActualOptional.get();
            universidadEditada.setNombre(nuevoNombre);
            universidadEditada.setIdPais(paisOrigen.getId());
        }
        else {
            throw new ErrorDAO("Ocurrió un error. Inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }

        return universidadEditada;
    }

    private UniversidadDTO prepararUniversidadNueva (String nombre, String pais) throws ErrorDAO {
        UniversidadDTO nuevaUniversidad;
        Optional<PaisDTO> paisOptional = PAIS_DAO.getPaisPorNombre(pais);

        if (paisOptional.isPresent()) {
            PaisDTO paisOrigen = paisOptional.get();
            nuevaUniversidad = new UniversidadDTO(nombre, paisOrigen.getId());
        }
        else {
            throw new ErrorDAO("Ocurrió un error. Inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }

        return nuevaUniversidad;
    }
}
