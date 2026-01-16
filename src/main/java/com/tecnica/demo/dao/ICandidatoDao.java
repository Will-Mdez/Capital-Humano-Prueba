package com.tecnica.demo.dao;

import com.tecnica.demo.dto.registry.ActualizarDatosPersonalesRequest;
import com.tecnica.demo.dto.registry.ActualizarDomicilioRequest;
import com.tecnica.demo.dto.registry.CrearCandidatoRequest;

import java.util.List;
import java.util.Map;

/**
 * <b>ICandidatoDao.java</b>
 *
 * @version:  1.0
 * @descripcion: Interface DAO para operaciones de candidatos
 * @author: Sistema de Gestión de Candidatos
 */
public interface ICandidatoDao {

    /**
     * Insertar candidato
     *
     * @param request datos del candidato a crear
     * @return Map con candidatoId y statusOper
     */
    Map<String, Object> insertarCandidato(CrearCandidatoRequest request);

    /**
     * Actualizar datos personales del candidato
     *
     * @param candidatoId ID del candidato
     * @param request datos personales a actualizar
     * @return Integer status de la operación
     */
    Integer actualizarDatosPersonales(Long candidatoId, ActualizarDatosPersonalesRequest request);

    /**
     * Actualizar domicilio del candidato
     *
     * @param candidatoId ID del candidato
     * @param request datos del domicilio a actualizar
     * @return Integer status de la operación
     */
    Integer actualizarDomicilio(Long candidatoId, ActualizarDomicilioRequest request);

    /**
     * Eliminar candidato (soft delete)
     *
     * @param candidatoId ID del candidato a eliminar
     * @return Integer status de la operación
     */
    Integer eliminarCandidato(Long candidatoId);

    /**
     * Obtener candidato por ID
     *
     * @param candidatoId ID del candidato
     * @return Map con el cursor de resultados y status
     */
    Map<String, Object> obtenerCandidato(Long candidatoId);

    /**
     * Listar todos los candidatos activos
     *
     * @return Map con el cursor de resultados y status
     */
    Map<String, Object> listarCandidatos();

    /**
     * Buscar candidatos por código postal
     *
     * @param codigoPostal código postal a buscar
     * @return Map con el cursor de resultados y status
     */
    Map<String, Object> buscarPorCodigoPostal(String codigoPostal);

}