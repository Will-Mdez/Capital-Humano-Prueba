package com.tecnica.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>ErrorResponse.java</b>
 *
 * @version: 1.0
 * @descripcion: DTO para respuestas de error
 * @author: Sistema de Gestión de Candidatos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * status - código de estado HTTP
     */
    private Integer status;

    /**
     * error - tipo de error
     */
    private String error;

    /**
     * mensaje - mensaje descriptivo del error
     */
    private String mensaje;

    /**
     * path - ruta donde ocurrió el error
     */
    private String path;

    /**
     * timestamp - fecha y hora del error
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * errores - lista de errores de validación
     */
    private List<ValidationError> errores;


    // Getters y Setters
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<ValidationError> getErrores() {
        return errores;
    }

    public void setErrores(List<ValidationError> errores) {
        this.errores = errores;
    }


    /**
     * Clase interna para errores de validación
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ValidationError {

        /**
         * campo - nombre del campo con error
         */
        private String campo;

        /**
         * mensaje - mensaje de error del campo
         */
        private String mensaje;

        /**
         * valorRechazado - valor que fue rechazado
         */
        private Object valorRechazado;


        // Getters y Setters
        public String getCampo() {
            return campo;
        }

        public void setCampo(String campo) {
            this.campo = campo;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public Object getValorRechazado() {
            return valorRechazado;
        }

        public void setValorRechazado(Object valorRechazado) {
            this.valorRechazado = valorRechazado;
        }

    }

}