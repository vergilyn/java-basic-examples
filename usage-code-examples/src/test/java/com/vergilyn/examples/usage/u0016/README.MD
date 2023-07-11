# JSON 序列化 和 反序列化

## 背景
假设存在以下JSON结构
```json5
{
  "bizNo": "biz-no-0001",
  "body": [
    {
      "param": {
        "key1": "value1",
        "key2": "value2"
      },
      "to": {
        "mobiles": [
          "123",
          "456"
        ]
      },
      "type": 100
    },
    {
      "param": {
        "key1": "value1",
        "key2": "value2"
      },
      "to": {
        "mobiles": [
          "789",
          "000"
        ]
      },
      "type": 100
    },
    {
      "param": {
        "key1": "value1",
        "key2": "value2"
      },
      "to": {
        "webhooks": [{
          "accessToken": "access-token-0001",
          "name": "name-0001",
          "secret": "secret-0001"
        }]
      },
      "type": 141
    }
  ]
}
```

反序列化时，需要根据`body.type`确定其Java类型。

### 方式一
自定义整个 `SendMsg` 的反序列化。

**不推荐**。因为：
1) 如果`SendMsg`中的 field's 有修改（新增、删除、修改），那么需要修改此反序列化代码。`

### 方式二
只自定义`SendMsg.body`字段的反序列化。

**推荐**。
