package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.lexico.Token

abstract class MetodoFuncion(private val modificadorAcceso: Token?, private val identificador: Token, private val listaArgumentos: ArrayList<Argumento>, private val listaBloqueInstrucciones: ArrayList<BloqueInstrucciones>) : BloqueSentencia()