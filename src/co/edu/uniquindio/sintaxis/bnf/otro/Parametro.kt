package co.edu.uniquindio.sintaxis.bnf.otro

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.Sintaxis
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class Parametro(val tipo: Token, private val identificador: Token) : Sintaxis() {
    init {
        nombre = "Parametro"
        estructura = "${tipo.lexema} $identificador"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        return TreeItem(SintaxisObservable(this))
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Tipo de dato", 0)
        agregarValor(tipo.lexema, 0)

        agregarAtributo("Identificador", 1)
        agregarValor(identificador.lexema, 1)

        return panel
    }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        tablaSimbolos.agregarVariable(identificador.lexema, tipo.lexema, ambito, identificador.fila, identificador.columna)
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {

    }
}