package co.edu.uniquindio.semantica

class TablaSimbolos (var listaErrores: ArrayList<ErrorSemantico>) {
    var listaSimbolos: ArrayList<Simbolo> = ArrayList()

    /**
     * Permite guardar un símbolo de tipo variable en la tabla de símbolos
     */
    fun agregarVariable(nombre: String, tipo: String, ambito: Ambito, fila: Int, columna: Int): Simbolo? {
        val s = buscarVariable(nombre, ambito)
        if (s == null) {
            val nuevo = Simbolo(nombre, tipo, ambito, fila, columna)
            listaSimbolos.add(nuevo)
            return nuevo
        } else {
            listaErrores.add(ErrorSemantico("La variable $nombre ya existe en el ámbito $ambito"))
        }
        return null
    }

    /**
     * Permite guardar un símbolo de tipo paquete en la tabla de símbolos
     */
    fun agregarImportacion(nombre: String, fila: Int, columna: Int): Simbolo? {
        val s = buscarImportacion(nombre)
        if (s == null) {
            val nuevo = Simbolo(nombre, fila, columna)
            listaSimbolos.add(nuevo)
            return nuevo
        } else {
            listaErrores.add(ErrorSemantico("La importacion '$nombre' ya existe"))
        }
        return null
    }

    /**
     * Permite guardar un símbolo de tipo función en la tabla de símbolos
     */
    fun agregarFuncion(nombre: String, tipo: String, tipoParametros: ArrayList<String>, ambito: Ambito): Simbolo? {
        val s = buscarFuncion(nombre, tipoParametros, ambito)
        if (s == null) {
            val nuevo = Simbolo(nombre, tipo, ambito, tipoParametros)
            listaSimbolos.add(nuevo)
            return nuevo
        } else {
            listaErrores.add(ErrorSemantico("La función $nombre $tipoParametros ya existe en el ambito $ambito"))
        }
        return null
    }

    private fun buscarImportacion(nombre: String): Simbolo? {
        for (simbolo in listaSimbolos) {
            if (simbolo.ambito == null && simbolo.tipoParametros == null) {
                if (nombre == simbolo.nombre) {
                    return simbolo
                }
            }
        }
        return null
    }

    private fun buscarVariable(nombre: String, ambito: Ambito): Simbolo? {
        for (simbolo in listaSimbolos) {
            if (simbolo.ambito != null) {
                if (nombre == simbolo.nombre && ambito == simbolo.ambito) {
                    return simbolo
                }
            }
        }
        return null
    }
    private fun buscarFuncion(nombre: String, tiposParametros: ArrayList<String>, ambito: Ambito): Simbolo? {
        for (simbolo in listaSimbolos) {
            if (simbolo.tipoParametros != null) {
                if (ambito == simbolo.ambito && nombre == simbolo.nombre && tiposParametros == simbolo.tipoParametros) {
                    return simbolo
                }
            }
        }
        return null
    }
}