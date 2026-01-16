package com.tecnica.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>EmpleoDto.java</b>
 *
 * @version:  1.0
 * @descripcion: DTO para manejo de información de empleos
 * @author: Sistema de Gestión de Candidatos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmpleoDto {

    /**
     * id
     */
    private Long id;

    /**
     * candidatoId
     */
    private Long candidatoId;

    /**
     * nombreEmpresa
     */
    @NotBlank(message = "El nombre de la empresa es requerido")
    @Size(max = 200, message = "El nombre de la empresa no puede exceder 200 caracteres")
    private String nombreEmpresa;

    /**
     * fechaIngreso
     */
    @NotNull(message = "La fecha de ingreso es requerida")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaIngreso;

    /**
     * fechaSalida
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaSalida;

    /**
     * ingresoMensual
     */
    @NotNull(message = "El ingreso mensual es requerido")
    @Positive(message = "El ingreso mensual debe ser mayor a cero")
    private BigDecimal ingresoMensual;

    /**
     * giroEmpresa
     */
    @Size(max = 100, message = "El giro de la empresa no puede exceder 100 caracteres")
    private String giroEmpresa;

    /**
     * activo - indica si el empleo está activo
     */
    private Boolean activo;

    /**
     * antiguedadAnios - calculado en consultas
     */
    private BigDecimal antiguedadAnios;


    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCandidatoId() {
        return candidatoId;
    }

    public void setCandidatoId(Long candidatoId) {
        this.candidatoId = candidatoId;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public BigDecimal getIngresoMensual() {
        return ingresoMensual;
    }

    public void setIngresoMensual(BigDecimal ingresoMensual) {
        this.ingresoMensual = ingresoMensual;
    }

    public String getGiroEmpresa() {
        return giroEmpresa;
    }

    public void setGiroEmpresa(String giroEmpresa) {
        this.giroEmpresa = giroEmpresa;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public BigDecimal getAntiguedadAnios() {
        return antiguedadAnios;
    }

    public void setAntiguedadAnios(BigDecimal antiguedadAnios) {
        this.antiguedadAnios = antiguedadAnios;
    }

}