package co.edu.uniquindio.sintaxis.bnf.control

import co.edu.uniquindio.app.observable.SintaxisObservable
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
 * Condicional
 */
class Condicional(
        private val estructuraSi: EstructuraSi,
        private val estructuraSino: EstructuraSiNo?
) : EstructuraControl("Condicional") {

    override fun toString(): String {
        return "$estructuraSi$estructuraSino"
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

        treeItem.children.add(estructuraSi.getTreeItem())
        if (estructuraSino != null) {
            treeItem.children.add(estructuraSino.getTreeItem())
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
        agregarAtributo("Condicion Si")
        agregarValor(estructuraSi.toString())

        agregarAtributo("Condicion Sino")
        agregarValor(estructuraSino?.toString())

        configurarTabla()
        return panel
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        estructuraSi.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)

    }

    override fun getJavaCode(): String {
        var codigo = "${estructuraSi.getJavaCode()} "
        if(estructuraSino != null){ codigo += "${estructuraSino.getJavaCode()}"}
        return codigo
    }
}