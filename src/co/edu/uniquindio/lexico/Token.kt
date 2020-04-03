package co.edu.uniquindio.lexico

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 1.1
 *
 * Token
 */
class Token(var palabra: String, var categoria: Categoria, var fila: Int, var columna: Int) {
    override fun toString(): String {
        return ("[$palabra : $categoria] ($fila:$columna)")
    }
}