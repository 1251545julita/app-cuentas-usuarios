# Application — Bank Customers — Herencia y  Modelado de Clases

** Diplomatura en Desarrollo de Software FinTech: IA y Microservicios — UTN / Codeki**
** Student: Julita, Jesica R. **

El objetivo de esta aplicación es diseñar una solución escalable que permita desarrollar 
una API orientada a la gestión de cuentas y usuarios, aplicando conceptos como Programación
Orientada a Objetos (POO) y buenas prácticas, persistir información en una base de datos 
relacional como MySQL y exponer funcionalidades a través de endpoints REST.

---

## 1. Qué se necesita antes de empezar

- **JDK 21** instalado.
- **MySQL** instalado.
- **IntelliJ IDEA** (Community alcanza).

---

## 2. Cómo abrirlo en IntelliJ IDEA

1. Descomprimir el `.zip` en una carpeta **sin espacios ni acentos en la ruta**
   (por ejemplo `C:\dev\app-cuentas-usuarios`).
2. **File > Open** → elegir la carpeta `app-cuentas-usuarios`, la que contiene el `pom.xml`.
   No abrir el `pom.xml` directamente.
3. Cuando pregunte, aceptar **Trust Project**.
4. Verificar el JDK en `File > Project Structure > Project > SDK`: tiene que apuntar a un **JDK 21**. Si no aparece ninguno, elegir `Add SDK > Download JDK` y descargar una distribución 21.
5. Si no ve el panel de Maven: `View > Tool Windows > Maven`, y tocar el botón de recargar.
6. En el archivo `application.yml` agregar la password de la Base de Datos MySql a ejecutar localmente, en datasource.password así queda en la configuración local.

**Si el `pom.xml` aparece en gris o no hay carpeta `External Libraries`:** clic derecho
sobre `pom.xml` → **Add as Maven Project**.

---

## 3. Cómo ejecutarlo

### Desde IntelliJ

Abrí `src/main/java/ar/edu/utn/diplomatura/cuentasusuarios/Main.java` y hacé clic en
la flecha verde ▶ que está al lado de `public static void main`.
Se ejecutan las nueve demos seguidas y la salida aparece en la consola de abajo.

### Desde la terminal, con Maven

```bash
mvn compile
java -cp target/classes ar.edu.utn.diplomatura.cuentasusuarios.Main
```

### Desde la terminal, solo con el JDK

```bash
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")
java -cp out ar.edu.utn.diplomatura.cuentasusuarios.Main
```

En PowerShell (Windows), esas dos líneas son:

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src\main\java | ForEach-Object { $_.FullName })
java -cp out ar.edu.utn.diplomatura.cuentasusuarios.Main
```

---

## 4. Cómo probar los endpoints

El `Main` contiene un main para ejecutar la API de Cuentas y Usuarios, el mismo carga los datos iniciales de cada 
tipo de cuenta e imprime por consola el inicio de espera de solicitudes de la API iniciada.

En la consola vas a ver, en este orden:

```
======================================================================
  App-cuentas-usuarios - Spring REST API y base de datos
  App:         http://localhost:8080/cuentas
               JDBC URL: jdbc:mysql://localhost:3306/app_cuentas_usuarios   user: root
======================================================================
```

Esa última línea es la que importa: quiere decir que el servidor está andando.

**Para probar que funciona**, abrí en el navegador:

http://localhost:8080/cuentas

Tenés que ver las 6 cuentas en formato JSON.


### Endpoints disponibles

| Método | Ruta | Qué hace |
|--------|------|----------|
| `GET` | `/cuentas` | Lista todas las cuentas |
| `GET` | `/cuentas?numeroCuenta=N` | Filtra por número de cuenta |
| `GET` | `/cuentas/{id}` | Trae una cuenta específica |
| `POST` | `/cuentas` | Crea una cuenta nueva (Caja de Ahorro o Cuenta Corriente) |
| `PUT` | `/cuentas/{id}` | Actualiza datos de la cuenta (ej. tasa anual) |
| `DELETE` | `/cuentas/{id}?modo=logico` | Elimina una cuenta de forma lógica |


Cuando algo no se puede hacer (el id no existe, los datos enviados no corresponden al tipo de cuenta), 
la respuesta llega con un código de error específico como **404 Not Found** o **400 Bad Request**.


### Cómo se prueban: con `curl`, desde **Git Bash**

**Abrí Git Bash** (viene con Git; en Windows, botón derecho en cualquier carpeta →
*Git Bash Here*, o buscalo en el menú Inicio). Dejá la aplicación corriendo en IntelliJ y
copiá y pegá los comandos de abajo, uno por vez.

> **Tiene que ser Git Bash, no PowerShell ni CMD.** En PowerShell, `curl` es en realidad
> otro comando disfrazado y esta sintaxis no le funciona.

> **Desde el navegador solo se puede probar `GET`.** La barra de direcciones no sabe hacer
> `POST`, `PUT` ni `DELETE`. Por eso usamos `curl`.

Cada comando va en **una sola línea**: copialo entero y pegalo.

**Listar todas las cuentas** (devuelve 200)
```bash

