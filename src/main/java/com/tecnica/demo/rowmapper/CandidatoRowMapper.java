package com.tecnica.demo.rowmapper;

import com.tecnica.demo.dto.CandidatoDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * <b>CandidatoRowMapper.java</b>
 *
 * @version: 1.0
 * @descripcion: RowMapper para mapear resultados de candidatos
 * @author: Sistema de Gestión de Candidatos
 */
public class CandidatoRowMapper implements RowMapper<CandidatoDto> {

    @Override
    public CandidatoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        CandidatoDto candidato = new CandidatoDto();

        candidato.setId(rs.getLong("ID"));
        candidato.setNombres(rs.getString("NOMBRES"));
        candidato.setApellidos(rs.getString("APELLIDOS"));
        candidato.setEmail(rs.getString("EMAIL"));

        // Mapeo de fecha de nacimiento
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

        // Mapeo de campo activo
        if (rs.getObject("ACTIVO") != null) {
            candidato.setActivo(rs.getInt("ACTIVO") == 1);
        }

        return candidato;
    }

}