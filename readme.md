# NHPlus

## Technische Hinweise

Wird das Open JDK verwendet, werden JavaFX-Abhängigkeiten nicht importiert. Die Lösung besteht in der Installation der neuesten JDK-Version der Firma Oracle.

## Technische Hinweise zur Datenbank

- Benutzername: SA
- Passwort: SA
- Bitte nicht in die Datenbank schauen, während die Applikation läuft. Das sorgt leider für einen Lock, der erst wieder verschwindet, wenn IntelliJ neugestartet wird!

## Logindaten
| Gruppe mit Rechten | Benutzername       | Passwort |
|--------------------|--------------------|----------|
| Administration     | administrator      | 12345678 |
| Personal           | personalverwaltung | 12345678 |
| Pflege             | pfleger            | 01234567 |
| Rechnungswesen     | rechnungswesen     | 01234567 |
Wenn alle Logindaten gelöscht werden, kann initial ein Admin-Nutzer erstellt werden.

## Testdokumentation
Authentifizierungssystem: [#16](https://github.com/mlhmz/nhplus/pull/16)  
Pflegermodul: [#13](https://github.com/mlhmz/nhplus/pull/13)  
Personalgruppe: [#18](https://github.com/mlhmz/nhplus/pull/18)  
Markierung des Behandlungsende: [#19](https://github.com/mlhmz/nhplus/pull/19)  
Sperrung von Daten: [#20](https://github.com/mlhmz/nhplus/pull/20)  
Löschung von Daten: [#21](https://github.com/mlhmz/nhplus/pull/21)

zusätzliche Features:
- Passwort ändern
- Passwort Hashing
- Gruppen und Permissions
- verändertes Controllersystem (mit abstraktem Controller und programmatischen Erstellen des Controllers)
- Initiale Nutzer erstellung
- SQL-Injection behoben

Zwar weiß ich wie ich #14 umsetze, habe dies jedoch zeitlich nicht mehr geschafft.
Auch hätte ich gerne das Logging und den Export im gängigen Dateiformat umgesetzt, und habe dies ebenfalls nicht geschafft.
