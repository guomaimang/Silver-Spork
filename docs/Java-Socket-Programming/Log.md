# 日志记录

## class Logger

多个线程需要同时读写日志，所以需要在写入日志时锁定，防止出现不同步。在多线程中，IO尤其需要防止死锁的出现。

之前已经说过，log文件的位置以及名称在全局变量中设置。当文件不存在时，将会被自动地创建。

每一条日志也会输出到终端

Silver Spork 已经实现了

- 文件资源抢占。当一个线程要求logger写入日志的时候，其他进程将会等待，直到资源可用。
- 不会出现死锁。无论在读写中是否出现异常，logger最终会被释放以让其他的线程使用。
- 内存安全。当产生一行日志后，将立刻追加写入到硬盘中的文件内，不会在内存中堆积。
- 追加写入。每次启动程序后，程序将自动写入下划线，以区分不同时间的日志。

```Java
synchronized(this){
  try{
    ...
    BufferedWriter bwo = null;
    bwo = new BufferedWriter(new OutputStreamWriter (new FileOutputStream(file, true)));
    bwo.write(text);
    ...
  }finally{
    bwo.close();
    this.notifyAll();
  }
}
```

## 记录什么

Silver Spork 的实现参考了 Nginx Project。日志保留了必要的信息。

- What event
  - new socket come in
  - client send a request
    - which type: GET/POST/DELETE etc.
    - which url/file
  - close socket connection
- Which socket
- Date (when takes down)
- Which ip address (with port) comes in
- Server resopnse
  - Status: 200/304/400/404

## 追加记录

进程只需要将文本传递给唯一的logger实例。

```java
String str = "Hello";
logger.add(str);
```

## Reference

- https://blog.csdn.net/jsjwk/article/details/3942167