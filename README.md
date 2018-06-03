# Mini Java

## Tests

### Tests automatiques (tests/test.ts)

```text

Building..               [OK]
Compiling test file..    [OK]


  # Grammar tests
    # Global declarations
      ✓ -> class, interfaces w/ generic types
    # Inner class declarations
      ✓ -> methods definitions
      ✓ -> methods parameters
    # Inner interface declarations
      ✓ -> public methods with different return types and generic types

  # Resolve/Checktype simple tests
    # About class
      ✓ -> one class
      ✓ -> two classes
      ✓ -> duplicate class
      ✓ -> class declaration with inner attributes
      ✓ -> class declaration with inner methods
    # About interface
      ✓ -> one interface
      ✓ -> two interfaces
      ✓ -> duplicate interface
      ✓ -> interface declaration with inner methods
    # About implementation
      ✓ -> interface implementation
      ✓ -> unexisting interface implementation
      ✓ -> self reference
      ✓ -> class implementing a class
    # About extension
      ✓ -> class extension
      ✓ -> unexisting class extension
      ✓ -> self reference
      ✓ -> class extending an interface
      ✓ -> interface extending an interface
      ✓ -> interface extending a class
      ✓ -> extending final class
    # About overloading
      ✓ -> method overloading in class
      ✓ -> method overloading in interfaces
      ✓ -> bad overloading
      ✓ -> bad overloading II
      ✓ -> bad overloading III
      ✓ -> bad overloading IV
    # About abstraction
      ✓ -> abstract class 
      ✓ -> abstract class w/ abstract method
      ✓ -> abstract class w/ extented one
      ✓ -> abstract class w/ extented one w/ unimplemented method
      ✓ -> abstract class w/ extented one w/ implemented method

  # Resolve / CheckType medium tests
    # About object
      ✓ -> class w/ attribute assignment
      ✓ -> class w/ attribute assignment w/ parameter use
      ✓ -> class w/ bad named constructor
    # About method
      ✓ -> correct typed method body
      ✓ -> correct void method body
      ✓ -> uncorrect typed method body
      ✓ -> uncorrect void method body
      ✓ -> public static void main
      ✓ -> correct method body w/ attribute use 
      ✓ -> uncorrect method body w/ attribute use 
      ✓ -> method call in inner class
      ✓ -> method call in a different class
      ✓ -> method call in a different class w/ use of result
      ✓ -> method call in a different class w/ bad use of result
    # About using instantiation
      ✓ -> class w/ custom attribute types
      ✓ -> class w/ attribute assignment w/ parameter use
      ✓ -> class w/ instantiation of existing object
      ✓ -> class w/ instantiation of unexisting object
      ✓ -> class w/ object instantiation w/out constructor
      ✓ -> class w/ real constructor
      ✓ -> class w/ instantiation of our type w/ bad type parameter
    # About extension
      ✓ -> class w/ superclass attribute use
    # About implementation
      ✓ -> simple implementation
      ✓ -> simple implementation w/ missing methods

  # Resolve / CheckType hard tests
    # About modifier
      ✓ -> class w/ constant
      ✓ -> this reference in static context
      ✓ -> call of static method from another class
      ✓ -> class w/ private method use in inner class
      ✓ -> class w/ public method use in inner class
      ✓ -> class w/ superclass public attribute use
      ✓ -> class w/ superclass private attribute use
      ✓ -> class w/ superclass public method use
      ✓ -> class w/ superclass private method use
      ✓ -> class w/ superclass attribute use w/ global constant use
    # About generics
      ✓ -> basic tests
      ✓ -> generic attribute
      ✓ -> generic methods
      ✓ -> generic with wrong checktype affectation
      ✓ -> generic type extending a class
    # About chains
      ✓ -> chained abstract method w/ implemented method
      ✓ -> chained abstract method w/ implemented method II
      ✓ -> chained abstract method w/ implemented method III
      ✓ -> chained abstract method w/ unimplemented method
      ✓ -> chained abstract method w/ unimplemented method II
      ✓ -> chained abstract method w/ unimplemented method III
      ✓ -> chained abstract method w/ far attribute use
      ✓ -> chained method 
      ✓ -> chained method w/ private one
      ✓ -> chained method w/ public attribute use
      ✓ -> chained method w/ private attribute use

  # Resolve / CheckType final tests
    ✓ -> use method interface
    ✓ -> arrays
    ✓ -> attribute use w/out "this"
    ✓ -> attribute and parameter with same names
    ✓ -> method use w/out "this"
    ✓ -> double generic example
    ✓ -> double generic example w/ generic method use
    ✓ -> difficult generic
    ✓ -> generic w/ extends
    ✓ -> generic w/ missing extends
    ✓ -> big generic

  # TAM code
    # public static void main()
      ✓ -> System.out.println(-)
    # attributes
      ✓ -> object instantiation, attribute access and assignment
      ✓ -> idem with multiple attributes
      ✓ -> attributes in superclass
      ✓ -> attributes of type Object
    # methods
      ✓ -> method call without parameter
      ✓ -> method call with multiple atomic type parameters
      ✓ -> static methods
      ✓ -> static attribute access
      ✓ -> static attribute assignment
      ✓ -> 'this' access in methods calls
      ✓ -> class instantiation and usage (methods+attributes) in a public static void main
      ✓ -> chained methods returning objects
      ✓ -> shared object pointer
    -> final complex test
      ✓ -> complex test


  111 passing (19s)


```

### Lancer le jeu de tests

Les tests se trouvent dans le dossier `tests`. Ils sont rédigés en `TypeScript` et utilisent la librairie `mocha`.

Pour installer le compilateur TypeScript et les outils nécessaires au lancement des tests avec `npm`:

```bash
npm i -g typescript mocha
npm i
```

Ensuite, pour lancer le jeu de tests, tapez

```bash
npm test {spec}
```

`{spec}` est le reporter souhaité pour mocha.


Plus d'infos sur mocha: https://mochajs.org/

Liste des reporters: https://mochajs.org/#reporters

Par exemple: `npm test spec` ou `npm test nyan`

## Manuel d'utilisation

### Compiler
Pour compiler le projet, tapez
```bash
ant build
```

### Verbose mode

Pour passer un fichier dans Mini Java:

```text
Usage       : sh launch.sh <file> <mode>
 <file>     : Input file
 <mode>     : 0 -> verbose mode
            : 1 -> only TAM code
            : 2 -> JSON: {resolve: boolean, checkType: boolean, TAM: string, ..}
```

### Executer

Pour compiler un fichier Mini Java

```bash
sh launch.sh {filepath} 1 > tmp.tam
java -jar tools/aspartam.jar tmp.tam
java -jar tools/tammachine.jar tmp.tamo
cat tmp.res
```

il est aussi possible de lancer un utilitaire fourni:

```bash
sh run.sh {filepath}
```

