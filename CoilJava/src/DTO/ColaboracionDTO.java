package DTO;

import Utilidades.ErrorDAO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColaboracionDTO {
    public enum TipoColaboracion {
        claseEspejo,
        COIL
    }
    public enum EstadoColaboracion {
        propuesta,
        aceptada,
        rechazada,
        disponible,
        vinculada,
        activa,
        enRevision,
        finalizada
    }

    private int idColaboracion;
    private TipoColaboracion tipo;
    private EstadoColaboracion estado;
    private String temaInteres;
    private String idioma;
    private String objetivo;
    private PeriodoDTO periodoDTO;
    private String perfilEstudiante;
    private AcademicoDTO academicoDTOPar;
    private AcademicoDTO anfitrion;

    public int getIdColaboracion() {
        return idColaboracion;
    }

    public void setIdColaboracion(int idColaboracion) {
        this.idColaboracion = idColaboracion;
    }

    public PeriodoDTO getPeriodo() {
        return periodoDTO;
    }

    public void setPeriodo(PeriodoDTO periodoDTO) {
        this.periodoDTO = periodoDTO;
    }

    public TipoColaboracion getTipo() {
        return tipo;
    }

    public void setTipo(TipoColaboracion tipo) {
        this.tipo = tipo;
    }

    public String getTemaInteres() {
        return temaInteres;
    }

    public void setTemaInteres(String temaInteres) {
        verificarTemaInteres(temaInteres);
        this.temaInteres = temaInteres;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        verificarObjetivo(objetivo);
        this.objetivo = objetivo;
    }

    public String getPerfilEstudiante() {
        return perfilEstudiante;
    }

    public void setPerfilEstudiante(String perfilEstudiante) {
        verificarPerfilEstudiante(perfilEstudiante);
        this.perfilEstudiante = perfilEstudiante;
    }
    public EstadoColaboracion getEstado () {
        return estado;
    }

    public void setEstado (EstadoColaboracion estado) {
        this.estado = estado;
    }

    public AcademicoDTO getAcademicoPar () {
        return academicoDTOPar;
    }

    public void setAcademicoPar (AcademicoDTO academicoDTOPar) {
        this.academicoDTOPar = academicoDTOPar;
    }

    public AcademicoDTO getAnfitrion () {
        return anfitrion;
    }

    public void setAnfitrion (AcademicoDTO anfitrion) {
        this.anfitrion = anfitrion;
    }

    private void verificarTemaInteres (String temaInteres) {
        String temaInteresRegex = "^(?!\\s).{5,100}(?<!\\s)$";
        Pattern patron = Pattern.compile(temaInteresRegex);
        if (temaInteres != null) {
            Matcher matcher = patron.matcher(temaInteres.trim());
            if (!matcher.find()) {
                throw new ErrorDAO("""
                    El campo 'Tema de Interés' no es válido.
                    1. La longitud debe ser entre 5 y 100 caracteres.
                    2. No debe tener espacios en blanco al inicio o al final.""", ErrorDAO.Tipo.VALIDACION);
            }
        }
    }

    private void verificarObjetivo (String objetivo) {
        String objetivoRegex = "^.{5,300}$";
        Pattern patron = Pattern.compile(objetivoRegex);
        if (objetivo != null) {
            Matcher matcher = patron.matcher(objetivo.trim());
            if (!matcher.find()) {
                throw new ErrorDAO("""
                    El campo 'Objetivo' no es válido.
                    1. La longitud debe ser entre 5 y 300 caracteres.
                    """, ErrorDAO.Tipo.VALIDACION);
            }
        }
    }

    private void verificarPerfilEstudiante (String perfilEstudiante) {
        String perfilEstudianteRegex = "^(?!\\s).{5,200}(?<!\\s)$";
        Pattern patron = Pattern.compile(perfilEstudianteRegex);
        if (perfilEstudiante != null) {
            Matcher matcher = patron.matcher(perfilEstudiante.trim());
            if (!matcher.find()) {
                throw new ErrorDAO("""
                    El campo 'Perfil del Estudiante' no es válido.
                    1. La longitud debe ser entre 5 y 50 caracteres.
                    2. No debe tener espacios en blanco al inicio o al final.
                    """, ErrorDAO.Tipo.VALIDACION);
            }
        }
    }


    public boolean esValido () {
        return tipo != null &&
                estado != null &&
                cadenaValida(temaInteres) &&
                cadenaValida(idioma) &&
                cadenaValida(objetivo) &&
                cadenaValida(perfilEstudiante);
    }

    private boolean cadenaValida(String cadena) {
        return cadena != null && !cadena.isBlank();
    }

    @Override
    public String toString () {
        return "ColaboracionDTO{" +
                "idColaboracion=" + idColaboracion +
                ", tipo=" + tipo +
                ", estado=" + estado +
                ", temaInteres='" + temaInteres + '\'' +
                ", idioma='" + idioma + '\'' +
                ", objetivo='" + objetivo + '\'' +
                ", periodoDTO=" + periodoDTO +
                ", perfilEstudiante='" + perfilEstudiante + '\'' +
                ", academicoDTOPar=" + academicoDTOPar.getNombre() +
                ", anfitrion=" + anfitrion.getNombre() +
                '}';
    }

    @Override
    public boolean equals (Object obj) {
        boolean igual;
        if (this == obj) {
            igual = true;
        }
        else if (!(obj instanceof ColaboracionDTO)) {
            igual = false;
        }
        else {
            ColaboracionDTO colaboracionDTO = (ColaboracionDTO) obj;
            igual = this.idColaboracion == colaboracionDTO.getIdColaboracion() && this.tipo.toString()
                    .equals(colaboracionDTO.getTipo()
                            .toString()) && this.estado.toString()
                    .equals(colaboracionDTO.getEstado()
                            .toString()) && this.temaInteres.equals(colaboracionDTO.getTemaInteres())
                    && this.idioma.equals(colaboracionDTO.getIdioma()) && this.objetivo.equals(colaboracionDTO.getObjetivo())
                    && this.periodoDTO.equals(colaboracionDTO.getPeriodo()) && this.perfilEstudiante.equals(colaboracionDTO.getPerfilEstudiante())
                    && (this.academicoDTOPar == colaboracionDTO.getAcademicoPar() || this.academicoDTOPar.equals(colaboracionDTO.getAcademicoPar()))
                    && (this.anfitrion == colaboracionDTO.getAnfitrion() || this.anfitrion.equals(colaboracionDTO.getAnfitrion()));
        }
        return igual;
    }
}
