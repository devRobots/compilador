package co.edu.uniquindio.app

import co.edu.uniquindio.lexico.Token
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty

class TokenObservable(token: Token) {
    val palabra: StringProperty
    val categoria: StringProperty
    val fila: StringProperty
    val columna: StringProperty

    init {
        val palabra = token.palabra
        val categoria = token.categoria
        val fila = token.fila
        val columna = token.columna
        this.palabra = SimpleStringProperty(palabra)
        this.categoria = SimpleStringProperty(categoria.toString())
        this.fila = SimpleStringProperty(fila.toString() + "")
        this.columna = SimpleStringProperty(columna.toString() + "")
    }
}