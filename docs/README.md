

# 객체공유



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

재배치 현상은 특정 메소드의 소스코드가 100% 코딩된 순서로 동작한다는 점을 보장할 수 없다는 점에 기인하는 문제이며, 단일 스레드로 동작할 때는 차이점을 전혀 알아챌 수 없지만 여러 스레드가 동시에 동작하는 경우에는 확연하게 나타날 수 있습니다. 

메인 스레드는 number 변수에 값을 먼저 저장하고 ready 변수에도 값을 저장하지만, 동기화되지 않은 상태이기 때문에 읽기 스레드 입장에서는 마치 ready 변수에 값이 먼저 쓰여진 이후에 number 변수에 값이 저장되는 것 처럼 순서가 바뀌어 보일 수 도 있고, 심지어는 아예 변경된 값을 읽지 못할 수도 있습니다.



### 스테일 데이터

스테일 데이터는 어떤 변수의 값을 읽으려고 할 때, 그 변수의 값이 최신 값이 아닌 상태를 말합니다.

**StaleInteger 클래스**

```java
public class StaleInteger {
    private int value;
    
    public int get() { return value; }
    public void set(int value) {this.value = value;}
}
```

> Stale 현상이 나타나는 이유 [그림]

![](https://github.com/DaeAkin/spring-multi-thread/blob/master/docs/images/%EC%8A%A4%ED%85%8C%EC%9D%BC%EC%98%88%EC%A0%9C.png?raw=true)

스테일 현상을 방지하기 위해서는 get메소드와 set 메소드 모두 동기화를 시켜줘야 합니다.

**SynchronizedInteger 클래스**

```java
public class SynchronizedInteger {
    private int value;

    public synchronized int get() { return value; }
    public synchronized void set(int value) {this.value = value;}
}
```



### volatile 변수

volatile로 선언된 변수의 값을 바꿨을 때 다른 스레드에서 항상 최신 값을 읽어갈 수 있도록 해줍니다. 특정 변수를 선언할 때 volatile 키워드를 지정하면, 컴파일러와 런타임 모두 <u>'이 변수는 공유해 사용하고, 따라서 실행 순서를 재배치 해서는 안된다'</u> 라고 이해합니다.

volatile로 지정된 변수는 프로세서의 레지스터에 캐시되지도 않고, 프로세서 외부의 캐시에도 들어가지 않기 때문에 volatile 변수의 값을 읽으면 항상 다른 스레드가 보관해둔 최신의 값을 읽어갈 수 있습니다.

volatile은 위에서 살펴본 **SynchronizedInteger** 클래스와 대략 비슷한 형태로 동작한다고 이해할 수 있습니다. 그러나 volatile 변수를 사용할 때에는 아무런 락이나 동기화 기능이 동작하지 않기 때문에 synchronized를 사용한 동기화보다는 아무래도 강도가 약할 수 밖에 없습니다. 

스레드A가 volatile 변수에 값을 써넣고 스레드 B가 해당 변수의 값을 읽어 사용한다고 할 때, 스레드 B가 volatile 변수의 값을 읽고 나면 스레드 A가 변수에 값을 쓰기 전에 볼 수 있었던 모든 변수의 값을 스레드 B도 모두 볼 수 있다는 점 입니다. 따라서 메모리 가시성의 입장에서 본다면 volatile 변수를 사용하는 것과 synchronized 키워드로 특정 코드를 묶는 게 비슷한 효과를 가져오고, volatile 변수의 값을 읽고 나면 synchronized 블록에 진입하는 것과 비슷한 상태에 해당합니다. 

그러나 volatile 변수만 사용해 메모리 가시성을 확보하도록 작성한 코드는 synchronized로 직접 동기화한 코드보다 훨씬 읽기가 어렵고, 따라서 오류가 발생할 가능성도 높습니다. 

volatile 변수는 다음과 같은 상황일 때 사용하면 좋습니다.

- 변수에 값을 저장하는 작업이 해당 변수의 현재 값과 관련이 없거나 해당 변수의 값을 변경한느 스레드가 하나만 존재
- 해당 변수가 객체의 불변조건을 이루는 다른 변수와 달리 불변조건에 관련되어 있지 않을 때 
- 해당 변수를 사용하는 동안에는 어떤 경우라도 락을 걸어 둘 필요가 없는 경우



## 공개와 유출

특정 객체를 현재 코드의 스코프 범위 밖에서 사용할 수 있도록 만들면 공개 되었다고 합니다. private이 아닌 메소드가 내부에서 생성한 객체를 리턴하거나, 다른 클래스의 메소드로 객체를 넘겨주는 경우 등이 해당 됩니다. 아니면 특정 객체를 공개해서 여러 부분에서 공유해서 사용 할 수 있도록 만들기도 하는데, 이런 경우에는 반드시 해당 객체를 동기화 해야 합니다. 만약 클래스 내부의 상태 변수를 외부에 공개해야 한다면 객체 캡슐화 작업이 물거품이 되거나 내부 데이터의 안정성을 해칠 수 있습니다. 따라서 객체가 안정적이지 않은 상태에서 공개하면 스레드 안정성에 문제가 생길 수 있습니다. 

이처럼 의도적으로 공개시키지 않았지만 외부에서 사용할 수 있게 공개된 경우를 유출 상태라고 합니다.

**객체가 공개 됨**

```java
public static Set<Secret> knownSecrets;

public void initialize() { 
	knownSecrets = new HashSet<Secret>();
}
```

그런데 특정 객체가 공개되면, 그와 관련된 다른 객체까지 덩달아 공개하게 되는 경우도 있습니다. 만약 위에 knownSecrets 변수에게 Secret 객체를 넣는다면, 그 객체도 공개되는 셈 입니다.



```java
public class UnsafeStates {
    private String[] states = new String[] {
        "AA","BB","CC"
    };
    // 내부적으로 사용할 변수를 외부에 공개하는건 좋지 않음!
    public String[] getStates() {return  states;}
}
```

private 키워드를 지정해 숨겨져 있는 states 변수를 다음과 같은 방법으로 공개하면 getStates() 메소드를 호출한 측에서 숨겨진 states 변수의 값을 직접 변경할 수 있기 때문에 권장하는 방법은 아닙니다. 그래서 states라는 변수는 getStates 메소드를 통해 외부에 공개될 수 있기 때문에, states 변수는 유출 상태에 놓여 있다고 볼 수 있습니다.