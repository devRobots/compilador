package co.edu.uniquindio.sintaxis

import co.edu.uniquindio.app.SintaxisObservable

import javafx.geometry.Insets

import javafx.scene.control.Label
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority

abstract class Sintaxis() {
    lateinit var nombre : String
    var estructura : String? = null

    protected val panel = GridPane()

    init {
        panel.gridLinesVisibleProperty().set(true)
    }

    override fun toString(): String {
        return "$nombre: $estructura"
    }

    abstract fun getTreeItem(): TreeItem<SintaxisObservable>
    abstract fun getPropertiesPanel(): GridPane

    fun agregarAtributo(atributo: String, indice: Int) {
        var texto = Label("$atributo:")
        texto.style = "-fx-font-weight: bold"

        texto.padding = Insets(10.0)

        GridPane.setVgrow(texto, Priority.ALWAYS)
        GridPane.setHgrow(texto, Priority.ALWAYS)
        panel.add(texto, 0, indice)
    }

    fun agregarValor(valor: String?, indice: Int) {
        var texto = if (valor != null) {
            Label("$valor")
        } else {
            Label("Null")
        }

        texto.padding = Insets(10.0)

        GridPane.setVgrow(texto, Priority.ALWAYS)
        GridPane.setHgrow(texto, Priority.ALWAYS)
        panel.add(texto, 1, indice)
    }
}