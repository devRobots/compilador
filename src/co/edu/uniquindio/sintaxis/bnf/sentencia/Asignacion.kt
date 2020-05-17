package co.edu.uniquindio.sintaxis.bnf.sentencia

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.bnf.expresion.Expresion
import co.edu.uniquindio.sintaxis.bnf.sentencia.InvocacionMetodo
import co.edu.uniquindio.sintaxis.bnf.sentencia.Sentencia
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class Asignacion(private val identificador: Token, private val expresion: Expresion?, private val metodo: InvocacionMetodo?): Sentencia() {
    init {
        nombre = "Asignacion"
        if (expresion != null){
            estructura = "${identificador.lexema} = ${expresion?.estructura}"
        }else{
            "${identificador.lexema} = ${metodo?.estructura}"
        }
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
        agregarAtributo("Identificador", 0)
        agregarValor(identificador.lexema, 0)
        if (expresion != null){
            agregarAtributo("Expresion", 1)
            agregarValor(expresion?.nombre, 1)
        }
        if (metodo != null){
            agregarAtributo("Metodo", 2)
            agregarValor(metodo.nombre, 2)
        }
        return panel
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {

    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        TODO("Not yet implemented")
    }
}
