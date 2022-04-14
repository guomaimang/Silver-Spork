---
article: false
date: 2022-04-19
index: 9


---

# Keep-alive

## 什么是 HTTP 持久连接？

HTTP 持久连接，也称为 HTTP keep-alive 或 HTTP 连接重用，是使用相同的 TCP 连接发送和接收多个 HTTP 请求/响应的想法，而不是为每个请求/响应对打开一个新连接。使用持久连接对于提高 HTTP 性能非常重要。

使用持久连接有几个优点，包括：

- 网络友好。由于 TCP 连接的设置和断开更少，因此网络流量更少。
- 减少后续请求的延迟。由于避免了初始 TCP 握手
- 持久连接允许 TCP 有足够的时间来确定网络的拥塞状态，从而做出适当的反应。

使用 HTTPS 或 HTTP over SSL/TLS 的优势更加明显。在那里，除了初始 TCP 连接设置之外，持久连接可以减少昂贵的 SSL/TLS 握手次数以建立安全关联。

在 HTTP/1.1 中，持久连接是任何连接的默认行为。也就是说，除非另有说明，否则客户端应该假设服务器将保持持久连接，即使在服务器的错误响应之后也是如此。但是，该协议为客户端和服务器提供了发出 TCP 连接关闭信号的方法。

## 示例

`含有 Keep-Alive` 首部的响应示例:

```
HTTP/1.1 200 OK
Connection: Keep-Alive
Content-Encoding: gzip
Content-Type: text/html; charset=utf-8
Date: Thu, 11 Aug 2016 15:23:13 GMT
Keep-Alive: timeout=10, max=1000
Last-Modified: Mon, 25 Jul 2016 04:32:39 GMT
Server: Apache
```

## 实现

TCP/IP 需要两次握手。使用keep-alive可以告知client that 服务器的socket没有关闭可以复用，这样将减少通信次数。Silver Spork 支持保持socket不关闭，直到IO阻塞超过10s。

默认的，该程序设置 `SoTimeout` 为10s。

```java
Socket socket = serverSocket.accept();
socket.setKeepAlive(true);
socket.setSoTimeout(10000);  // millisecond
```

- 当线程未终止时，线程将一直阻塞在新的inputStream。
- 如果10s 内无新的input stream（即 `socket.getInputStream()` 阻塞超过10s），socket 将会抛出 `InterruptedIOException`。
  - 这时，socket将会关闭。

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

## Tag: Expired

设定client cache过期时间。Server 默认指定为 14天后。

## Reference

- https://docs.oracle.com/javase/8/docs/technotes/guides/net/http-keepalive.html
- https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Keep-Alive

