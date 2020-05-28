package co.edu.uniquindio.sintaxis.bnf.expresion

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Expresion Relacional
 */
class ExpresionRelacional(
        private val izquierda: ExpresionAritmetica?,
        private val operacion: Token
        , private val derecho: ExpresionAritmetica?
) : Expresion("Expresion Relacional") {

    override fun toString(): String {
        return "$izquierda ${operacion.lexema} $derecho"
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

        if (izquierda != null) {
            treeItem.children.add(izquierda?.getTreeItem())
        }
        if (derecho != null) {
            treeItem.children.add(derecho?.getTreeItem())
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
        if (izquierda != null) {
            agregarAtributo("Izquierda")
            agregarValor(izquierda.toString())
        }
        if (izquierda != null) {
            agregarAtributo("Operador")
            agregarValor(operacion.lexema)
        }
        if (derecho != null) {
            agregarAtributo("Derecha")
            agregarValor(derecho.toString())
        }
        configurarTabla()
        return panel
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        TODO("Not yet implemented")
    }

    override fun getJavaCode(): String {
        var codigo = ""
        if (operacion.lexema == "¬") {
            return return if (izquierda != null) {
                codigo += "!${izquierda.getJavaCode()}"
                codigo
            } else {
                codigo += "!${derecho?.getJavaCode()}"
                codigo
            }
        }
        return "${izquierda?.getJavaCode()} ${operacion.getJavaCode()} ${derecho?.getJavaCode()}"
    }
}
