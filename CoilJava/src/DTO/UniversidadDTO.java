package DTO;

public class UniversidadDTO {
    private int id;
    private String nombre;
    private int idPais;
    public static final int LONGITUD_NOMBRE = 50;

    public UniversidadDTO() {
    }

    public UniversidadDTO(int id) {
        this.id = id;
    }

    public UniversidadDTO(String nombre) {
        this.nombre = nombre;
    }

    public UniversidadDTO(String nombre, int idPais) {
        this.nombre = nombre;
        this.idPais = idPais;
    }

    public UniversidadDTO(int id, String nombre, int idPais) {
        this.id = id;
        this.nombre = nombre;
        this.idPais = idPais;
    }

    public int getId () {
        return this.id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdPais () {
        return this.idPais;
    }

    public void setIdPais (int idPais) {
        this.idPais = idPais;
    }

    public boolean nombreValido () {
        return this.nombre != null && !this.nombre.isBlank();
    }

    @Override
    public boolean equals (Object obj) {
        boolean igual;
        if (this == obj) {
            igual = true;
        }
        else if (!(obj instanceof UniversidadDTO)) {
            igual = false;
        }
        else {
            UniversidadDTO universidadDTO = (UniversidadDTO) obj;
            igual = this.id == universidadDTO.getId() && this.nombre.equals(universidadDTO.getNombre()) && this.idPais == universidadDTO.getIdPais();
        }
        return igual;
    }

}
