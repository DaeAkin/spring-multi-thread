

## 가시성

단일 스레드만을 사용하는 환경이라면 특정 변수에 값을 지정하고 다음번에 해당 변수의 값을 다시 읽어보면, 이전에 저장해뒀던 바로 그 값을 가져 올 수 있습니다. 

**NoVisibility 클래스**

```java
public class NoVisibility {
    private static boolean ready;
    private static int number;

    public static class ReaderThread extends Thread {
        @Override
        public void run() {
            while(!ready)
                Thread.yield();
            System.out.println(number);
        }
    }

     public static void main(String[] args) {
          new ReaderThread().start();
          number =42;
          ready = true;
      }
}
```

위에 있는 NoVisibility 클래스를 보면 동기화 작업이 되어 있지 않은 상태에서 여러 스레드가 동일한 변수를 사용 할 때 어떤 문제가 생길 수 있는지를 알 수 있습니다. 

메인 스레드와 읽기스레드가 `ready` 와 `number` 라는 변수를 공유해 사용합니다. 메인 스레드는 읽기 스레드를 실행시킨 다음  number 변수에 42라는 값을 넣고, ready 변수의 값을 true로 지정합니다. 읽기 스레드는 ready 변수의 값이 true 될 때 까지 반복문에서 기다리다가 ready 값이 true로 변경되면 number 변수의 값을 출력합니다.

일반적으로 이 클래스를 실행시켜보면 42가 나올 것이라고 예상할 수 있지만, 0이라는 값을 출력할 수 도 있고, 심지어 영원히 값을 출력하지 못하고 ready 변수의 값이 true로 바뀌기를 계속해서 기다릴 수 도 있습니다. 말하자면 메인 스레드에서 number 변수와 ready 변수에 지정한 값을 읽기 스레드에서 사용 할 수 없는 상황인데, 두 개 스레드에서 변수를 공유해 사용함에도 불구하고, 동기화 기법을 사용하지 않았기 때문입니다.

그래서 위에 클래스는 영원히 무한 반복에 빠질 수 도 있고 더 이상하게는 읽기 스레드가 메인 스레드에서 number 변수에 지정한 값 보다 ready 변수의 값을 먼저 읽어 갈 수 도 있는데, 이런 현상을 재배치(reordering)라고 합니다. 

재배치 현상은