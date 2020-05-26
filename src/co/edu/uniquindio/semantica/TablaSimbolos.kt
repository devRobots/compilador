package co.edu.uniquindio.semantica

import co.edu.uniquindio.semantica.simbolo.Funcion
import co.edu.uniquindio.semantica.simbolo.Importacion
import co.edu.uniquindio.semantica.simbolo.Simbolo
import co.edu.uniquindio.semantica.simbolo.Variable

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 3.0
 *
 * Tabla de Simbolos
 */
class TablaSimbolos(private var listaErrores: ArrayList<ErrorSemantico>) {
    var listaSimbolos: ArrayList<Simbolo> = ArrayList()

    /**
     * Permite guardar un símbolo de tipo variable en la tabla de símbolos
     *
     * @param nombre            El nombre de la variable
     * @param tipo              El tipo de dato de la variable
     * @param modificadorAcceso El modificador de acceso de la variable, si existe
     * @param ambito            El ambito en el que se encuentra la variable
     * @param fila              La fila en la que se encuentra la variable
     * @param columna           La columna en la que se encuentra la variable
     *
     * @return                  Simbolo si la variable no existe en la tabla, null en caso contrario
     */
    fun agregarVariable(nombre: String, tipo: String, modificadorAcceso: String?, ambito: Ambito, fila: Int, columna: Int): Simbolo? {
        val s = buscarVariable(nombre, ambito)
        if (s == null) {
            val nuevo = Variable(nombre, tipo, modificadorAcceso ?: "pack", ambito, fila, columna)
            listaSimbolos.add(nuevo)
            return nuevo
        } else {
            listaErrores.add(ErrorSemantico("La variable $nombre ya existe en el ámbito $ambito"))
        }
        return null
    }

    /**
     * Permite guardar un símbolo de tipo paquete en la tabla de símbolos
     *
     * @param nombre            El nombre de la importacion
     * @param fila              La fila en la que se encuentra la variable
     * @param columna           La columna en la que se encuentra la variable
     *
     * @return                  Simbolo si la importacion no existe en la tabla, null en caso contrario
     */
    fun agregarImportacion(nombre: String, fila: Int, columna: Int): Simbolo? {
        val s = buscarImportacion(nombre)
        if (s == null) {
            val nuevo = Importacion(nombre, fila, columna)
            listaSimbolos.add(nuevo)
            return nuevo
        } else {
            listaErrores.add(ErrorSemantico("La importacion '$nombre' ya existe en $fila:$columna"))
        }
        return null
    }

    /**
     * Permite guardar un símbolo de tipo función en la tabla de símbolos
     *
     * @param nombre            El nombre de la variable
     * @param tipo              El tipo de dato de la variable
     * @param modificadorAcceso El modificador de acceso de la variable, si existe
     * @param tipoParametros    Una lista con el tipo de dato de los parametros
     *
     * @return                  Simbolo si la funcion no existe en la tabla, null en caso contrario
     */
    fun agregarFuncion(nombre: String, tipo: String?, modificadorAcceso: String?, tipoParametros: ArrayList<String>): Simbolo? {
        val s = buscarFuncion(nombre, tipoParametros)
        if (s == null) {
            val nuevo = Funcion(nombre, tipo ?: "void", modificadorAcceso ?: "pack", tipoParametros)
            listaSimbolos.add(nuevo)
            return nuevo
        } else {
            listaErrores.add(ErrorSemantico("La función '$nombre' de parametros $tipoParametros ya existe"))
        }
        return null
    }

    /**
     * Busca un simbolo importacion en la tabla de simbolos
     *
     * @param nombre El nombre de la importacion a buscar
     *
     * @return Importacion si la encontro, null sino la encuentra
     */
    private fun buscarImportacion(nombre: String): Importacion? {
        for (simbolo in listaSimbolos) {
            if (simbolo is Importacion) {
                if (nombre == simbolo.nombre) {
                    return simbolo
                }
            }
        }
        return null
    }

    /**
     * Busca un simbolo variable en la tabla de simbolos
     *
     * @param nombre El nombre de la variable a buscar
     * @param ambito El ambito en el que se encuentra la variable a buscar
     *
     * @return Variable si la encontro, null sino la encuentra
     */
    private fun buscarVariable(nombre: String, ambito: Ambito): Simbolo? {
        for (simbolo in listaSimbolos) {
            if (simbolo is Variable) {
                if (nombre == simbolo.nombre && ambito == simbolo.ambito) {
                    return simbolo
                }
            }
        }
        return null
    }

    /**
     * Busca un simbolo funcion en la tabla de simbolos
     *
     * @param nombre El nombre de la funcion a buscar
     * @param ambito La lista de el tipo de parametros de la funcion a buscar
     *
     * @return Funcion si la encontro, null sino la encuentra
     */
    private fun buscarFuncion(nombre: String, tiposParametros: ArrayList<String>): Simbolo? {
        for (simbolo in listaSimbolos) {
            if (simbolo is Funcion) {
                if (nombre == simbolo.nombre && tiposParametros == simbolo.tiposParametros) {
                    return simbolo
                }
            }
        }
        return null
    }
}