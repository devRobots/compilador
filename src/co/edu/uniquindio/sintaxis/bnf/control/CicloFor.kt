package co.edu.uniquindio.sintaxis.bnf.control

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
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

    /**
     * Obtiene el nodo TreeItem necesario para la construccion
     * de la vista del arbol sintactico
     *
     * @return TreeItem<SintaxisObservable> El nodo TreeItem
     */
    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (decVariableLocal != null) {
            treeItem.children.add(decVariableLocal.getTreeItem())
        }

        treeItem.children.add(expLogica.getTreeItem())
        treeItem.children.add(asignacionCiclo.getTreeItem())

        val listaObservable = ListaSintactica("Bloques de Sentencias", bloqueInstrucciones)
        treeItem.children.add(listaObservable.getTreeItem())

        return treeItem
    }

    /**
     * Obtiene el panel necesario para mostrar la informacion
     * de la estructura sintactica en la interfaz
     *
     * @return GridPane el panel que se mostrara en pantalla
     */
    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Declaracion")
        agregarValor(decVariableLocal?.toString())

        agregarAtributo("Expresion Logica")
        agregarValor(expLogica.toString())

        agregarAtributo("Asignacion")
        agregarValor(asignacionCiclo.toString())

        agregarAtributo("Lista de sentencia")
        agregarValor(ListaSintactica("Sentencias", bloqueInstrucciones).getPropertiesPanel())

        configurarTabla()
        return panel
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        decVariableLocal?.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, Ambito(ambito, "CicloFor"))

    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        expLogica.analizarSemantica(tablaSimbolos, erroresSemanticos, Ambito(ambito, "CicloFor"))
    }
}