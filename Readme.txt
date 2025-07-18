# Proyecto Fullstack Java Spring Boot - Evaluación 3

Este proyecto es una aplicación completa desarrollada con Java Spring Boot que cubre dos módulos principales: **Módulo 1 (Cliente y Usuario)** y **Módulo 2 (Producto y Carrito de Compras, Ordenes)**. Incluye APIs REST con validaciones, lógica de negocio, DTOs (Request/Response) y pruebas unitarias.

---

## Índice

- [Requisitos Previos](#requisitos-previos)  
- [Configuración Inicial](#configuración-inicial)  
- [Ejecución del Proyecto](#ejecución-del-proyecto)  
- [Uso del Proyecto](#uso-del-proyecto)  
  - [Módulo 1: Gestión de Clientes y Usuarios](#módulo-1-gestión-de-clientes-y-usuarios)  
  - [Módulo 2: Gestión de Productos, Carrito y Ordenes](#módulo-2-gestión-de-productos-carrito-y-ordenes)  
- [Uso de Postman](#uso-de-postman)  
- [Pruebas Unitarias](#pruebas-unitarias)  
- [Contacto](#contacto)  

---

## Requisitos Previos

- Java JDK 17 o superior instalado.  
- Maven instalado.  
- Base de datos MySQL "H2" corriendo y accesible.  
- IDE recomendado: IntelliJ IDEA o similar con soporte para Maven y Spring Boot.  
- Postman para probar los endpoints con los cuerpos (bodies) predefinidos.  

---

## Configuración Inicial

1. Clonar este repositorio:  
   ```bash
   git clone https://github.com/MACHINE-GUN-SADOMY/Evaluacion3_Examen_Fullstackl.git
   
   O tambien descargar este Release: 
  
2. Importar el proyecto a tu IDE (IntelliJ, Eclipse, etc.) como proyecto Maven.

3. Funciones del Proyecto: 

		POST /clientes - Crear un cliente.

		GET /clientes - Listar todos los clientes.

		PUT /clientes/{id} - Actualizar cliente.

		DELETE /clientes/{id} - Eliminar cliente.

		POST /usuarios - Crear usuario.

		PUT /usuarios/{id} - Actualizar usuario.

		DELETE /usuarios/{id} - Eliminar usuario.

		Importante: Para crear o actualizar clientes y usuarios, debes enviar en el cuerpo (body) los datos JSON siguiendo los modelos ClienteRequest y UsuarioRequest.

4. Antes de crear un cliente o usuario, es importante crear lo siguiente:

	1. Region , 2. Provincia , 3. Comuna, 4. Role.

5. Módulo 2: Gestión de Productos, Carrito y Órdenes
	Endpoints principales:

		POST /productos - Crear productos.

		GET /productos - Listar productos.

		POST /carritos - Crear carrito para un cliente.

		POST /carritos/{id}/items - Agregar ítems al carrito.

		DELETE /carritos/{id}/items/{itemId} - Eliminar ítem del carrito.

		POST /ordenes - Crear orden desde un carrito.

		POST /ordenes/detalles - Agregar detalles a la orden.

		DELETE /ordenes/{id} - Eliminar orden.

	Flujo típico:

	1. Crear un cliente y un usuario (Módulo 1).

	2. Crear productos.

	3. Crear un carrito para el cliente.

	4. Agregar ítems al carrito.

	5. Crear una orden basada en el carrito.

	6. Agregar detalles a la orden.

	IMPORTANTE:
	
	Uso de Postman
	En la carpeta /postman del repositorio encontrarás colecciones con todos los requests configurados (clientes, usuarios, productos, carritos, órdenes).

	Importa estas colecciones en Postman para hacer pruebas rápidas.

	Asegúrate de cambiar las URLs base si modificas el puerto o el host.  
	
	Tambien en el video demo se muestra el funcionamiento especifico de cada endpoint! 
   
6. Pruebas Unitarias:
	El proyecto cuenta con Junit y Mockito para realizar las pruebas todas las funciones de Service se encuentran cubiertas de pruebas.
	
	1. Puedes ejecutarlas en la ruta de "Modulo nombre (depende del modulo que quieras testear)"
	
	2. Test (se encontraran todos los test segun el service del modulo.

7. Contacto:
	Puedes contactarme directamente a mi correo: call_from_helheim@protonmail.com , ante cualquier inconveniente
		con el proyecto!

8. Video Demo: 
	https://www.youtube.com/watch?v=YHTJn-Vb5P0
		
