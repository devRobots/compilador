package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.lexico.Token
import javafx.scene.control.TreeItem

class Clase(var modificadorAcceso: Token?, var identificador: Token, var cuerpoClase: CuerpoClase?)  {
    override fun toString(): String {
        return "Clase"
    }

    fun getTreeItem(): TreeItem<String> {
        val treeItem = TreeItem<String>(toString())

        treeItem.children.add(cuerpoClase?.getTreeItem())

        return treeItem
    }
}