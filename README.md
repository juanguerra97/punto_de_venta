# Punto de Venta

Aplicacion de punto de venta sencillo, creado como proyecto en el curso Bases de Datos I

## Capturas de pantalla

![alt text](https://github.com/juanguerra97/punto_de_venta/raw/master/screenshoots/screen-productos.png "Captura de pantalla")

![alt text](https://github.com/juanguerra97/punto_de_venta/raw/master/screenshoots/screen-compras.png "Captura de pantalla")

![alt text](https://github.com/juanguerra97/punto_de_venta/raw/master/screenshoots/screen-nueva-venta.png "Captura de pantalla")

## Instalación

Instrucciones generales para ejecutar la aplicación

### Prerequisitos

El siguiente software es necesario para poder ejecutar la aplicación

* Java JDK 8 en adelante
* Oracle 11g (solamente he hecho las pruebas en la version Express Edition)
* Apache Maven (en mi caso, he utilizado la version 3.2.2)
* Eclipse IDE para Java EE(o Java SE con el plugin de maven instalado) o cualquier otro IDE con soporte para Apache Maven

> Nota: Para ejecutar los scripts SQL es necesario tener instalado SQLDeveloper o cualquier otra aplicación que permita conectarse a la base de datos y ejecutar scripts.

### Instalando

Sigue los siguientes pasos para poder ejecutar el programa

1. Crear un usuario en la base de datos con permisos para ejecutar comandos DDL. 

Para esto, necesitas conectarte a la base de datos con una cuenta de usuario administrador y ejecutar lo siguiente:

```

  CREATE USER admin_pv IDENTIFIED BY admin_pv;

```

> Puedes crear un usuario con otro nombre y contraseña, sin embargo tambien debes modificar las variables USERNAME y PASSWORD en el archivo [Conexion.java](src/main/java/jguerra/punto_de_venta/db/oracle/Conexion.java) que se encuentra en la ruta src/main/java/jguerra/punto_de_venta/db/oracle/Conexion.java

Luego hay que asignar los roles y privilegios del usuario

```

GRANT CONNECT, RESOURCE, DBA TO admin_pv;
GRANT CREATE SESSION GRANT ANY PRIVILEGE TO admin_pv;
GRANT UNLIMITED TABLESPACE TO admin_pv;

```

> Recuerda cambiar admin_pv por el nombre del usuario a utilizar

2. Iniciar sesión con el usuario creado y ejecutar el script [punto_de_venta-schema.sql](src/main/resources/db/punto_de_venta-schema.sql) que se encuentra en src/main/resources/db/punto_de_venta-schema.sql , este script creará las tablas y todo lo relacionado con el esquema de la base de datos.

3. Importar el proyecto como proyecto Maven en el IDE escogido.

> Se descargarán las dependencias y es posible que aparezca un error en el archivo pom.xml en la dependencia del driver ojdbc6. Para solucionar este error, ir al siguiente paso.


4. Solucionar error con dependencia ojdbc6

Abrir la ventana comandos en la carpeta del proyecto y ejecutar lo siguiente:

```

mvn install:install-file -Dfile='ruta' -DgroupId='com.oracle' -DartifactId=ojdbc6 -Dversion='11.2.0.4' -Dpackaging=jar

```
Debes sustituir ruta por la ruta del archivo [ojdbc6.jar](libs/ojdbc6.jar) que se encuentra en la carpeta libs del proyecto. Por ejemplo: C:\\users\\usuario\\proyectos\\punto_de_venta\\libs\\ojdbc6.jar

Una vez hecho esto, el error en el archivo pom.xml debería desaparecer y ya puedes ejecutar la aplicación. La clase con el método main es src/main/java/jguerra/punto_de_venta/gui/Main.java

## Autor

* **Juan Guerra** - [juanguerra97](https://github.com/juanguerra97)

