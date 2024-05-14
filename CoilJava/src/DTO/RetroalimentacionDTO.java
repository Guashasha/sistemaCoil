package DTO;

import java.util.Optional;

public class RetroalimentacionDTO {
    private int idRetroalimentacion;
    private Optional<String> comentario = Optional.empty();
    private int interaccionConPar;
    private int idUsuario;

    public int getIdRetroalimentacion () {
        return idRetroalimentacion;
    }

    public void setIdRetroalimentacion (int idRetroalimentacion) {
        this.idRetroalimentacion = idRetroalimentacion;
    }

    public Optional<String> getComentario () {
        return comentario;
    }

    public void setComentario (String comentario) {
        if (comentario == null) {
            return;
        }

        if (comentario.isEmpty())
            this.comentario = Optional.empty();
        else
            this.comentario = Optional.of(comentario);
    }

    public int getInteraccionConPar () {
        return interaccionConPar;
    }

    public void setInteraccionConPar (int interaccionConPar) {
        this.interaccionConPar = interaccionConPar;
    }

    public int getIdUsuario () {
        return idUsuario;
    }

    public void setIdUsuario (int idUsuario) {
        this.idUsuario = idUsuario;
    }
}