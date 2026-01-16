
CREATE USER TEST
IDENTIFIED BY Test123
DEFAULT TABLESPACE USERS
TEMPORARY TABLESPACE TEMP
QUOTA UNLIMITED ON USERS;


-- =====================================================
-- Script de creación de tablas
-- Base de datos: Oracle 19c
-- Proyecto: Sistema de Gestión de Candidatos
-- =====================================================

-- Tabla de Candidatos
CREATE TABLE candidatos (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombres VARCHAR2(100) NOT NULL,
    apellidos VARCHAR2(100) NOT NULL,
    email VARCHAR2(150) NOT NULL UNIQUE,
    fecha_nacimiento DATE NOT NULL,
    rfc VARCHAR2(13) NOT NULL UNIQUE,
    telefono VARCHAR2(20),
    calle VARCHAR2(200),
    numero VARCHAR2(20),
    colonia VARCHAR2(100),
    ciudad VARCHAR2(100),
    estado VARCHAR2(100),
    pais VARCHAR2(100),
    codigo_postal VARCHAR2(10),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo NUMBER(1) DEFAULT 1,
    CONSTRAINT chk_activo CHECK (activo IN (0, 1))
);

-- Tabla de Empleos
CREATE TABLE empleos (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    candidato_id NUMBER NOT NULL,
    nombre_empresa VARCHAR2(200) NOT NULL,
    fecha_ingreso DATE NOT NULL,
    fecha_salida DATE,
    ingreso_mensual NUMBER(10,2) NOT NULL,
    giro_empresa VARCHAR2(100),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo NUMBER(1) DEFAULT 1,
    CONSTRAINT fk_empleo_candidato FOREIGN KEY (candidato_id) 
        REFERENCES candidatos(id) ON DELETE CASCADE,
    CONSTRAINT chk_empleo_activo CHECK (activo IN (0, 1)),
    CONSTRAINT chk_fechas CHECK (fecha_salida IS NULL OR fecha_salida >= fecha_ingreso),
    CONSTRAINT chk_ingreso CHECK (ingreso_mensual > 0)
);

-- Índices para mejorar el rendimiento
CREATE INDEX idx_candidatos_email ON candidatos(email);
CREATE INDEX idx_candidatos_rfc ON candidatos(rfc);
CREATE INDEX idx_candidatos_cp ON candidatos(codigo_postal);
CREATE INDEX idx_empleos_candidato ON empleos(candidato_id);
CREATE INDEX idx_empleos_empresa ON empleos(nombre_empresa);
CREATE INDEX idx_empleos_ingreso ON empleos(ingreso_mensual);
CREATE INDEX idx_empleos_fecha_salida ON empleos(fecha_salida);

-- Trigger para actualizar fecha_actualizacion en candidatos
CREATE OR REPLACE TRIGGER trg_candidatos_update
BEFORE UPDATE ON candidatos
FOR EACH ROW
BEGIN
    :NEW.fecha_actualizacion := CURRENT_TIMESTAMP;
END;


-- Trigger para actualizar fecha_actualizacion en empleos
CREATE OR REPLACE TRIGGER trg_empleos_update
BEFORE UPDATE ON empleos
FOR EACH ROW
BEGIN
    :NEW.fecha_actualizacion := CURRENT_TIMESTAMP;
END;



-- =====================================================
-- Paquete de Candidatos
-- Contiene todos los procedimientos y funciones para 
-- gestionar candidatos
-- =====================================================

CREATE OR REPLACE PACKAGE pkg_candidatos AS
    
    -- Procedimiento para insertar un candidato
    PROCEDURE sp_insertar_candidato(
        p_nombres IN VARCHAR2,
        p_apellidos IN VARCHAR2,
        p_email IN VARCHAR2,
        p_fecha_nacimiento IN DATE,
        p_rfc IN VARCHAR2,
        p_telefono IN VARCHAR2,
        p_calle IN VARCHAR2,
        p_numero IN VARCHAR2,
        p_colonia IN VARCHAR2,
        p_ciudad IN VARCHAR2,
        p_estado IN VARCHAR2,
        p_pais IN VARCHAR2,
        p_codigo_postal IN VARCHAR2,
        p_candidato_id OUT NUMBER
    );
    
    -- Procedimiento para actualizar datos personales
    PROCEDURE sp_actualizar_datos_personales(
        p_id IN NUMBER,
        p_nombres IN VARCHAR2,
        p_apellidos IN VARCHAR2,
        p_email IN VARCHAR2,
        p_fecha_nacimiento IN DATE,
        p_rfc IN VARCHAR2,
        p_telefono IN VARCHAR2,
        p_status_oper OUT NUMBER
    );
    
    -- Procedimiento para actualizar domicilio
    PROCEDURE sp_actualizar_domicilio(
        p_id IN NUMBER,
        p_calle IN VARCHAR2,
        p_numero IN VARCHAR2,
        p_colonia IN VARCHAR2,
        p_ciudad IN VARCHAR2,
        p_estado IN VARCHAR2,
        p_pais IN VARCHAR2,
        p_codigo_postal IN VARCHAR2,
        p_status_oper OUT NUMBER
    );
    
    -- Procedimiento para eliminar candidato (soft delete)
    PROCEDURE sp_eliminar_candidato(
        p_id IN NUMBER,
        p_status_oper OUT NUMBER
    );
    
    -- Función para obtener candidato por ID
    FUNCTION fn_obtener_candidato(
        p_id IN NUMBER
    ) RETURN SYS_REFCURSOR;
    
    -- Función para listar todos los candidatos activos
    PROCEDURE sp_listar_candidatos(p_cursor OUT SYS_REFCURSOR);
    
    -- Función para buscar candidatos por código postal
    FUNCTION fn_buscar_por_cp(
        p_codigo_postal IN VARCHAR2
    ) RETURN SYS_REFCURSOR;
    
