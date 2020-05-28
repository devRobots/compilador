package co.edu.uniquindio.sintaxis.bnf.sentencia

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.semantica.simbolo.Variable
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Incremento o Decremento
 */
class IncrementoDecremento(
        private val identificador: Token,
        private val operacion: Token
) : Sentencia("Incremento/Decremento") {

    override fun toString(): String {
        return "${identificador.lexema}${operacion.lexema}"
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
        agregarAtributo("Identificador: ")
        agregarValor(identificador.lexema)

        agregarAtributo("Operacion: ")
        agregarValor(operacion.lexema)

        configurarTabla()
        return panel
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        val variable = tablaSimbolos.buscarVariable(identificador.lexema, ambito) as Variable?
        if (variable != null) {
            if (variable.tipoDato != "ent" && variable.tipoDato != "dec") {
                erroresSemanticos.add(ErrorSemantico("Los tipos de dato no coinciden. Se esperaba un ent o dec pero se encontro un ${variable.tipoDato} en $ambito"))
            }
        } else {
            erroresSemanticos.add(ErrorSemantico("La variable ${identificador.lexema} no se encuentra declarada"))
        }
    }

    override fun getJavaCode(): String {
        return "${identificador.getJavaCode()}${operacion.getJavaCode()}"
    }
}
