package fr.n7.stl.block;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java_cup.runtime.Symbol;
import java.lang.*;
import java.io.InputStreamReader;

%%

%class Lexer
%implements sym
%public
%unicode
%line
%column
%cup
%char
%{
    public Lexer(ComplexSymbolFactory _symbolFactory, java.io.InputStream _inputStream){
		this(_inputStream);
        this.symbolFactory = _symbolFactory;
    }

	public Lexer(ComplexSymbolFactory _symbolFactory, java.io.Reader _reader){
		this(_reader);
        this.symbolFactory = _symbolFactory;
    }

    private StringBuffer sb;
    private ComplexSymbolFactory symbolFactory;
    private int csline,cscolumn;

    public Symbol symbol(String name, int code){
		return symbolFactory.newSymbol(name, code,
						new Location(yyline+1,yycolumn+1, yychar), // -yylength()
						new Location(yyline+1,yycolumn+yylength(), yychar+yylength())
				);
    }
    public Symbol symbol(String name, int code, String lexem){
		return symbolFactory.newSymbol(name, code,
						new Location(yyline+1, yycolumn +1, yychar),
						new Location(yyline+1,yycolumn+yylength(), yychar+yylength()), lexem);
    }

    protected void emit_warning(String message){
    		System.out.println("scanner warning: " + message + " at : 2 "+
    			(yyline+1) + " " + (yycolumn+1) + " " + yychar);
    }

    protected void emit_error(String message){
    		System.out.println("scanner error: " + message + " at : 2" +
    			(yyline+1) + " " + (yycolumn+1) + " " + yychar);
    }
%}

PassageLigne		= \r | \n | \r\n
Blanc 			= [ \t\f] | {PassageLigne}
Entier     		= [0-9]+

/* comments */
Commentaire 			= {BlocCommentaire} | {CommentaireFinLigne}
BlocCommentaire 		= "/*" {ContenuCommentaire} \*+ "/"
CommentaireFinLigne 	= "//" [^\r\n]* {PassageLigne}
ContenuCommentaire 	= ( [^*] | \*+[^*/] )*
Chaine				= \" ([^\"] | \\\") * \"
Caractere			= \' ([^\\] | \\\\) \'

Identificateur = ([:jletter:] | "_" ) ([:jletterdigit:] | [:jletter:] | "_" )*


%eofval{
    return symbolFactory.newSymbol("EOF",sym.EOF);
%eofval}

%state CODESEG

%%

