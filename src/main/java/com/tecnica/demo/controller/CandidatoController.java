package com.tecnica.demo.controller;

import com.tecnica.demo.dto.CandidatoDto;
import com.tecnica.demo.dto.registry.ActualizarDatosPersonalesRequest;
import com.tecnica.demo.dto.registry.ActualizarDomicilioRequest;
import com.tecnica.demo.dto.registry.CrearCandidatoRequest;
import com.tecnica.demo.model.ApiResponse;
import com.tecnica.demo.service.ICandidatoService;
import com.tecnica.demo.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <b>CandidatoController.java</b>
 *
 * @version:  1.0
 * @descripcion: Controlador REST para gestión de candidatos
 * @author: Sistema de Gestión de Candidatos
 */
@RestController
@RequestMapping("${basePath}"+"/candidatos")
public class CandidatoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CandidatoController.class);

    private final ICandidatoService candidatoService;

    public CandidatoController(ICandidatoService candidatoService) {
        this.candidatoService = candidatoService;
    }

    /**
     * Crear candidato
     *
     * @param request datos del candidato
     * @return ResponseEntity con el ID del candidato creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<Long>> crearCandidato(@Valid @RequestBody CrearCandidatoRequest request) {
        LOGGER.info("POST /api/v1/candidatos - Creando candidato: {}", request.getEmail());

        Result<Long> result = candidatoService.crearCandidato(request);

        if (result.isSuccess()) {
            ApiResponse<Long> response = ApiResponse.success(result.getData(), "Candidato creado exitosamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            ApiResponse<Long> response = ApiResponse.error(
                    HttpStatus.BAD_REQUEST.value(),
                    "Error al crear candidato",
                    result.getError(),
                    "/api/v1/candidatos"
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Listar todos los candidatos
     *
     * @return ResponseEntity con lista de candidatos
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<CandidatoDto>>> listarCandidatos() {
        LOGGER.info("GET /api/v1/candidatos - Listando todos los candidatos");

        Result<List<CandidatoDto>> result = candidatoService.listarCandidatos();

        if (result.isSuccess()) {
            ApiResponse<List<CandidatoDto>> response = ApiResponse.success(
                    result.getData(),
                    "Candidatos obtenidos exitosamente"
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<List<CandidatoDto>> response = ApiResponse.error(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al obtener candidatos",
                    result.getError(),
                    "/api/v1/candidatos"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obtener candidato por ID
     *
     * @param id ID del candidato
     * @return ResponseEntity con datos del candidato
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<CandidatoDto>> obtenerCandidato(@PathVariable Long id) {
        LOGGER.info("GET /api/v1/candidatos/{} - Obteniendo candidato", id);

        Result<CandidatoDto> result = candidatoService.obtenerCandidato(id);

        if (result.isSuccess()) {
            ApiResponse<CandidatoDto> response = ApiResponse.success(
                    result.getData(),
                    "Candidato encontrado"
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<CandidatoDto> response = ApiResponse.error(
                    HttpStatus.NOT_FOUND.value(),
                    "Candidato no encontrado",
                    result.getError(),
                    "/api/v1/candidatos/" + id
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Buscar candidatos por código postal
     *
     * @param codigoPostal código postal
     * @return ResponseEntity con lista de candidatos
     */
    @GetMapping("/buscar/codigo-postal/{codigoPostal}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<CandidatoDto>>> buscarPorCodigoPostal(
            @PathVariable String codigoPostal) {
        LOGGER.info("GET /api/v1/candidatos/buscar/codigo-postal/{} - Buscando candidatos", codigoPostal);

        Result<List<CandidatoDto>> result = candidatoService.buscarPorCodigoPostal(codigoPostal);

        if (result.isSuccess()) {
            ApiResponse<List<CandidatoDto>> response = ApiResponse.success(
                    result.getData(),
                    "Búsqueda completada"
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<List<CandidatoDto>> response = ApiResponse.error(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error en la búsqueda",
                    result.getError(),
                    "/api/v1/candidatos/buscar/codigo-postal/" + codigoPostal
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Actualizar datos personales
     *
     * @param id ID del candidato
     * @param request datos personales
     * @return ResponseEntity con resultado
     */
    @PutMapping("/{id}/datos-personales")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<Void>> actualizarDatosPersonales(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarDatosPersonalesRequest request) {
        LOGGER.info("PUT /api/v1/candidatos/{}/datos-personales - Actualizando datos personales", id);

        Result<Integer> result = candidatoService.actualizarDatosPersonales(id, request);

        if (result.isSuccess()) {
            ApiResponse<Void> response = ApiResponse.success("Datos personales actualizados exitosamente");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Void> response = ApiResponse.error(
                    HttpStatus.BAD_REQUEST.value(),
                    "Error al actualizar datos personales",
                    result.getError(),
                    "/api/v1/candidatos/" + id + "/datos-personales"
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Actualizar domicilio
     *
     * @param id ID del candidato
     * @param request datos del domicilio
     * @return ResponseEntity con resultado
     */
    @PutMapping("/{id}/domicilio")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<Void>> actualizarDomicilio(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarDomicilioRequest request) {
        LOGGER.info("PUT /api/v1/candidatos/{}/domicilio - Actualizando domicilio", id);

        Result<Integer> result = candidatoService.actualizarDomicilio(id, request);

        if (result.isSuccess()) {
            ApiResponse<Void> response = ApiResponse.success("Domicilio actualizado exitosamente");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Void> response = ApiResponse.error(
                    HttpStatus.BAD_REQUEST.value(),
                    "Error al actualizar domicilio",
                    result.getError(),
                    "/api/v1/candidatos/" + id + "/domicilio"
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Eliminar candidato
     *
     * @param id ID del candidato
     * @return ResponseEntity con resultado
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<Void>> eliminarCandidato(@PathVariable Long id) {
        LOGGER.info("DELETE /api/v1/candidatos/{} - Eliminando candidato", id);

        Result<Integer> result = candidatoService.eliminarCandidato(id);

        if (result.isSuccess()) {
            ApiResponse<Void> response = ApiResponse.success("Candidato eliminado exitosamente");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Void> response = ApiResponse.error(
                    HttpStatus.BAD_REQUEST.value(),
                    "Error al eliminar candidato",
                    result.getError(),
                    "/api/v1/candidatos/" + id
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

}