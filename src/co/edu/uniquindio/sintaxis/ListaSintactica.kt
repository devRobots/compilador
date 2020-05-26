package co.edu.uniquindio.sintaxis

import co.edu.uniquindio.app.observable.SintaxisObservable
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Lista Sintactica
 */
class ListaSintactica(
        contenido: String,
        private val lista: ArrayList<*>
) : Sintaxis("Lista de $contenido") {

    override fun toString(): String {
        return nombre
    }

    /**
     * Obtiene el nodo TreeItem necesario para la construccion
     * de la vista del arbol sintactico
     *
     * @return TreeItem<SintaxisObservable> El nodo TreeItem
     */
	override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val treeItem = TreeItem(SintaxisObservable(this))

        for (elemento in lista) {
            elemento as Sintaxis
            val child = elemento.getTreeItem()
            treeItem.children.add(child)
        }

        return treeItem
    }

    /**
     * Obtiene el panel necesario para mostrar la informacion
     * de la estructura sintactica en la interfaz
     *
     * @return GridPane el panel que se mostrara en pantalla
     */
	override fun getPropertiesPanel(): GridPane {
        for (elemento in lista) {
            elemento as Sintaxis
            agregarValor(elemento.getPropertiesPanel())
        }

        configurarTabla()
        return panel
    }
}