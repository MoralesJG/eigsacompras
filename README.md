# Sistema de Gestión de Compras🖥️

Aplicación de escritorio desarrollada en Java para administrar compras, proveedores, productos y usuarios, con herramientas de auditoría, consultas y generación de documentos PDF.

## Descripción✍️

El Sistema de Gestión de Compras centraliza información relacionada con las operaciones de adquisición de una organización. Permite administrar compras, requisiciones y rentas, relacionarlas con proveedores y productos, y consultar información para dar seguimiento a las operaciones registradas.

El proyecto incorpora módulos de administración de usuarios, auditoría de operaciones, notificaciones y generación de documentos PDF.

## Funcionalidades🔖

- **Gestión de compras:** registro y administración de compras, requisiciones y rentas, con información asociada de proveedores, productos, fechas y estatus.
<img width="1915" height="976" alt="image" src="https://github.com/user-attachments/assets/b00f0410-09a7-43bc-84cf-e2a1959a156a" />

- **Gestión de proveedores:** administración de información de proveedores y de los productos que ofrecen.
<img width="1915" height="969" alt="image" src="https://github.com/user-attachments/assets/fe1eea8f-a41a-4f83-9669-ee24e94f80d6" />

- **Gestión de productos:** consulta y administración del catálogo de productos y su relación con proveedores.
- **Consultas y reportes:** filtrado de información y generación de documentos PDF relacionados con compras.
<img width="1918" height="975" alt="image" src="https://github.com/user-attachments/assets/3e59b1f3-912e-4849-a8af-8d71d2bcf485" />

- **Administración de usuarios:** gestión de cuentas y tipos de acceso.
- **Auditoría:** registro y consulta de operaciones de inserción, actualización y eliminación.
- **Notificaciones:** avisos relacionados con las compras registradas.
<img width="529" height="918" alt="image" src="https://github.com/user-attachments/assets/a2b66e2e-2513-4e85-80c2-b2e12efa18dd" />

- **Recuperación de contraseña:** flujo de recuperación de acceso mediante correo electrónico.
<img width="780" height="556" alt="image" src="https://github.com/user-attachments/assets/1b7eb090-01a9-41b5-96f9-4b01dbf5e9b6" />
<img width="1332" height="477" alt="image" src="https://github.com/user-attachments/assets/377ec44f-37f1-4350-8aa3-c6314e6927a9" />

## Tecnologías utilizadas👨‍💻

- **Lenguaje:** Java 21.
- **Interfaz gráfica:** Java Swing.
- **Base de datos:** MySQL.
- **Conectividad con la base de datos:** JDBC.
- **Gestión de dependencias:** Apache Maven.
- **Bibliotecas adicionales:** jBCrypt, JavaMail, SwingX, iText y Apache PDFBox.

## Arquitectura del proyecto📑

El código se organiza en paquetes que separan las responsabilidades principales de la aplicación:

- `controlador`: coordinación de las operaciones de los módulos.
- `dao`: acceso y persistencia de datos.
- `modelo`: representación de las entidades del sistema.
- `interfaz`: ventanas, formularios y componentes de la aplicación.
- `enums`: enumeraciones utilizadas para representar estados, acciones y tipos de acceso.
- `utilidades`: funciones auxiliares, configuración, generación de documentos y servicios complementarios.
- `basededatos`: gestión de conexiones con MySQL.

## Base de datos🗄️

El proyecto incluye un script SQL para definir el esquema de la base de datos. Entre las entidades principales se encuentran usuarios, proveedores, productos, compras, productos asociados a compras, auditoría, notificaciones y recuperación de contraseñas.
<img width="951" height="1112" alt="BaseDeDatos" src="https://github.com/user-attachments/assets/b9965085-3fdc-4893-ac2d-92bc38e9f55f" />


## Instalación y ejecución⬇️

Las instrucciones de instalación se incorporarán una vez que la configuración de la base de datos esté preparada para una ejecución segura y reproducible.

No se deben incluir credenciales reales en el repositorio. Cada instalación deberá utilizar su propia configuración local.

## Contexto del proyecto🌟

Proyecto desarrollado en un entorno empresarial como parte de la experiencia profesional, orientado a centralizar y organizar operaciones de compra y la información relacionada con proveedores y productos.
