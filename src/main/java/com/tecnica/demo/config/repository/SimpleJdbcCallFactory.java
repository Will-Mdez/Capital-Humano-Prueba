package com.tecnica.demo.config.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;


@Component
public class SimpleJdbcCallFactory {

  /**
   * Metodo que crea una nueva instancia de SimpleJdbcCall con resultSet
   *
   * @param paquete       Paquete del procedure que se ejecutará
   * @param procedure     Nombre del Procedure que se ejecutará
   * @param parameterName Nombre del Parametro que será el resultSet
   * @param rowMapper     RowMapper que indica el mapeo para el resultSet
   * @param parameters    Parametros de entrada y salida que recibe el procedure
   * @return SimpleJdbcCall creado con los campos enviados
   */

  public SimpleJdbcCall createJdbcCall(JdbcTemplate jdbcTemplate, String paquete, String procedure,
                                       String parameterName, RowMapper<?> rowMapper, SqlParameter... parameters) {
    return new SimpleJdbcCall(jdbcTemplate).withCatalogName(paquete)
      .withProcedureName(procedure).withoutProcedureColumnMetaDataAccess().declareParameters(parameters)
      .returningResultSet(parameterName, rowMapper);
  }

  /**
   * Metodo que crea una nueva instancia de SimpleJdbcCall sin resultSet
   *
   * @param paquete    Paquete del procedure que se ejecutará
   * @param procedure  Nombre del Procedure que se ejecutará
   * @param parameters Parametros de entrada y salida que recibe el procedure
   * @return SimpleJdbcCall creado con los campos enviados
   */

  public SimpleJdbcCall createJdbcCall(JdbcTemplate jdbcTemplate, String paquete, String procedure,
                                       SqlParameter... parameters) {
    return new SimpleJdbcCall(jdbcTemplate).withCatalogName(paquete)
      .withProcedureName(procedure).withoutProcedureColumnMetaDataAccess().declareParameters(parameters);
  }
}
