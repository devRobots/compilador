package co.edu.uniquindio.semantica.simbolo

import co.edu.uniquindio.semantica.Ambito

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 3.0
 *
 * Simbolo Variable
 */
class Variable(
        val nombre: String,
        private val tipoDato: String,
        private val modificadorAcceso: String?,
        val ambito: Ambito,
        private val fila: Int,
        private val columna: Int
) : Simbolo(nombre, ambito, fila, columna)