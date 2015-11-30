# Simple Calculator exercice to learn Scala

This is very simple project to implement JSON/REST service providing calculation functions.

##Prerequisite
- Java [https://www.java.com/en/download/]
- Scala [http://www.scala-lang.org/download/]
- sbt [http://www.scala-sbt.org/download.html]

Also sbt downloads all needed dependencies. Web Server is build on top of Spray (+Akka) [http://spray.io/]

##Usage

- clone repo
- go to cloned repos directory
- then:
```
sbt
> test
> re-start
```

And there you go

This creates local web server with endpoints 
- http://localhost:8888/calculus/example (makes following calculation 5+((1+2)*4)-3)
- http://localhost:8888/calculus which takes base64 encoded calculation on parameter query

##Testing

To make base64 calculations you can use for example https://www.base64encode.org/ 
Remember to change following characters on base64 encoded calculation as they are not allowed in url
```
= as %3D
+ as %2B
/ as %2F
```

You can test Web endpoint with curl for example:

```
This makes following calculation 0.5*(1000/100)+(5+5)
curl.exe "http://localhost:8888/calculus?query=MC41KigxMDAwLzEwMCkrKDUrNSk%3D
{
  "error": "false",
  "result": "15.0"
}
This makes following calculation 2 * (23/(3*3)) - 23 * (2*3)
curl.exe "http://localhost:8888/calculus?query=MiAqICgyMy8oMyozKSktIDIzICogKDIqMyk%3D"
{
  "error": "false",
  "result": "-132.88888888888889"
}
```

##Deploy

To run in heroku you only need to have account on heroku and run following after cloning project from git:

```
heroku create
git push heroku master
heroku open
```

Longer instructions can be found [https://devcenter.heroku.com/articles/getting-started-with-scala]
