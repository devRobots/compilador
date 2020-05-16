package co.edu.uniquindio.sintaxis.bnf.bloque

import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.bnf.otro.Argumento
import co.edu.uniquindio.sintaxis.bnf.sentencia.Sentencia

abstract class MetodoFuncion(private val modificadorAcceso: Token?, private val identificador: Token, private val listaArgumentos: ArrayList<Argumento>, private val listaSentencias: ArrayList<Sentencia>) : Bloque()