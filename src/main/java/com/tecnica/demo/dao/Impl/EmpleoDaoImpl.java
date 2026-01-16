package com.tecnica.demo.dao.Impl;

import com.tecnica.demo.config.repository.SimpleJdbcCallFactory;
import com.tecnica.demo.constant.Constants;
import com.tecnica.demo.dao.IEmpleoDao;
import com.tecnica.demo.dto.registry.ActualizarEmpleoRequest;
import com.tecnica.demo.dto.registry.CrearEmpleoRequest;
import com.tecnica.demo.rowmapper.CandidatoEmpleoRowMapper;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.SqlReturnResultSet;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>EmpleoDaoImpl.java</b>
 *
 * @version:  1.0
 * @descripcion: Implementación DAO para operaciones de empleos
 * @author: Sistema de Gestión de Candidatos
 */
@Repository
public class EmpleoDaoImpl implements IEmpleoDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmpleoDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SimpleJdbcCallFactory simpleJdbcCallFactory;

    @Override
    public Map<String, Object> insertarEmpleo(Long candidatoId, CrearEmpleoRequest request) {
        SimpleJdbcCall jdbcCall = simpleJdbcCallFactory.createJdbcCall(
                jdbcTemplate,
                Constants.PAQUETE,
                Constants.SP_INSERTAR_EMPLEO,
                new SqlParameter(Constants.PA_CANDIDATO_ID, OracleTypes.NUMBER),
                new SqlParameter(Constants.PA_NOMBRE_EMPRESA, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_FECHA_INGRESO, OracleTypes.DATE),
                new SqlParameter(Constants.PA_FECHA_SALIDA, OracleTypes.DATE),
                new SqlParameter(Constants.PA_INGRESO_MENSUAL, OracleTypes.NUMBER),
                new SqlParameter(Constants.PA_GIRO_EMPRESA, OracleTypes.VARCHAR),
                new SqlOutParameter(Constants.PA_EMPLEO_ID, OracleTypes.NUMBER)
        );

        try {
            Map<String, Object> inParams = new HashMap<>();
            inParams.put(Constants.PA_CANDIDATO_ID, candidatoId);
            inParams.put(Constants.PA_NOMBRE_EMPRESA, request.getNombreEmpresa());
            inParams.put(Constants.PA_FECHA_INGRESO, Date.valueOf(request.getFechaIngreso()));
            inParams.put(Constants.PA_FECHA_SALIDA, request.getFechaSalida() != null ? Date.valueOf(request.getFechaSalida()) : null);
            inParams.put(Constants.PA_INGRESO_MENSUAL, request.getIngresoMensual());
            inParams.put(Constants.PA_GIRO_EMPRESA, request.getGiroEmpresa());

            return jdbcCall.execute(inParams);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            throw e;
        }
    }

    @Override
    public Integer actualizarEmpleo(Long empleoId, ActualizarEmpleoRequest request) {
        SimpleJdbcCall jdbcCall = simpleJdbcCallFactory.createJdbcCall(
                jdbcTemplate,
                Constants.PAQUETE,
                Constants.SP_ACTUALIZAR_EMPLEO,
                new SqlParameter(Constants.PA_ID, OracleTypes.NUMBER),
                new SqlParameter(Constants.PA_NOMBRE_EMPRESA, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_FECHA_INGRESO, OracleTypes.DATE),
                new SqlParameter(Constants.PA_FECHA_SALIDA, OracleTypes.DATE),
                new SqlParameter(Constants.PA_INGRESO_MENSUAL, OracleTypes.NUMBER),
                new SqlParameter(Constants.PA_GIRO_EMPRESA, OracleTypes.VARCHAR),
                new SqlOutParameter(Constants.PA_STATUS_OPER, OracleTypes.NUMBER)
        );

        try {
            Map<String, Object> out = jdbcCall.execute(
                    empleoId,
                    request.getNombreEmpresa(),
                    Date.valueOf(request.getFechaIngreso()),
                    request.getFechaSalida() != null ? Date.valueOf(request.getFechaSalida()) : null,
                    request.getIngresoMensual(),
                    request.getGiroEmpresa()
            );

            BigDecimal statusOperBD = (BigDecimal) out.get(Constants.PA_STATUS_OPER);
            return statusOperBD != null ? statusOperBD.intValue() : null;

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return 1;
        }
    }

    @Override
    public Integer eliminarEmpleo(Long empleoId) {
        SimpleJdbcCall jdbcCall = simpleJdbcCallFactory.createJdbcCall(
                jdbcTemplate,
                Constants.PAQUETE,
                Constants.SP_ELIMINAR_EMPLEO,
                new SqlParameter(Constants.PA_ID, OracleTypes.NUMBER),
                new SqlOutParameter(Constants.PA_STATUS_OPER, OracleTypes.NUMBER)
        );

        try {
            Map<String, Object> out = jdbcCall.execute(empleoId);
            BigDecimal statusOperBD = (BigDecimal) out.get(Constants.PA_STATUS_OPER);
            return statusOperBD != null ? statusOperBD.intValue() : null;

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return 1;
        }
    }

    @Override
    public Map<String, Object> obtenerEmpleosCandidato(Long candidatoId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(Constants.PAQUETE)
                .withFunctionName(Constants.FN_OBTENER_EMPLEOS_CANDIDATO)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlOutParameter(Constants.CUR_RESULTADO, OracleTypes.CURSOR, new CandidatoEmpleoRowMapper()),
                        new SqlParameter(Constants.PA_CANDIDATO_ID, OracleTypes.NUMBER)
                );

        try {
            Map<String, Object> inParams = new HashMap<>();
            inParams.put(Constants.PA_CANDIDATO_ID, candidatoId);
            return jdbcCall.execute(inParams);
        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            throw e;
        }
    }

    @Override
    public Map<String, Object> buscarCandidatosPorEmpresa(String nombreEmpresa) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(Constants.PAQUETE)
                .withFunctionName(Constants.FN_CANDIDATOS_POR_EMPRESA)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlOutParameter(Constants.CUR_RESULTADO, OracleTypes.CURSOR, new CandidatoEmpleoRowMapper()),
                        new SqlParameter(Constants.PA_NOMBRE_EMPRESA, OracleTypes.VARCHAR)
                );

        try {
            Map<String, Object> inParams = new HashMap<>();
            inParams.put(Constants.PA_NOMBRE_EMPRESA, nombreEmpresa);
            return jdbcCall.execute(inParams);
        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            throw e;
        }
    }

    @Override
    public Map<String, Object> candidatosConAntiguedad2Anios() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(Constants.PAQUETE)
                .withFunctionName(Constants.FN_CANDIDATOS_ANTIGUEDAD_2ANIOS)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlOutParameter(Constants.CUR_RESULTADO, OracleTypes.CURSOR, new CandidatoEmpleoRowMapper())
                );

        try {
            return jdbcCall.execute(new HashMap<>());
        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            throw e;
        }
    }

    @Override
    public Map<String, Object> candidatosConIngresoMayor(BigDecimal ingresoMinimo) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(Constants.PAQUETE)
                .withFunctionName(Constants.FN_CANDIDATOS_INGRESO_MAYOR)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlOutParameter(Constants.CUR_RESULTADO, OracleTypes.CURSOR, new CandidatoEmpleoRowMapper()),
                        new SqlParameter(Constants.PA_INGRESO_MINIMO, OracleTypes.NUMBER)
                );

        try {
            Map<String, Object> inParams = new HashMap<>();
            inParams.put(Constants.PA_INGRESO_MINIMO, ingresoMinimo);
            return jdbcCall.execute(inParams);
        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            throw e;
        }
    }

    @Override
    public Map<String, Object> candidatosConExperiencia(BigDecimal aniosExperiencia) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(Constants.PAQUETE)
                .withFunctionName(Constants.FN_CANDIDATOS_EXPERIENCIA)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlOutParameter(Constants.CUR_RESULTADO, OracleTypes.CURSOR, new CandidatoEmpleoRowMapper()),
                        new SqlParameter(Constants.PA_ANIOS_EXPERIENCIA, OracleTypes.NUMBER)
                );

        try {
            Map<String, Object> inParams = new HashMap<>();
            inParams.put(Constants.PA_ANIOS_EXPERIENCIA, aniosExperiencia);
            return jdbcCall.execute(inParams);
        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            throw e;
        }
    }

    @Override
    public Map<String, Object> candidatosLaborandoActualmente() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(Constants.PAQUETE)
                .withFunctionName(Constants.FN_CANDIDATOS_LABORANDO_ACTUAL)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlOutParameter(Constants.CUR_RESULTADO, OracleTypes.CURSOR,
                                new CandidatoEmpleoRowMapper())
                )
                .returningResultSet(Constants.CUR_RESULTADO, new CandidatoEmpleoRowMapper());

        try {
            Map<String, Object> result = jdbcCall.execute(new HashMap<>());
            return result;
        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            throw e;
        }
    }

}