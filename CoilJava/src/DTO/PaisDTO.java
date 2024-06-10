package DTO;

/**
 * La clase PaisDTO funciona como transfer object, para transferir la información desde la base de datos a capas
 * superiores dentro de la aplicación.
 * @author pale
 */
public class PaisDTO {
    /**
     * Id con el que está registrado el país en la base de datos.
     */
    private int id;
    /**
     * iso que describe el nombre del país.
     */
    private String iso;
    /**
     * Nombre del país.
     */
    private String nombre;

    public PaisDTO() {
    }

    /**
     * Inicializa el país solamente con el id al que está asociado en la base de datos
     * @param id id con el que está registrado el país en la base de datos
     */
    public PaisDTO(int id) {
        this.id = id;
    }

    /**
     * Inicializa el país con su nombre
     * @param nombre nombre del país.
     */
    public PaisDTO(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Inicializa el pasís con su id y nombre
     * @param id id con el que está registrado el país en la base de datos.
     * @param nombre nombre del país.
     */
    public PaisDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Inicializa el país con su id, iso y nombre.
     * @param id id con el que está registrado el país en la base de datos.
     * @param iso iso que describe el nombre del país.
     * @param nombre nombre del país
     */
    public PaisDTO(int id, String iso, String nombre) {
        this.id = id;
        this.iso = iso;
        this.nombre = nombre;
    }

    public void setId (int id) {
        this.id = id;
    }

    public void setIso (String iso) {
        this.iso = iso;
    }

    public void setNombre (String nombre) {
        this.nombre = nombre;
    }

    public int getId () {
        return this.id;
    }

    public String getIso () {
        return this.iso;
    }

    public String getNombre () {
        return this.nombre;
    }

    /**
     * Verifica que el nombre del país esté inicializado.
     * @return true si el nombre está inicializado y no es una cadena vacía, de otra manera false.
     */
    public boolean nombreValido () {
        return this.nombre != null && !this.nombre.isBlank();
    }

    @Override
    public boolean equals (Object obj) {
        boolean igual;
        if (this == obj) {
            igual = true;
        }
        else if (!(obj instanceof PaisDTO)) {
            igual = false;
        }
        else {
            PaisDTO paisDTO = (PaisDTO) obj;
            igual = this.id == paisDTO.getId() && this.iso.equals(paisDTO.getIso()) && this.nombre.equals(paisDTO.getNombre());
        }
        return igual;
    }
}
