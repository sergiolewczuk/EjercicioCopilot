package com.ejerciciocopilot.exception;

/**
 * Excepción personalizada lanzada cuando no se encuentra una entidad en la base de datos.
 * Hereda de RuntimeException para ser una excepción no verificada.
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * Constructor con mensaje descriptivo.
     *
     * @param message mensaje de error descriptivo
     */
    public EntityNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa raíz.
     *
     * @param message mensaje de error descriptivo
     * @param cause   causa original de la excepción
     */
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
