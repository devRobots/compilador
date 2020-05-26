package co.edu.uniquindio.app.observable

import co.edu.uniquindio.sintaxis.Sintaxis

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Sintaxis Observable
 */
class SintaxisObservable(var sintaxis: Sintaxis) {
    private val nombre: StringProperty = SimpleStringProperty(sintaxis.nombre)

    override fun toString(): String {
        return sintaxis.nombre
    }
}