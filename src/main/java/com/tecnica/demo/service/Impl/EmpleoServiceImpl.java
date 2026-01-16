package com.tecnica.demo.service.Impl;

import com.tecnica.demo.constant.Constants;
import com.tecnica.demo.dao.IEmpleoDao;
import com.tecnica.demo.dto.CandidatoDto;
import com.tecnica.demo.dto.EmpleoDto;
import com.tecnica.demo.dto.registry.ActualizarEmpleoRequest;
import com.tecnica.demo.dto.registry.CrearEmpleoRequest;
import com.tecnica.demo.service.IEmpleoService;
import com.tecnica.demo.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <b>EmpleoServiceImpl.java</b>
 *
 * @version:  1.0
 * @descripcion: Implementación del servicio de empleos
 * @author: Sistema de Gestión de Candidatos
 */
@Service
public class EmpleoServiceImpl implements IEmpleoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmpleoServiceImpl.class);

    @Autowired
    private IEmpleoDao iempleoDao;

    @Override
    public Result<Long> agregarEmpleo(Long candidatoId, CrearEmpleoRequest request) {
        try {
            LOGGER.info(Constants.LOG_INFO_INSERTAR + " para candidato ID: {}", candidatoId);
            Map<String, Object> result = iempleoDao.insertarEmpleo(candidatoId, request);

            BigDecimal empleoIdBD = (BigDecimal) result.get(Constants.PA_EMPLEO_ID);

            if (empleoIdBD == null) {
                return Result.failure("Error al agregar empleo: No se obtuvo ID");
            }

            Long empleoId = empleoIdBD.longValue();
            LOGGER.info("Empleo agregado exitosamente con ID: {}", empleoId);

            return Result.success(empleoId);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return Result.failure("Error interno al agregar empleo: " + e.getMessage());
        }
    }

    @Override
    public Result<Integer> actualizarEmpleo(Long empleoId, ActualizarEmpleoRequest request) {
        try {
            LOGGER.info(Constants.LOG_INFO_ACTUALIZAR + " ID: {}", empleoId);
            Integer statusOper = iempleoDao.actualizarEmpleo(empleoId, request);

            if (statusOper == null || statusOper != Constants.STATUS_OK) {
                return Result.failure("Error al actualizar empleo. Status: " + statusOper);
            }

            LOGGER.info("Empleo actualizado exitosamente ID: {}", empleoId);
            return Result.success(statusOper);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return Result.failure("Error interno al actualizar empleo: " + e.getMessage());
        }
    }

    @Override
    public Result<Integer> eliminarEmpleo(Long empleoId) {
        try {
            LOGGER.info(Constants.LOG_INFO_ELIMINAR + " ID: {}", empleoId);
            Integer statusOper = iempleoDao.eliminarEmpleo(empleoId);

            if (statusOper == null || statusOper != Constants.STATUS_OK) {
                return Result.failure("Error al eliminar empleo. Status: " + statusOper);
            }

            LOGGER.info("Empleo eliminado exitosamente ID: {}", empleoId);
            return Result.success(statusOper);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return Result.failure("Error interno al eliminar empleo: " + e.getMessage());
        }
    }

    @Override
    public Result<List<EmpleoDto>> obtenerEmpleosCandidato(Long candidatoId) {
        try {
            LOGGER.info(Constants.LOG_INFO_CONSULTAR + " para candidato ID: {}", candidatoId);
            Map<String, Object> result = iempleoDao.obtenerEmpleosCandidato(candidatoId);

            @SuppressWarnings("unchecked")
            List<EmpleoDto> empleos = (List<EmpleoDto>) result.get(Constants.CUR_RESULTADO);

            if (empleos == null) {
                return Result.failure("Error al obtener empleos del candidato");
            }

            LOGGER.info("Se obtuvieron {} empleos", empleos.size());
            return Result.success(empleos);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return Result.failure("Error interno al obtener empleos: " + e.getMessage());
        }
    }

    @Override
    public Result<List<CandidatoDto>> buscarCandidatosPorEmpresa(String nombreEmpresa) {
        try {
            LOGGER.info("Buscando candidatos que trabajaron en: {}", nombreEmpresa);
            Map<String, Object> result = iempleoDao.buscarCandidatosPorEmpresa(nombreEmpresa);

            @SuppressWarnings("unchecked")
            List<CandidatoDto> candidatos = (List<CandidatoDto>) result.get(Constants.CUR_RESULTADO);

            if (candidatos == null) {
                return Result.failure("Error al buscar candidatos por empresa");
            }

            LOGGER.info("Se encontraron {} candidatos que trabajaron en {}", candidatos.size(), nombreEmpresa);
            return Result.success(candidatos);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return Result.failure("Error interno al buscar candidatos por empresa: " + e.getMessage());
        }
    }

    @Override
    public Result<List<CandidatoDto>> candidatosConAntiguedad2Anios() {
        try {
            LOGGER.info("Buscando candidatos con antigüedad mayor a 2 años");
            Map<String, Object> result = iempleoDao.candidatosConAntiguedad2Anios();

            @SuppressWarnings("unchecked")
            List<CandidatoDto> candidatos = (List<CandidatoDto>) result.get(Constants.CUR_RESULTADO);

            if (candidatos == null) {
                return Result.failure("Error al buscar candidatos con antigüedad");
            }

            LOGGER.info("Se encontraron {} candidatos con antigüedad > 2 años", candidatos.size());
            return Result.success(candidatos);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return Result.failure("Error interno al buscar candidatos con antigüedad: " + e.getMessage());
        }
    }

    @Override
    public Result<List<CandidatoDto>> candidatosConIngresoMayor(BigDecimal ingresoMinimo) {
        try {
            BigDecimal ingreso = ingresoMinimo != null ? ingresoMinimo :
                    new BigDecimal(Constants.INGRESO_MINIMO_DEFAULT);

            LOGGER.info("Buscando candidatos con ingreso mayor a: {}", ingreso);
            Map<String, Object> result = iempleoDao.candidatosConIngresoMayor(ingreso);

            @SuppressWarnings("unchecked")
            List<CandidatoDto> candidatos = (List<CandidatoDto>) result.get(Constants.CUR_RESULTADO);

            if (candidatos == null) {
                return Result.failure("Error al buscar candidatos con ingreso mayor");
            }

            LOGGER.info("Se encontraron {} candidatos con ingreso > {}", candidatos.size(), ingreso);
            return Result.success(candidatos);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return Result.failure("Error interno al buscar candidatos con ingreso mayor: " + e.getMessage());
        }
    }

    @Override
    public Result<List<CandidatoDto>> candidatosConExperiencia(BigDecimal aniosExperiencia) {
        try {
            BigDecimal anios = aniosExperiencia != null ? aniosExperiencia :
                    new BigDecimal(Constants.ANIOS_EXPERIENCIA_DEFAULT);

            LOGGER.info("Buscando candidatos con experiencia mayor a {} años", anios);
            Map<String, Object> result = iempleoDao.candidatosConExperiencia(anios);

            @SuppressWarnings("unchecked")
            List<CandidatoDto> candidatos = (List<CandidatoDto>) result.get(Constants.CUR_RESULTADO);

            if (candidatos == null) {
                return Result.failure("Error al buscar candidatos con experiencia");
            }

            LOGGER.info("Se encontraron {} candidatos con > {} años experiencia", candidatos.size(), anios);
            return Result.success(candidatos);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return Result.failure("Error interno al buscar candidatos con experiencia: " + e.getMessage());
        }
    }

    @Override
    public Result<List<CandidatoDto>> candidatosLaborandoActualmente() {
        try {
            LOGGER.info("Buscando candidatos laborando actualmente");
            Map<String, Object> result = iempleoDao.candidatosLaborandoActualmente();

            @SuppressWarnings("unchecked")
            List<CandidatoDto> candidatos = (List<CandidatoDto>) result.get(Constants.CUR_RESULTADO);

            if (candidatos == null) {
                return Result.failure("Error al buscar candidatos laborando actualmente");
            }

            LOGGER.info("Se encontraron {} candidatos laborando actualmente", candidatos.size());
            return Result.success(candidatos);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return Result.failure("Error interno al buscar candidatos laborando: " + e.getMessage());
        }
    }

}