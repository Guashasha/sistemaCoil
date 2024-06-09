package DTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
