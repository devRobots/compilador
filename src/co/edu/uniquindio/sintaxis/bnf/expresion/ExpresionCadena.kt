package co.edu.uniquindio.sintaxis.bnf.expresion

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.AmbitoTipo
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
 * Expresion Cadena
 */
class ExpresionCadena(
        private val cadena: Token,
        private val valor: Expresion?
) : Expresion("Expresion Cadena") {

    override fun toString(): String {
        return if (valor != null) {
            "${cadena.lexema} + ${valor.nombre}"
        } else {
            cadena.lexema
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
        if (valor != null) {
            treeItem.children.add(valor.getTreeItem())
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
        agregarAtributo("Cadena")
        agregarValor(cadena.lexema)

        agregarAtributo("Expresion")
        agregarValor(valor.toString())

        configurarTabla()
        return panel
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        val ambitoTipo = ambito.obtenerAmbitoTipo()!!
        if (ambitoTipo.tipoRetorno != "pal") {
            erroresSemanticos.add(ErrorSemantico("No concuerdan los tipos de dato. Se esperaba un pal en $ambito."))
        } else {
            valor?.analizarSemantica(tablaSimbolos, erroresSemanticos, AmbitoTipo(ambito, "ExpresionCadena", "pal"))
        }
    }

    override fun getJavaCode(): String {
        return if (valor != null) {
            "${cadena.getJavaCode()} + ${valor.getJavaCode()}"
        } else {
            cadena.getJavaCode()
        }
    }
}
