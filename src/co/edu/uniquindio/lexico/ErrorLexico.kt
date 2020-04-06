package co.edu.uniquindio.lexico

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 1.1
 *
 * Error Lexico
 */
class ErrorLexico(var palabra: String, var fila: Int, var columna: Int) {
    override fun toString(): String {
        return ("Error Lexico: No se reconoce el lexema '$palabra' en $fila:$columna")
    }
}