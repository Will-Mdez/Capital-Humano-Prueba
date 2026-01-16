package com.tecnica.demo.dto.registry;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>ActualizarEmpleoRequest.java</b>
 *
 * @version: 1.0
 * @descripcion: DTO Request para actualizar un empleo
 * @author: Sistema de Gestión de Candidatos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActualizarEmpleoRequest {

    @NotBlank(message = "El nombre de la empresa es requerido")
    @Size(max = 200)
    private String nombreEmpresa;

    @NotNull(message = "La fecha de ingreso es requerida")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaIngreso;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaSalida;

    @NotNull(message = "El ingreso mensual es requerido")
    @Positive(message = "El ingreso mensual debe ser mayor a cero")
    private BigDecimal ingresoMensual;

    @Size(max = 100)
    private String giroEmpresa;

}
