# COMP2322 Networking Project Report

**Han Jiaming 20075519d**

- Project Program Name: **Silver Spork**
- This report has been upload to https://guomaimang.github.io/note/cs/cn/Java-Socket-Programming on 4.20/2022

## Introduction

Socket programming is an important dependency for communication between processes. Silver Spork has implemented a socket communication program using Java natively. 

This report clearly describes the idea and development process of Silver Spork.

## Environment

- Development
  - Macos
  - Java 17
  - Intelj idea 2021

- Testing
  - ![img](https://img.shields.io/badge/Build%20on%20Windows%2010-Pass-brightgreen) ![img](https://img.shields.io/badge/Build%20on%20Macos-Pass-brightgreen) ![img](https://img.shields.io/badge/Build%20on%20Centos%20Linux%207.9-Pass-brightgreen) 
  - Postman
  - Chrome dev100
  - IPv4 + IPv6

## Organization

### File

- Public: Storing static website pages
- src: Storing Java source code
  - `HttpServer.java`: Program Ontology, also Launcher
    - `class->clock`: Executable, timing stop process
    - `class->worker`: Handler for single socket
  - `Request.Java`: Parsing the incoming Request message
  - `Response.Java`: Sending messages based on Response
  - `Logger.java`: Take down the log

### Configurable global variables

- `serverRoo`t: Specifies the location where the static site files are stored, i.e. the root directory of the site.
  - Default is "public"
  - The site owner can place files in this directory
- `port`: Server socket port 
  - Default is 80
- `logger`: set the location of log file
  - Default is "log.txt"

## Advantage

- Developed using native Java, starting from sockets . Does not use any existing httpServer library.
- Extensible. Modular development using Java. Facilitates functional expansion and secondary development.
- Development specifications.
  - Comments are clear.
  - Use Java beans to set variables.
  - No useless interfaces.
  - No dangerous functions/methods are used. 

## Multi-threaded

<img src="https://pic.hanjiaming.com.cn/2022/04/14/096e7cecca441.png" alt="1649948280489.png" style="zoom: 21%;" />

The program uses Java thread pools to create multiple threads instead of using the traditional way. This facilitates the management of threads later.

In essence, multiple sockets coexist and the program needs to maintain communication with the client. Silver Spork lets each child thread handle each socket. When the socket expires, the child thread will be interrupted.

```java
// create thread pool for timer exec
ExecutorService workerExec = Executors.newCachedThreadPool();
```

A thread handles a socket connection. When a new socket is passed in, the program will start a new worker thread to handle the interaction with the client.

```java
Socket socket = serverSocket.accept(); 
workerExec.execute(new Worker(socket, ++clientID,logger));
```

## IO & Transport

### Wait for incoming

Class: Worker uses `BufferedInputStream` instead of ` InputStream`.

- When there is no new input stream, IO blocking will occur to prevents `while loop` from consuming system resources.
- The buffer will be automatically emptied after the stream is read to prevent duplicate read.

```Java
DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
```

### Implementation

<img src="https://pic.hanjiaming.com.cn/2022/04/14/4fe7f6153c316.png" alt="1649941971522.png" style="zoom:20%;" />

## Response

### Implementation

The Silver Spork implementation references the Nginx Project. Currently, the program only supports request type: `GET` or `HEAD`. Incorrect request will result in a corresponding 400 on the server. It is easy to extend other types.

- For url: `/`, return`index.html` with code 200。
- For statue = 404, return `404.html` with code 404。
- The folder already contains the sample index.html and 404.html. It can be replaced if you like.
  - These html template files are from bt.cn and can be used for teaching or demonstration purposes only.

The specific implementation is shown in the figure below.

<img src="https://pic.hanjiaming.com.cn/2022/04/18/bf0f73f47bc3a.png" alt="1650261914706.png" style="zoom: 25%;" />

### MIME

Common text, image and web files are supported. Other file extensions will be determined as `text/plain`.

- text/html: .html & .htm
- image/jpeg
- image/png
- image/gif

## Keep-alive

TCP/IP requires two handshakes. Using keep-alive will tell the client that the server's socket is not closed and can be reused, which will reduce the number of communications. Silver spork supports keeping the socket open until the IO blocks for more than 10s。

By default, the program sets `SoTimeout` to 10s.

```java
Socket socket = serverSocket.accept();
socket.setKeepAlive(true);
socket.setSoTimeout(10000);  // millisecond
```

- While the thread is not terminated, the thread will keep blocking on a new `inputStream`.
- If there is no new input stream for 10s (i.e. `socket.getInputStream()` blocks for more than 10s), the socket will throw an `InterruptedIOException`.
  - At this point, the socket will be closed.

```java
 try(){
    while (true){
		DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
  	......
	}catch(InterruptedIOException e)
  }finally{
    socket.close();
  }
```

## 304 Not Modified

### Related header tags

- `Last-Modified` and `If-Modified-Since` are both standard HTTP request header tags used to record the last modified time of a page.
  - `Last-Modified` is the HTTP request header tag sent from the server to the client.
  - `If-Modified-Since` is the HTTP request header tag sent by the client to the server.
  - `Expired` is the tag used by the server to specify the cache expiration time.

### Determine last modified time

服务器确认文件的最后修改时间，然后包含到response。这可以通过 File 的方法得到，尽管需要一些处理。

```java
long millisec = file.lastModified();
Date dt = new Date(millisec);
return dt.toString();
```

### Implementation

<img src="https://pic.hanjiaming.com.cn/2022/04/14/e160a548909a2.png" alt="1649939333143.png" style="zoom: 25%;" />

### Tag: Expired

Set the client cache expiration time. server default specifies 14 days after.

## class Logger

Multiple threads need to write logs at the same time. They need to be locked while writing logs to prevent out-of-sync. In multithreading, IO especially needs to prevent deadlock.

As mentioned before, the location of the log file and its name are set in a global variable. When the file does not exist, it will be created automatically. Log text will also be output to the terminal.

Silver Spork has implemented

- **File resource seizure.** When a thread asks the logger to write a log, other processes will wait until resource is available.
- **No deadlocks will occur.** The logger will eventually be released for other threads to use, regardless of exceptions in reading or writing.
- **Memory safe.** When a line of logging is generated, it is immediately appended and written to a file on the hard disk and does not accumulate in memory.
- **Append write.** Each time the program is started, the program will automatically write an underscore to distinguish the logs at different times.

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

### Record what

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

### Usage

The process only needs to pass the text to the unique logger instance.

```java
String str = "Hello";
logger.add(str);
```

## Demo

Server address: localhost:8082

### Start application

<img src="https://pic.hanjiaming.com.cn/2022/04/14/2c081234feaf9.png" alt="CleanShot 2022-04-14 at 23.23.28@2x.png" style="zoom: 20%;" />

### Chrome

#### Visit /

<img src="https://pic.hanjiaming.com.cn/2022/04/14/a8950b5c6e8c2.png" alt="1649950487826.png" style="zoom: 25%;" />

#### Revisit /

<img src="https://pic.hanjiaming.com.cn/2022/04/14/c0c2d2a708471.png" alt="1649950761195.png" style="zoom: 20%;" />

#### Visit /demo.png (picture file)

<img src="https://pic.hanjiaming.com.cn/2022/04/14/87b7ae13ab9e4.png" alt="1649950879472.png" style="zoom: 20%;" />

#### Visit /mount.html

<img src="https://pic.hanjiaming.com.cn/2022/04/14/9d33cd5996d65.png" alt="1649951090662.png" style="zoom: 25%;" />

#### Visit /LICENSE (text file)

<img src="https://pic.hanjiaming.com.cn/2022/04/14/f2452950b6267.png" alt="1649951216661.png" style="zoom: 25%;" />

### Postman

#### GET /

<img src="https://pic.hanjiaming.com.cn/2022/04/14/2f2b31038e34c.png" alt="1649951453575.png" style="zoom: 25%;" />

#### POST /demo.png

<img src="https://pic.hanjiaming.com.cn/2022/04/14/62557f5b1de9d.png" alt="1649951656250.png" style="zoom: 20%;" />

#### HEAD /logo.png

<img src="https://pic.hanjiaming.com.cn/2022/04/18/cb045464dd447.png" alt="CleanShot 2022-04-18 at 13.49.58@2x.png" style="zoom: 25%;" />

#### Log file

<img src="https://pic.hanjiaming.com.cn/2022/04/18/0a98385cc44c2.png" alt="1650261667772.png" style="zoom: 21%;" />

## Summary

Silver Spork has implemented 

- [x] Use Java Muti-Thread to process socket
- [x] Create a connection socket when contacted by a client (browser)
- [x] Receive the HTTP request from this connection
- [x] Parse the request to determine the specific file being requested
- [x] Get the requested file from the server’s file system
- [x] Proper request and responsemessage exchanges 
- [x] Create an HTTP response message consisting of the requested file preceded by header lines;
- [x] Can't use the HTTPServer class directly
- [x] Only process GET and HEAD request
- [x] Only response HTTP1.1 200, 400, 404, 304
- [x] Handle Last-Modified and If-Modified-Since headerfields
- [x] Handle Connection: Keep-Aliveheader field

With

- [x] A good summary.
- [x] A clear README text file
- [x] High quality.
- [x] A full demonstration.

## Reference

Including report, idea, model, template, etc.

- https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
- https://blog.csdn.net/allway2/article/details/120255402
- https://www.runoob.com/java/java-networking.html
- https://nginx.org/en/
- https://blog.csdn.net/weixin_43599377/article/details/101393573
- https://www.jianshu.com/p/c7ba05b1afa0
- https://blog.csdn.net/u011250186/article/details/106121823/
- https://www.codebye.com/http-ru-he-chuan-shu-tu-pian.html
- https://docs.oracle.com/javase/8/docs/technotes/guides/net/http-keepalive.html
- https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Keep-Alive
- https://blog.csdn.net/jsjwk/article/details/3942167
- https://www.runoob.com/java/java-multithreading.html
- https://www.liaoxuefeng.com/wiki/1252599548343744/1306581130018849
- https://www.cnblogs.com/wxd0108/p/5479442.html
- https://blog.csdn.net/a1275302036/article/details/116662394
- http://www.bt.cn
- https://datatracker.ietf.org/doc/html/rfc7231
- https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Methods/HEAD
- https://blog.csdn.net/lhl1124281072/article/details/80067764
- https://www.yiibai.com/java_io/file.lastmodified.html

