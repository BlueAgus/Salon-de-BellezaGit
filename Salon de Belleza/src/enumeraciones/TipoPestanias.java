package enumeraciones;

public enum TipoPestanias {
    TRES_D(20.0),
    DOS_D(15.0),
    CLASICAS(10.0);

    private double precio;

    TipoPestanias(double precio) {
        this.precio = precio;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double nuevoPrecio) {
        this.precio = precio;
    }
}
