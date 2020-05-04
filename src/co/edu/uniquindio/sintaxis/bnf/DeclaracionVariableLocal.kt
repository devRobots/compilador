package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.Sintaxis
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class DeclaracionVariableLocal(private val tipoDato: TipoDato, private val identificador: Token,private val expresion:Expresion?,private val metodo:InvocacionMetodo?) : Sentencia() {
    init {
        this.nombre = "Variable local"
    }
    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(tipoDato.getTreeItem())
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
        agregarValor(tipoDato.toString(), 0)

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
}