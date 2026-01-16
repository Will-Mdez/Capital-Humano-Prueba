package com.tecnica.demo.dto.registry;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * <b>ActualizarDatosPersonalesRequest.java</b>
 *
 * @version: 1.0
 * @descripcion: DTO Request para actualizar datos personales
 * @author: Sistema de Gestión de Candidatos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActualizarDatosPersonalesRequest {

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

}
