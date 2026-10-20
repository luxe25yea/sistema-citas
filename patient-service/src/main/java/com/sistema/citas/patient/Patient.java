package com.sistema.citas.patient;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un paciente en la base de datos.
 * Mapea directamente con la tabla 'patients' en H2.
 */
@Entity
@Table(name = "patients")
public class Patient {

    // Identificador único generado automáticamente por la base de datos (clave primaria)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de pila del paciente
    private String firstName;

    // Apellido del paciente
    private String lastName;

    // Correo electrónico (identificador único para contacto y validación de duplicados)
    private String email;

    // Teléfono de contacto
    private String phone;

    /**
     * Constructor por defecto requerido por JPA / Hibernate.
     */
    protected Patient() {
    }

    /**
     * Constructor con argumentos para crear una nueva instancia de paciente.
     */
    public Patient(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Retorna el nombre completo concatenando nombre y apellido.
     */
    public String fullName() {
        return firstName + " " + lastName;
    }
}
