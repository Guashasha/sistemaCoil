package DTO;

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
        this.objetivo = objetivo;
    }

    public String getPerfilEstudiante() {
        return perfilEstudiante;
    }

    public void setPerfilEstudiante(String perfilEstudiante) {
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
