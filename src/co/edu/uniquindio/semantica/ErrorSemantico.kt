package co.edu.uniquindio.semantica

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 3.0
 *
 * Error Semantico
 */
class ErrorSemantico(private var mensaje: String) {
    override fun toString(): String {
        return "Error Semantico: $mensaje"
    }
}