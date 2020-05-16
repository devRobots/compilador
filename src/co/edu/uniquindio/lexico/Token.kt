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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Token

        if (lexema != other.lexema) return false
        if (categoria != other.categoria) return false
        if (fila != other.fila) return false
        if (columna != other.columna) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lexema.hashCode()
        result = 31 * result + categoria.hashCode()
        result = 31 * result + fila
        result = 31 * result + columna
        return result
    }
}