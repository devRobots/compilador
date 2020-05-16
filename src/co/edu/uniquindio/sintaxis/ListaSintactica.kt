package co.edu.uniquindio.sintaxis

import co.edu.uniquindio.app.SintaxisObservable

import javafx.scene.control.TreeItem
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.scene.text.Text

class ListaSintactica(private val contenido: String) : Sintaxis() {
    init {
        this.nombre = "Lista de $contenido"
    }

    override fun toString(): String {
        return nombre
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        return TreeItem()
    }

    override fun getPropertiesPanel(): GridPane {
        val lista = GridPane()

        var texto = Text("Lista de $contenido")
        texto.style = "-fx-font-weight: bold"

        val centrador = BorderPane()
        centrador.center = texto

        lista.gridLinesVisibleProperty().set(true)

        GridPane.setVgrow(centrador, Priority.ALWAYS)
        GridPane.setHgrow(centrador, Priority.ALWAYS)
        lista.add(centrador, 0, 0)

        return lista
    }
}