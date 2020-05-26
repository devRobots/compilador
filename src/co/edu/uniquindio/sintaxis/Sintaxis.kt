package co.edu.uniquindio.sintaxis

import co.edu.uniquindio.app.observable.SintaxisObservable

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
    /**
     * Elementos de la sintaxis
     */
    protected val panel : GridPane = GridPane()

    private var editable = true
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
    open fun getTreeItem(): TreeItem<SintaxisObservable> {
        return TreeItem(SintaxisObservable(this))
    }

    /**
     * Obtiene el panel necesario para mostrar la informacion
     * de la estructura sintactica en la interfaz
     *
     * @return GridPane el panel que se mostrara en pantalla
     */
    open fun getPropertiesPanel(): GridPane {
        configurarTabla()
        return panel
    }

    /**
     * Obtiene la traduccion a Java del elemento
     *
     * @return String el codigo de Java al que corresponde el elemento
     */
    open fun getJavaCode(): String {
        return "// TODO: No implementado aun"
    }

    /**
     * Agrega un atributo al panel que se va a mostrar
     *
     * @param atributo El atributo que se va a agregar al panel
     */
    fun agregarAtributo(atributo: String) {
        if (editable) {
            val texto = Label("$atributo:")
            texto.style = "-fx-font-weight: bold"

            texto.padding = Insets(10.0)

            GridPane.setVgrow(texto, Priority.ALWAYS)
            GridPane.setHgrow(texto, Priority.SOMETIMES)

            panel.add(texto, 0, contAtributo)

            contAtributo++
        }
    }

    /**
     * Agrega un valor al panel que se va a mostrar
     *
     * @param valor El valor que se va a agregar al panel
     */
    protected fun agregarValor(valor: String?) {
        if (editable) {
            val texto = Label(valor ?: "Null")

            texto.padding = Insets(10.0)

            GridPane.setVgrow(texto, Priority.ALWAYS)
            GridPane.setHgrow(texto, Priority.ALWAYS)
            panel.add(texto, 1, contValor)

            contValor++
        }
    }

    /**
     * Agrega un subpanel al panel que se va a mostrar
     *
     * @param panel El subpanel que se va a agregar al panel
     */
    protected fun agregarValor(panel: GridPane) {
        if (editable) {
            GridPane.setVgrow(panel, Priority.ALWAYS)
            GridPane.setHgrow(panel, Priority.ALWAYS)
            this.panel.add(panel, 1, contValor)

            contValor++
        }
    }

    /**
     * Agrega un subpanel al panel que se va a mostrar
     *
     * @param panel El subpanel que se va a agregar al panel
     */
    protected fun agregarValor(panel: GridPane, posicion: Int) {
        if (editable) {
            GridPane.setVgrow(panel, Priority.ALWAYS)
            GridPane.setHgrow(panel, Priority.ALWAYS)
            this.panel.add(panel, 1, posicion)

            contValor++
        }
    }

    /**
     * Configura la tabla para evitar mas agregaciones
     */
    protected fun configurarTabla() {
        editable = false
    }
}