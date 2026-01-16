package com.tecnica.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * <b>CandidatoDto.java</b>
 *
 * @version:  1.0
 * @descripcion: DTO para manejo de información de candidatos
 * @author: Sistema de Gestión de Candidatos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidatoDto {

    /**
     * id
     */
    private Long id;

    /**
     * nombres
     */
    @NotBlank(message = "Los nombres son requeridos")
    @Size(max = 100, message = "Los nombres no pueden exceder 100 caracteres")
    private String nombres;

    /**
     * apellidos
     */
    @NotBlank(message = "Los apellidos son requeridos")
    @Size(max = 100, message = "Los apellidos no pueden exceder 100 caracteres")
    private String apellidos;

    /**
     * email
     */
    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    private String email;

    /**
     * fechaNacimiento
     */
    @NotNull(message = "La fecha de nacimiento es requerida")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    /**
     * rfc
     */
    @NotBlank(message = "El RFC es requerido")
    @Pattern(regexp = "^[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}$", message = "El RFC no tiene un formato válido")
    @Size(min = 12, max = 13, message = "El RFC debe tener 12 o 13 caracteres")
    private String rfc;

    /**
     * telefono
     */
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;

    /**
     * calle
     */
    @Size(max = 200, message = "La calle no puede exceder 200 caracteres")
    private String calle;

    /**
     * numero
     */
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
    @Size(max = 10, message = "El código postal no puede exceder 10 caracteres")
    private String codigoPostal;

    /**
     * listaEmpleos
     */
    @Valid
    private List<EmpleoDto> listaEmpleos;

    /**
     * activo - indica si el candidato está activo
     */
    private Boolean activo;

    /**
     * experienciaTotal - años totales de experiencia (calculado)
     */
    private Double experienciaTotal;


    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

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

    public List<EmpleoDto> getListaEmpleos() {
        return listaEmpleos;
    }

    public void setListaEmpleos(List<EmpleoDto> listaEmpleos) {
        this.listaEmpleos = listaEmpleos;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Double getExperienciaTotal() {
        return experienciaTotal;
    }

    public void setExperienciaTotal(Double experienciaTotal) {
        this.experienciaTotal = experienciaTotal;
    }

}