curl -i http://localhost:8080/cuentas

```

**Traer la cuenta con id 1** (devuelve 200)
```bash

curl -i http://localhost:8080/cuentas/1

```

**Traer un id que no existe** (devuelve 404)
```bash

curl -i http://localhost:8080/cuentas/999

```

**Filtrar por número de cuenta**
```bash

curl -i "http://localhost:8080/cuentas?numeroCuenta=123456789012"

```

**Crear una Caja de Ahorro válida** (devuelve 201 Created)
```bash

curl -i -X POST http://localhost:8080/cuentas -H "Content-Type: application/json" -d '{"tipo": "Caja de Ahorro","moneda": "USD","usuarioId": "1","saldoInicial": 0.00,"porcentajeTasaInteresAnual": 16.00}'

```

**Intentar crear una Caja de Ahorro inválida (con límite al descubierto)** (devuelve 400 Bad Request)
```bash

curl -i -X POST http://localhost:8080/cuentas -H "Content-Type: application/json" -d '{"tipo": "Caja de Ahorro","moneda": "USD","usuarioId": "1","saldoInicial": 0.00,"limiteDescubierto": 1000000.00}'

```

**Crear una Cuenta Corriente válida** (devuelve 201 Created)
```bash

curl -i -X POST http://localhost:8080/cuentas -H "Content-Type: application/json" -d '{"tipo": "Cuenta Corriente","moneda": "USD","usuarioId": "1","saldoInicial": 10.00,"limiteDescubierto": 10000.00}'

```

**Actualizar porcentaje de tasa anual de la cuenta 1** (devuelve 200)
```bash

curl -i -X PUT http://localhost:8080/cuentas/1 -H "Content-Type: application/json" -d '{"porcentajeTasaAnual": 10.00}'

```

**Acreditar 15000 al cliente 1**
```bash

curl -i -X POST "http://localhost:8080/clientes/1/acreditaciones?importe=15000"

```

**Eliminar la cuenta 12 lógicamente** (devuelve 204 No Content)
```bash

curl -i -X DELETE "http://localhost:8080/cuentas/12?modo=logico"

```

**Intentar borrar la ruta raíz de cuentas** (devuelve 405 Method Not Allowed)
```bash

curl -i -X DELETE http://localhost:8080/cuentas

```

> **Qué significa el `-i`:** muestra también los encabezados de la respuesta, incluido el

> código de estado (`HTTP/1.1 200`, `201`, `400`, etc.). Sin `-i` verías solo el JSON del cuerpo.

---

## 5. Estructura del proyecto

```
app-cuentas-usuarios/
├── pom.xml
├── README.md
├── src/main/resources/application.yml    # Configuración de Base de Datos MySql
└── src/main/java/ar/edu/utn/diplomatura/cuentasusuarios/
    ├── CuentasUsuariosApplication.java   # Ejecuta la carga y la impresión de los datos de las cuentas y usuarios
    ├── Constants.java           # Definición de constantes que se utilizan en el sistema.
    ├── componentes/             # Métodos de validación
    ├── controladores/           # Controllers de SpringBoot para gestionar API de Cuentas y Usuarios
    ├── dto/                     # dtos para request y response de solicitudes de API de Cuentas y Usuarios
    ├── excepciones/             # Cada definición de las excepciones del sistema
    ├── repositorios/            # Contiene repositorio para Cuentas y para Usuarios
    ├── servicios/               # Services de SpringBoot para cuentas y usuarios, que funcionan con el Controller para cada endpoint.
    └── entidades/
        └── Cuenta.java          # clase abstracta, atributos, constructores, getters/setters
        ├── CajaAhorro.java      # subclase, atributos, constructores, getters/setters, métodos especificos
        ├── CuentaCorriente.java # subclase, atributos, constructores, getters/setters, métodos especificos
        ├── Operable.java        # interfaz con definición de métodos especificos
        └── Usuario.java         # clase, atributos, constructores, getters/setters

```

---

## 7. Problemas frecuentes

**Los acentos se ven como `?` o `�` en la consola.** Pasa en `cmd` y en PowerShell, porque la consola de Windows usa un codepage viejo en lugar de UTF-8. Es un problema de la terminal, no del código. Corré `chcp 65001` antes de ejecutar, o usá directamente la consola de IntelliJ, que muestra bien los acentos.

**`error: invalid source release: 21`.** El JDK configurado es anterior a 21. Revisá el punto 2.

**El botón ▶ no aparece al lado del `main`.** IntelliJ todavía no terminó de indexar, o no reconoció la carpeta como proyecto Maven. Esperá a que termine la barra de progreso de abajo, o recargá el proyecto desde el panel de Maven.
