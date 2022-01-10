# daemon-thread & user-thread

1. [Launch thread is just another user thread](http://www.javapractices.com/topic/TopicAction.do?Id=50)

2. [Use System.exit with care](http://www.javapractices.com/topic/TopicAction.do?Id=86)
> `System.exit` should be used with care. **The normal method of terminating a program is to terminate all <font color="red">user threads</font>.**
