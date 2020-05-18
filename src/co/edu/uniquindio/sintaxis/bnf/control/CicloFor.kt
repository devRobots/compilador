package co.edu.uniquindio.sintaxis.bnf.control

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.bnf.expresion.ExpresionLogica
import co.edu.uniquindio.sintaxis.bnf.sentencia.IncrementoDecremento
import co.edu.uniquindio.sintaxis.bnf.sentencia.Sentencia
import co.edu.uniquindio.sintaxis.bnf.sentencia.VariableLocal
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Ciclo For
 */
class CicloFor(
        private val decVariableLocal: VariableLocal?,
        private val expLogica: ExpresionLogica,
        private val asignacionCiclo: IncrementoDecremento,
        private val bloqueInstrucciones: ArrayList<Sentencia>
) : EstructuraControl("Ciclo For") {

    override fun toString(): String {
        return "ciclo [ ${decVariableLocal?.toString()} | $expLogica | $asignacionCiclo ] ¿ ... ?"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (decVariableLocal != null) {
            treeItem.children.add(decVariableLocal.getTreeItem())
        }

        treeItem.children.add(expLogica.getTreeItem())
        treeItem.children.add(asignacionCiclo.getTreeItem())

        val listaObservable = SintaxisObservable(ListaSintactica("Bloques de Sentencias"))
        val treeBloqueInstrucciones = TreeItem(listaObservable)
        for (bloque in bloqueInstrucciones) {
            treeBloqueInstrucciones.children.add(bloque.getTreeItem())
        }
        treeItem.children.add(treeBloqueInstrucciones)


        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Declaracion")
        agregarValor(decVariableLocal?.toString())

        agregarAtributo("Expresion Logica")
        agregarValor(expLogica.toString())

        agregarAtributo("Asignacion")
        agregarValor(asignacionCiclo.toString())

        agregarAtributo("Lista de sentencia")
        agregarValor("Lista de Sentencias")

        return panel
    }
}