package co.edu.uniquindio.sintaxis.bnf.control

import co.edu.uniquindio.app.SintaxisObservable
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Condicional
 */
class Condicional(
        private val estructuraSi: EstructuraSi,
        private val estructuraSino: EstructuraSiNo?
) : EstructuraControl("Condicional") {

    override fun toString(): String {
        return "$estructuraSi$estructuraSino"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(estructuraSi.getTreeItem())
        if (estructuraSino != null) {
            treeItem.children.add(estructuraSino.getTreeItem())
        }

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Condicion Si")
        agregarValor(estructuraSi.toString())

        agregarAtributo("Condicion Sino")
        agregarValor(estructuraSino?.toString())

        return panel
    }

}