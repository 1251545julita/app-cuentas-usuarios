package ar.edu.utn.diplomatura.cuentasusuarios.excepciones;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarUsuarioNoEncontrado( UsuarioNoEncontradoException ex, HttpServletRequest request) {

        log.warn("{} {}", request.getMethod() + request.getRequestURI(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(CuentaNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> manejarCuentaNoEncontrada( CuentaNoEncontradaException ex, HttpServletRequest request) {

        log.warn("{} {}", request.getMethod() + request.getRequestURI(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error",ex.getMessage()));
    }

    @ExceptionHandler(CamposInvalidosException.class)
    public ResponseEntity<Map<String, String>> manejarCamposInvalidos( CamposInvalidosException ex, HttpServletRequest request) {

        log.warn("{} - {}", request.getMethod() + request.getRequestURI(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(DniInvalidoException.class)
    public ResponseEntity<Map<String, String>> manejarDniInvalido( DniInvalidoException ex, HttpServletRequest request) {

        log.warn("{} - {}", request.getMethod() + request.getRequestURI(), ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<Map<String, String>> manejarEmailDuplicado( EmailDuplicadoException ex, HttpServletRequest request) {

        log.warn("{} - {}", request.getMethod() + request.getRequestURI(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarErroresValidacion( MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errores);
    }

    @ExceptionHandler(ModoEliminacionInvalidoException.class)
    public ResponseEntity<String> manejarModoInvalido( ModoEliminacionInvalidoException ex, HttpServletRequest request) {

        log.warn("{} - {}", request.getMethod() + request.getRequestURI(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(TipoCuentaInvalidosException.class)
    public ResponseEntity<Map<String, String>> manejarTipoCuentaInvalido( TipoCuentaInvalidosException ex, HttpServletRequest request) {

        log.warn("{} - {}", request.getMethod() + request.getRequestURI(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

}
