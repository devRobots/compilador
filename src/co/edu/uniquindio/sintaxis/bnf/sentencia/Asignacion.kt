package co.edu.uniquindio.sintaxis.bnf.sentencia

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
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
 * Asignacion
 */
class Asignacion(
        private val identificador: Token,
        private val operador: Token,
        private val expresion: Expresion?,
        private val metodo: InvocacionMetodo?
) : Sentencia("Asignacion") {

    override fun toString(): String = if (expresion != null) {
        "${identificador.lexema} $operador $expresion"
    } else {
        "${identificador.lexema} $operador $metodo"
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

        if (metodo != null) {
            treeItem.children.add(metodo.getTreeItem())
        }
        if (expresion != null) {
            treeItem.children.add(expresion.getTreeItem())
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
        agregarValor(identificador.lexema)
        agregarAtributo("Operador de Asignacion")
        agregarValor(operador.lexema)
        if (expresion != null) {
            agregarAtributo("Expresion")
            agregarValor(expresion.toString())
        }
        if (metodo != null) {
            agregarAtributo("Metodo")
            agregarValor(metodo.toString())
        }
        configurarTabla()
        return panel
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        TODO("Not yet implemented")
    }

    override fun getJavaCode(): String {
        var codigo = "${identificador.getJavaCode()} ${operador.getJavaCode()} "
        if(expresion!= null){
            codigo += "${expresion.getJavaCode()};"
        }else{
            codigo += "${metodo?.getJavaCode()};"
        }
        return codigo
    }
}
