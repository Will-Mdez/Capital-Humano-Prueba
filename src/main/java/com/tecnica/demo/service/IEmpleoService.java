package com.tecnica.demo.service;

import com.tecnica.demo.dto.CandidatoDto;
import com.tecnica.demo.dto.EmpleoDto;
import com.tecnica.demo.dto.registry.ActualizarEmpleoRequest;
import com.tecnica.demo.dto.registry.CrearEmpleoRequest;
import com.tecnica.demo.util.Result;

import java.math.BigDecimal;
import java.util.List;

/**
 * <b>IEmpleoService.java</b>
 *
 * @version:  1.0
 * @descripcion: Interface de servicio para operaciones de empleos
 * @author: Sistema de Gestión de Candidatos
 */
public interface IEmpleoService {

    /**
     * Agregar empleo a candidato
     *
     * @param candidatoId ID del candidato
     * @param request datos del empleo
     * @return Result con el ID del empleo creado
     */
    Result<Long> agregarEmpleo(Long candidatoId, CrearEmpleoRequest request);

    /**
     * Actualizar empleo
     *
     * @param empleoId ID del empleo
     * @param request datos del empleo a actualizar
     * @return Result con status de operación
     */
    Result<Integer> actualizarEmpleo(Long empleoId, ActualizarEmpleoRequest request);

    /**
     * Eliminar empleo
     *
     * @param empleoId ID del empleo a eliminar
     * @return Result con status de operación
     */
    Result<Integer> eliminarEmpleo(Long empleoId);

    /**
     * Obtener empleos de un candidato
     *
     * @param candidatoId ID del candidato
     * @return Result con lista de empleos
     */
    Result<List<EmpleoDto>> obtenerEmpleosCandidato(Long candidatoId);

    /**
     * Buscar candidatos que trabajaron en una empresa específica
     *
     * @param nombreEmpresa nombre de la empresa
     * @return Result con lista de candidatos
     */
    Result<List<CandidatoDto>> buscarCandidatosPorEmpresa(String nombreEmpresa);

    /**
     * Listar candidatos con antigüedad mayor a 2 años en último empleo
     *
     * @return Result con lista de candidatos
     */
    Result<List<CandidatoDto>> candidatosConAntiguedad2Anios();

    /**
     * Listar candidatos con ingreso mayor al especificado
     *
     * @param ingresoMinimo ingreso mínimo (default: 10000)
     * @return Result con lista de candidatos
     */
    Result<List<CandidatoDto>> candidatosConIngresoMayor(BigDecimal ingresoMinimo);

    /**
     * Listar candidatos con más de X años de experiencia
     *
     * @param aniosExperiencia años de experiencia (default: 5)
     * @return Result con lista de candidatos
     */
    Result<List<CandidatoDto>> candidatosConExperiencia(BigDecimal aniosExperiencia);

    /**
     * Listar candidatos laborando actualmente
     *
     * @return Result con lista de candidatos
     */
    Result<List<CandidatoDto>> candidatosLaborandoActualmente();

}