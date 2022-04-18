# Response 消息

## 实现

本程序的实现参照了Nginx Project。

目前，该程序仅支持 request type: GET or HEAD。错误的request会导致服务器相应400。拓展其他类型也是容易的。

- 对于 url: `/`, 返回`index.html` with code 200。
- 对与 statue = 404, 返回 `404.html` with code 404。
- The folder already contains the sample index.html and 404.html. It can be replaced if you like.
  - These html template files are from bt.cn and can be used for teaching or demonstration purposes only.


具体的实现如下图所示。

<img src="https://pic.hanjiaming.com.cn/2022/04/18/bf0f73f47bc3a.png" alt="1650261914706.png" style="zoom:50%;" />

## MIME

支持常见的文本、图片以及网页文件。

- text/html: .html & .htm
- image/jpeg
- image/png
- image/gif

其他文件后缀将会被判定为 text/plain

## Reference

- http://www.bt.cn
- https://datatracker.ietf.org/doc/html/rfc7231
- https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Methods/HEAD





