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
class Token(var lexema: String, var categoria: Categoria, var fila: Int, var columna: Int) {
    override fun toString(): String {
        return ("[$lexema : $categoria] ($fila:$columna)")
    }
}