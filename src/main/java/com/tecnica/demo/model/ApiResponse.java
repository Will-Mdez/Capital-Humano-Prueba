package com.tecnica.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <b>ApiResponse.java</b>
 *
 * @version: 1.0
 * @descripcion: DTO genérico para respuestas de la API
 * @author: Sistema de Gestión de Candidatos
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * status - código de estado HTTP
     */
    private Integer status;

    /**
     * mensaje - mensaje descriptivo de la respuesta
     */
    private String mensaje;

    /**
     * data - datos de la respuesta
     */
    private T data;

    /**
     * timestamp - fecha y hora de la respuesta
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * path - ruta de la petición
     */
    private String path;

    /**
     * error - detalles del error si existe
     */
    private String error;


    // Getters y Setters
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    /**
     * Método helper para crear respuesta exitosa
     */
    public static <T> ApiResponse<T> success(T data, String mensaje) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMensaje(mensaje);
        response.setData(data);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    /**
     * Método helper para crear respuesta exitosa sin datos
     */
    public static <T> ApiResponse<T> success(String mensaje) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMensaje(mensaje);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    /**
     * Método helper para crear respuesta de error
     */
    public static <T> ApiResponse<T> error(Integer status, String mensaje, String error, String path) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(status);
        response.setMensaje(mensaje);
        response.setError(error);
        response.setPath(path);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

}