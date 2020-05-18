package co.edu.uniquindio.sintaxis

import co.edu.uniquindio.app.SintaxisObservable
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Sintaxis
 */
abstract class Sintaxis(var nombre: String) {
    protected val panel = GridPane()

    private var contAtributo = 0
    private var contValor = 0

    init {
        panel.gridLinesVisibleProperty().set(true)
    }

    /**
     * Obtiene el nodo TreeItem necesario para la construccion
     * de la vista del arbol sintactico
     *
     * @return TreeItem<SintaxisObservable> El nodo TreeItem
     */
    abstract fun getTreeItem(): TreeItem<SintaxisObservable>

    /**
     * Obtiene el panel necesario para mostrar la informacion
     * de la estructura sintactica en la interfaz
     *
     * @return GridPane el panel que se mostrara en pantalla
     */
    abstract fun getPropertiesPanel(): GridPane

    fun agregarAtributo(atributo: String) {
        var texto = Label("$atributo:")
        texto.style = "-fx-font-weight: bold"

        texto.padding = Insets(10.0)

        GridPane.setVgrow(texto, Priority.ALWAYS)
        GridPane.setHgrow(texto, Priority.ALWAYS)
        panel.add(texto, 0, contAtributo)

        contAtributo++
    }

    fun agregarValor(valor: String?) {
        var texto = if (valor != null) {
            Label("$valor")
        } else {
            Label("Null")
        }

        texto.padding = Insets(10.0)

        GridPane.setVgrow(texto, Priority.ALWAYS)
        GridPane.setHgrow(texto, Priority.ALWAYS)
        panel.add(texto, 1, contValor)

        contValor++
    }

    fun agregarValor(panel: GridPane) {
        GridPane.setVgrow(panel, Priority.ALWAYS)
        GridPane.setHgrow(panel, Priority.ALWAYS)
        panel.add(panel, 1, contValor)

        contValor++
    }
}