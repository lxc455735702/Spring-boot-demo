# demo-mq-rabbitmq
> 此demo为了学习Rabbitmq

## 注意
此demo是用docker搭建的环境, https://hub.docker.com/ 搜索rabbitmq
```
docker pull rabbitmq:management-alpine // 拉取镜像
docker run -d --hostname my-rabbit --name rabbit -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=123456 -p 4369:4369 -p 5671:5671 -p 5672:5672 -p 15672:15672 rabbitmq:management-alpine // 运行rabbitMq镜像
```
## 配置
```
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: 123456
    virtual-host: /test
```
