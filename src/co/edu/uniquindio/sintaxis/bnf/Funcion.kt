package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.ListaSintactica
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane
import java.util.ArrayList

class Funcion(private val modificadorAcceso: Token?, private val tipoDato: TipoDato, private val identificador: Token, private val listaArgumentos: ArrayList<Argumento>, private val bloqueSentencia: BloqueSentencia, private val retorno: Retorno) : MetodoFuncion(modificadorAcceso, identificador, listaArgumentos,bloqueSentencia) {
    init {
        nombre = "Funcion"
        estructura = "$modificadorAcceso ${tipoDato.estructura} $identificador [ ... ] ¿ ... ?"
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

        val listaBloquesObservable = SintaxisObservable(ListaSintactica("Bloques de Sentencias"))
        val treeBloques = TreeItem(listaBloquesObservable)
        treeItem.children.add(bloqueSentencia.getTreeItem())
        treeItem.children.add(treeBloques)

        treeItem.children.add(retorno.getTreeItem())

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Modificador de Acceso", 0)
        agregarValor(modificadorAcceso?.lexema, 0)

        agregarAtributo("Tipo de Dato de Retorno", 1)
        agregarValor(tipoDato.estructura, 1)

        agregarAtributo("Identificador", 2)
        agregarValor(identificador.lexema, 2)

        agregarAtributo("Argumentos", 3)
        agregarValor(listaArgumentos.toString(), 3)

        agregarAtributo("Bloques de Sentencia", 4)
        agregarValor(bloqueSentencia.toString(), 4)

        agregarAtributo("Retorno", 5)
        agregarValor(retorno.estructura, 5)

        return panel
    }

}