package co.edu.uniquindio.app.observable

import co.edu.uniquindio.semantica.simbolo.Importacion
import javafx.beans.property.SimpleStringProperty

class ImportacionObservable(importacion: Importacion) {
    val nombre: SimpleStringProperty = SimpleStringProperty(importacion.nombre)
    val fila: SimpleStringProperty = SimpleStringProperty(importacion.fila.toString())
    val columna: SimpleStringProperty = SimpleStringProperty(importacion.columna.toString())
}