- java.time.LocalDate -> 只对年月日做出处理
- java.time.LocalTime -> 只对时分秒纳秒做出处理
- java.time.LocalDateTime -> 同时可以处理年月日和时分秒

###
1. 关于java.util.Date与LocalDate、LocalTime、LocalDateTime的转换。


### JDBC映射Java8
|      SQL      |       java              |
|:-------------:|:-----------------------:|
| date          | java.time.LocalDate     |
| time          | java.time.LocalTime     |
| timestamp     | java.time.LocalDateTime |
