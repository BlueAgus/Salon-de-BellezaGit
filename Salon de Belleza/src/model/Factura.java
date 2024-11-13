package model;

import enumeraciones.TipoDePago;
import enumeraciones.TipoServicio;
import excepciones.ClienteInvalidoException;
import excepciones.FacturaSinTurnosException;
import excepciones.TurnoExistenteException;
import excepciones.TurnoNoExistenteException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Factura {

    private String codigoFactura;
    private TipoDePago tipoPago;
    private double precioFinal;
    private Cliente cliente;
    private List<Turno> turnosPorCliente;
    private LocalDate fecha; // fecha y hora de la creacion de la factura
    private LocalTime hora;


    public Factura(TipoDePago tipoPago, Cliente cliente) {

        this.codigoFactura = generarIDEunico();
        this.tipoPago = tipoPago;
        this.precioFinal = 0.0;
        this.turnosPorCliente = new ArrayList<>();
        this.cliente = cliente;
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
    }

@Override
public String toString() {
    return
            "| Detalles Factura: " +
            "| Metodo de pago: " +tipoPago  + "\n" +
            "| Precio final : " + precioFinal+ "\n" +
            "| Servicios aplicados : " + detallesDeServicios() + "\n" +
            "| Datos del cliente : " + cliente.toString()+"\n" +
            "| Fecha : " + fecha +"\n" +
            "| Hora : " + hora +"\n" +
            "=========================================\n";
}

public String detallesDeServicios() { // Esto es para que en el detalle de la factura en el caso de tener
        //dos turnos del mismo servicio muestre por ej Manicura x2
        StringBuilder detalles = new StringBuilder();

        List<TipoServicio> cantidadPorServicio = new ArrayList<>();

        // Recorremos los turnos y contamos los servicios
        for (Turno turno : turnosPorCliente) {
            TipoServicio tipoServicio = turno.getServicio().getTipoService();

            // omite el servicio si ya lo proceso
            if (!cantidadPorServicio.contains(tipoServicio)) {
                int cantidad = 0;

                // Contamos cuántos turnos de este tipo tiene el cliente
                for (Turno t : turnosPorCliente) {
                    if (t.getServicio().getTipoService() == tipoServicio) {
                        cantidad++;
                    }
                }
                // Añadimos la descripción al StringBuilder
                //name es un metodo de los enum para traer literalmente el nombre y usar el toLower
                detalles.append(tipoServicio.name().toLowerCase())
                        .append(" x")
                        .append(cantidad)
                        .append("\n");

                // Marcamos este tipo de servicio como procesado
                cantidadPorServicio.add(tipoServicio);
            }
        }
        return detalles.toString();
    }

    public double calcularPrecioFinal(){

      double precioBase = 0.0;

      for(Turno turno : turnosPorCliente){
          precioBase += turno.getServicio().calcularPrecio();
      }

      this.precioFinal = tipoPago.calcularPagoTotal(precioBase);

      return this.precioFinal;
    }

    public void agregarTurno(Turno turno)throws TurnoExistenteException, ClienteInvalidoException
    {
        if(turnosPorCliente.contains(turno))
        {
            throw new TurnoExistenteException("El turno ya esta ingresado en la factura");
        }
        //ahora que lo pienso, puede pasar que por ejemplo me pague el turno a mi y a una amiga por ej,
        // en ese caso esto no seria necesario, despues lo hablamos porque no se
       // if(!turno.getCliente().equals(this.cliente)){
        //    throw new ClienteInvalidoException("El turno que desea ingresar no coincide con el cliente "+this.cliente.getDni()+" asociado a esta factura");
     //   }
        turnosPorCliente.add(turno);
        System.out.println("La informacion del turno se agrego con exito a la factura!");
    }

    public void eliminarTurno(Turno turno) throws TurnoNoExistenteException, FacturaSinTurnosException
    {
        if(!turnosPorCliente.contains(turno)){
            throw new TurnoNoExistenteException("El turno que desea eliminar en la factura no existe aqui.");
        }
       // opcion 1: no permitimos que se elimine un turno en el caso de que haya solo uno.
        if(turnosPorCliente.size() == 1){
            throw new FacturaSinTurnosException("La factura debe contener al menos un turno");
        }

        turnosPorCliente.remove(turno);
        System.out.println("El turno fue quitado de la factura final");

        //opcion 2: eliminamos el turno pero dejamos avisado que la instancia de factura no contiene turnos
       /* if(turnosPorCliente.isEmpty()){
            throw new FacturaSinTurnosException("La factura no contiene turnos, por favor ingrese un turno para validar la factura");
        }*/
    }

    private String generarIDEunico() {
        long numeroUnico = (long) (Math.random() * 10000000000000L);  // Genera un número entre 0 y 9999999999
        return String.valueOf(numeroUnico);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Factura factura = (Factura) o;
        return Objects.equals(fecha, factura.fecha) && Objects.equals(hora, factura.hora);
    }



    public String getCodigoFactura() {
        return codigoFactura;
    }

    public TipoDePago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoDePago tipoPago) {
        this.tipoPago = tipoPago;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }

    public List<Turno> getTurnosPorCliente() {
        return turnosPorCliente;
    }

    public void setTurnosPorCliente(List<Turno> turnosPorCliente) {
        this.turnosPorCliente = turnosPorCliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