END pkg_candidatos;


CREATE OR REPLACE PACKAGE BODY pkg_candidatos AS
    
    -- Implementación: Insertar candidato
    PROCEDURE sp_insertar_candidato(
        p_nombres IN VARCHAR2,
        p_apellidos IN VARCHAR2,
        p_email IN VARCHAR2,
        p_fecha_nacimiento IN DATE,
        p_rfc IN VARCHAR2,
        p_telefono IN VARCHAR2,
        p_calle IN VARCHAR2,
        p_numero IN VARCHAR2,
        p_colonia IN VARCHAR2,
        p_ciudad IN VARCHAR2,
        p_estado IN VARCHAR2,
        p_pais IN VARCHAR2,
        p_codigo_postal IN VARCHAR2,
        p_candidato_id OUT NUMBER
    ) AS
    BEGIN
        INSERT INTO candidatos (
            nombres, apellidos, email, fecha_nacimiento, rfc, telefono,
            calle, numero, colonia, ciudad, estado, pais, codigo_postal
        ) VALUES (
            p_nombres, p_apellidos, p_email, p_fecha_nacimiento, p_rfc, p_telefono,
            p_calle, p_numero, p_colonia, p_ciudad, p_estado, p_pais, p_codigo_postal
        ) RETURNING id INTO p_candidato_id;
        
        COMMIT;
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(-20001, 'El email o RFC ya existe en la base de datos');
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE_APPLICATION_ERROR(-20002, 'Error al insertar candidato: ' || SQLERRM);
    END sp_insertar_candidato;
    
    -- Implementación: Actualizar datos personales
    PROCEDURE sp_actualizar_datos_personales(
        p_id IN NUMBER,
        p_nombres IN VARCHAR2,
        p_apellidos IN VARCHAR2,
        p_email IN VARCHAR2,
        p_fecha_nacimiento IN DATE,
        p_rfc IN VARCHAR2,
        p_telefono IN VARCHAR2,
    	p_status_oper OUT NUMBER
    ) AS
        v_count NUMBER;
    BEGIN
        SELECT COUNT(*) INTO v_count FROM candidatos WHERE id = p_id AND activo = 1;
        
        IF v_count = 0 THEN
            RAISE_APPLICATION_ERROR(-20003, 'Candidato no encontrado o inactivo');
        END IF;
        
        UPDATE candidatos
        SET nombres = p_nombres,
            apellidos = p_apellidos,
            email = p_email,
            fecha_nacimiento = p_fecha_nacimiento,
            rfc = p_rfc,
            telefono = p_telefono
        WHERE id = p_id AND activo = 1;
        IF SQL%ROWCOUNT > 0 THEN
	        p_status_oper := 0; 
	        COMMIT;
	    ELSE
	        p_status_oper := 1;
	    END IF;
        
        COMMIT;
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(-20001, 'El email o RFC ya existe en la base de datos');
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END sp_actualizar_datos_personales;
    
    -- Implementación: Actualizar domicilio
    PROCEDURE sp_actualizar_domicilio(
        p_id IN NUMBER,
        p_calle IN VARCHAR2,
        p_numero IN VARCHAR2,
        p_colonia IN VARCHAR2,
        p_ciudad IN VARCHAR2,
        p_estado IN VARCHAR2,
        p_pais IN VARCHAR2,
        p_codigo_postal IN VARCHAR2,
    	p_status_oper OUT NUMBER
    ) AS
        v_count NUMBER;
    BEGIN
        SELECT COUNT(*) INTO v_count FROM candidatos WHERE id = p_id AND activo = 1;
        
        IF v_count = 0 THEN
            RAISE_APPLICATION_ERROR(-20003, 'Candidato no encontrado o inactivo');
        END IF;
        
        UPDATE candidatos
        SET calle = p_calle,
            numero = p_numero,
            colonia = p_colonia,
            ciudad = p_ciudad,
            estado = p_estado,
            pais = p_pais,
            codigo_postal = p_codigo_postal
        WHERE id = p_id AND activo = 1;
        IF SQL%ROWCOUNT > 0 THEN
	        p_status_oper := 0; 
	        COMMIT;
	    ELSE
	        p_status_oper := 1;
	    END IF;
        
        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END sp_actualizar_domicilio;
    
    -- Implementación: Eliminar candidato (soft delete)
    PROCEDURE sp_eliminar_candidato(
    	p_id IN NUMBER,
	    p_status_oper OUT NUMBER -- Agregamos el parámetro de salida
	) AS
	BEGIN
	    -- Intentamos la actualización lógica (baja lógica)
	    UPDATE candidatos 
	    SET activo = 0 
	    WHERE id = p_id AND activo = 1;
	
	    -- Verificamos si se encontró y actualizó el candidato
	    IF SQL%ROWCOUNT > 0 THEN
	        -- Si existía el candidato, procedemos a desactivar sus empleos
	        UPDATE empleos 
	        SET activo = 0 
	        WHERE candidato_id = p_id;
	        
	        p_status_oper := 0; -- Éxito
	        COMMIT;
	    ELSE
	        -- Si no se afectaron filas, el candidato no existe o ya estaba inactivo
	        p_status_oper := 1; 
	    END IF;
	
	EXCEPTION
	    WHEN OTHERS THEN
	        ROLLBACK;
	        p_status_oper := 1; -- Error en la transacción
	        -- Opcional: registrar el error en una tabla de LOGS antes de salir
	END sp_eliminar_candidato;
    
    -- Implementación: Obtener candidato por ID
    FUNCTION fn_obtener_candidato(
        p_id IN NUMBER
    ) RETURN SYS_REFCURSOR AS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM candidatos WHERE id = p_id AND activo = 1;
        RETURN v_cursor;
    END fn_obtener_candidato;
    
    -- Implementación: Listar candidatos
    PROCEDURE sp_listar_candidatos(p_cursor OUT SYS_REFCURSOR) AS
	BEGIN
	    OPEN p_cursor FOR
	        SELECT * FROM candidatos 
	        WHERE activo = 1 
	        ORDER BY apellidos, nombres;
	END sp_listar_candidatos;
    
    -- Implementación: Buscar por código postal
    FUNCTION fn_buscar_por_cp(
        p_codigo_postal IN VARCHAR2
    ) RETURN SYS_REFCURSOR AS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM candidatos 
            WHERE codigo_postal = p_codigo_postal AND activo = 1
            ORDER BY apellidos, nombres;
        RETURN v_cursor;
    END fn_buscar_por_cp;
    
