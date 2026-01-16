package com.tecnica.demo.constant;

/**
 * <b>Constants.java</b>
 */

public class Constants {


  /**
   * Valor para clase Util
   */
  public static final int VAL5 = 5;

  /**
   * The constant OPERACION_EXITOSA_ESTATUS.
   */
  public static final String OPERACION_EXITOSA_ESTATUS = "Operación exitosa";
  /**
   * The constant PARAM_NULL.
   */
  public static final String PARAM_NULL = "Parámetros de entrada incorrectos, por favor valide su información.";
  /**
   * The constant PRO_IN_SERVER.
   */
  public static final String PRO_IN_SERVER = "Problema interno en el servidor, favor de validar.";
  /**
   * Mensaje Excepcion 500 a
   */
  public static final String DETALLES_EXCEPCION_500_A = "Error en la petición";
  /**
   * The constant REQUIRED_HEADER_FECHA.
   */
  public static final String REQUIRED_HEADER_FECHA = "El header 'headerFecha' es requerido";
  /**
   * The constant AFORE_AZTECA_SCAFFOLDING.
   */
  public static final String PROJECT_MSJ_EXCEPTION = "Project-Test";
  /**
   * The constant SUCCESSFUL_REQUEST_MESSAGE.
   */
  public static final String SUCCESSFUL_REQUEST_MESSAGE = "Operación Exitosa";
  /**
   * The constant MSJ_CAMPO_REQUERIDO.
   */
  public static final String MSJ_CAMPO_REQUERIDO = "Campo Requerido: ";
  /**
   * The constant MSJ_BODY_REQUERIDO.
   */
  public static final String MSJ_BODY_REQUERIDO = "Body Requerido: ";
  /**
   * The constant DATE_FORMAT_UTC.
   */
  public static final String DATE_FORMAT_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
  /**
   * The constant DATE_FORMAT_FOLIO.
   */
  public static final String DATE_FORMAT_FOLIO = "yyyyMMddHHmmssSSS";


  public static final String URL_DEVELOPER_INFORMATION_CODES = "info#";

  //ERORES

  public static final String ERROR_AL_OBTENER_MONEDAS = "Error al obtener monedas, código de estado: ";
  public static final String ERROR_AL_AGREGAR_MONEDAS = "Error al agregar monedas, código de estado ";
  public static final String ERROR_AL_ACTUALIZAR_MONEDAS = "Error al actualizar monedas, código de estado: ";
  public static final String ERROR_AL_ELIMINAR_MONEDAS = "Error al eliminar monedas, código de estado: ";


  // Nombre del paquete PL/SQL
  public static final String PAQUETE = "TEST.PKG_EMPLEOS";
  public static final String PAQUETE_CANDIDATOS = "TEST.PKG_CANDIDATOS";

  // Nombres de procedimientos almacenados
  public static final String SP_INSERTAR_EMPLEO = "SP_INSERTAR_EMPLEO";
  public static final String SP_ACTUALIZAR_DOMICILIO = "SP_ACTUALIZAR_DOMICILIO";
  public static final String SP_ACTUALIZAR_DATOS_PERSONALES = "SP_ACTUALIZAR_DATOS_PERSONALES";
  public static final String SP_ELIMINAR_CANDIDATO = "SP_ELIMINAR_CANDIDATO";
  public static final String SP_INSERTAR_CANDIDATO = "SP_INSERTAR_CANDIDATO";
  public static final String SP_ACTUALIZAR_EMPLEO = "SP_ACTUALIZAR_EMPLEO";
  public static final String SP_ELIMINAR_EMPLEO = "SP_ELIMINAR_EMPLEO";

  // Nombres de funciones
  public static final String FN_OBTENER_EMPLEOS_CANDIDATO = "FN_OBTENER_EMPLEOS_CANDIDATO";
  public static final String FN_OBTENER_CANDIDATO = "FN_OBTENER_CANDIDATO";
  public static final String sp_listar_candidatos = "sp_listar_candidatos";
  public static final String FN_BUSCAR_POR_CP = "FN_BUSCAR_POR_CP";
  public static final String FN_CANDIDATOS_POR_EMPRESA = "FN_CANDIDATOS_POR_EMPRESA";
  public static final String FN_CANDIDATOS_ANTIGUEDAD_2ANIOS = "FN_CANDIDATOS_ANTIGUEDAD_2ANIOS";
  public static final String FN_CANDIDATOS_INGRESO_MAYOR = "FN_CANDIDATOS_INGRESO_MAYOR";
  public static final String FN_CANDIDATOS_EXPERIENCIA = "FN_CANDIDATOS_EXPERIENCIA";
  public static final String FN_CANDIDATOS_LABORANDO_ACTUAL = "FN_CANDIDATOS_LABORANDO_ACTUAL";

