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
    BOOLEANO,

    IDENTIFICADOR,
    PALABRA_RESERVADA,

    CADENA_CARACTERES,
    CARACTER,

    OPERADOR_ASIGNACION,

    OPERADOR_ARTIMETICO,
    OPERADOR_INCREMENTO,
    OPERADOR_DECREMENTO,

    OPERADOR_RELACIONAL,
    OPERADOR_LOGICO,

    PARENTESIS,
    LLAVES,
    CORCHETES,

    FIN_SENTENCIA,
    SEPARADOR,

    COMENTARIO_LINEA,
    COMENTARIO_BLOQUE,

    PUNTO,
    DOS_PUNTOS,

    DESCONOCIDO
}