END pkg_candidatos;



-- =====================================================
-- Paquete de Empleos
-- Contiene todos los procedimientos y funciones para 
-- gestionar empleos de candidatos
-- =====================================================

CREATE OR REPLACE PACKAGE pkg_empleos AS
    
    -- Procedimiento para insertar un empleo
    PROCEDURE sp_insertar_empleo(
        p_candidato_id IN NUMBER,
        p_nombre_empresa IN VARCHAR2,
        p_fecha_ingreso IN DATE,
        p_fecha_salida IN DATE,
        p_ingreso_mensual IN NUMBER,
        p_giro_empresa IN VARCHAR2,
        p_empleo_id OUT NUMBER
    );
    
    -- Procedimiento para actualizar empleo
    PROCEDURE sp_actualizar_empleo(
        p_id IN NUMBER,
        p_nombre_empresa IN VARCHAR2,
        p_fecha_ingreso IN DATE,
        p_fecha_salida IN DATE,
        p_ingreso_mensual IN NUMBER,
        p_giro_empresa IN VARCHAR2,
	    p_status_oper OUT NUMBER 
    );
    
    -- Procedimiento para eliminar empleo
    PROCEDURE sp_eliminar_empleo(
        p_id IN NUMBER,
	    p_status_oper OUT NUMBER 
    );
    
    -- Función para obtener empleos de un candidato
    FUNCTION fn_obtener_empleos_candidato(
        p_candidato_id IN NUMBER
    ) RETURN SYS_REFCURSOR;
    
    -- Función para buscar candidatos por empresa
    FUNCTION fn_candidatos_por_empresa(
        p_nombre_empresa IN VARCHAR2
    ) RETURN SYS_REFCURSOR;
    
    -- Función para candidatos con antigüedad > 2 años en último empleo
    FUNCTION fn_candidatos_antiguedad_2anios RETURN SYS_REFCURSOR;
    
    -- Función para candidatos con ingreso > 10000
    FUNCTION fn_candidatos_ingreso_mayor(
        p_ingreso_minimo IN NUMBER DEFAULT 10000
    ) RETURN SYS_REFCURSOR;
    
    -- Función para candidatos con más de X años de experiencia
    FUNCTION fn_candidatos_experiencia(
        p_anios_experiencia IN NUMBER DEFAULT 5
    ) RETURN SYS_REFCURSOR;
    
    -- Función para candidatos laborando actualmente
    FUNCTION fn_candidatos_laborando_actual RETURN SYS_REFCURSOR;
    
