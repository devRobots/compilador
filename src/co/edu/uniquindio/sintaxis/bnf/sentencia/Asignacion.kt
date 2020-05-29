package co.edu.uniquindio.sintaxis.bnf.sentencia

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.AmbitoTipo
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.semantica.simbolo.Variable
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
        val variable = tablaSimbolos.buscarVariable(identificador.lexema, ambito)
        if (variable != null) {
            expresion?.analizarSemantica(tablaSimbolos, erroresSemanticos, AmbitoTipo(ambito, identificador.lexema, variable.tipoDato))
            metodo?.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
            val tipoExpr = expresion?.obtenerTipoDato(tablaSimbolos, ambito)
            val tipoFun = metodo?.obtenerTipoDato(tablaSimbolos, ambito)
            if (variable.tipoDato != tipoExpr || variable.tipoDato != tipoFun) {
                erroresSemanticos.add(ErrorSemantico("Los tipos de dato no coinciden. Se esperaba un ${ambito.obtenerAmbitoTipo()!!.tipoRetorno} pero se encontro un $tipoExpr en $ambito"))
            }
        } else {
            erroresSemanticos.add(ErrorSemantico("La variable ${identificador.lexema} no se encuentra declarada"))
        }
    }

    override fun getJavaCode(): String {
        var codigo = "${identificador.getJavaCode()} ${operador.getJavaCode()} "
        codigo += if(expresion!= null){
            "${expresion.getJavaCode()};"
        }else{
            "${metodo?.getJavaCode()}"
        }
        return codigo
    }
}
