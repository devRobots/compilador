package co.edu.uniquindio.sintaxis.bnf.bloque

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.AmbitoTipo
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos

import co.edu.uniquindio.sintaxis.bnf.expresion.Expresion
import co.edu.uniquindio.sintaxis.bnf.sentencia.InvocacionMetodo
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Variable Global
 */
class VariableGlobal(
        private var modificador: Token?,
        private val tipo: Token,
        private val identificador: Token,
        private val expresion: Expresion?,
        private val metodo: InvocacionMetodo?
) : Bloque("Variable Global") {

    override fun toString(): String {
        return "${modificador?.lexema} ${identificador.lexema}"
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
        agregarAtributo("Tipo Dato")
        agregarValor(tipo.toString())

        agregarAtributo("Identificador")
        agregarValor(identificador.lexema)

        agregarAtributo("Expresion")
        agregarValor(expresion.toString())

        agregarAtributo("Invocacion Metodo")
        agregarValor(metodo.toString())

        configurarTabla()
        return panel
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        tablaSimbolos.agregarVariable(identificador.lexema, tipo.lexema, modificador?.lexema, ambito, identificador.fila, identificador.columna)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        expresion?.analizarSemantica(tablaSimbolos, erroresSemanticos, AmbitoTipo(ambito, "VariableGlobal", tipo.lexema))
    }

    override fun getJavaCode(): String {
        var codigo = ""
        if (modificador != null){
            codigo += "${modificador?.getJavaCode()} "
        }
        codigo += " ${tipo.getJavaCode()} ${identificador.getJavaCode()}"
        if(expresion != null){
            codigo += " = ${expresion.getJavaCode()};"
        }else if( metodo != null){
            codigo += " = ${metodo.getJavaCode()}"
        }
        return codigo
    }
}
