package co.edu.uniquindio.sintaxis

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Error Sintactico
 */
class ErrorSintactico(
        token: String,
        var fila: Int,
        var columna: Int
) : Error("Se esperaba $token en $fila:$columna")