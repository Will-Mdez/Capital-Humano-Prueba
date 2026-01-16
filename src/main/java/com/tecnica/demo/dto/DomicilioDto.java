package com.tecnica.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * <b>DomicilioDto.java</b>
 *
 * @version: 1.0
 * @descripcion: DTO para manejo de información de domicilio
 * @author: Sistema de Gestión de Candidatos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DomicilioDto {

    /**
     * calle
     */
    @NotBlank(message = "La calle es requerida")
    @Size(max = 200, message = "La calle no puede exceder 200 caracteres")
    private String calle;

    /**
     * numero
     */
    @NotBlank(message = "El número es requerido")
    @Size(max = 20, message = "El número no puede exceder 20 caracteres")
    private String numero;

    /**
     * colonia
     */
    @Size(max = 100, message = "La colonia no puede exceder 100 caracteres")
    private String colonia;

    /**
     * ciudad
     */
    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    private String ciudad;

    /**
     * estado
     */
    @Size(max = 100, message = "El estado no puede exceder 100 caracteres")
    private String estado;

    /**
     * pais
     */
    @Size(max = 100, message = "El país no puede exceder 100 caracteres")
    private String pais;

    /**
     * codigoPostal
     */
    @NotBlank(message = "El código postal es requerido")
    @Size(max = 10, message = "El código postal no puede exceder 10 caracteres")
    private String codigoPostal;


    // Getters y Setters
    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

}