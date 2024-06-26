package DTO;

/**
 * La clase FacultadDTO funciona como transfer object, para transferir la información desde la base de datos a capas
 * superiores dentro de la aplicación.
 * @author pale
 */
public class FacultadDTO {
    /**
     * Id con el que está registrada la facultad en la base de datos.
     */
    private int id;
    /**
     * Nombre de la facultad
     */
    private String nombre;
    /**
     * Id de la región a la cual está asociada la facultad en la base de datos.
     */
    private int idRegion;

    public FacultadDTO() {}

    /**
     * Inicializa la facultad con su nombre.
     * @param nombre Nombre de la facultda.
     */
    public FacultadDTO (String nombre) {
        this.nombre = nombre;
    }

    /**
     * Inicializa la facultad con su id, nombre e id de su región.
     * @param id con el que está registrada la facultad en la base de datos.
     * @param nombre nombre de la facultad.
     * @param idRegion id de la región a la cual está asociada la facultad.
     */
    public FacultadDTO(int id, String nombre, int idRegion) {
        this.id = id;
        this.nombre = nombre;
        this.idRegion = idRegion;
    }

    public int getId () {
        return this.id;
    }

    public String getNombre () {
        return nombre;
    }

    public int getIdRegion () {
        return this.idRegion;
    }

    public void setId (int id) {
        this.id = id;
    }

    public void setNombre (String nombre) {
        this.nombre = nombre;
    }

    public void setIdRegion (int idRegion) {
        this.idRegion = idRegion;
    }

    /**
     * Verifica que el nombre de la facultad esté inicializado.
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
        else if (!(obj instanceof FacultadDTO)) {
            igual = false;
        }
        else {
            FacultadDTO facultadDTO = (FacultadDTO) obj;
            igual = this.id == facultadDTO.getId() && this.nombre.equals(facultadDTO.getNombre()) && this.idRegion == facultadDTO.getIdRegion();
        }
        return igual;
    }
}
