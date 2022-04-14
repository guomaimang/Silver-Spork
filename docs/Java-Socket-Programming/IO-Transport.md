# IO & Transport

## 等待传入

Class: Worker使 用BufferedInputStream而不是InputStream。

- 当无新的input stream时，会产生IO阻塞。防止while循环消耗系统资源。
- 读取stream后将自动清空buffer，防止重复读取。

```Java
DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
```

## 实现

![1649941971522.png](https://pic.hanjiaming.com.cn/2022/04/14/4fe7f6153c316.png)

## Reference

- https://blog.csdn.net/weixin_43599377/article/details/101393573
- https://www.jianshu.com/p/c7ba05b1afa0
- https://blog.csdn.net/u011250186/article/details/106121823/
- https://www.codebye.com/http-ru-he-chuan-shu-tu-pian.html