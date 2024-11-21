package model;

import enumeraciones.TipoDePago;
import enumeraciones.TipoServicio;
import excepciones.*;
import gestores.GestorPersona;
import gestores.GestorServicio;
import interfaces.CrearID;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Factura implements CrearID {

    private String codigoFactura;
    private TipoDePago tipoPago;
    private double precioFinal = 0.0;
    private Cliente cliente;
    private List<Turno> turnosPorCliente;
    private double descuento;
    private double ajuste = 0.0;
    private LocalDate fecha; // fecha y hora de la creacion de la factura
    private LocalTime hora;
    GestorServicio gestorServicio;

    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////

    public Factura(TipoDePago tipoPago, Cliente cliente, GestorServicio gestorServicio) {

        this.codigoFactura = this.generarIDEunico(); // aca usamos el metodo de la interfaz directamente
        this.tipoPago = tipoPago;
        this.precioFinal = 0.0;
        this.descuento = 0.0;
        this.ajuste = 0.0;
        this.turnosPorCliente = new ArrayList<>();
        this.cliente = cliente;
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
        this.gestorServicio = gestorServicio;
    }

    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////

    public String detallesDeServicios() {
        StringBuilder detalles = new StringBuilder();
        Map<TipoServicio, Integer> cantidadPorServicio = new HashMap<>();

        for (Turno turno : turnosPorCliente) {
            try {
                // Obtener el servicio correspondiente al código
                Servicio servicio = gestorServicio.bus(turno.getCodigo_servicio());
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

        // Calcula explícitamente el ajuste para mostrarlo en la factura
        this.ajuste = this.precioFinal - precioBase;

        return this.precioFinal;
    }

    public void agregarTurno(Turno turno) throws TurnoExistenteException {
        if (turnosPorCliente.contains(turno)) {
            throw new TurnoExistenteException("El turno ya está ingresado en la factura.");
        }
        turnosPorCliente.add(turno);
    }

    public void eliminarTurno(Turno turno) throws TurnoNoExistenteException, FacturaSinTurnosException {
        if (!turnosPorCliente.contains(turno)) {
            throw new TurnoNoExistenteException("El turno que desea eliminar en la factura no existe aqui.");
        }
        // opcion 1: no permitimos que se elimine un turno en el caso de que haya solo uno.
        if (turnosPorCliente.size() == 1) {
            throw new FacturaSinTurnosException("La factura debe contener al menos un turno, en caso contrario eliminar la factura completa");
        } //

        turnosPorCliente.remove(turno);
        System.out.println("El turno fue quitado de la factura final");

        //opcion 2: eliminamos el turno pero dejamos avisado que la instancia de factura no contiene turnos
       /* if(turnosPorCliente.isEmpty()){
            throw new FacturaSinTurnosException("La factura no contiene turnos, por favor ingrese un turno para validar la factura");
        }*/
    }

    @Override
    public String generarIDEunico() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid.substring(0, 15);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Factura factura = (Factura) o;
        return Objects.equals(codigoFactura, factura.codigoFactura);
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

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }


    ////////////////////////////////////// TO STRING ////////////////////////////////////////////////////

    @Override
    public String toString() {
        return
                "| Detalles Factura: " +
                        "| Metodo de pago: " + tipoPago + "\n" +
                        "| Precio final : " + precioFinal + "\n" +
                        "| Descuento aplicado : " + descuento + "\n" +
                        "| Ajuste aplicado : " + mostrarAjuste() + "\n" +
                        "| Servicios aplicados : " + detallesDeServicios() + "\n" +
                        "| Datos del cliente : " + cliente.datosClienteSinGenero() + "\n" +
                        "| Fecha : " + fecha + "\n" +
                        "| Hora : " + hora + "\n" +
                        "=========================================\n";
    }

    private String mostrarAjuste() {
        if (ajuste == 0) {
            return "Sin ajuste";
        }
        // aca muestra el signo del numero , si es posi o negativo
        return String.format("%+.2f", ajuste);
    }
}

