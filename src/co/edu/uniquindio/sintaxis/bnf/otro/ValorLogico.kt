package co.edu.uniquindio.sintaxis.bnf.otro

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.lexico.Categoria
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.semantica.simbolo.Variable
import co.edu.uniquindio.sintaxis.Sintaxis
import co.edu.uniquindio.sintaxis.bnf.expresion.ExpresionRelacional

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Valor Logico
 */
class ValorLogico(
        private val identificador: Token?,
        private val expresionRelacional: ExpresionRelacional?
) : Sintaxis("Valor Logico") {

    override fun toString(): String = expresionRelacional?.toString() ?: "${identificador?.lexema}"

    /**
     * Obtiene el nodo TreeItem necesario para la construccion
     * de la vista del arbol sintactico
     *
     * @return TreeItem<SintaxisObservable> El nodo TreeItem
     */
	override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (expresionRelacional != null) {
            treeItem.children.add(expresionRelacional.getTreeItem())
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
        agregarAtributo("Identificador")
        agregarValor(identificador?.lexema)

        agregarAtributo("Expresion Relacional")
        agregarValor(expresionRelacional.toString())

        configurarTabla()
        return panel
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        val ambitoTipo = ambito.obtenerAmbitoTipo()
        if (ambitoTipo?.tipoRetorno == "bip") {
            if (identificador?.categoria == Categoria.IDENTIFICADOR) {
                val variable = tablaSimbolos.buscarVariable(identificador.lexema, ambito) as Variable?
                if (variable != null) {
                    if (variable?.tipoDato != ambitoTipo.tipoRetorno) {
                        erroresSemanticos.add(ErrorSemantico("No concuerdan los tipos de dato. Se esperaba un ${ambitoTipo.tipoRetorno} pero se encontro un ${variable?.tipoDato} en $ambito."))
                    }
                } else {
                    erroresSemanticos.add(ErrorSemantico("La variable ${identificador.lexema} no se encuentra declarada"))
                }
            } else if (identificador?.categoria == Categoria.BOOLEANO) {
                erroresSemanticos.add(ErrorSemantico("No concuerdan los tipos de dato. Se esperaba un bip en $ambito."))
            }
        }
    }

    override fun getJavaCode(): String {
        return identificador?.lexema?.substring(1) ?: expresionRelacional?.getJavaCode() ?: ""
    }
}