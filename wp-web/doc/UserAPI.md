#登录注册API
|     key      |             value           |
|:------------:|:---------------------------:|
|base_url      |[http://192.168.51.60:8080/api]()|
|code          |返回值,0代表成功,其他值代表失败   |
|message       |错误信息                      |
|data          |接口返回实体信息                |
##1、获取验证码
>* url: [/vcode/send]()
>* 请求方式: POST

|  参数  | 描述  | 是否必填 |
|:-----:|:-----:|:------:|
|phone|手机号 |是 |

>返回值

|  code  | message   |
|:------:|:---------:|
|0|SUCCESS |
|1|请求参数格式错误|

>返回实例

```
{
  "code": "0",
  "message": "SUCCESS"
}
```
##2、注册
>* url: [/user/register]()
>* 请求方式: POST

|  参数  | 描述  | 是否必填 |
|:-----:|:-----:|:------:|
|userName|手机号（使用手机号作为userName） |是 |
|password|密码|是|
|code|验证码|是|

>返回值

|  code  | message   |
|:------:|:---------:|
|0|SUCCESS |
|1|您的手机号已注册|
|2|获取token失败|
|3|注册失败|
|4|验证码无效|

>Data数据

|  key  | 描述   |
|:------:|:---------:|
| userName |注册的手机号|
| token |token值|
| type |token类型：0为access token，1为refresh token|
| createAt |注册时间|
| expiresIn |token 失效时间|

>返回实例

```
{
  "code": "0",
  "data": {
    "userName": "18612182957",
    "token": "GevHLuCcBxvdeiFZPXxGvrwiUbYecYoN",
    "type": 0,
    "createAt": 1488188688946,
    "expiresIn": 86400
  },
  "message": "SUCCESS"
}
```
##3、登录
>* url: [/user/logIn]()
>* 请求方式: POST

|  参数  | 描述  | 是否必填 |
|:-----:|:-----:|:------:|
|userName|手机号（使用手机号作为userName） |是 |
|password|密码|是|

>返回值

|  code  | message   |
|:------:|:---------:|
|0|SUCCESS |
|1|用户不存在|
|2|密码错误|
|3|登录失败|

>Data数据

|  key  | 描述   |
|:------:|:---------:|
| id |tokenId |
| userName |注册的手机号|
| token |token值|
| type |token类型：0为access token，1为refresh token|
| createAt |注册时间|
| expiresIn |token 失效时间|

>返回实例

```
{
  "code": "0",
  "data": {
    "id": 10,
    "userName": "18612182957",
    "token": "GevHLuCcBxvdeiFZPXxGvrwiUbYecYoN",
    "type": 0,
    "createAt": 1488188688946,
    "expiresIn": 86400
  },
  "message": "SUCCESS"
}
```

##4、完善信息
>* url: [/user/perfectInfo]()
>* 请求方式: POST

|  参数  | 描述  | 是否必填 |
|:-----:|:-----:|:------:|
|userName|手机号（注册时的手机号） |是 |
|token|注册或登录成功后返回的token|是|
|nickName|姓名|否|
|email|邮箱|否|
|job|职称|否|
|company|公司名称|否|

>返回值

|  code  | message   |
|:------:|:---------:|
|0|SUCCESS |
|1|该用户未注册|
|2|Token无效|
|3|信息修改失败|

>返回实例

```
{
  "code": "0",
  "message": "SUCCESS"
}
```

##5、Token验证

>* url: [/user/checkToken]()
>* 请求方式: POST

|  参数  | 描述  | 是否必填 |
|:-----:|:-----:|:------:|
|userName|手机号（注册时的手机号） |是 |
|token|Token（登录注册时返回的）|是|

>返回值

|  code  | message   |
|:------:|:---------:|
|0|SUCCESS |
|1|Token无效|

>返回实例

```
{
  "code": "0",
  "message": "SUCCESS"
}
```

##6、重置密码

>* url: [/user/resetPassword]()
>* 请求方式: POST

|  参数  | 描述  | 是否必填 |
|:-----:|:-----:|:------:|
|phone|手机号（注册时的手机号） |是 |
|code|验证码|是|
|newPassword|新密码|是|

>返回值

|  code  | message   |
|:------:|:---------:|
|0|SUCCESS |
|1|Token无效|

>返回实例

```
{
  "code": "0",
  "message": "修改密码成功"
}
```

##7、根据Token获取用户信息
>* url: [/user/get]()
>* 请求方式: POST

|  参数  | 描述  | 是否必填 |
|:-----:|:-----:|:------:|
|token  |Token（登录注册时返回的）|是 |

>返回值

|  code  | message   |
|:------:|:---------:|
|0|SUCCESS |
|1|Token无效|
|2|获取失败|

>Data数据

|  key  | 描述   |
|:------:|:---------:|
| id |userId |
| userName |注册的手机号|
| role |用户类型|
| nickName |用户名|
| company |公司名称|
| job |职位|
| email |邮箱地址|
| phoneNumber |手机号|

>返回实例

```
{
  "code": "0",
  "data": {
    "id": 18,
    "userName": "18612182957",
    "role": "USER",
    "nickName": Jack,
    "company": 骨朵影视,
    "job": 开发,
    "email": wangzhiheng@jinguduo.com,
    "phoneNumber": "18612182957"
  },
  "message": "SUCCESS"
}
```
##8、校验验证码
>* url: [/vcode/verify]()
>* 请求方式: POST

|  参数  | 描述  | 是否必填 |
|:-----:|:-----:|:------:|
|phone|手机号 |是 |
|code|验证码 |是 |

>返回值

|  code  | message   |
|:------:|:---------:|
|0|验证成功 |
|1|验证失败|

>返回实例

```
{
  "code": "0",
  "message": "验证成功"
}
```

