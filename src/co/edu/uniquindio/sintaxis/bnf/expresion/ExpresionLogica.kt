package co.edu.uniquindio.sintaxis.bnf.expresion

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.AmbitoTipo
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.bnf.otro.ValorLogico
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Expresion Logica
 */
class ExpresionLogica(
        private val logico: ExpresionLogica?,
        private val izquierda: ValorLogico?,
        private val operador: Token?
) : Expresion("Expresion Logica") {

    override fun toString(): String = if (operador == null) {
        "$izquierda"
    } else {
        if (operador?.lexema == "¬") {
            "$operador [ $izquierda ]"
        } else {
            "$izquierda $operador $logico"
        }
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

        if (logico != null) {
            treeItem.children.add(logico.getTreeItem())
        }
        if (izquierda != null) {
            treeItem.children.add(izquierda.getTreeItem())
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
        agregarAtributo("Izquierda")
        agregarValor(izquierda.toString())

        agregarAtributo("Operador")
        agregarValor(operador.toString())

        agregarAtributo("Logico")
        agregarValor(logico.toString())

        configurarTabla()
        return panel
    }

    override fun obtenerTipoDato(tablaSimbolos: TablaSimbolos, ambito: Ambito): String {
        return "bip"
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        logico?.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        izquierda?.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
    }

    override fun getJavaCode(): String {
        return when {
            operador?.lexema == "¬" -> if(izquierda != null) "!(${izquierda.getJavaCode()})"  else "!${logico?.getJavaCode()}"
            operador != null -> "${izquierda?.getJavaCode()} ${operador.getJavaCode()} ${logico?.getJavaCode()}"
            else -> izquierda?.getJavaCode() ?: "${logico?.getJavaCode()}"
        }
    }
}
