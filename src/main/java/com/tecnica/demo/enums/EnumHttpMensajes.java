package com.tecnica.demo.enums;

import com.tecnica.demo.constant.Constants;
import org.springframework.http.HttpStatus;

import lombok.Getter;

import java.util.Locale;

@Getter
public enum EnumHttpMensajes {

	/**
	 * The E 400.
	 */
	E400(HttpStatus.BAD_REQUEST.value() + "." + getBasePathCapitalized(Constants.BASE_PATH) + ".00",
			EnumHttpMensajes.E400_MENSAJE, HttpStatus.BAD_REQUEST),
	/**
	 * The E 401.
	 */
	E401(HttpStatus.UNAUTHORIZED.value() + "." + getBasePathCapitalized(Constants.BASE_PATH) + ".00",
			EnumHttpMensajes.E401_MENSAJE, HttpStatus.UNAUTHORIZED),
	/**
	 * The E 404.
	 */
	E404(HttpStatus.NOT_FOUND.value() + "." + getBasePathCapitalized(Constants.BASE_PATH) + ".00",
			EnumHttpMensajes.E404_MENSAJE, HttpStatus.NOT_FOUND),
	/**
	 * The E 500.
	 */
	E500(HttpStatus.INTERNAL_SERVER_ERROR.value() + "." + getBasePathCapitalized(Constants.BASE_PATH)
			+ ".00", EnumHttpMensajes.E500_MENSAJE, HttpStatus.INTERNAL_SERVER_ERROR);

	public static final String EOK_MENSAJE = "Operación exitosa.";
	public static final String E401_MENSAJE = "Acceso a recurso no autorizado.";
	public static final String E400_MENSAJE = "Parámetros no válidos, por favor valide su información.";
	public static final String E404_MENSAJE = "Información no encontrada, favor de validar.";
	public static final String E500_MENSAJE = "Problema interno en el servidor, favor de validar.";
	private final String codigo;
	private final String mensaje;
	private final String info;
	private final HttpStatus httpStatus;

	public String getCodigo() {
		return codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public String getInfo() {
		return info;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	EnumHttpMensajes(String codigo, String mensaje, HttpStatus httpStatus) {

		this.codigo = codigo;
		this.mensaje = mensaje;
		this.info = Constants.URL_DEVELOPER_INFORMATION_CODES + codigo;
		this.httpStatus = httpStatus;
	}

	private static String getBasePathCapitalized(String basePath) {

		String[] palabras = basePath.split("/");

		StringBuilder resultado = new StringBuilder();

		for (int i = 0; i < palabras.length - 1; i++) {

			String palabra = palabras[i];

			if (palabra.isEmpty()) {

				continue;
			}

			String[] partes = palabra.split("-");

			for (int j = 0; j < partes.length; j++) {

				resultado.append(partes[j].substring(0, 1).toUpperCase(Locale.forLanguageTag("es-ES")));
				resultado.append(partes[j].substring(1).toLowerCase(Locale.forLanguageTag("es-ES")));

				if (j < partes.length - 1) {

					resultado.append("-");
				}
			}
			resultado.append("-");
		}

		resultado.setLength(resultado.length() - 1);

		return resultado.toString();
	}
}
