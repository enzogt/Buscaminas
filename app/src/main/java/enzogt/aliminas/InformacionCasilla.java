package enzogt.aliminas;

public class InformacionCasilla {
    private int fila;
    private int columna;
    private int indice;

    public InformacionCasilla(int fila, int columna, int indice) {
        this.fila = fila;
        this.columna = columna;
        this.indice = indice;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public int getIndice() {
        return indice;
    }
}
