# REKT - (RE)dis (K)o(T)lin
`rekt` is a lightweight, non-bloated redis client, primarily written for the [kotlin](https://kotlinlang.org/) programming language, while also supporting 
other JVM-based languages, such as [Java](https://www.java.com/en/), [Scala](https://www.scala-lang.org/), and obviously way more.

# Why use REKT
* You probably **should not** if you're working on a production server, REKT is still unstable and in heavy development, however we do have some benefits over other libraries:
  * Lightweight, not-bloated
    * REKT does what it has to do - handle redis messaging, nothing else.
  * Despite being lightweight, it still has an easy-to-use API, primarily focusing on making the life of kotlin developers easier.
  * It's less than `2000` lines of code! Whereas, for example [jedis](https://github.com/redis/jedis) has `92000` lines of code, and [lettuce-core](https://github.com/lettuce-io/lettuce-core) has a whopping `215000` lines of code!

# Wiki
Our wiki is currently located on the [github repository](https://github.com/devrawr/rekt/wiki), feel free to visit it for any information
regarding the usage of our API.