package co.edu.uniquindio.app

import co.edu.uniquindio.sintaxis.Sintaxis

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author-fx-font-size: 24 Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Sintaxis Observable
 */
class SintaxisObservable(var sintaxis: Sintaxis) {
    private val nombre: StringProperty
    private val estructura: StringProperty

    override fun toString(): String {
        return sintaxis.nombre
    }

    init {
        this.nombre = SimpleStringProperty(sintaxis.nombre)
        this.estructura = SimpleStringProperty(sintaxis.estructura.toString())
    }
}