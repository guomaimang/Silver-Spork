# Silver-Spork
COMP2322(CN) Project

A small HTTP Server (Networking Project & Socket Programming)

- Document: [https://guomaimang.github.io/note/cs/cn/java-socket-programming](https://guomaimang.github.io/note/cs/cn/Java-Socket-Programming)
- Demo: [http://cn2322.hanjiaming.com.cn:8082](http://cn2322.hanjiaming.com.cn:8082)

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
