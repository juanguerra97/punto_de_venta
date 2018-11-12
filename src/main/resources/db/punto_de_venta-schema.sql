
CREATE TABLE sucursales(
    id_sucursal INTEGER PRIMARY KEY,
    nombre VARCHAR2(50 CHAR) NOT NULL UNIQUE
);

CREATE SEQUENCE sucursal_id_seq
    START WITH 5000
    INCREMENT BY 1;

CREATE TABLE proveedores(
    id_proveedor INTEGER PRIMARY KEY,
    nombre VARCHAR2(50 CHAR) NOT NULL,
    telefono CHAR(8 CHAR) NOT NULL CHECK(REGEXP_LIKE(telefono,'^\d{8}$'))
);

CREATE SEQUENCE proveedor_id_seq
    START WITH 1000
    INCREMENT BY 1;

CREATE TABLE clientes(
    nit VARCHAR2(20 CHAR) PRIMARY KEY CHECK(REGEXP_LIKE(nit,'^\d+-[0-9kK]$')),
    nombre VARCHAR2(50 CHAR) NOT NULL,
    apellido VARCHAR2(50 CHAR) NOT NULL,
    telefono CHAR(8 CHAR) NOT NULL CHECK(REGEXP_LIKE(telefono,'^\d{8}$'))
);

CREATE TABLE productos(
    id_producto INTEGER PRIMARY KEY,
    nombre VARCHAR2(50 CHAR) NOT NULL,
    marca VARCHAR2(50 CHAR) NOT NULL,
    CONSTRAINT nom_marca_unique UNIQUE(nombre,marca)
);

CREATE SEQUENCE producto_id_seq
    START WITH 1000
    INCREMENT BY 1;

CREATE TABLE presentaciones(
    id_presentacion INTEGER PRIMARY KEY,
    nombre VARCHAR2(50 CHAR) NOT NULL,
    precio NUMBER(10,2) NOT NULL CHECK(precio >= 0.00),
    costo NUMBER(10,2) NOT NULL CHECK(costo >= 0.00),
    id_producto INTEGER NOT NULL,
    CONSTRAINT producto_presentacion_fk FOREIGN KEY(id_producto)
        REFERENCES productos(id_producto),
    CONSTRAINT presentacion_producto_unique UNIQUE(nombre,id_producto)
);

CREATE SEQUENCE presentacion_id_seq
    START WITH 1
    INCREMENT BY 1;

-- trigger que impide precios menores al costo en una presentacion
CREATE OR REPLACE TRIGGER verif_costo_less_precio_pres
    BEFORE INSERT OR UPDATE OF precio,costo 
    ON presentaciones
    FOR EACH ROW
BEGIN
        IF :NEW.precio < :NEW.costo THEN
            RAISE_APPLICATION_ERROR(-20001,'El precio no puede ser menor al costo');
        END IF;
END;
/
    
CREATE TABLE existencias(
    id_presentacion INTEGER NOT NULL,
    id_sucursal INTEGER NOT NULL,
    cantidad INTEGER NOT NULL CHECK(cantidad >= 0),
    CONSTRAINT existencia_pk PRIMARY KEY(id_presentacion,id_sucursal),
    CONSTRAINT presentacion_existencia_fk FOREIGN KEY(id_presentacion)
        REFERENCES presentaciones(id_presentacion),
    CONSTRAINT sucursal_existencia_fk FOREIGN KEY(id_sucursal)
        REFERENCES sucursales(id_sucursal)
);

CREATE TABLE ventas(
    numero INTEGER PRIMARY KEY,
    nit_cliente VARCHAR2(20 CHAR) NOT NULL,
    fecha DATE NOT NULL,
    total NUMBER(15,2) NOT NULL CHECK(total >= 0.00),
    id_sucursal INTEGER NOT NULL,
    CONSTRAINT cliente_venta_fk FOREIGN KEY(nit_cliente)
        REFERENCES clientes(nit),
    CONSTRAINT sucursal_venta_fk FOREIGN KEY(id_sucursal)
        REFERENCES sucursales(id_sucursal)
);

CREATE SEQUENCE numero_venta_seq
    START WITH 1
    INCREMENT BY 1;
    
CREATE TABLE detalle_venta(
    numero_venta INTEGER NOT NULL,
    nombre_producto VARCHAR2(50 CHAR) NOT NULL,
    marca_producto VARCHAR2(50 CHAR) NOT NULL,
    presentacion_producto VARCHAR2(50 CHAR) NOT NULL,
    costo NUMBER(10,2) NOT NULL CHECK(costo >= 0.00),
    precio NUMBER(10,2) NOT NULL CHECK(precio >= 0.00),
    cantidad INTEGER NOT NULL CHECK(cantidad > 0),
    CONSTRAINT detalle_venta_pk PRIMARY KEY(numero_venta,nombre_producto,marca_producto,presentacion_producto),
    CONSTRAINT venta_detalle_fk FOREIGN KEY(numero_venta)
        REFERENCES ventas(numero)
);

-- trigger que impide precios menores al costo en un detalle de venta
CREATE OR REPLACE TRIGGER verif_costo_less_precio_detev 
    BEFORE INSERT OR UPDATE OF precio,costo 
    ON detalle_venta
    FOR EACH ROW
BEGIN
        IF :NEW.precio < :NEW.costo THEN
            RAISE_APPLICATION_ERROR(-20001,'El precio no puede ser menor al costo');
        END IF;
END;
/

CREATE TABLE compras(
    numero INTEGER PRIMARY KEY,
    id_proveedor INTEGER NOT NULL,
    fecha DATE NOT NULL,
    total NUMBER(15,2) NOT NULL CHECK(total >= 0.00),
    CONSTRAINT proveedor_compra_fk FOREIGN KEY(id_proveedor)
        REFERENCES proveedores(id_proveedor)
);

CREATE SEQUENCE numero_compra_seq
    START WITH 1
    INCREMENT BY 1;
    
CREATE TABLE detalle_compra(
    numero_compra INTEGER NOT NULL,
    nombre_producto VARCHAR2(50 CHAR) NOT NULL,
    marca_producto VARCHAR2(50 CHAR) NOT NULL,
    presentacion_producto VARCHAR2(50 CHAR) NOT NULL,
    nombre_sucursal VARCHAR2(50 CHAR) NOT NULL,
    costo NUMBER(10,2) NOT NULL CHECK(costo >= 0.00),
    cantidad INTEGER NOT NULL CHECK(cantidad > 0),
    CONSTRAINT detalle_compra_pk PRIMARY KEY(numero_compra,nombre_producto,marca_producto,presentacion_producto,nombre_sucursal),
    CONSTRAINT compra_detalle_fk FOREIGN KEY(numero_compra)
        REFERENCES compras(numero)
);