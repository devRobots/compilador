package co.edu.uniquindio.sintaxis.bnf.bloque

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.bnf.otro.Parametro
import co.edu.uniquindio.sintaxis.bnf.otro.Retorno
import co.edu.uniquindio.sintaxis.bnf.sentencia.Sentencia

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane
import kotlin.collections.ArrayList

class Funcion(private val modificadorAcceso: Token?, private val tipo: Token, private val identificador: Token, private val listaParametros: ArrayList<Parametro>, private val listaSentencias: ArrayList<Sentencia>, private val retorno: Retorno) : Bloque() {
    init {
        nombre = "Funcion"
        estructura = "$modificadorAcceso ${tipo.lexema} $identificador [ ... ] ¿ ... ?"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        val listaParametrosObservable = SintaxisObservable(ListaSintactica("Parametros"))
        val treeParametros = TreeItem(listaParametrosObservable)
        for (parametro in listaParametros) {
            treeParametros.children.add(parametro.getTreeItem())
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
        agregarValor(tipo.lexema, 1)

        agregarAtributo("Identificador", 2)
        agregarValor(identificador.lexema, 2)

        agregarAtributo("Argumentos", 3)
        agregarValor(listaParametros.toString(), 3)

        agregarAtributo("Bloques de Sentencia", 4)
        agregarValor(listaSentencias.toString(), 4)

        agregarAtributo("Sentencia de Retorno", 5)
        agregarValor(retorno.nombre, 5)

        return panel
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        val tiposParametros = ArrayList<String>()

        for (argumento in listaParametros) {
            tiposParametros.add(argumento.tipo.lexema)
            argumento.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, Ambito(ambito, identificador.lexema))
        }

        tablaSimbolos.agregarFuncion(identificador.lexema, tipo.lexema, tiposParametros, ambito)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {

    }
}