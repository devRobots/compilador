package co.edu.uniquindio.sintaxis.bnf.bloque

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.bnf.otro.Argumento
import co.edu.uniquindio.sintaxis.bnf.otro.TipoDato
import co.edu.uniquindio.sintaxis.bnf.sentencia.Sentencia

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane
import java.util.ArrayList

class Funcion(private val modificadorAcceso: Token?, private val tipoDato: TipoDato, private val identificador: Token, private val listaArgumentos: ArrayList<Argumento>, private val listaSentencias: ArrayList<Sentencia>, private val retorno: Sentencia) : MetodoFuncion(modificadorAcceso, identificador, listaArgumentos, listaSentencias) {
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

        val listaSentenciasObservable = SintaxisObservable(ListaSintactica("Sentencias"))
        val treeSentencias = TreeItem(listaSentenciasObservable)
        for (sentencia in listaSentencias) {
            treeSentencias.children.add(sentencia.getTreeItem())
        }
        treeItem.children.add(treeSentencias)

        treeItem.children.add(retorno.getTreeItem())

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Modificador de Acceso", 0)
        agregarValor(modificadorAcceso?.lexema, 0)

        agregarAtributo("Tipo de Dato de Retorno", 1)
        agregarValor(tipoDato.nombre, 1)

        agregarAtributo("Identificador", 2)
        agregarValor(identificador.lexema, 2)

        agregarAtributo("Argumentos", 3)
        agregarValor(listaArgumentos.toString(), 3)

        agregarAtributo("Bloques de Sentencia", 4)
        agregarValor(listaSentencias.toString(), 4)

        agregarAtributo("Sentencia de Retorno", 5)
        agregarValor(retorno.nombre, 5)

        return panel
    }

}