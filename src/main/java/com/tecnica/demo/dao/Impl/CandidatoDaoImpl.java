package com.tecnica.demo.dao.Impl;

import com.tecnica.demo.config.repository.SimpleJdbcCallFactory;
import com.tecnica.demo.dao.ICandidatoDao;
import com.tecnica.demo.constant.Constants;
import com.tecnica.demo.dto.registry.ActualizarDatosPersonalesRequest;
import com.tecnica.demo.dto.registry.ActualizarDomicilioRequest;
import com.tecnica.demo.dto.registry.CrearCandidatoRequest;
import com.tecnica.demo.rowmapper.CandidatoRowMapper;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>CandidatoDaoImpl.java</b>
 *
 * @version: 1.0
 * @descripcion: Implementación DAO para operaciones de candidatos
 * @author: Sistema de Gestión de Candidatos
 */
@Repository
public class CandidatoDaoImpl implements ICandidatoDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CandidatoDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SimpleJdbcCallFactory simpleJdbcCallFactory;

    @Override
    public Map<String, Object> insertarCandidato(CrearCandidatoRequest request) {
        SimpleJdbcCall jdbcCall = simpleJdbcCallFactory.createJdbcCall(
                jdbcTemplate,
                Constants.PAQUETE_CANDIDATOS,
                Constants.SP_INSERTAR_CANDIDATO,
                new SqlParameter(Constants.PA_NOMBRES, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_APELLIDOS, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_EMAIL, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_FECHA_NACIMIENTO, OracleTypes.DATE),
                new SqlParameter(Constants.PA_RFC, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_TELEFONO, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_CALLE, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_NUMERO, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_COLONIA, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_CIUDAD, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_ESTADO, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_PAIS, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_CODIGO_POSTAL, OracleTypes.VARCHAR),
                new SqlOutParameter(Constants.PA_CANDIDATO_ID, OracleTypes.NUMBER)
        );

        try {
            Map<String, Object> inParams = new HashMap<>();
            inParams.put(Constants.PA_NOMBRES, request.getNombres());
            inParams.put(Constants.PA_APELLIDOS, request.getApellidos());
            inParams.put(Constants.PA_EMAIL, request.getEmail());
            inParams.put(Constants.PA_FECHA_NACIMIENTO, Date.valueOf(request.getFechaNacimiento()));
            inParams.put(Constants.PA_RFC, request.getRfc());
            inParams.put(Constants.PA_TELEFONO, request.getTelefono());
            inParams.put(Constants.PA_CALLE, request.getCalle());
            inParams.put(Constants.PA_NUMERO, request.getNumero());
            inParams.put(Constants.PA_COLONIA, request.getColonia());
            inParams.put(Constants.PA_CIUDAD, request.getCiudad());
            inParams.put(Constants.PA_ESTADO, request.getEstado());
            inParams.put(Constants.PA_PAIS, request.getPais());
            inParams.put(Constants.PA_CODIGO_POSTAL, request.getCodigoPostal());

            return jdbcCall.execute(inParams);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            throw e;
        }
    }

    @Override
    public Integer actualizarDatosPersonales(Long candidatoId, ActualizarDatosPersonalesRequest request) {
        SimpleJdbcCall jdbcCall = simpleJdbcCallFactory.createJdbcCall(
                jdbcTemplate,
                Constants.PAQUETE_CANDIDATOS,
                Constants.SP_ACTUALIZAR_DATOS_PERSONALES,
                new SqlParameter(Constants.PA_ID, OracleTypes.NUMBER),
                new SqlParameter(Constants.PA_NOMBRES, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_APELLIDOS, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_EMAIL, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_FECHA_NACIMIENTO, OracleTypes.DATE),
                new SqlParameter(Constants.PA_RFC, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_TELEFONO, OracleTypes.VARCHAR),
                new SqlOutParameter(Constants.PA_STATUS_OPER, OracleTypes.NUMBER)
        );

        try {
            Map<String, Object> out = jdbcCall.execute(
                    candidatoId,
                    request.getNombres(),
                    request.getApellidos(),
                    request.getEmail(),
                    Date.valueOf(request.getFechaNacimiento()),
                    request.getRfc(),
                    request.getTelefono()
            );

            BigDecimal statusOperBD = (BigDecimal) out.get(Constants.PA_STATUS_OPER);
            return statusOperBD != null ? statusOperBD.intValue() : null;

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return 1;
        }
    }

    @Override
    public Integer actualizarDomicilio(Long candidatoId, ActualizarDomicilioRequest request) {
        SimpleJdbcCall jdbcCall = simpleJdbcCallFactory.createJdbcCall(
                jdbcTemplate,
                Constants.PAQUETE_CANDIDATOS,
                Constants.SP_ACTUALIZAR_DOMICILIO,
                new SqlParameter(Constants.PA_ID, OracleTypes.NUMBER),
                new SqlParameter(Constants.PA_CALLE, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_NUMERO, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_COLONIA, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_CIUDAD, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_ESTADO, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_PAIS, OracleTypes.VARCHAR),
                new SqlParameter(Constants.PA_CODIGO_POSTAL, OracleTypes.VARCHAR),
                new SqlOutParameter(Constants.PA_STATUS_OPER, OracleTypes.NUMBER)
        );

        try {
            Map<String, Object> out = jdbcCall.execute(
                    candidatoId,
                    request.getCalle(),
                    request.getNumero(),
                    request.getColonia(),
                    request.getCiudad(),
                    request.getEstado(),
                    request.getPais(),
                    request.getCodigoPostal()
            );

            BigDecimal statusOperBD = (BigDecimal) out.get(Constants.PA_STATUS_OPER);
            return statusOperBD != null ? statusOperBD.intValue() : null;

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return 1;
        }
    }

    @Override
    public Integer eliminarCandidato(Long candidatoId) {
        SimpleJdbcCall jdbcCall = simpleJdbcCallFactory.createJdbcCall(
                jdbcTemplate,
                Constants.PAQUETE_CANDIDATOS,
                Constants.SP_ELIMINAR_CANDIDATO,
                new SqlParameter(Constants.PA_ID, OracleTypes.NUMBER),
                new SqlOutParameter(Constants.PA_STATUS_OPER, OracleTypes.NUMBER)
        );

        try {
            Map<String, Object> out = jdbcCall.execute(candidatoId);
            BigDecimal statusOperBD = (BigDecimal) out.get(Constants.PA_STATUS_OPER);
            return statusOperBD != null ? statusOperBD.intValue() : null;

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            return 1;
        }
    }

    @Override
    public Map<String, Object> obtenerCandidato(Long candidatoId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(Constants.PAQUETE_CANDIDATOS)
                .withFunctionName(Constants.FN_OBTENER_CANDIDATO)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        // Parámetro de RETORNO de la función
                        new SqlOutParameter(Constants.CUR_RESULTADO, OracleTypes.CURSOR, new CandidatoRowMapper()),
                        // Parámetro de entrada
                        new SqlParameter(Constants.PA_ID, OracleTypes.NUMBER)
                );

        try {
            Map<String, Object> inParams = new HashMap<>();
            inParams.put(Constants.PA_ID, candidatoId);

            return jdbcCall.execute(inParams);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            throw e;
        }
    }

    @Override
    public Map<String, Object> listarCandidatos() {
        SimpleJdbcCall jdbcCall = simpleJdbcCallFactory.createJdbcCall(
                jdbcTemplate,
                Constants.PAQUETE_CANDIDATOS,
                Constants.sp_listar_candidatos,
                new SqlOutParameter(Constants.CUR_RESULTADO, OracleTypes.CURSOR, new CandidatoRowMapper())
        );

        try {
            return jdbcCall.execute();

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            throw e;
        }
    }

    @Override
    public Map<String, Object> buscarPorCodigoPostal(String codigoPostal) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(Constants.PAQUETE_CANDIDATOS)
                .withFunctionName(Constants.FN_BUSCAR_POR_CP)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        // Parámetro de RETORNO de la función
                        new SqlOutParameter(Constants.CUR_RESULTADO, OracleTypes.CURSOR, new CandidatoRowMapper()),
                        // Parámetro de entrada
                        new SqlParameter(Constants.PA_CODIGO_POSTAL, OracleTypes.VARCHAR)
                );

        try {
            Map<String, Object> inParams = new HashMap<>();
            inParams.put(Constants.PA_CODIGO_POSTAL, codigoPostal);

            return jdbcCall.execute(inParams);

        } catch (RuntimeException e) {
            LOGGER.error(Constants.LOG_ERROR, e);
            throw e;
        }
    }

}