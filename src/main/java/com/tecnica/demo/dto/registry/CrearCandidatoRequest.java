package com.tecnica.demo.dto.registry;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


/**
 * <b>CrearCandidatoRequest.java</b>
 *
 * @version: 1.0
 * @descripcion: DTO Request para crear un candidato
 * @author: Sistema de Gestión de Candidatos
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CrearCandidatoRequest {

    @NotBlank(message = "Los nombres son requeridos")
    @Size(max = 100)
    private String nombres;

    @NotBlank(message = "Los apellidos son requeridos")
    @Size(max = 100)
    private String apellidos;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150)
    private String email;

    @NotNull(message = "La fecha de nacimiento es requerida")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El RFC es requerido")
    @Pattern(regexp = "^[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}$")
    @Size(min = 12, max = 13)
    private String rfc;

    @Size(max = 20)
    private String telefono;

    @Size(max = 200)
    private String calle;

    @Size(max = 20)
    private String numero;

    @Size(max = 100)
    private String colonia;

    @Size(max = 100)
    private String ciudad;

    @Size(max = 100)
    private String estado;

    @Size(max = 100)
    private String pais;

    @Size(max = 10)
    private String codigoPostal;

    @Valid
    private List<CrearEmpleoRequest> empleos;

}




