**tcp-spring-boot-starter** - Spring Boot extension library, which allows to quickly bootstrap TCP Servers.

Controller can contain three types of methods:
* Message recieving method. Method should have name starting *receive* and have two arguments: *TcpConnection* and *Object* (or any subtype).
* Client connection listening method. Method should have name starting *connect* and have *TcpConnection* argument.
* Client discconect listening method. Method should have name starting *disconnect* and have *TcpConnection* argument.

## Example
```java
import com.segg3r.spring.tcp.TcpConnection;
import com.segg3r.spring.tcp.TcpController;

@TcpController
public class TestTcpController {

    public void receiveData(TcpConnection connection, byte[] data) {
        String answer = new String(data);
        connection.send(answer.toUpperCase().getBytes());
    }

    public void connect(TcpConnection connection) {
        System.out.println("New connection " + connection.getAddress().getCanonicalHostName());
    }

    public void disconnect(TcpConnection connection) {
        System.out.println("Disconnect " + connection.getAddress().getCanonicalHostName());
    }

}
```

**application.properties**:
```properties
tcp.server.port = 20502  # Value 0 means random port
```


## Setup
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compile 'com.github.segg3r:tcp-spring-boot-starter:___version___'
}
```

Visit https://jitpack.io for setup for other building platforms.