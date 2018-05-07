# Mini Java

## Lancer le jeu de tests

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