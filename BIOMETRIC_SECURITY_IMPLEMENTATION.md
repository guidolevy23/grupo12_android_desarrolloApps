# Implementación de Seguridad Biométrica - RitmoFit

## Resumen

Se ha implementado un sistema de autenticación biométrica que se ejecuta después del login exitoso y antes de acceder a la aplicación principal. El sistema incluye:

- Autenticación biométrica (huella digital, reconocimiento facial)
- Fallback a PIN del dispositivo
- Límite de 3 intentos fallidos
- Redirección automática a configuración de seguridad del dispositivo

## Arquitectura

### Componentes Principales

1. **SecurityRepository**: Maneja el almacenamiento seguro de configuraciones
2. **SecurityService**: Lógica de autenticación biométrica
3. **SecurityActivity**: Interfaz de usuario para la autenticación
4. **SecurityModule**: Inyección de dependencias con Dagger Hilt

### Flujo de Autenticación

```
Usuario abre app → MainActivity
    ↓
¿Tiene token? → No → AuthActivity (Login)
    ↓ Sí                    ↓
¿Seguridad habilitada? → No → Aplicación principal
    ↓ Sí                    ↓
SecurityActivity → Autenticación exitosa → MainActivity
    ↓
¿3 intentos fallidos? → Sí → Configuración de PIN
```

## Archivos Implementados

### Modelos
- `SecurityEvent.java`: Eventos de seguridad para EventBus

### Repositorio
- `SecurityRepository.java`: Interfaz del repositorio
- `SecurityRepositoryImpl.java`: Implementación con EncryptedSharedPreferences

### Servicios
- `SecurityService.java`: Interfaz del servicio de seguridad
- `SecurityServiceImpl.java`: Implementación con BiometricPrompt

### UI
- `SecurityActivity.java`: Actividad de autenticación biométrica
- `activity_security.xml`: Layout de la pantalla de seguridad

### Dependencias
- `SecurityModule.java`: Módulo de Dagger Hilt

### Tests
- `SecurityRepositoryTest.java`: Tests unitarios del repositorio
- `SecurityServiceTest.java`: Tests unitarios del servicio
- `SecurityActivityTest.java`: Tests de integración de UI

## Configuración

### Dependencias Agregadas

```kotlin
// Biometric Authentication
implementation("androidx.biometric:biometric:1.1.0")
```

### Permisos Agregados

```xml
<uses-permission android:name="android.permission.USE_BIOMETRIC" />
<uses-permission android:name="android.permission.USE_FINGERPRINT" />
```

## Funcionalidades

### 1. Autenticación Biométrica
- Soporte para huella digital, reconocimiento facial y PIN del dispositivo
- Prompt personalizado en español argentino
- Manejo de errores y cancelaciones

### 2. Límite de Intentos
- Máximo 3 intentos fallidos
- Contador persistente en EncryptedSharedPreferences
- Redirección automática a configuración del dispositivo

### 3. Integración con Flujo Existente
- Se ejecuta después del login exitoso
- No interfiere con el flujo normal de la aplicación
- Manejo de estados con EventBus

## Configuración de Seguridad

### Habilitar/Deshabilitar
La seguridad está habilitada por defecto. Para cambiar:

```java
@Inject SecurityRepository securityRepository;

// Deshabilitar seguridad
securityRepository.setSecurityEnabled(false);

// Habilitar seguridad  
securityRepository.setSecurityEnabled(true);
```

### Reiniciar Intentos
Para reiniciar el contador de intentos fallidos:

```java
@Inject SecurityService securityService;
securityService.resetFailedAttempts();
```

## Textos en Español Argentino

Todos los textos de la interfaz están en español argentino:
- "Autenticación requerida"
- "Usá tu huella digital..."
- "Intentá de nuevo"
- "Fallaste demasiadas veces"

## Casos de Uso

### Caso 1: Autenticación Exitosa
1. Usuario abre la app con token válido
2. Se muestra SecurityActivity
3. Usuario autentica con biometría
4. Redirección a MainActivity principal

### Caso 2: Fallo de Autenticación
1. Usuario falla la autenticación
2. Se incrementa contador de intentos
3. Se muestra botón "Reintentar"
4. Después de 3 intentos → Configuración de PIN

### Caso 3: Dispositivo Sin Biometría
1. Se detecta que no hay biometría configurada
2. Se muestra mensaje informativo
3. Botón para ir a configuración del dispositivo

## Seguridad

- **EncryptedSharedPreferences**: Almacenamiento seguro de configuraciones
- **BiometricPrompt**: API oficial de Android para biometría
- **Prevención de bypass**: No se puede volver atrás sin autenticar
- **Timeout de sesión**: La seguridad se solicita en cada apertura de app

## Testing

### Tests Unitarios
```bash
./gradlew testDebugUnitTest --tests="*Security*"
```

### Tests de Integración
```bash
./gradlew connectedAndroidTest --tests="*Security*"
```

## Notas de Implementación

1. **Compatibilidad**: Mínimo Android API 24 (Android 7.0)
2. **Fallback**: Siempre incluye PIN del dispositivo como alternativa
3. **UX**: Minimiza la app en lugar de cerrar al presionar atrás
4. **Performance**: Usa executor principal para callbacks
5. **Memoria**: Limpia recursos automáticamente

## Posibles Mejoras Futuras

1. **Configuración de intentos**: Hacer configurable el límite de 3 intentos
2. **Timeout de sesión**: Agregar timeout automático después de inactividad
3. **Biometría avanzada**: Soporte para iris o reconocimiento de voz
4. **Analytics**: Tracking de intentos de autenticación
5. **Personalización**: Temas y colores personalizables
