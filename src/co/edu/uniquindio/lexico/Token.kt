package co.edu.uniquindio.lexico

class Token(var palabra: String, var categoria: Categoria, var fila: Int, var columna: Int) {
    override fun toString(): String {
        return ("[$palabra : $categoria] ($fila:$columna)")
    }

}