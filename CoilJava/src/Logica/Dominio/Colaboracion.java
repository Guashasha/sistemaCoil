package Logica.Dominio;

public class Colaboracion {
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
        finalizada,
    }

    private int idColaboracion;
    private TipoColaboracion tipo;
    private EstadoColaboracion estado;
    private String temaInteres;
    private String idioma;
    private String objetivo;
    private Periodo periodo;
    private String perfilEstudiante;
    private Academico academicoPar;
    private Academico anfitrion;

    public int getIdColaboracion() {
        return idColaboracion;
    }

    public void setIdColaboracion(int idColaboracion) {
        this.idColaboracion = idColaboracion;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
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

    public Academico getAcademicoPar () {
        return academicoPar;
    }

    public void setAcademicoPar (Academico academicoPar) {
        this.academicoPar = academicoPar;
    }

    public Academico getAnfitrion () {
        return anfitrion;
    }

    public void setAnfitrion (Academico anfitrion) {
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
        return "Colaboracion{" +
                "idColaboracion=" + idColaboracion +
                ", tipo=" + tipo +
                ", estado=" + estado +
                ", temaInteres='" + temaInteres + '\'' +
                ", idioma='" + idioma + '\'' +
                ", objetivo='" + objetivo + '\'' +
                ", periodo=" + periodo +
                ", perfilEstudiante='" + perfilEstudiante + '\'' +
                ", academicoPar=" + academicoPar.getNombre() +
                ", anfitrion=" + anfitrion.getNombre() +
                '}';
    }

    @Override
    public boolean equals (Object obj) {
        boolean igual;
        if (this == obj) {
            igual = true;
        }
        else if (!(obj instanceof Colaboracion)) {
            igual = false;
        }
        else {
            Colaboracion colaboracion = (Colaboracion) obj;
            igual = this.idColaboracion == colaboracion.getIdColaboracion() && this.tipo.toString()
                    .equals(colaboracion.getTipo()
                            .toString()) && this.estado.toString()
                    .equals(colaboracion.getEstado()
                            .toString()) && this.temaInteres.equals(colaboracion.getTemaInteres())
                    && this.idioma.equals(colaboracion.getIdioma()) && this.objetivo.equals(colaboracion.getObjetivo())
                    && this.periodo.equals(colaboracion.getPeriodo()) && this.perfilEstudiante.equals(colaboracion.getPerfilEstudiante())
                    && (this.academicoPar == colaboracion.getAcademicoPar() || this.academicoPar.equals(colaboracion.getAcademicoPar()))
                    && (this.anfitrion == colaboracion.getAnfitrion() || this.anfitrion.equals(colaboracion.getAnfitrion()));
        }
        return igual;
    }
}
