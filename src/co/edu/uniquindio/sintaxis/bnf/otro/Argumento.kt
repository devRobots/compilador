package co.edu.uniquindio.sintaxis.bnf.otro

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.Sintaxis
import co.edu.uniquindio.sintaxis.bnf.bloque.Funcion
import co.edu.uniquindio.sintaxis.bnf.bloque.VariableGlobal
import co.edu.uniquindio.sintaxis.bnf.expresion.Expresion
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Argumento
 */
class Argumento(private val expresion: Expresion) : Sintaxis("Argumento") {
    override fun toString(): String {
        return expresion.toString()
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

        treeItem.children.add(expresion.getTreeItem())

        return treeItem
    }

    /**
     * Obtiene el panel necesario para mostrar la informacion
     * de la estructura sintactica en la interfaz
     *
     * @return GridPane el panel que se mostrara en pantalla
     */
	override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Argumento")
        agregarValor(expresion.nombre)

        configurarTabla()
        return panel
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        expresion.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
    }

    override fun getJavaCode(): String {
        return expresion.getJavaCode()
    }
}
