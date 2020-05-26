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
        val tipoDato: String,
        val modificadorAcceso: String,
        val ambito: Ambito,
        val fila: Int,
        val columna: Int
) : Simbolo(nombre, ambito, fila, columna)