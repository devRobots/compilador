package co.edu.uniquindio.app.observable

import co.edu.uniquindio.semantica.simbolo.Variable
import javafx.beans.property.SimpleStringProperty

class VariableObservable(variable: Variable) {
    val acceso: SimpleStringProperty = SimpleStringProperty(variable.modificadorAcceso)
    val tipo: SimpleStringProperty = SimpleStringProperty(variable.tipoDato)
    val nombre: SimpleStringProperty = SimpleStringProperty(variable.nombre)
    val ambito: SimpleStringProperty = SimpleStringProperty(variable.ambito.toString())
    val fila: SimpleStringProperty = SimpleStringProperty(variable.fila.toString())
    val columna: SimpleStringProperty = SimpleStringProperty(variable.columna.toString())
}