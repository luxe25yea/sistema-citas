package com.sistema.citas.patient;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

/**
 * Controlador REST que expone los endpoints para la gestión de pacientes.
 * Base path: /patients
 */
@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientRepository patientRepository;

    // Inyección de dependencias por constructor
    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Endpoint GET /patients
     * Retorna la lista completa de pacientes registrados.
     */
    @GetMapping
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    /**
     * Endpoint GET /patients/{id}
     * Busca y devuelve un paciente por su identificador primario.
     * Si no existe, responde HTTP 404 Not Found.
     *
     * @param id Identificador del paciente
     * @return El paciente encontrado
     */
    @GetMapping("/{id}")
    public Patient findById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe un paciente con id " + id));
    }

    /**
     * Endpoint POST /patients
     * Registra un nuevo paciente validando previamente los datos de entrada y unicidad de correo.
     *
     * @param request DTO con los datos del nuevo paciente, validado con @Valid
     * @return HTTP 201 Created con cabecera Location hacia el nuevo recurso (/patients/{id})
     *         y el objeto Patient creado en el cuerpo.
     */
    @PostMapping
    public ResponseEntity<Patient> create(@Valid @RequestBody CreatePatientRequest request) {
        // Regla de negocio: no se permiten dos pacientes con el mismo email
        if (patientRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un paciente con el email " + request.email());
        }

        // Mapeo del DTO a la entidad de base de datos
        Patient patient = new Patient(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phone()
        );
        Patient saved = patientRepository.save(patient);

        // Construcción dinámica de la URL del nuevo recurso: /patients/{id}
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        // Retorna HTTP 201 Created con header Location y entidad persistida
        return ResponseEntity.created(location).body(saved);
    }
}
