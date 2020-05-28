package co.edu.uniquindio.sintaxis.bnf.expresion

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.bnf.otro.ValorNumerico

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Expresion Aritmetica
 */
class ExpresionAritmetica(
        private val izquierda: ExpresionAritmetica?,
        private val derecho: ExpresionAritmetica?,
        private val operador: Token?,
        private val valor: ValorNumerico?
) : Expresion("Expresion Aritmetica") {

    override fun toString(): String {
        var estructura: String = valor?.toString() ?: izquierda.toString()
        if (operador != null) {
            estructura += "${operador.lexema}$derecho"
        }
        return estructura
    }

    constructor(valor: ValorNumerico?, operador: Token, derecho: ExpresionAritmetica?) : this(null, derecho, operador, valor)
    constructor(izquierda: ExpresionAritmetica?, operador: Token, derecho: ExpresionAritmetica?) : this(izquierda, derecho, operador, null)
    constructor(expresionAritmetica: ExpresionAritmetica) : this(expresionAritmetica, null, null, null)
    constructor(valor: ValorNumerico?) : this(null, null, null, valor)

    /**
     * Obtiene el nodo TreeItem necesario para la construccion
     * de la vista del arbol sintactico
     *
     * @return TreeItem<SintaxisObservable> El nodo TreeItem
     */
    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (valor != null) {
            treeItem.children.add(valor.getTreeItem())
        }
        if (izquierda != null) {
            treeItem.children.add(izquierda.getTreeItem())
        }
        if (derecho != null) {
            treeItem.children.add(derecho.getTreeItem())
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
        agregarAtributo("Valor")
        agregarValor(valor?.toString())

        agregarAtributo("Izquierda")
        agregarValor(izquierda?.toString())

        agregarAtributo("Operador")
        agregarValor(operador?.lexema)

        agregarAtributo("Derecha")
        agregarValor(derecho?.toString())

        configurarTabla()
        return panel
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        TODO("Not yet implemented")
    }

    override fun obtenerTipo(): String {
        TODO("Not yet implemented")
    }

    override fun getJavaCode(): String {
        return ""
    }
}
