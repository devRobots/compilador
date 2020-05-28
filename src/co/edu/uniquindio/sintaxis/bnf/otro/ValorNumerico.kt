package co.edu.uniquindio.sintaxis.bnf.otro

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.lexico.Categoria
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.semantica.simbolo.Variable
import co.edu.uniquindio.sintaxis.Sintaxis
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Valor Numerico
 */
class ValorNumerico(val signo: Token?, val identificador: Token) : Sintaxis("Valor Numerico") {

    override fun toString(): String {
        return "${signo?.lexema ?: ""}${identificador.lexema}"
    }

    /**
     * Obtiene el nodo TreeItem necesario para la construccion
     * de la vista del arbol sintactico
     *
     * @return TreeItem<SintaxisObservable> El nodo TreeItem
     */
    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        return TreeItem(observable)
    }

    /**
     * Obtiene el panel necesario para mostrar la informacion
     * de la estructura sintactica en la interfaz
     *
     * @return GridPane el panel que se mostrara en pantalla
     */
    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Signo")
        agregarValor(signo?.lexema)

        agregarAtributo("Valor")
        agregarValor(identificador.lexema)

        configurarTabla()
        return panel
    }

    fun obtenerTipoDato(tablaSimbolos: TablaSimbolos, ambito: Ambito): String {
        return if (identificador.categoria == Categoria.IDENTIFICADOR) {
            val variable = tablaSimbolos.buscarVariable(identificador.lexema, ambito) as Variable?
            variable?.tipoDato ?: "null"
        } else {
            when(identificador.categoria) {
                Categoria.ENTERO -> "dec"
                Categoria.REAL -> "ent"
                else -> "null"
            }
        }
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        if (identificador.categoria == Categoria.IDENTIFICADOR) {
            val variable = tablaSimbolos.buscarVariable(identificador.lexema, ambito) as Variable?
            if (variable == null) {
                erroresSemanticos.add(ErrorSemantico("La variable ${identificador.lexema} no se encuentra declarada"))
            }
        }
    }

    override fun getJavaCode(): String {
        return if (signo != null) {
            "${signo.lexema}${identificador.lexema.substring(1)}"
        } else {
            identificador.lexema.substring(1)
        }
    }
}