package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.Sintaxis
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class DeclaracionVariableGlobal(private val modificador:Token?, private val tipoDato:TipoDato,  val identificador:Token,private val expresion:Expresion?,private val metodo:InvocacionMetodo?) : Bloque() {
    init {
        this.nombre = "Variable Glocal"
        this.estructura = "${modificador?.lexema} ${identificador.lexema}"
    }
    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(tipoDato.getTreeItem())
        treeItem.children.add(metodo?.getTreeItem())
        treeItem.children.add(expresion?.getTreeItem())
        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Tipo Dato", 0)
        agregarValor(tipoDato.toString(), 0)

        agregarAtributo("identificador", 1)
        agregarValor(estructura, 1)

        if (expresion != null){
            agregarAtributo("valor", 2)
            agregarValor(expresion.toString(), 2)
        }else{
            agregarAtributo("valor", 2)
            agregarValor(metodo.toString(), 2)
        }

        return panel
    }

}