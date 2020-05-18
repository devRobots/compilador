package co.edu.uniquindio.sintaxis.bnf.otro

import co.edu.uniquindio.app.SintaxisObservable
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
class Parametro(val tipo: Token, private val identificador: Token) : Sintaxis("Parametro") {

    override fun toString(): String {
        return  "${tipo.lexema} $identificador"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        return TreeItem(SintaxisObservable(this))
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Tipo de dato")
        agregarValor(tipo.lexema)

        agregarAtributo("Identificador")
        agregarValor(identificador.lexema)

        return panel
    }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        tablaSimbolos.agregarVariable(identificador.lexema, tipo.lexema, null, ambito, identificador.fila, identificador.columna)
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {

    }
}