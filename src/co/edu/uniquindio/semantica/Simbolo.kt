package co.edu.uniquindio.semantica

class Simbolo {
    var nombre: String? = null
    var tipo: String? = null
    var fila = 0
    var columna = 0
    var ambito: Ambito? = null
    var tipoParametros: ArrayList<String>? = null

    /**
     * Constructor de Variable
     */
    constructor(nombre: String, tipo: String, ambito: Ambito, fila:Int, columna:Int){
        this.nombre = nombre
        this.tipo = tipo
        this.ambito = ambito
        this.fila = fila
        this.columna = columna
    }

    /**
     * Constructor de Metodos/Funciones
     */
    constructor(nombre: String, tipo: String, ambito: Ambito, tipoParametros: ArrayList<String>){
        this.nombre = nombre
        this.tipo = tipo
        this.tipoParametros = tipoParametros
    }

    /**
     * Contructor de Importaciones
     */
    constructor(nombre: String, fila:Int, columna:Int){
        this.nombre = nombre
        this.fila = fila
        this.columna = columna
    }
}