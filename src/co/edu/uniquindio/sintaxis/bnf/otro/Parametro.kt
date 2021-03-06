package co.edu.uniquindio.sintaxis.bnf.otro

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
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
 * Parametro
 */
class Parametro(val tipo: Token, val identificador: Token) : Sintaxis("Parametro") {

    override fun toString(): String {
        return "${tipo.lexema} $identificador"
    }

    /**
     * Obtiene el nodo TreeItem necesario para la construccion
     * de la vista del arbol sintactico
     *
     * @return TreeItem<SintaxisObservable> El nodo TreeItem
     */
	override fun getTreeItem(): TreeItem<SintaxisObservable> {
        return TreeItem(SintaxisObservable(this))
    }

    /**
     * Obtiene el panel necesario para mostrar la informacion
     * de la estructura sintactica en la interfaz
     *
     * @return GridPane el panel que se mostrara en pantalla
     */
	override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Tipo de dato")
        agregarValor(tipo.lexema)

        agregarAtributo("Identificador")
        agregarValor(identificador.lexema)

        configurarTabla()
        return panel
    }

    override fun getJavaCode(): String {
        return "${tipo.getJavaCode()} ${identificador.lexema.substring(1)}"
    }
}