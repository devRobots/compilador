package co.edu.uniquindio.semantica.simbolo

import co.edu.uniquindio.semantica.Ambito

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 3.0
 *
 * Simbolo Importacion
 */
class Importacion(
        val nombre: String,
        private val fila: Int,
        private val columna: Int
) : Simbolo(nombre, Ambito("Unidad de Compilacion"), fila, columna)