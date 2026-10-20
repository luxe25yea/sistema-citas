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

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @GetMapping("/{id}")
    public Patient findById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe un paciente con id " + id));
    }

    @PostMapping
    public ResponseEntity<Patient> create(@Valid @RequestBody CreatePatientRequest request) {
        if (patientRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un paciente con el email " + request.email());
        }

        Patient patient = new Patient(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phone()
        );
        Patient saved = patientRepository.save(patient);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }
}
