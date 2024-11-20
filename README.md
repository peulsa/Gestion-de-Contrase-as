# Gestor de Contraseñas

Este proyecto consiste en una aplicación Android desarrollada utilizando Java y XML. Su principal función es permitir a los usuarios gestionar sus contraseñas de manera sencilla y segura. La interfaz de usuario está diseñada para ser simple, con botones para iniciar sesión y registrarse.

## Descripción

La aplicación cuenta con una pantalla principal que incluye dos botones principales:
1. **Iniciar sesión**: Permite al usuario acceder a su cuenta.
2. **Registrarse**: Permite al usuario crear una nueva cuenta.

La pantalla está diseñada utilizando `ConstraintLayout`, lo que permite un diseño flexible y adaptable a diferentes tamaños de pantalla.

## Tecnologías utilizadas

- **Android**: Desarrollo para dispositivos móviles con Android.
- **Java**: Lenguaje de programación principal.
- **XML**: Para la definición de la interfaz de usuario.

## Estructura de la interfaz

### Elementos en la pantalla principal

- **Título**: Un texto que indica el propósito de la aplicación, centrado en la parte superior de la pantalla.
- **Botón "Iniciar sesión"**: Situado debajo del título, este botón permite a los usuarios iniciar sesión en su cuenta.
- **Botón "Registrarse"**: Colocado debajo del botón "Iniciar sesión", este botón permite a los usuarios registrarse para obtener una nueva cuenta.

## Código XML

El archivo de diseño principal (`activity_main.xml`) utiliza un `ConstraintLayout` para organizar los elementos en la pantalla. Aquí se describen los componentes clave del layout:

- **Título de la pantalla**: Un `MaterialTextView` que muestra "GESTOR DE CONTRASEÑAS", centrado en la parte superior.
- **Botones**:
  - El botón "Iniciar sesión" es de tipo `MaterialButton`, con un fondo de color lavanda claro y texto en color oscuro. Este botón se posiciona debajo del título.
  - El botón "Registrarse" es otro `MaterialButton`, con un diseño similar, colocado debajo del botón de inicio de sesión.


## Instalación
Para ejecutar este proyecto, sigue estos pasos:

Clona el repositorio en tu máquina local:

bash
Copiar código
git clone <repositorio_url>
Abre el proyecto en Android Studio.

Construye y ejecuta la aplicación en un emulador o dispositivo Android.


Este README proporciona una descripción general del proyecto, cómo usar el código, y cómo puedes contribuir a él.
