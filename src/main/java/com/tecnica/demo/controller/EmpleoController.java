package com.tecnica.demo.controller;

import com.tecnica.demo.dto.CandidatoDto;
import com.tecnica.demo.dto.EmpleoDto;
import com.tecnica.demo.dto.registry.ActualizarEmpleoRequest;
import com.tecnica.demo.dto.registry.CrearEmpleoRequest;
import com.tecnica.demo.model.ApiResponse;
import com.tecnica.demo.service.IEmpleoService;
import com.tecnica.demo.util.Result;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <b>EmpleoController.java</b>
 *
 * @version:  1.0
 * @descripcion: Controlador REST para gestión de empleos
 * @author: Sistema de Gestión de Candidatos
 */
@RestController
@RequestMapping("${basePath}"+"/empleos")
public class EmpleoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmpleoController.class);

    private final IEmpleoService empleoService;

    public EmpleoController(IEmpleoService empleoService) {
        this.empleoService = empleoService;
    }

    /**
     * Agregar empleo a candidato
     *
     * @param candidatoId ID del candidato
     * @param request datos del empleo
     * @return ResponseEntity con el ID del empleo creado
     */
    @PostMapping("/candidato/{candidatoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<Long>> agregarEmpleo(
            @PathVariable Long candidatoId,
            @Valid @RequestBody CrearEmpleoRequest request) {
        LOGGER.info("POST /api/v1/empleos/candidato/{} - Agregando empleo", candidatoId);

        Result<Long> result = empleoService.agregarEmpleo(candidatoId, request);

        if (result.isSuccess()) {
            ApiResponse<Long> response = ApiResponse.success(result.getData(), "Empleo agregado exitosamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            ApiResponse<Long> response = ApiResponse.error(
                    HttpStatus.BAD_REQUEST.value(),
                    "Error al agregar empleo",
                    result.getError(),
                    "/api/v1/empleos/candidato/" + candidatoId
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Obtener empleos de un candidato
     *
     * @param candidatoId ID del candidato
     * @return ResponseEntity con lista de empleos
     */
    @GetMapping("/candidato/{candidatoId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<EmpleoDto>>> obtenerEmpleosCandidato(
            @PathVariable Long candidatoId) {
        LOGGER.info("GET /api/v1/empleos/candidato/{} - Obteniendo empleos", candidatoId);

        Result<List<EmpleoDto>> result = empleoService.obtenerEmpleosCandidato(candidatoId);

        if (result.isSuccess()) {
            ApiResponse<List<EmpleoDto>> response = ApiResponse.success(
                    result.getData(),
                    "Empleos obtenidos exitosamente"
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<List<EmpleoDto>> response = ApiResponse.error(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al obtener empleos",
                    result.getError(),
                    "/api/v1/empleos/candidato/" + candidatoId
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Actualizar empleo
     *
     * @param empleoId ID del empleo
     * @param request datos del empleo
     * @return ResponseEntity con resultado
     */
    @PutMapping("/{empleoId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<Void>> actualizarEmpleo(
            @PathVariable Long empleoId,
            @Valid @RequestBody ActualizarEmpleoRequest request) {
        LOGGER.info("PUT /api/v1/empleos/{} - Actualizando empleo", empleoId);

        Result<Integer> result = empleoService.actualizarEmpleo(empleoId, request);

        if (result.isSuccess()) {
            ApiResponse<Void> response = ApiResponse.success("Empleo actualizado exitosamente");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Void> response = ApiResponse.error(
                    HttpStatus.BAD_REQUEST.value(),
                    "Error al actualizar empleo",
                    result.getError(),
                    "/api/v1/empleos/" + empleoId
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Eliminar empleo
     *
     * @param empleoId ID del empleo
     * @return ResponseEntity con resultado
     */
    @DeleteMapping("/{empleoId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<Void>> eliminarEmpleo(@PathVariable Long empleoId) {
        LOGGER.info("DELETE /api/v1/empleos/{} - Eliminando empleo", empleoId);

        Result<Integer> result = empleoService.eliminarEmpleo(empleoId);

        if (result.isSuccess()) {
            ApiResponse<Void> response = ApiResponse.success("Empleo eliminado exitosamente");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Void> response = ApiResponse.error(
                    HttpStatus.BAD_REQUEST.value(),
                    "Error al eliminar empleo",
                    result.getError(),
                    "/api/v1/empleos/" + empleoId
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Buscar candidatos por empresa
     *
     * @param nombreEmpresa nombre de la empresa
     * @return ResponseEntity con lista de candidatos
     */
    @GetMapping("/buscar/empresa")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<CandidatoDto>>> buscarCandidatosPorEmpresa(
            @RequestParam String nombreEmpresa) {
        LOGGER.info("GET /api/v1/empleos/buscar/empresa?nombreEmpresa={}", nombreEmpresa);

        Result<List<CandidatoDto>> result = empleoService.buscarCandidatosPorEmpresa(nombreEmpresa);

        if (result.isSuccess()) {
            ApiResponse<List<CandidatoDto>> response = ApiResponse.success(
                    result.getData(),
                    "Candidatos encontrados"
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<List<CandidatoDto>> response = ApiResponse.error(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error en la búsqueda",
                    result.getError(),
                    "/api/v1/empleos/buscar/empresa"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Candidatos con antigüedad mayor a 2 años
     *
     * @return ResponseEntity con lista de candidatos
     */
    @GetMapping("/buscar/antiguedad-2-anios")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<CandidatoDto>>> candidatosConAntiguedad2Anios() {
        LOGGER.info("GET /api/v1/empleos/buscar/antiguedad-2-anios");

        Result<List<CandidatoDto>> result = empleoService.candidatosConAntiguedad2Anios();

        if (result.isSuccess()) {
            ApiResponse<List<CandidatoDto>> response = ApiResponse.success(
                    result.getData(),
                    "Candidatos con antigüedad > 2 años"
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<List<CandidatoDto>> response = ApiResponse.error(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error en la búsqueda",
                    result.getError(),
                    "/api/v1/empleos/buscar/antiguedad-2-anios"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Candidatos con ingreso mayor al especificado
     *
     * @param ingresoMinimo ingreso mínimo (opcional, default: 10000)
     * @return ResponseEntity con lista de candidatos
     */
    @GetMapping("/buscar/ingreso-mayor")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<CandidatoDto>>> candidatosConIngresoMayor(
            @RequestParam(required = false) BigDecimal ingresoMinimo) {
        LOGGER.info("GET /api/v1/empleos/buscar/ingreso-mayor?ingresoMinimo={}", ingresoMinimo);

        Result<List<CandidatoDto>> result = empleoService.candidatosConIngresoMayor(ingresoMinimo);

        if (result.isSuccess()) {
            ApiResponse<List<CandidatoDto>> response = ApiResponse.success(
                    result.getData(),
                    "Candidatos con ingreso mayor a " + (ingresoMinimo != null ? ingresoMinimo : 10000)
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<List<CandidatoDto>> response = ApiResponse.error(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error en la búsqueda",
                    result.getError(),
                    "/api/v1/empleos/buscar/ingreso-mayor"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Candidatos con más de X años de experiencia
     *
     * @param aniosExperiencia años de experiencia (opcional, default: 5)
     * @return ResponseEntity con lista de candidatos
     */
    @GetMapping("/buscar/experiencia")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<CandidatoDto>>> candidatosConExperiencia(
            @RequestParam(required = false) BigDecimal aniosExperiencia) {
        LOGGER.info("GET /api/v1/empleos/buscar/experiencia?aniosExperiencia={}", aniosExperiencia);

        Result<List<CandidatoDto>> result = empleoService.candidatosConExperiencia(aniosExperiencia);

        if (result.isSuccess()) {
            ApiResponse<List<CandidatoDto>> response = ApiResponse.success(
                    result.getData(),
                    "Candidatos con experiencia > " + (aniosExperiencia != null ? aniosExperiencia : 5) + " años"
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<List<CandidatoDto>> response = ApiResponse.error(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error en la búsqueda",
                    result.getError(),
                    "/api/v1/empleos/buscar/experiencia"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Candidatos laborando actualmente
     *
     * @return ResponseEntity con lista de candidatos
     */
    @GetMapping("/buscar/laborando-actualmente")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<CandidatoDto>>> candidatosLaborandoActualmente() {
        LOGGER.info("GET /api/v1/empleos/buscar/laborando-actualmente");

        Result<List<CandidatoDto>> result = empleoService.candidatosLaborandoActualmente();

        if (result.isSuccess()) {
            ApiResponse<List<CandidatoDto>> response = ApiResponse.success(
                    result.getData(),
                    "Candidatos laborando actualmente"
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<List<CandidatoDto>> response = ApiResponse.error(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error en la búsqueda",
                    result.getError(),
                    "/api/v1/empleos/buscar/laborando-actualmente"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}