<YYINITIAL> {

  {Blanc} 			{ }
  {Commentaire} 	{ }
  ";"          		{ return symbolFactory.newSymbol("Point Virgule", UL_Point_Virgule); }
  ","          		{ return symbolFactory.newSymbol("Virgule", UL_Virgule); }
  "."          		{ return symbolFactory.newSymbol("Point", UL_Point); }
  ":"          		{ return symbolFactory.newSymbol("Deux Points", UL_Deux_Points); }
  "?"          		{ return symbolFactory.newSymbol("Point d'Interrogation", UL_Point_Interrogation); }
  "!"          		{ return symbolFactory.newSymbol("Point d'Exclamation", UL_Point_Exclamation); }
  "="				{ return symbolFactory.newSymbol("Egal", UL_Egal); }
  "=="				{ return symbolFactory.newSymbol("Double Egal", UL_Double_Egal); }
  "!="				{ return symbolFactory.newSymbol("Exclamation Egal", UL_Exclamation_Egal); }
  "<"				{ return symbolFactory.newSymbol("Inférieur", UL_Inferieur); }
  "<="				{ return symbolFactory.newSymbol("Inférieur Egal", UL_Inferieur_Egal); }
  ">"				{ return symbolFactory.newSymbol("Supérieur", UL_Superieur); }
  ">="				{ return symbolFactory.newSymbol("Supérieur Egal", UL_Superieur_Egal); }
  "+"          		{ return symbolFactory.newSymbol("Plus", UL_Plus); }
  "++"          	{ return symbolFactory.newSymbol("Double Plus", UL_Double_Plus); }
  "-"          		{ return symbolFactory.newSymbol("Moins", UL_Moins); }
  "--"          	{ return symbolFactory.newSymbol("Double Moins", UL_Double_Moins); }
  "*"          		{ return symbolFactory.newSymbol("Asterisque", UL_Asterisque); }
  "/"          		{ return symbolFactory.newSymbol("Oblique", UL_Oblique); }
  "%"          		{ return symbolFactory.newSymbol("Pour Cent", UL_Pour_Cent); }
  "||"         		{ return symbolFactory.newSymbol("Double Barre", UL_Double_Barre); }
  "&"          		{ return symbolFactory.newSymbol("Esperluette", UL_Esperluette); }
  "&&"         		{ return symbolFactory.newSymbol("Double Esperluette", UL_Double_Esperluette); }
  "("          		{ return symbolFactory.newSymbol("Parenthese Ouvrante", UL_Parenthese_Ouvrante); }
  ")"          		{ return symbolFactory.newSymbol("Parenthese Fermante", UL_Parenthese_Fermante); }
  "{"          		{ return symbolFactory.newSymbol("Accolade Ouvrante", UL_Accolade_Ouvrante); }
  "}"          		{ return symbolFactory.newSymbol("Accolade Fermante", UL_Accolade_Fermante); }
  "["          		{ return symbolFactory.newSymbol("Crochet Ouvrant", UL_Crochet_Ouvrant); }
  "]"          		{ return symbolFactory.newSymbol("Crochet Fermant", UL_Crochet_Fermant); }
  "const"			{ return symbolFactory.newSymbol("Définition Constante", UL_Definition_Constante); }
//  "typedef"			{ return symbolFactory.newSymbol("Définition Type", UL_Definition_Type); }
//  "struct"			{ return symbolFactory.newSymbol("Enregistrement", UL_Enregistrement); }
//  "enum"			{ return symbolFactory.newSymbol("Enumération", UL_Enumeration); }
  "int"				{ return symbolFactory.newSymbol("Type Entier", UL_Type_Entier); }
  "character"		{ return symbolFactory.newSymbol("Type Caractère", UL_Type_Caractere); }
  "String"			{ return symbolFactory.newSymbol("Type Chaîne", UL_Type_Chaine); }
  "boolean"			{ return symbolFactory.newSymbol("Type Booléen", UL_Type_Booleen); }
  "void"			{ return symbolFactory.newSymbol("Type Vide", UL_Type_Vide); }
  "if"				{ return symbolFactory.newSymbol("Si", UL_Si); }
  "else"			{ return symbolFactory.newSymbol("Sinon", UL_Sinon); }
  "print"			{ return symbolFactory.newSymbol("Afficher", UL_Afficher); }
  "new"				{ return symbolFactory.newSymbol("Nouveau", UL_Nouveau); }
  "null"			{ return symbolFactory.newSymbol("Nul", UL_Nul); }
  "true"			{ return symbolFactory.newSymbol("Vrai", UL_Vrai); }
  "false"			{ return symbolFactory.newSymbol("Faux", UL_Faux); }
//  "fst"				{ return symbolFactory.newSymbol("Premier", UL_Premier); }
//  "snd"				{ return symbolFactory.newSymbol("Second", UL_Second); }
  "while"			{ return symbolFactory.newSymbol("Tant que", UL_Tant_Que); }
  "return"			{ return symbolFactory.newSymbol("Return", UL_Retour); }
"class" { return symbolFactory.newSymbol("Classe", UL_Class); }
"interface" { return symbolFactory.newSymbol("Interface", UL_Interface); }
"implements" { return symbolFactory.newSymbol("Implements", UL_Implements); }
"extends" { return symbolFactory.newSymbol("Extends", UL_Extends); }
"public" { return symbolFactory.newSymbol("Public", UL_Public);	}
"private" { return symbolFactory.newSymbol("Private", UL_Private); }
"static" { return symbolFactory.newSymbol("Static", UL_Static); }
"final" { return symbolFactory.newSymbol("Final", UL_Final); }
//"this" { return symbolFactory.newSymbol("This", UL_This); }
"abstract" { return symbolFactory.newSymbol("Abstract", UL_Abstract); }
"System.out.println" { return symbolFactory.newSymbol("Log", UL_Log); }
  {Caractere}		{ return symbolFactory.newSymbol("Caractère", UL_Caractere, yytext()); }
  {Chaine}			{ return symbolFactory.newSymbol("Chaine de caractères", UL_Chaine, yytext()); }
  {Entier}     		{ return symbolFactory.newSymbol("Nombre Entier", UL_Nombre_Entier, yytext()); }
  {Identificateur}	{ return symbolFactory.newSymbol("Identificateur", UL_Identificateur, yytext()); }
}



// error fallback
.|\n          		{ emit_warning( "Unrecognized character '" + yytext() + "' -- ignored" ); }