-- Script para borrar los objetos de la base de datos del punto de venta

DROP TABLE detalle_compra;
DROP TABLE compras;
DROP TRIGGER verif_costo_less_precio_detev;
DROP TABLE detalle_venta;
DROP TABLE ventas;
DROP TABLE existencias;
DROP TRIGGER verif_costo_less_precio_pres;
DROP TABLE presentaciones;
DROP TABLE productos;
DROP TABLE clientes;
DROP TABLE proveedores;
DROP TABLE sucursales;
DROP SEQUENCE sucursal_id_seq;
DROP SEQUENCE proveedor_id_seq;
DROP SEQUENCE producto_id_seq;
DROP SEQUENCE presentacion_id_seq;
DROP SEQUENCE numero_venta_seq;
DROP SEQUENCE numero_compra_seq;