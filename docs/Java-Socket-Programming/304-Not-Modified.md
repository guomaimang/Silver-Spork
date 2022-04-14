# 304 Not Modified

## 相关标签头

Last-Modified 与If-Modified-Since 都是标准的HTTP请求头标签，用于记录页面的最后修改时间。

- Last-Modified 是由服务器发送给客户端的HTTP请求头标签。
- If-Modified-Since 则是由客户端发送给服务器的HTTP请求头标签。
- Expired 是server用于指定缓存过期时间的标签。

## 确定最后修改时间

服务器确认文件的最后修改时间，然后包含到response。这可以通过 File 的方法得到，尽管需要一些处理。

```java
long millisec = file.lastModified();
Date dt = new Date(millisec);
return dt.toString();
```

## 实现

如下图所示。

<img src="https://pic.hanjiaming.com.cn/2022/04/14/e160a548909a2.png" alt="1649939333143.png" style="zoom:50%;" />

## Reference

- https://blog.csdn.net/lhl1124281072/article/details/80067764
- https://www.yiibai.com/java_io/file.lastmodified.html