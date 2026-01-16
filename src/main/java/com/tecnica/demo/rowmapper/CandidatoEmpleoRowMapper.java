package com.tecnica.demo.rowmapper;

import com.tecnica.demo.dto.CandidatoDto;
import com.tecnica.demo.dto.EmpleoDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

/**
 * <b>CandidatoEmpleoRowMapper.java</b>
 *
 * @version:  1.0
 * @descripcion: RowMapper para mapear resultados de candidatos con empleos
 * @author: Sistema de Gestión de Candidatos
 */
public class CandidatoEmpleoRowMapper implements RowMapper<CandidatoDto> {

    @Override
    public CandidatoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        CandidatoDto candidato = new CandidatoDto();

        // Mapeo de datos del candidato
        candidato.setId(rs.getLong("ID"));
        candidato.setNombres(rs.getString("NOMBRES"));
        candidato.setApellidos(rs.getString("APELLIDOS"));
        candidato.setEmail(rs.getString("EMAIL"));

        if (rs.getDate("FECHA_NACIMIENTO") != null) {
            candidato.setFechaNacimiento(rs.getDate("FECHA_NACIMIENTO").toLocalDate());
        }

        candidato.setRfc(rs.getString("RFC"));
        candidato.setTelefono(rs.getString("TELEFONO"));
        candidato.setCalle(rs.getString("CALLE"));
        candidato.setNumero(rs.getString("NUMERO"));
        candidato.setColonia(rs.getString("COLONIA"));
        candidato.setCiudad(rs.getString("CIUDAD"));
        candidato.setEstado(rs.getString("ESTADO"));
        candidato.setPais(rs.getString("PAIS"));
        candidato.setCodigoPostal(rs.getString("CODIGO_POSTAL"));

        if (rs.getObject("ACTIVO") != null) {
            candidato.setActivo(rs.getInt("ACTIVO") == 1);
        }

        // Mapeo de datos de empleo (si existen en el resultado)
        try {
            EmpleoDto empleo = new EmpleoDto();

            if (rs.getObject("NOMBRE_EMPRESA") != null) {
                empleo.setNombreEmpresa(rs.getString("NOMBRE_EMPRESA"));
            }

            if (rs.getDate("FECHA_INGRESO") != null) {
                empleo.setFechaIngreso(rs.getDate("FECHA_INGRESO").toLocalDate());
            }

            if (rs.getDate("FECHA_SALIDA") != null) {
                empleo.setFechaSalida(rs.getDate("FECHA_SALIDA").toLocalDate());
            }

            if (rs.getObject("INGRESO_MENSUAL") != null) {
                empleo.setIngresoMensual(rs.getBigDecimal("INGRESO_MENSUAL"));
            }

            if (rs.getObject("GIRO_EMPRESA") != null) {
                empleo.setGiroEmpresa(rs.getString("GIRO_EMPRESA"));
            }

            // Antigüedad en años (si existe en la consulta)
            if (hasColumn(rs, "ANIOS_ANTIGUEDAD")) {
                if (rs.getObject("ANIOS_ANTIGUEDAD") != null) {
                    empleo.setAntiguedadAnios(rs.getBigDecimal("ANIOS_ANTIGUEDAD"));
                }
            }

            // Solo agregar el empleo si tiene información
            if (empleo.getNombreEmpresa() != null) {
                candidato.setListaEmpleos(java.util.Collections.singletonList(empleo));
            }

        } catch (SQLException e) {
            // Si no hay columnas de empleo, simplemente no se mapean
            // Esto permite que el mismo mapper funcione para diferentes consultas
        }

        // Experiencia total (si existe en la consulta)
        if (hasColumn(rs, "ANIOS_EXPERIENCIA")) {
            if (rs.getObject("ANIOS_EXPERIENCIA") != null) {
                candidato.setExperienciaTotal(rs.getDouble("ANIOS_EXPERIENCIA"));
            }
        }

        return candidato;
    }

    /**
     * Método helper para verificar si una columna existe en el ResultSet
     */
    private boolean hasColumn(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

}