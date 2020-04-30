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
class ErrorSintactico(private var mensaje: String) {
    override fun toString(): String {
        return ("Error Sintactico: $mensaje")
    }
}