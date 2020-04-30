package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.lexico.Token
import javafx.scene.control.TreeItem

class Importacion(var importacion: Token) {
    override fun toString(): String {
        return "Importacion"
    }

    fun getTreeItem(): TreeItem<String> {
        return TreeItem<String>(toString())
    }
}