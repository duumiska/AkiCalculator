# Simple Calculator exercice to learn Scala

This is very simple project to implement JSON/REST service providing calculation functions.

prerequest:
- Java [https://www.java.com/en/download/]
- Scala [http://www.scala-lang.org/download/]
- sbt [http://www.scala-sbt.org/download.html]

Usage:
- clone repo
- go to cloned repos directory
- then:
```
sbt
> test
> re-start
```

And there you go

You can test Web endpoint with curl for example:

```
curl.exe "http://localhost:8888/calculus?query=MiAqICgyMy8oMyozKSkgLSAyMyAqICgyKjMp"
{
  "error": "false",
  "result": "-132.88888888888889"
}
curl.exe "http://localhost:8888/calculus?query=MiAqICgyMy8oMyozKSktIDIzICogKDIqMyk%3D"
{
  "error": "false",
  "result": "-132.88888888888889"
}
```