END pkg_empleos;


CREATE OR REPLACE PACKAGE BODY pkg_empleos AS
    
    -- Implementación: Insertar empleo
    PROCEDURE sp_insertar_empleo(
        p_candidato_id IN NUMBER,
        p_nombre_empresa IN VARCHAR2,
        p_fecha_ingreso IN DATE,
        p_fecha_salida IN DATE,
        p_ingreso_mensual IN NUMBER,
        p_giro_empresa IN VARCHAR2,
        p_empleo_id OUT NUMBER
    ) AS
        v_count NUMBER;
    BEGIN
        -- Verificar que el candidato existe
        SELECT COUNT(*) INTO v_count FROM candidatos 
        WHERE id = p_candidato_id AND activo = 1;
        
        IF v_count = 0 THEN
            RAISE_APPLICATION_ERROR(-20004, 'Candidato no encontrado o inactivo');
        END IF;
        
        INSERT INTO empleos (
            candidato_id, nombre_empresa, fecha_ingreso, fecha_salida,
            ingreso_mensual, giro_empresa
        ) VALUES (
            p_candidato_id, p_nombre_empresa, p_fecha_ingreso, p_fecha_salida,
            p_ingreso_mensual, p_giro_empresa
        ) RETURNING id INTO p_empleo_id;
        
        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE_APPLICATION_ERROR(-20005, 'Error al insertar empleo: ' || SQLERRM);
    END sp_insertar_empleo;
    
    -- Implementación: Actualizar empleo
    PROCEDURE sp_actualizar_empleo(
        p_id IN NUMBER,
        p_nombre_empresa IN VARCHAR2,
        p_fecha_ingreso IN DATE,
        p_fecha_salida IN DATE,
        p_ingreso_mensual IN NUMBER,
        p_giro_empresa IN VARCHAR2,
	    p_status_oper OUT NUMBER 
    ) AS
        v_count NUMBER;
    BEGIN
        SELECT COUNT(*) INTO v_count FROM empleos WHERE id = p_id AND activo = 1;
        
        IF v_count = 0 THEN
            RAISE_APPLICATION_ERROR(-20006, 'Empleo no encontrado o inactivo');
        END IF;
        
        UPDATE empleos
        SET nombre_empresa = p_nombre_empresa,
            fecha_ingreso = p_fecha_ingreso,
            fecha_salida = p_fecha_salida,
            ingreso_mensual = p_ingreso_mensual,
            giro_empresa = p_giro_empresa
        WHERE id = p_id AND activo = 1;
        IF SQL%ROWCOUNT > 0 THEN
	        p_status_oper := 0; 
	        COMMIT;
	    ELSE
	        p_status_oper := 1;
	    END IF;
        
        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END sp_actualizar_empleo;
    
    -- Implementación: Eliminar empleo
    PROCEDURE sp_eliminar_empleo(
        p_id IN NUMBER,
	    p_status_oper OUT NUMBER 
    ) AS
        v_count NUMBER;
    BEGIN
        SELECT COUNT(*) INTO v_count FROM empleos WHERE id = p_id;
        
        IF v_count = 0 THEN
            RAISE_APPLICATION_ERROR(-20006, 'Empleo no encontrado');
        END IF;
        
        UPDATE empleos SET activo = 0 WHERE id = p_id;
        IF SQL%ROWCOUNT > 0 THEN
	        p_status_oper := 0; 
	        COMMIT;
	    ELSE
	        p_status_oper := 1;
	    END IF;
        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END sp_eliminar_empleo;
    
    -- Implementación: Obtener empleos de candidato
    FUNCTION fn_obtener_empleos_candidato(
        p_candidato_id IN NUMBER
    ) RETURN SYS_REFCURSOR AS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM empleos 
            WHERE candidato_id = p_candidato_id AND activo = 1
            ORDER BY fecha_ingreso DESC;
        RETURN v_cursor;
    END fn_obtener_empleos_candidato;
    
    -- Implementación: Candidatos por empresa
    FUNCTION fn_candidatos_por_empresa(
        p_nombre_empresa IN VARCHAR2
    ) RETURN SYS_REFCURSOR AS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT DISTINCT c.*
            FROM candidatos c
            INNER JOIN empleos e ON c.id = e.candidato_id
            WHERE UPPER(e.nombre_empresa) LIKE UPPER('%' || p_nombre_empresa || '%')
                AND c.activo = 1
                AND e.activo = 1
            ORDER BY c.apellidos, c.nombres;
        RETURN v_cursor;
    END fn_candidatos_por_empresa;
    
    -- Implementación: Candidatos con antigüedad > 2 años en último empleo
    FUNCTION fn_candidatos_antiguedad_2anios RETURN SYS_REFCURSOR AS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            WITH ultimo_empleo AS (
                SELECT candidato_id, 
                       MAX(fecha_ingreso) as ultima_fecha_ingreso
                FROM empleos
                WHERE activo = 1
                GROUP BY candidato_id
            )
            SELECT DISTINCT c.*, e.nombre_empresa, e.fecha_ingreso, e.fecha_salida,
                   CASE 
                       WHEN e.fecha_salida IS NULL THEN 
                           TRUNC(MONTHS_BETWEEN(SYSDATE, e.fecha_ingreso) / 12, 2)
                       ELSE 
                           TRUNC(MONTHS_BETWEEN(e.fecha_salida, e.fecha_ingreso) / 12, 2)
                   END as anios_antiguedad
            FROM candidatos c
            INNER JOIN empleos e ON c.id = e.candidato_id
            INNER JOIN ultimo_empleo ue ON e.candidato_id = ue.candidato_id 
                AND e.fecha_ingreso = ue.ultima_fecha_ingreso
            WHERE c.activo = 1
                AND e.activo = 1
                AND (
                    (e.fecha_salida IS NULL AND MONTHS_BETWEEN(SYSDATE, e.fecha_ingreso) >= 24) OR
                    (e.fecha_salida IS NOT NULL AND MONTHS_BETWEEN(e.fecha_salida, e.fecha_ingreso) >= 24)
                )
            ORDER BY c.apellidos, c.nombres;
        RETURN v_cursor;
    END fn_candidatos_antiguedad_2anios;
    
    -- Implementación: Candidatos con ingreso mayor a X
    FUNCTION fn_candidatos_ingreso_mayor(
        p_ingreso_minimo IN NUMBER DEFAULT 10000
    ) RETURN SYS_REFCURSOR AS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT DISTINCT c.*, 
                   e.nombre_empresa, 
                   e.ingreso_mensual,
                   e.fecha_ingreso,
                   e.fecha_salida
            FROM candidatos c
            INNER JOIN empleos e ON c.id = e.candidato_id
            WHERE e.ingreso_mensual > p_ingreso_minimo
                AND c.activo = 1
                AND e.activo = 1
            ORDER BY c.apellidos, c.nombres;
        RETURN v_cursor;
    END fn_candidatos_ingreso_mayor;
    
    -- Implementación: Candidatos con más de X años de experiencia
    FUNCTION fn_candidatos_experiencia(
        p_anios_experiencia IN NUMBER DEFAULT 5
    ) RETURN SYS_REFCURSOR AS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            WITH experiencia_total AS (
                SELECT candidato_id,
                       SUM(
                           CASE 
                               WHEN fecha_salida IS NULL THEN 
                                   MONTHS_BETWEEN(SYSDATE, fecha_ingreso)
                               ELSE 
                                   MONTHS_BETWEEN(fecha_salida, fecha_ingreso)
                           END
                       ) / 12 as total_anios
                FROM empleos
                WHERE activo = 1
                GROUP BY candidato_id
                HAVING SUM(
                    CASE 
                        WHEN fecha_salida IS NULL THEN 
                            MONTHS_BETWEEN(SYSDATE, fecha_ingreso)
                        ELSE 
                            MONTHS_BETWEEN(fecha_salida, fecha_ingreso)
                    END
                ) / 12 > p_anios_experiencia
            )
            SELECT c.*, TRUNC(et.total_anios, 2) as anios_experiencia
            FROM candidatos c
            INNER JOIN experiencia_total et ON c.id = et.candidato_id
            WHERE c.activo = 1
            ORDER BY et.total_anios DESC, c.apellidos, c.nombres;
        RETURN v_cursor;
    END fn_candidatos_experiencia;
    
    -- Implementación: Candidatos laborando actualmente
    FUNCTION fn_candidatos_laborando_actual RETURN SYS_REFCURSOR AS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT DISTINCT c.*, 
                   e.nombre_empresa, 
                   e.fecha_ingreso,
                   e.ingreso_mensual,
                   e.giro_empresa
            FROM candidatos c
            INNER JOIN empleos e ON c.id = e.candidato_id
            WHERE e.fecha_salida IS NULL
                AND c.activo = 1
                AND e.activo = 1
            ORDER BY c.apellidos, c.nombres;
        RETURN v_cursor;
    END fn_candidatos_laborando_actual;
    
