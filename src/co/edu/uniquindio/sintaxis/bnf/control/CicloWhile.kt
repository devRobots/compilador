package co.edu.uniquindio.sintaxis.bnf.control

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.bnf.expresion.ExpresionLogica
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
 * Ciclo While
 */
class CicloWhile(
        private val expLogica: ExpresionLogica,
        private val listaSentencia: ArrayList<Sentencia>
) : EstructuraControl("Ciclo While") {
    override fun toString(): String {
        return "durante [ $expLogica ] ¿ ... ?"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(expLogica.getTreeItem())

        val listaSentenciasObservable = SintaxisObservable(ListaSintactica("Sentencias"))
        val treeSentencias = TreeItem(listaSentenciasObservable)
        for (sentencia in listaSentencia) {
            treeSentencias.children.add(sentencia.getTreeItem())
        }
        treeItem.children.add(treeSentencias)

        return treeItem

    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Expresion Logica")
        agregarValor(expLogica.toString())

        agregarAtributo("Bloques de Sentencia")
        agregarValor("Lista de Sentencias")

        return panel
    }

}