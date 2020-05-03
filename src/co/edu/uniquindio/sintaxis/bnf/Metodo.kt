package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.ListaSintactica
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane
import java.util.ArrayList

class Metodo(private val modificadorAcceso: Token?, private val identificador: Token, private val listaArgumentos: ArrayList<Argumento>, private val bloqueInstrucciones: ArrayList<Sentencia>) : MetodoFuncion(modificadorAcceso, identificador, listaArgumentos, bloqueInstrucciones) {
    init {
        nombre = "Metodo"
        estructura = "$modificadorAcceso $identificador [ ... ] ¿ ... ?"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        val listaParametrosObservable = SintaxisObservable(ListaSintactica("Parametros"))
        val treeParametros = TreeItem(listaParametrosObservable)
        for (argumento in listaArgumentos) {
            treeParametros.children.add(argumento.getTreeItem())
        }
        treeItem.children.add(treeParametros)

        val listaBloquesObservable = SintaxisObservable(ListaSintactica("Bloques de Intrucciones"))
        val treeBloques = TreeItem(listaBloquesObservable)
        for (bloque in bloqueInstrucciones) {
            treeBloques.children.add(bloque.getTreeItem())
        }
        treeItem.children.add(treeBloques)

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Modificador de Acceso", 0)
        agregarValor(modificadorAcceso?.lexema, 0)

        agregarAtributo("Identificador", 1)
        agregarValor(identificador.lexema, 1)

        agregarAtributo("Argumentos", 2)
        agregarValor(listaArgumentos.toString(), 2)

        agregarAtributo("Bloques de Intrucciones", 3)
        agregarValor(bloqueInstrucciones.toString(), 3)

        return panel
    }
}