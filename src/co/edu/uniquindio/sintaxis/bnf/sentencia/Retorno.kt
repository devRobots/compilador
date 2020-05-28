package co.edu.uniquindio.sintaxis.bnf.sentencia

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.bnf.expresion.Expresion
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane
import kotlin.math.exp

class Retorno(private val expresion: Expresion?) : Sentencia("Retorno") {

    override fun toString(): String {
        return "devolver ${expresion.toString()} !"
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
        if(expresion != null){
            treeItem.children.add(expresion?.getTreeItem())
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
        agregarAtributo("Expresion de retorno: ")
        agregarValor(expresion.toString())

        configurarTabla()
        return panel
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {

    }

    override fun getJavaCode(): String {
        if(expresion != null){
            return "return ${expresion.getJavaCode()};"
        }else{
            return "return;"
        }
    }
}