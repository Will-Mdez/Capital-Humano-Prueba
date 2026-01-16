package com.tecnica.demo.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.tecnica.demo.enums.EnumHttpMensajes;
import lombok.Data;

/**
 * The type Generic insert response.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class GenericResponse<T> {

	/**
	 * codigo.
	 */
	private String codigo;
	/**
	 * mensaje.
	 */
	private String mensaje;
	/**
	 * folio.
	 */
	private String folio;
	/**
	 * info.
	 */
	private String info;
	/**
	 * detalles.
	 */
	private List<String> detalles;
	private Object resultado;

	public GenericResponse() {
		this.mensaje = EnumHttpMensajes.EOK_MENSAJE;
	}

	public GenericResponse(Object resultado) {
		this.mensaje = EnumHttpMensajes.EOK_MENSAJE;
		this.resultado = resultado;
	}

	public GenericResponse(List<String> detalles, EnumHttpMensajes enumHttpMensajes) {
		this.codigo = enumHttpMensajes.getCodigo();
		this.mensaje = enumHttpMensajes.getMensaje();
		this.info = enumHttpMensajes.getInfo();
		this.detalles = new ArrayList<>(detalles);
	}

	public GenericResponse(String codigo, String mensaje, Object resultado) {
		this.codigo = codigo;
		this.mensaje = mensaje;
		this.resultado = resultado;
	}
}
