package co.edu.uniquindio.lexico

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 1.1
 *
 * Categoria de los Token
 */
enum class Categoria {
    ENTERO,
    REAL,
    HEXADECIMAL,

    IDENTIFICADOR,
    PALABRA_RESERVADA,

    CADENA_CARACTERES,

    OPERADOR_ASIGNACION,

    OPERADOR_ARTIMETICO,
    OPERADOR_INCREMENTO,
    OPERADOR_DECREMENTO,

    OPERADOR_RELACIONAL,
    OPERADOR_LOGICO,

    PARENTESIS,
    LLAVES,

    FIN_SENTENCIA,
    SEPARADOR,

    COMENTARIO_LINEA,
    COMENTARIO_BLOQUE,

    DESCONOCIDO
}