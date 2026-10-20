package com.sistema.citas.appointment;

/**
 * Enumeración que define los posibles estados de una cita médica.
 */
public enum AppointmentStatus {
    /** Cita activa y confirmada; ocupa el turno del médico y del paciente */
    CONFIRMED,

    /** Cita cancelada; libera el turno para que pueda ser reservado nuevamente */
    CANCELLED
}
