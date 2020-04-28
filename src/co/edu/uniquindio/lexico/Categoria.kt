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
    BOOLEANO,

    IDENTIFICADOR,
    PALABRA_RESERVADA,

    CADENA_CARACTERES,
    CARACTER,

    OPERADOR_ASIGNACION,

    OPERADOR_ARITMETICO,
    OPERADOR_INCREMENTO,
    OPERADOR_DECREMENTO,

    OPERADOR_RELACIONAL,
    OPERADOR_LOGICO,

    PARENTESIS_IZQUIERDO,
    PARENTESIS_DERECHO,
    LLAVE_IZQUIERDO,
    LLAVE_DERECHA,
    CORCHETE_IZQUIERDO,
    CORCHETE_DERECHO,

    FIN_SENTENCIA,
    SEPARADOR,

    COMENTARIO_LINEA,
    COMENTARIO_BLOQUE,

    PUNTO,
    DOS_PUNTOS,

    DESCONOCIDO
}