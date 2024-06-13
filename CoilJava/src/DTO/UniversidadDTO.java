package DTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * La clase UniversidadDTO funciona como transfer object, para transferir la información desde la base de datos a capas
 * superiores dentro de la aplicación.
 * @author pale
 */
public class UniversidadDTO {
    /**
     * Id con el que está registrado la universidad en la base de datos.
     */
    private int id;
    /**
     * Nombre de la universidad.
     */
    private String nombre;
    /**
     * Id del pais al que pertenece la universidad.
     */
    private int idPais;
    /**
     * Límite de longitud del nombre de la universidad para ser registrada en la base de datos.
     */
    public static final int LONGITUD_NOMBRE = 50;

    public UniversidadDTO() {}

    /**
     * inicializa la universidad solamente con su id
     * @param id id con el que está registrado la universidad en la base de datos
     */
    public UniversidadDTO(int id) {
        this.id = id;
    }

    /**
     * Inicializa la universidad solamente con su nombre
     * @param nombre nombre de la universidad
     */
    public UniversidadDTO(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Inicializa la universidad con su nombre y el id del país al cual pertenece.
     * @param nombre nombre de la universidad
     * @param idPais id del pais al que pertenece la universidad
     */
    public UniversidadDTO(String nombre, int idPais) {
        this.nombre = nombre;
        this.idPais = idPais;
    }

    /**
     * Inicializa la universidad con su id, su nombre y el id del pais al cual pertenece.
     * @param id id de la universidad.
     * @param nombre nombre de la universidad.
     * @param idPais id del país al cual pertenece la universidad.
     */
    public UniversidadDTO(int id, String nombre, int idPais) {
        this.id = id;
        this.nombre = nombre;
        this.idPais = idPais;
    }

    public int getId () {
        return this.id;
    }

    public int getIdPais () {
        return this.idPais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setId (int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdPais (int idPais) {
        this.idPais = idPais;
    }

    /**
     * Determina que el nombre de la universidad esté inicializado y que tenga un formato válido.
     * @return true si el nombre está inicializado y en formato válido, de otra manera false.
     */
    public boolean nombreValido () {
        boolean nombreValido;
        if (this.nombre == null || this.nombre.isBlank()) {
            nombreValido = false;
        }
        else {
            String expresionRegular = "^[A-Za-záéíóúÁÉÍÓÚñÑ][-A-Za-záéíóúÁÉÍÓÚñÑ\\s]*[A-Za-záéíóúÁÉÍÓÚñÑ]$";
            Pattern patron = Pattern.compile(expresionRegular);
            Matcher matcher = patron.matcher(this.nombre.trim());
            nombreValido = matcher.matches();
        }
        return nombreValido;
    }

    @Override
    public boolean equals (Object obj) {
        boolean igual;
        if (this == obj) {
            igual = true;
        }
        else if (!(obj instanceof UniversidadDTO universidad)) {
            igual = false;
        }
        else {
            igual = this.id == universidad.getId() && this.nombre.equals(universidad.getNombre()) && this.idPais == universidad.getIdPais();
        }
        return igual;
    }

}
