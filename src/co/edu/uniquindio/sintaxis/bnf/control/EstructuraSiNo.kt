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

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (estructuraSi != null) {
            treeItem.children.add(estructuraSi.getTreeItem())
        } else {
            val listaObservable = SintaxisObservable(ListaSintactica("Lista de Sentencias"))
            val treeBloqueInstrucciones = TreeItem(listaObservable)
            for (bloque in bloqueInstrucciones) {
                treeBloqueInstrucciones.children.add(bloque.getTreeItem())
            }
            treeItem.children.add(treeBloqueInstrucciones)
        }
        if (estructuraSiNo != null) {
            treeItem.children.add(estructuraSiNo.getTreeItem())
        }

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Condición si")
        agregarValor(estructuraSi.toString())

        agregarAtributo("Instrucciones")
        agregarValor("Bloque de Intrucciones")

        agregarAtributo("Sentencia Sino")
        agregarValor(estructuraSiNo?.toString())


        return panel
    }
}