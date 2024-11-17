package Interfaces;

import model.Servicio;

import java.time.LocalDate;

public interface MantenimientoMaquinas {
    // Programar mantenimiento planificado
    void programarMantenimiento(LocalDate fecha);

    // Marcar máquina como fuera de servicio por falla
    void reportarFalla();

    // Verificar si la máquina está disponible
    boolean estaDisponible();
}
//esto puede ser util en dos contextos, falla repentina: hacer algun metodo para cancelar turnos si se reportaFalla()
// y tambien para manejar mantenimientos programados, una vez al mes por ejemplo, uno o dos dias se omiten esas fechas
//para que no se puedan reservar ya que la maquina va a estar en mantenimiento, quiza de este contexto le encuentro mas sentido
//pero en l contexto de falla repentina capaz no tanto porque de ultima si se rompe repentinamente el recepcionista mira los turnos de ese dia y llama manualmente
// para vr que dias pueden cambiar las clientes, que eso es medio paja tambien pero bueno
//tenemos que verlo eso juntas a ver que hacemos.