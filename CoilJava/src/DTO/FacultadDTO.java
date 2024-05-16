package DTO;

public class FacultadDTO {
    private int id;
    private String nombre;
    private int idRegion;

    public FacultadDTO() {

    }

    public FacultadDTO(int id) {
        this.id = id;
    }

    public FacultadDTO(String nombre, int idRegion) {
        this.nombre = nombre;
        this.idRegion = idRegion;
    }

    public FacultadDTO(int id, String nombre, int idRegion) {
        this.id = id;
        this.nombre = nombre;
        this.idRegion = idRegion;
    }

    public int getId () {
        return this.id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getNombre () {
        return nombre;
    }

    public void setNombre (String nombre) {
        this.nombre = nombre;
    }

    public void setIdRegion (int idRegion) {
        this.idRegion = idRegion;
    }

    public int getIdRegion () {
        return this.idRegion;
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
            igual = this.id == facultadDTO.getId() && this.nombre.equals(facultadDTO.getNombre()) && this.idRegion == facultadDTO.getIdRegion()? true:false;
        }
        return igual;
    }
}
