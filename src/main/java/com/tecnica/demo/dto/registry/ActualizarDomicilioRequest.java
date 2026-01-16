package com.tecnica.demo.dto.registry;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>ActualizarDomicilioRequest.java</b>
 *
 * @version: 1.0
 * @descripcion: DTO Request para actualizar domicilio
 * @author: Sistema de Gestión de Candidatos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActualizarDomicilioRequest {

    @NotBlank(message = "La calle es requerida")
    @Size(max = 200)
    private String calle;

    @NotBlank(message = "El número es requerido")
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

    @NotBlank(message = "El código postal es requerido")
    @Size(max = 10)
    private String codigoPostal;

}
