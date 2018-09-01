# Automatic Budged Book

Java desktop application (Swing) to read your bank account transactions, store them local and encrypted, 
create categories and rules to have transactions sorted into categories, and finally view statistics and diagrams from this data.

## Status: BETA

Hobby project in beta mode: Usable, but don't blame me for anything. Constructive comments or pull requests are welcome.

## Build 

```
./gradlew build
```

## Run

```
./gradlew run
```

## Security
Bank accounts are accessed read-only by encrypted HBCI. No other connections are made.
All data is stored local in the selected project file, which is a fully encrytped H2 database, using DB encryption provided by H2.
The decryption key is generated from the given project password. Decryption is on-the-fly during access, no decrypted data is stored.

## Supported platforms
This is pure java and should run on any platform with java >= 8. Development and testing is done on linux.

## UI languages
So far there is only a german language file, but you're welcome to get /src/main/resources/MessagesBundle_de.properties and send me a translated version ;)

## License
GPL 3. See [license](LICENSE)

## Author
[Bjoern](http://bjrn.de/)

