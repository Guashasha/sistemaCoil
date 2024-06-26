package DTO;

/**
 * La clase RegionDTO funciona como transfer object, para transferir la información desde la base de datos a capas superiores dentro de la aplicación.
 * @author pale
 */
public class RegionDTO {
    /**
     * Id con el que está registrada la región en la base de datos.
     */
    private int id;
    /**
     * Nombre de la región
     */
    private String nombre;

    public RegionDTO () {}

    /**
     * Inicializa la Región con su nombre.
     * @param nombre Nombre de la región.
     */
    public RegionDTO (String nombre) {
        this.nombre = nombre;
    }

    /**
     * Inicializa la región con su id y nombre
     * @param id Id con el que está registrada la región en la base de datos.
     * @param nombre Nombre de la región.
     */
    public RegionDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId () {
        return this.id;
    }

    public String getNombre () {
        return this.nombre;
    }

    public void setId (int id) {
        this.id = id;
    }

    public void setNombre (String nombre) {
        this.nombre = nombre;
    }

    /**
     * Verifica que el nombre de la región esté inicializado.
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
        else if (!(obj instanceof RegionDTO)) {
            igual = false;
        }
        else {
            RegionDTO regionDTO = (RegionDTO) obj;
            igual = this.id == regionDTO.getId() && this.nombre.equals(regionDTO.getNombre());
        }
        return igual;
    }
}
