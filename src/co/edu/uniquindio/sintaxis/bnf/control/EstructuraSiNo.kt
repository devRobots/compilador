package co.edu.uniquindio.sintaxis.bnf.control

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.Sintaxis
import co.edu.uniquindio.sintaxis.bnf.sentencia.Sentencia
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Estructura Sino
 */
class EstructuraSiNo(
        private val bloqueInstrucciones: ArrayList<Sentencia>,
        private val estructuraSi: EstructuraSi?,
        private val estructuraSiNo: EstructuraSiNo?
) : Sintaxis("Estructura Sino") {

    override fun toString(): String = if (estructuraSi != null) {
        "wo $estructuraSi"
    } else {
        "wo ¿ .. ?"
    }

    /**
     * Obtiene el nodo TreeItem necesario para la construccion
     * de la vista del arbol sintactico
     *
     * @return TreeItem<SintaxisObservable> El nodo TreeItem
     */
	override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (estructuraSi != null) {
            treeItem.children.add(estructuraSi.getTreeItem())
        } else {
            val listaObservable = ListaSintactica("Lista de Sentencias", bloqueInstrucciones)
            treeItem.children.add(listaObservable.getTreeItem())
        }
        if (estructuraSiNo != null) {
            treeItem.children.add(estructuraSiNo.getTreeItem())
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
        agregarAtributo("Condición si")
        agregarValor(estructuraSi.toString())

        agregarAtributo("Lista de Sentencias")
        agregarValor(ListaSintactica("Sentencias", bloqueInstrucciones).getPropertiesPanel())

        agregarAtributo("Sentencia Sino")
        agregarValor(estructuraSiNo?.toString())

        configurarTabla()
        return panel
    }
}