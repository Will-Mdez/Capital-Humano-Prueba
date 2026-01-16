package com.tecnica.demo.dao;

import com.tecnica.demo.dto.registry.ActualizarEmpleoRequest;
import com.tecnica.demo.dto.registry.CrearEmpleoRequest;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <b>IEmpleoDao.java</b>
 *
 * @version: 1.0
 * @descripcion: Interface DAO para operaciones de empleos
 * @author: Sistema de Gestión de Candidatos
 */
public interface IEmpleoDao {

    /**
     * Insertar empleo
     *
     * @param candidatoId ID del candidato
     * @param request datos del empleo a crear
     * @return Map con empleoId y statusOper
     */
    Map<String, Object> insertarEmpleo(Long candidatoId, CrearEmpleoRequest request);

    /**
     * Actualizar empleo
     *
     * @param empleoId ID del empleo
     * @param request datos del empleo a actualizar
     * @return Integer status de la operación
     */
    Integer actualizarEmpleo(Long empleoId, ActualizarEmpleoRequest request);

    /**
     * Eliminar empleo (soft delete)
     *
     * @param empleoId ID del empleo a eliminar
     * @return Integer status de la operación
     */
    Integer eliminarEmpleo(Long empleoId);

    /**
     * Obtener empleos de un candidato
     *
     * @param candidatoId ID del candidato
     * @return Map con el cursor de resultados y status
     */
    Map<String, Object> obtenerEmpleosCandidato(Long candidatoId);

    /**
     * Buscar candidatos por empresa
     *
     * @param nombreEmpresa nombre de la empresa
     * @return Map con el cursor de resultados y status
     */
    Map<String, Object> buscarCandidatosPorEmpresa(String nombreEmpresa);

    /**
     * Listar candidatos con antigüedad mayor a 2 años en último empleo
     *
     * @return Map con el cursor de resultados y status
     */
    Map<String, Object> candidatosConAntiguedad2Anios();

    /**
     * Listar candidatos con ingreso mayor al especificado
     *
     * @param ingresoMinimo ingreso mínimo a buscar
     * @return Map con el cursor de resultados y status
     */
    Map<String, Object> candidatosConIngresoMayor(BigDecimal ingresoMinimo);

    /**
     * Listar candidatos con más de X años de experiencia
     *
     * @param aniosExperiencia años de experiencia mínimos
     * @return Map con el cursor de resultados y status
     */
    Map<String, Object> candidatosConExperiencia(BigDecimal aniosExperiencia);

    /**
     * Listar candidatos laborando actualmente
     *
     * @return Map con el cursor de resultados y status
     */
    Map<String, Object> candidatosLaborandoActualmente();

}