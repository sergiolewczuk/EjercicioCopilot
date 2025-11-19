package com.ejerciciocopilot.model;

/**
 * Enumeración que define los tipos de excusas que se pueden generar.
 */
public enum ExcuseType {
    /**
     * Simple: Solo fragmentos (contexto, causa, consecuencia, recomendación).
     */
    SIMPLE,

    /**
     * Con Meme: Fragmentos + meme tech argentino.
     */
    CON_MEME,

    /**
     * Con Ley: Fragmentos + ley o axioma del desarrollo.
     */
    CON_LEY,

    /**
     * Ultra Shark: Fragmentos + meme + ley. La versión más completa.
     */
    ULTRA_SHARK
}
