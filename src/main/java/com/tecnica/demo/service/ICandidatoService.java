package com.tecnica.demo.service;

import com.tecnica.demo.dto.CandidatoDto;
import com.tecnica.demo.dto.registry.ActualizarDatosPersonalesRequest;
import com.tecnica.demo.dto.registry.ActualizarDomicilioRequest;
import com.tecnica.demo.dto.registry.CrearCandidatoRequest;
import com.tecnica.demo.util.Result;

import java.util.List;

/**
 * <b>ICandidatoService.java</b>
 *
 * @version:  1.0
 * @descripcion: Interface de servicio para operaciones de candidatos
 * @author: Sistema de Gestión de Candidatos
 */
public interface ICandidatoService {

    /**
     * Crear candidato
     *
     * @param request datos del candidato a crear
     * @return Result con el ID del candidato creado
     */
    Result<Long> crearCandidato(CrearCandidatoRequest request);

    /**
     * Actualizar datos personales del candidato
     *
     * @param candidatoId ID del candidato
     * @param request datos personales a actualizar
     * @return Result con status de operación
     */
    Result<Integer> actualizarDatosPersonales(Long candidatoId, ActualizarDatosPersonalesRequest request);

    /**
     * Actualizar domicilio del candidato
     *
     * @param candidatoId ID del candidato
     * @param request datos del domicilio
     * @return Result con status de operación
     */
    Result<Integer> actualizarDomicilio(Long candidatoId, ActualizarDomicilioRequest request);

    /**
     * Eliminar candidato
     *
     * @param candidatoId ID del candidato a eliminar
     * @return Result con status de operación
     */
    Result<Integer> eliminarCandidato(Long candidatoId);

    /**
     * Obtener candidato por ID
     *
     * @param candidatoId ID del candidato
     * @return Result con los datos del candidato
     */
    Result<CandidatoDto> obtenerCandidato(Long candidatoId);

    /**
     * Listar todos los candidatos activos
     *
     * @return Result con lista de candidatos
     */
    Result<List<CandidatoDto>> listarCandidatos();

    /**
     * Buscar candidatos por código postal
     *
     * @param codigoPostal código postal a buscar
     * @return Result con lista de candidatos
     */
    Result<List<CandidatoDto>> buscarPorCodigoPostal(String codigoPostal);

}
