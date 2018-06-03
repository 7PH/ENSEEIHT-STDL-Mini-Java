# Mini Java

## Tests

### Tests automatiques (tests/test.ts)

```text
Building..               [OK]
Compiling test file..    [OK]
 109 _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-__,------,
 0   _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-__|  /\_/\  
 0   _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_~|_( ^ .^)  
     _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ ""  ""  

  109 passing (18s)
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

ou

```bash
sh run.sh {filepath}
```