  // Parámetros de entrada/salida
  public static final String PA_ID = "P_ID";
  public static final String PA_CANDIDATO_ID = "P_CANDIDATO_ID";
  public static final String PA_NOMBRE_EMPRESA = "P_NOMBRE_EMPRESA";
  public static final String PA_FECHA_INGRESO = "P_FECHA_INGRESO";
  public static final String PA_FECHA_SALIDA = "P_FECHA_SALIDA";
  public static final String PA_INGRESO_MENSUAL = "P_INGRESO_MENSUAL";
  public static final String PA_GIRO_EMPRESA = "P_GIRO_EMPRESA";
  public static final String PA_EMPLEO_ID = "P_EMPLEO_ID";
  public static final String PA_STATUS_OPER = "P_STATUS_OPER";
  public static final String PA_INGRESO_MINIMO = "P_INGRESO_MINIMO";
  public static final String PA_ANIOS_EXPERIENCIA = "P_ANIOS_EXPERIENCIA";

  // Parámetros de entrada/salida
  public static final String PA_NOMBRES = "P_NOMBRES";
  public static final String PA_APELLIDOS = "P_APELLIDOS";
  public static final String PA_EMAIL = "P_EMAIL";
  public static final String PA_FECHA_NACIMIENTO = "P_FECHA_NACIMIENTO";
  public static final String PA_RFC = "P_RFC";
  public static final String PA_TELEFONO = "P_TELEFONO";
  public static final String PA_CALLE = "P_CALLE";
  public static final String PA_NUMERO = "P_NUMERO";
  public static final String PA_COLONIA = "P_COLONIA";
  public static final String PA_CIUDAD = "P_CIUDAD";
  public static final String PA_ESTADO = "P_ESTADO";
  public static final String PA_PAIS = "P_PAIS";
  public static final String PA_CODIGO_POSTAL = "P_CODIGO_POSTAL";
  // Cursores
  public static final String CUR_RESULTADO = "CUR_RESULTADO";

  // Mensajes de log
  public static final String LOG_ERROR = "Error en operación de empleo";
  public static final String LOG_INFO_INSERTAR = "Insertando empleo";
  public static final String LOG_INFO_ACTUALIZAR = "Actualizando empleo";
  public static final String LOG_INFO_ELIMINAR = "Eliminando empleo";
  public static final String LOG_INFO_CONSULTAR = "Consultando empleos";

  // Códigos de status
  public static final int STATUS_OK = 0;
  public static final int STATUS_ERROR = 1;

  // Valores por defecto
  public static final String INGRESO_MINIMO_DEFAULT = "10000";
  public static final String ANIOS_EXPERIENCIA_DEFAULT = "5";



  /**
   * The constant ZONE_ID.
   */
  public static final String ZONE_ID = "America/Mexico_City";
  /**
   * The constant HEADER_UDUARIO.
   */
  public static final String HEADER_UDUARIO = "x-token-usuario";
  /**
   * The constant PATH_CONTROLLER.
   */
  public static final String PATH_CONTROLLER = "/Test" + "";
  /**
   * The constant BASE_PATH.
   */
  public static final String BASE_PATH = "/Test/v1" + "";
  /**
   * The constant E400.
   */
  public static final String E400 = "E400";
  /**
   * The constant E401.
   */
  public static final String E401 = "E401";
  /**
   * The constant E404.
   */
  public static final String E404 = "E404";
  /**
   * The constant DETALLE.
   */
  public static final String DETALLE = "detalle";
  /**
   * The constant FOLIO_TRACE.
   */
  public static final String FOLIO_TRACE = "x-id-interaccion";
  /**
   * The constant ERROR_INTERNO.
   */
  public static final String ERROR_INTERNO = "Error interno en el servidor";
  public static final String ERROR_INTERNO_FAVOR_DE_VALIDAR = "Error interno en el servidor";



  public static final String PATH_CONTROLLER_LISTAR_CANDIDATOS = "/listarCandidatos";


  /**
   * Constructor privado, se agrega para sobreescribir el implicito publico y la
   * clase no pueda ser instanciada por otros paquetes
   */
  private Constants() {

    super();
  }

}
