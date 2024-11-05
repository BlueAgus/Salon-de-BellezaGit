package enumeraciones;

public enum TipoDepilacion {
    CERA(10.0),
    LASER(10.0);

    private double precio;

    TipoDepilacion(double precio){
        this.precio = precio;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double nuevoPrecio) {
        this.precio = precio;
    }
}
