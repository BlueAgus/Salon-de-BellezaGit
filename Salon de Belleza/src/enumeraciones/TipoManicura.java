package enumeraciones;

public enum TipoManicura {
        GEL(10.0),
        ESCULPIDAS(15.0),
        SEMIPERMANENTE(15.0);

        private double precio;

        TipoManicura(double precio) {
            this.precio = precio;
        }

        public double getPrecio() {
            return precio;
        }

        public void setPrecio(double nuevoPrecio) {
            this.precio = nuevoPrecio;
        }
    }

