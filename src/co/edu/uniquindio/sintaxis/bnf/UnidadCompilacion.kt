package co.edu.uniquindio.sintaxis

import co.edu.uniquindio.sintaxis.bnf.Clase
import co.edu.uniquindio.sintaxis.bnf.Importacion
import co.edu.uniquindio.sintaxis.bnf.Paquete

class UnidadCompilacion(var paquete: Paquete?, var listaImportaciones: ArrayList<Importacion>, var clase: Clase)