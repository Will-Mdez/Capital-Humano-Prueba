package com.tecnica.demo.service.Impl;

import com.tecnica.demo.constant.Constants;
import com.tecnica.demo.dao.ICandidatoDao;
import com.tecnica.demo.dto.CandidatoDto;
import com.tecnica.demo.dto.registry.ActualizarDatosPersonalesRequest;
import com.tecnica.demo.dto.registry.ActualizarDomicilioRequest;
import com.tecnica.demo.dto.registry.CrearCandidatoRequest;
import com.tecnica.demo.service.ICandidatoService;
import com.tecnica.demo.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import java.math.BigDecimal;
import java.util.List;

/**
 * <b>CandidatoServiceImpl.java</b>
 *
 * @version:  1.0
 * @descripcion: Implementación del servicio de candidatos
 * @author: Sistema de Gestión de Candidatos
 */
@Service
public class CandidatoServiceImpl implements ICandidatoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CandidatoServiceImpl.class);

    @Autowired
    private ICandidatoDao candidatoDao;

    @Override
    public Result<Long> crearCandidato(CrearCandidatoRequest request) {
        try {
            LOGGER.info(Constants.LOG_INFO_INSERTAR);
            Map<String, Object> result = candidatoDao.insertarCandidato(request);

            BigDecimal candidatoIdBD = (BigDecimal) result.get(Constants.PA_CANDIDATO_ID);

            if (candidatoIdBD == null) {
                return Result.failure("Error al crear candidato: No se obtuvo ID");
            }

            Long candidatoId = candidatoIdBD.longValue();
            LOGGER.info("Candidato creado exitosamente con ID: {}", candidatoId);

            return Result.success(candidatoId);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return Result.failure("Error interno al crear candidato: " + e.getMessage());
        }
    }

    @Override
    public Result<Integer> actualizarDatosPersonales(Long candidatoId, ActualizarDatosPersonalesRequest request) {
        try {
            LOGGER.info(Constants.LOG_INFO_ACTUALIZAR + " ID: {}", candidatoId);
            Integer statusOper = candidatoDao.actualizarDatosPersonales(candidatoId, request);

            if (statusOper == null || statusOper != Constants.STATUS_OK) {
                return Result.failure("Error al actualizar datos personales. Status: " + statusOper);
            }

            LOGGER.info("Datos personales actualizados exitosamente para candidato ID: {}", candidatoId);
            return Result.success(statusOper);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return Result.failure("Error interno al actualizar datos personales: " + e.getMessage());
        }
    }

    @Override
    public Result<Integer> actualizarDomicilio(Long candidatoId, ActualizarDomicilioRequest request) {
        try {
            LOGGER.info("Actualizando domicilio para candidato ID: {}", candidatoId);
            Integer statusOper = candidatoDao.actualizarDomicilio(candidatoId, request);

            if (statusOper == null || statusOper != Constants.STATUS_OK) {
                return Result.failure("Error al actualizar domicilio. Status: " + statusOper);
            }

            LOGGER.info("Domicilio actualizado exitosamente para candidato ID: {}", candidatoId);
            return Result.success(statusOper);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return Result.failure("Error interno al actualizar domicilio: " + e.getMessage());
        }
    }

    @Override
    public Result<Integer> eliminarCandidato(Long candidatoId) {
        try {
            LOGGER.info(Constants.LOG_INFO_ELIMINAR + " ID: {}", candidatoId);
            Integer statusOper = candidatoDao.eliminarCandidato(candidatoId);

            if (statusOper == null || statusOper != Constants.STATUS_OK) {
                return Result.failure("Error al eliminar candidato. Status: " + statusOper);
            }

            LOGGER.info("Candidato eliminado exitosamente ID: {}", candidatoId);
            return Result.success(statusOper);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return Result.failure("Error interno al eliminar candidato: " + e.getMessage());
        }
    }

    @Override
    public Result<CandidatoDto> obtenerCandidato(Long candidatoId) {
        try {
            LOGGER.info(Constants.LOG_INFO_CONSULTAR + " ID: {}", candidatoId);
            Map<String, Object> result = candidatoDao.obtenerCandidato(candidatoId);

            @SuppressWarnings("unchecked")
            List<CandidatoDto> candidatos = (List<CandidatoDto>) result.get(Constants.CUR_RESULTADO);

            if (candidatos == null || candidatos.isEmpty()) {
                return Result.failure("Candidato no encontrado con ID: " + candidatoId);
            }

            return Result.success(candidatos.get(0));

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return Result.failure("Error interno al obtener candidato: " + e.getMessage());
        }
    }

    @Override
    public Result<List<CandidatoDto>> listarCandidatos() {
        try {
            LOGGER.info("Listando todos los candidatos");
            Map<String, Object> result = candidatoDao.listarCandidatos();

            @SuppressWarnings("unchecked")
            List<CandidatoDto> candidatos = (List<CandidatoDto>) result.get(Constants.CUR_RESULTADO);

            if (candidatos == null) {
                return Result.failure("Error al obtener la lista de candidatos");
            }

            LOGGER.info("Se obtuvieron {} candidatos", candidatos.size());
            return Result.success(candidatos);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return Result.failure("Error interno al listar candidatos: " + e.getMessage());
        }
    }

    @Override
    public Result<List<CandidatoDto>> buscarPorCodigoPostal(String codigoPostal) {
        try {
            LOGGER.info("Buscando candidatos por código postal: {}", codigoPostal);
            Map<String, Object> result = candidatoDao.buscarPorCodigoPostal(codigoPostal);

            @SuppressWarnings("unchecked")
            List<CandidatoDto> candidatos = (List<CandidatoDto>) result.get(Constants.CUR_RESULTADO);

            if (candidatos == null) {
                return Result.failure("Error al buscar candidatos por código postal");
            }

            LOGGER.info("Se encontraron {} candidatos con CP: {}", candidatos.size(), codigoPostal);
            return Result.success(candidatos);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return Result.failure("Error interno al buscar por código postal: " + e.getMessage());
        }
    }

}
