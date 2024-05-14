package DTO;

public class PaisDTO {
    private int id;
    private String iso;
    private String nombre;

    public PaisDTO() {
    }

    public PaisDTO(int id) {
        this.id = id;
    }

    public PaisDTO(String nombre) {
        this.nombre = nombre;
    }

    public PaisDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public PaisDTO(int id, String iso, String nombre) {
        this.id = id;
        this.iso = iso;
        this.nombre = nombre;
    }

    public void setId (int id) {
        this.id = id;
    }

    public int getId () {
        return this.id;
    }

    public void setIso (String iso) {
        this.iso = iso;
    }

    public String getIso () {
        return this.iso;
    }

    public void setNombre (String nombre) {
        this.nombre = nombre;
    }

    public String getNombre () {
        return this.nombre;
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
        else if (!(obj instanceof PaisDTO)) {
            igual = false;
        }
        else {
            PaisDTO paisDTO = (PaisDTO) obj;
            igual = this.id == paisDTO.getId() && this.iso.equals(paisDTO.getIso()) && this.nombre.equals(paisDTO.getNombre())? true:false;
        }
        return igual;
    }
}
