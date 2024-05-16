package DTO;

public class RegionDTO {
    private int id;
    private String nombre;

    public RegionDTO() {

    }

    public RegionDTO(String nombre) {
        this.nombre = nombre;
    }

    public RegionDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getNombre () {
        return this.nombre;
    }

    public void setNombre (String nombre) {
        this.nombre = nombre;
    }

    public int getId () {
        return this.id;
    }

    public void setId (int id) {
        this.id = id;
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
            igual = this.id == regionDTO.getId() && this.nombre.equals(regionDTO.getNombre())? true:false;
        }
        return igual;
    }
}
