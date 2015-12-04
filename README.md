# Multithread-ru.sbt.mipt.fifo


Using GitHub Integration on your IDEA
https://www.jetbrains.com/idea/help/using-github-integration.html

Before commiting ignore all files except source files!


In Java, these methods should be used in the following structured
way.
``` JAVA
1 mutex.lock();
2 try {
3 ... // body
4 } finally {
5 mutex.unlock();
6 }
```
This idiom ensures that the lock is acquired before entering the try block, and
that the lock is released when control leaves the block, even if some statement
in the block throws an unexpected exception.
