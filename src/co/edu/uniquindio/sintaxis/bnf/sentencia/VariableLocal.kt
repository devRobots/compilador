package co.edu.uniquindio.sintaxis.bnf.sentencia

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.bnf.expresion.Expresion
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class VariableLocal(private val tipo: Token, private val identificador: Token, private val expresion: Expresion?, private val metodo: InvocacionMetodo?) : Sentencia() {
    init {
        this.nombre = "Variable local"
    }
    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (metodo != null){
            treeItem.children.add(metodo.getTreeItem())
        }
        if (expresion != null){
            treeItem.children.add(expresion.getTreeItem())
        }
        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Tipo Dato", 0)
        agregarValor(tipo.lexema, 0)

        agregarAtributo("identificador", 1)
        agregarValor(identificador.lexema, 1)

        if (expresion != null){
            agregarAtributo("valor", 2)
            agregarValor(expresion.toString(), 2)
        }
        if(metodo!= null){
            agregarAtributo("valor", 2)
            agregarValor(metodo.toString(), 2)
        }

        return panel
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        tablaSimbolos.agregarVariable(identificador.lexema, tipo.lexema, ambito, identificador.fila, identificador.columna)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {

    }
}