END pkg_empleos;



-- =====================================================
-- Script de inserción de datos de prueba
-- =====================================================

-- Variables para almacenar IDs generados
DECLARE
    v_candidato_id NUMBER;
    v_empleo_id NUMBER;
BEGIN
    -- =====================================================
    -- CANDIDATO 1: Juan Pérez (Laborando actualmente en Banamex)
    -- =====================================================
    pkg_candidatos.sp_insertar_candidato(
        p_nombres => 'Juan Carlos',
        p_apellidos => 'Pérez González',
        p_email => 'juan.perez@email.com',
        p_fecha_nacimiento => TO_DATE('1990-05-15', 'YYYY-MM-DD'),
        p_rfc => 'PEGJ900515ABC',
        p_telefono => '5512345678',
        p_calle => 'Insurgentes Sur',
        p_numero => '1234',
        p_colonia => 'Del Valle',
        p_ciudad => 'Ciudad de México',
        p_estado => 'CDMX',
        p_pais => 'México',
        p_codigo_postal => '14000',
        p_candidato_id => v_candidato_id
    );
    
    -- Empleo anterior
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'BBVA',
        p_fecha_ingreso => TO_DATE('2015-01-15', 'YYYY-MM-DD'),
        p_fecha_salida => TO_DATE('2020-06-30', 'YYYY-MM-DD'),
        p_ingreso_mensual => 15000,
        p_giro_empresa => 'Banca',
        p_empleo_id => v_empleo_id
    );
    
    -- Empleo actual
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'Banamex',
        p_fecha_ingreso => TO_DATE('2020-07-01', 'YYYY-MM-DD'),
        p_fecha_salida => NULL,
        p_ingreso_mensual => 25000,
        p_giro_empresa => 'Banca',
        p_empleo_id => v_empleo_id
    );
    
    -- =====================================================
    -- CANDIDATO 2: María López (Mucha experiencia laboral)
    -- =====================================================
    pkg_candidatos.sp_insertar_candidato(
        p_nombres => 'María Fernanda',
        p_apellidos => 'López Martínez',
        p_email => 'maria.lopez@email.com',
        p_fecha_nacimiento => TO_DATE('1985-03-20', 'YYYY-MM-DD'),
        p_rfc => 'LOMF850320XYZ',
        p_telefono => '5587654321',
        p_calle => 'Revolución',
        p_numero => '567',
        p_colonia => 'San Ángel',
        p_ciudad => 'Ciudad de México',
        p_estado => 'CDMX',
        p_pais => 'México',
        p_codigo_postal => '01000',
        p_candidato_id => v_candidato_id
    );
    
    -- Múltiples empleos (más de 5 años de experiencia)
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'Televisa',
        p_fecha_ingreso => TO_DATE('2010-02-01', 'YYYY-MM-DD'),
        p_fecha_salida => TO_DATE('2013-12-31', 'YYYY-MM-DD'),
        p_ingreso_mensual => 12000,
        p_giro_empresa => 'Medios',
        p_empleo_id => v_empleo_id
    );
    
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'Google México',
        p_fecha_ingreso => TO_DATE('2014-03-01', 'YYYY-MM-DD'),
        p_fecha_salida => TO_DATE('2018-08-31', 'YYYY-MM-DD'),
        p_ingreso_mensual => 35000,
        p_giro_empresa => 'Tecnología',
        p_empleo_id => v_empleo_id
    );
    
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'Amazon',
        p_fecha_ingreso => TO_DATE('2018-09-15', 'YYYY-MM-DD'),
        p_fecha_salida => NULL,
        p_ingreso_mensual => 45000,
        p_giro_empresa => 'Tecnología',
        p_empleo_id => v_empleo_id
    );
    
    -- =====================================================
    -- CANDIDATO 3: Carlos Ramírez (Trabajó en Banamex)
    -- =====================================================
    pkg_candidatos.sp_insertar_candidato(
        p_nombres => 'Carlos Alberto',
        p_apellidos => 'Ramírez Sánchez',
        p_email => 'carlos.ramirez@email.com',
        p_fecha_nacimiento => TO_DATE('1992-11-10', 'YYYY-MM-DD'),
        p_rfc => 'RASC921110DEF',
        p_telefono => '5598765432',
        p_calle => 'Paseo de la Reforma',
        p_numero => '890',
        p_colonia => 'Juárez',
        p_ciudad => 'Ciudad de México',
        p_estado => 'CDMX',
        p_pais => 'México',
        p_codigo_postal => '06600',
        p_candidato_id => v_candidato_id
    );
    
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'Banamex',
        p_fecha_ingreso => TO_DATE('2017-04-01', 'YYYY-MM-DD'),
        p_fecha_salida => TO_DATE('2021-03-31', 'YYYY-MM-DD'),
        p_ingreso_mensual => 18000,
        p_giro_empresa => 'Banca',
        p_empleo_id => v_empleo_id
    );
    
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'Scotiabank',
        p_fecha_ingreso => TO_DATE('2021-05-01', 'YYYY-MM-DD'),
        p_fecha_salida => NULL,
        p_ingreso_mensual => 22000,
        p_giro_empresa => 'Banca',
        p_empleo_id => v_empleo_id
    );
    
    -- =====================================================
    -- CANDIDATO 4: Ana García (Residencia Tlalpan)
    -- =====================================================
    pkg_candidatos.sp_insertar_candidato(
        p_nombres => 'Ana Patricia',
        p_apellidos => 'García Hernández',
        p_email => 'ana.garcia@email.com',
        p_fecha_nacimiento => TO_DATE('1995-07-25', 'YYYY-MM-DD'),
        p_rfc => 'GAHA950725GHI',
        p_telefono => '5545678901',
        p_calle => 'Camino Real a Coyoacán',
        p_numero => '100',
        p_colonia => 'Pedregal de Santa Úrsula',
        p_ciudad => 'Ciudad de México',
        p_estado => 'CDMX',
        p_pais => 'México',
        p_codigo_postal => '14420',
        p_candidato_id => v_candidato_id
    );
    
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'Liverpool',
        p_fecha_ingreso => TO_DATE('2019-06-15', 'YYYY-MM-DD'),
        p_fecha_salida => NULL,
        p_ingreso_mensual => 11000,
        p_giro_empresa => 'Retail',
        p_empleo_id => v_empleo_id
    );
    
    -- =====================================================
    -- CANDIDATO 5: Roberto Torres (Alto salario)
    -- =====================================================
    pkg_candidatos.sp_insertar_candidato(
        p_nombres => 'Roberto',
        p_apellidos => 'Torres Jiménez',
        p_email => 'roberto.torres@email.com',
        p_fecha_nacimiento => TO_DATE('1988-09-12', 'YYYY-MM-DD'),
        p_rfc => 'TOJR880912JKL',
        p_telefono => '5534567890',
        p_calle => 'Polanco',
        p_numero => '456',
        p_colonia => 'Polanco',
        p_ciudad => 'Ciudad de México',
        p_estado => 'CDMX',
        p_pais => 'México',
        p_codigo_postal => '11560',
        p_candidato_id => v_candidato_id
    );
    
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'Microsoft',
        p_fecha_ingreso => TO_DATE('2016-08-01', 'YYYY-MM-DD'),
        p_fecha_salida => TO_DATE('2020-12-31', 'YYYY-MM-DD'),
        p_ingreso_mensual => 50000,
        p_giro_empresa => 'Tecnología',
        p_empleo_id => v_empleo_id
    );
    
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'Meta',
        p_fecha_ingreso => TO_DATE('2021-02-01', 'YYYY-MM-DD'),
        p_fecha_salida => NULL,
        p_ingreso_mensual => 60000,
        p_giro_empresa => 'Tecnología',
        p_empleo_id => v_empleo_id
    );
    
    -- =====================================================
    -- CANDIDATO 6: Laura Méndez (Antigüedad > 2 años)
    -- =====================================================
    pkg_candidatos.sp_insertar_candidato(
        p_nombres => 'Laura Elena',
        p_apellidos => 'Méndez Ruiz',
        p_email => 'laura.mendez@email.com',
        p_fecha_nacimiento => TO_DATE('1993-04-18', 'YYYY-MM-DD'),
        p_rfc => 'MERL930418MNO',
        p_telefono => '5523456789',
        p_calle => 'Eje Central',
        p_numero => '789',
        p_colonia => 'Centro',
        p_ciudad => 'Ciudad de México',
        p_estado => 'CDMX',
        p_pais => 'México',
        p_codigo_postal => '06000',
        p_candidato_id => v_candidato_id
    );
    
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'Pemex',
        p_fecha_ingreso => TO_DATE('2020-01-15', 'YYYY-MM-DD'),
        p_fecha_salida => NULL,
        p_ingreso_mensual => 28000,
        p_giro_empresa => 'Energía',
        p_empleo_id => v_empleo_id
    );
    
    -- =====================================================
    -- CANDIDATO 7: Pedro Morales (Sin empleo actual)
    -- =====================================================
    pkg_candidatos.sp_insertar_candidato(
        p_nombres => 'Pedro Luis',
        p_apellidos => 'Morales Castro',
        p_email => 'pedro.morales@email.com',
        p_fecha_nacimiento => TO_DATE('1991-12-05', 'YYYY-MM-DD'),
        p_rfc => 'MOCP911205PQR',
        p_telefono => '5556789012',
        p_calle => 'Universidad',
        p_numero => '321',
        p_colonia => 'Copilco',
        p_ciudad => 'Ciudad de México',
        p_estado => 'CDMX',
        p_pais => 'México',
        p_codigo_postal => '04360',
        p_candidato_id => v_candidato_id
    );
    
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'Bimbo',
        p_fecha_ingreso => TO_DATE('2018-03-01', 'YYYY-MM-DD'),
        p_fecha_salida => TO_DATE('2023-12-31', 'YYYY-MM-DD'),
        p_ingreso_mensual => 14000,
        p_giro_empresa => 'Alimentos',
        p_empleo_id => v_empleo_id
    );
    
    -- =====================================================
    -- CANDIDATO 8: Sofía Vargas (Residencia Tlalpan 14000)
    -- =====================================================
    pkg_candidatos.sp_insertar_candidato(
        p_nombres => 'Sofía Isabel',
        p_apellidos => 'Vargas Rojas',
        p_email => 'sofia.vargas@email.com',
        p_fecha_nacimiento => TO_DATE('1994-08-30', 'YYYY-MM-DD'),
        p_rfc => 'VARS940830STU',
        p_telefono => '5567890123',
        p_calle => 'Tlalpan',
        p_numero => '555',
        p_colonia => 'Tlalpan Centro',
        p_ciudad => 'Ciudad de México',
        p_estado => 'CDMX',
        p_pais => 'México',
        p_codigo_postal => '14000',
        p_candidato_id => v_candidato_id
    );
    
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'Banamex',
        p_fecha_ingreso => TO_DATE('2019-09-01', 'YYYY-MM-DD'),
        p_fecha_salida => TO_DATE('2022-08-31', 'YYYY-MM-DD'),
        p_ingreso_mensual => 16000,
        p_giro_empresa => 'Banca',
        p_empleo_id => v_empleo_id
    );
    
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'Santander',
        p_fecha_ingreso => TO_DATE('2022-10-01', 'YYYY-MM-DD'),
        p_fecha_salida => NULL,
        p_ingreso_mensual => 19000,
        p_giro_empresa => 'Banca',
        p_empleo_id => v_empleo_id
    );
    
    -- =====================================================
    -- CANDIDATO 9: Diego Flores (Más de 5 años experiencia)
    -- =====================================================
    pkg_candidatos.sp_insertar_candidato(
        p_nombres => 'Diego Armando',
        p_apellidos => 'Flores Delgado',
        p_email => 'diego.flores@email.com',
        p_fecha_nacimiento => TO_DATE('1987-02-14', 'YYYY-MM-DD'),
        p_rfc => 'FODD870214VWX',
        p_telefono => '5578901234',
        p_calle => 'Constituyentes',
        p_numero => '999',
        p_colonia => 'Lomas Altas',
        p_ciudad => 'Ciudad de México',
        p_estado => 'CDMX',
        p_pais => 'México',
        p_codigo_postal => '11950',
        p_candidato_id => v_candidato_id
    );
    
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'IBM',
        p_fecha_ingreso => TO_DATE('2012-05-01', 'YYYY-MM-DD'),
        p_fecha_salida => TO_DATE('2016-12-31', 'YYYY-MM-DD'),
        p_ingreso_mensual => 22000,
        p_giro_empresa => 'Tecnología',
        p_empleo_id => v_empleo_id
    );
    
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'Oracle',
        p_fecha_ingreso => TO_DATE('2017-02-01', 'YYYY-MM-DD'),
        p_fecha_salida => TO_DATE('2020-06-30', 'YYYY-MM-DD'),
        p_ingreso_mensual => 28000,
        p_giro_empresa => 'Tecnología',
        p_empleo_id => v_empleo_id
    );
    
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'SAP',
        p_fecha_ingreso => TO_DATE('2020-08-01', 'YYYY-MM-DD'),
        p_fecha_salida => NULL,
        p_ingreso_mensual => 35000,
        p_giro_empresa => 'Tecnología',
        p_empleo_id => v_empleo_id
    );
    
    -- =====================================================
    -- CANDIDATO 10: Gabriela Ruiz (Alto ingreso actual)
    -- =====================================================
    pkg_candidatos.sp_insertar_candidato(
        p_nombres => 'Gabriela',
        p_apellidos => 'Ruiz Ortega',
        p_email => 'gabriela.ruiz@email.com',
        p_fecha_nacimiento => TO_DATE('1989-06-22', 'YYYY-MM-DD'),
        p_rfc => 'RUOG890622YZA',
        p_telefono => '5589012345',
        p_calle => 'Santa Fe',
        p_numero => '777',
        p_colonia => 'Santa Fe',
        p_ciudad => 'Ciudad de México',
        p_estado => 'CDMX',
        p_pais => 'México',
        p_codigo_postal => '01376',
        p_candidato_id => v_candidato_id
    );
    
    pkg_empleos.sp_insertar_empleo(
        p_candidato_id => v_candidato_id,
        p_nombre_empresa => 'Citibanamex',
        p_fecha_ingreso => TO_DATE('2022-01-10', 'YYYY-MM-DD'),
        p_fecha_salida => NULL,
        p_ingreso_mensual => 42000,
        p_giro_empresa => 'Banca',
        p_empleo_id => v_empleo_id
    );
    
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Datos de prueba insertados correctamente');
    DBMS_OUTPUT.PUT_LINE('Total candidatos: 10');
    DBMS_OUTPUT.PUT_LINE('Total empleos: Varios por candidato');
    
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error al insertar datos: ' || SQLERRM);
        RAISE;
END;





SELECT * FROM EMPLEOS e 

