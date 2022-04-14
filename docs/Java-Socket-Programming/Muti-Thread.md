---
article: false
date: 2022-04-19
index: 1

---

# 多线程实现

Java 使用多线程而不是多进程。

多线程中，最难处理的是IO。Silver Spork 需要将日志写入到文件中，所以需要加锁。由于存在使用stop终止线程，这部分需要格外小心。防止出现死锁。

## 实现

<img src="https://pic.hanjiaming.com.cn/2022/04/14/096e7cecca441.png" alt="1649948280489.png" style="zoom:50%;" />

程序使用Java 线程池创建多线程，而不是使用传统的方式。这样有利于后期管理线程。

实质上是多个socket并存，程序需要保持和客户端的通信。Silver Spork让每个子线程处理每个socket。当socket 过期后，子线程将被终止。

```java
// create thread pool for timer exec
ExecutorService workerExec = Executors.newCachedThreadPool();
```

一个线程处理一个socket连接。当新的socket传入时，程序将启动一个新的worker 线程处理 和客户端的交互。

```java
Socket socket = serverSocket.accept(); 
workerExec.execute(new Worker(socket, ++clientID,logger));
```

## Reference

- https://www.runoob.com/java/java-multithreading.html
- https://www.liaoxuefeng.com/wiki/1252599548343744/1306581130018849
- https://www.cnblogs.com/wxd0108/p/5479442.html
- https://blog.csdn.net/a1275302036/article/details/116662394