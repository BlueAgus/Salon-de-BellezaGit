package model;

import enumeraciones.TipoDePago;
import enumeraciones.TipoServicio;
import excepciones.*;
import gestores.GestorPersona;
import gestores.GestorServicio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Factura {

    private String codigoFactura;
    private TipoDePago tipoPago;
    private double precioFinal;
    private Cliente cliente;
    private List<Turno> turnosPorCliente;
    private LocalDate fecha; // fecha y hora de la creacion de la factura
    private LocalTime hora;

    private GestorServicio gestorServicio;
    private GestorPersona gestorPersona;

    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////
    public Factura(TipoDePago tipoPago, Cliente cliente, GestorPersona gestorPersona, GestorServicio gestorServicio) {

        this.codigoFactura = generarIDEunico();
        this.tipoPago = tipoPago;
        this.precioFinal = 0.0;
        this.turnosPorCliente = new ArrayList<>();
        this.cliente = cliente;
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
        this.gestorPersona = gestorPersona;
        this.gestorServicio = gestorServicio;
    }

    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////

    public String detallesDeServicios() {
        StringBuilder detalles = new StringBuilder();
        Map<TipoServicio, Integer> cantidadPorServicio = new HashMap<>();

        for (Turno turno : turnosPorCliente) {
            try {
                // Obtener el servicio correspondiente al código
                Servicio servicio = gestorServicio.buscarServicio(turno.getCodigo_servicio());
                TipoServicio tipoServicio = servicio.getTipoService();

                // Incrementar la cantidad de este servicio
                cantidadPorServicio.put(tipoServicio, cantidadPorServicio.getOrDefault(tipoServicio, 0) + 1);
            } catch (CodigoNoEncontradoException e) {
                detalles.append("Servicio no encontrado para el código: ").append(turno.getCodigo_servicio()).append("\n");
            }
        }

        // Construir el string de detalles
        for (Map.Entry<TipoServicio, Integer> entry : cantidadPorServicio.entrySet()) {
            detalles.append(entry.getKey().name().toLowerCase())
                    .append(" x")
                    .append(entry.getValue())
                    .append("\n");
        }

        return detalles.toString();
    }

    public double calcularPrecioFinal() {
        double precioBase = 0.0;

        for (Turno turno : turnosPorCliente) {
            try {
                // Obtener el servicio correspondiente al código
                Servicio servicio = gestorServicio.buscarServicio(turno.getCodigo_servicio());
                precioBase += servicio.calcularPrecio();
            } catch (CodigoNoEncontradoException e) {
                System.out.println("Servicio no encontrado para el código: " + turno.getCodigo_servicio());
            }
        }
        this.precioFinal = tipoPago.calcularPagoTotal(precioBase);
        ///mirar
        return this.precioFinal;
    }

    public void agregarTurno(Turno turno) {
        try {
            if (turnosPorCliente.contains(turno)) {

                throw new TurnoExistenteException("El turno ya está ingresado en la factura.");
            }
            turnosPorCliente.add(turno);
            System.out.println("El turno se agregó correctamente a la factura.");

        } catch (TurnoExistenteException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    public void eliminarTurno(Turno turno) throws TurnoNoExistenteException, FacturaSinTurnosException {
        if (!turnosPorCliente.contains(turno)) {
            throw new TurnoNoExistenteException("El turno que desea eliminar en la factura no existe aqui.");
        }
        // opcion 1: no permitimos que se elimine un turno en el caso de que haya solo uno.
        if (turnosPorCliente.size() == 1) {
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
        long numeroUnico = (long) (Math.random() * 1000L);  // Genera un número entre 0 y 999
        return String.valueOf(numeroUnico);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Factura factura = (Factura) o;
        return Objects.equals(fecha, factura.fecha) && Objects.equals(hora, factura.hora);
    }

    ////////////////////////////////////////////////////////GET Y SET ////////////////////////////////////////////////////

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


    //////////////////////////////////////////////////////// TO STRING ////////////////////////////////////////////////////
    @Override
    public String toString() {
        return
                "| Detalles Factura: " +
                        "| Metodo de pago: " + tipoPago + "\n" +
                        "| Precio final : " + precioFinal + "\n" +
                        "| Servicios aplicados : " + detallesDeServicios() + "\n" +
                        "| Datos del cliente : " + cliente.datosClienteSinGenero() + "\n" +
                        "| Fecha : " + fecha + "\n" +
                        "| Hora : " + hora + "\n" +
                        "=========================================\n";
    }

}
