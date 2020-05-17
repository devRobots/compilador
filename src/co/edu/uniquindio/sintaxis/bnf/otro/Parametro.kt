package co.edu.uniquindio.sintaxis.bnf.otro

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.Sintaxis
import co.edu.uniquindio.sintaxis.bnf.otro.TipoDato
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class Parametro(val tipo: TipoDato, private val identificador: Token) : Sintaxis() {
    init {
        nombre = "Parametro"
        estructura = "${tipo.estructura} $identificador"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(tipo.getTreeItem())

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Tipo de dato", 0)
        agregarValor(tipo.estructura, 0)

        agregarAtributo("Identificador", 1)
        agregarValor(identificador.lexema, 1)

        return panel
    }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        tablaSimbolos.agregarVariable(identificador.lexema, tipo.tipo.lexema, ambito, identificador.fila, identificador.columna)
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {

    }
}