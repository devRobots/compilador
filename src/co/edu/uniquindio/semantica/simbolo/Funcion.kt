package co.edu.uniquindio.semantica.simbolo

import co.edu.uniquindio.semantica.Ambito

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 3.0
 *
 * Simbolo Funcion
 */
class Funcion(
        val nombre: String,
        private val tipoRetorno: String,
        private val modificadorAcceso: String?,
        val tiposParametros: ArrayList<String>
) : Simbolo(nombre, Ambito("Clase principal"), -1, -1)