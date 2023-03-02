# $ 넘블 뱅킹 서버 구축하기 챌린지

- GitHub : https://github.com/StolenMoments/numble-banking
- Server : https://49.50.163.50:8080/api
- Swagger : http://49.50.163.53:8080/api/swagger-ui/index.html

## 🤔 구현 목표

### 요구사항
- 친구 추가 API
- 친구 목록 조회 API
- 계좌 이체 API (친구 관계만 가능)
- 내 잔고 조회 API (본인만 가능)
- 회원가입 및 로그인 API (단순 ID/PW 형태)
- 유닛 테스트


### 어떻게?
- 본 과제의 메인은 **계좌 이체 로직**이다. 동시 다발적으로 이체가 발생하면 금액 부분에서 불일치 할 수 있는, 즉 동시성 이슈가 생길텐데 이에 대한 문제 해결, 부하 테스트에 초점을 맞추려고 한다.
- 계좌이체 API 에 최대한 집중하고 나머지 API 들은 보조 및 확인 용도로서 되도록 단순하게 구현하려고 한다.

## 🔥 구현 내용

### 개요
- 사용 기술
    - Java 17
    - Spring Boot 3
    - MariaDB 10.6
    -
- REST API 사용
    - 웹, 모바일 앱 환경까지 고려하면 클라이언트에 크게 종속되지 않는 REST API 가 가장 합리적인 선택이라 판단했다.

- Domain, Service, Controller 레이어로 나누어 개발
    - 관심사로 나누어 코드를 작성하는게 유지보수 관점에서 좋기 때문이다.
    - 널리 사용되고 관련 자료도 많다.
    - 익숙하다.

- DTO 사용
    - 다양한 요청, 응답에 따라 꼭 필요한 데이터만 주고 받기 위해 DTO 를 사용했다.

- 테스트 코드
    - Repository, Service 레이어를 대상으로 한다.
    - Repository 레이어는 단순 저장, 업데이트 기능을 테스트 한다. Repository 테스트는 DB 동작에만 집중.
    - Service 레이어는 Mocking 을 통해 로직의 결과가 의도대로 동작하는지 테스트 한다. DB 는 Mocking 하여 로직에만 집중.
    - Controller 레이어는 Service 를 호출하는 역할 말고는 로직이 없으므로 굳이 테스트 하지 않았다.

### 테이블 명세

![](https://i.imgur.com/AsAxTVY.png)


![](https://i.imgur.com/9xfmgDf.png)



### 원자성 보장

- transferMoney 메서드에서 사용되는 repository 메서드에 비관적 잠금을 설정하는 방식으로 시도했다.

![](https://i.imgur.com/D5GW10B.png)

![](https://i.imgur.com/gF1k4ze.png)


- 다음의 테스트 코드로 검증을 해보았다

```java
// sender 의 초기 금액 = 100000L

@Test
    void transferTestWithConcurrency() throws InterruptedException {

        long amount = 500L;
        int numberOfThreads = 10;
        int executeCnt = numberOfThreads * 5;

        ExecutorService exeService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(executeCnt);
        for (int i = 0; i < executeCnt; i++) {
            exeService.execute(() -> {
                try {
                    accountService.transferMoney(
                        AccountTransferRequestDto.builder()
                            .senderAccountId(senderAccountId)
                            .receiverAccountId(receiverAccountId)
                            .receiverLoginId("receiver")
                            .loginId("sender")
                            .amount(amount)
                            .build());
                } catch (Exception e) {
                    System.out.println(e.getMessage());

                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Long senderBalance = accountService.getBalance(senderAccountId);
        Long receiverBalance = accountService.getBalance(receiverAccountId);
        assertThat(senderBalance + receiverBalance).isEqualTo(100000L);
        assertThat(receiverBalance).isEqualTo(amount * executeCnt);
        assertThat(senderBalance).isEqualTo(100000L - (amount * executeCnt));
    }
```

- 테스트는 10회 반복해서 실시하여 모두 성공했다.


## 느낀점
- 계좌 이체에만 집중하다 보니 디테일한 부분에는 신경쓰지 못했다.
- 특히 Exception 처리, 예상되는 결함 처리 (숫자 범위 등..), 보안 등을 제대로 하지 못해 아쉬웠다. 공식적인 프로젝트 기간은 끝났지만 앞으로도 개선하면서 경험치를 쌓으려고 한다. 

## 새롭게 공부한 내용

## 궁금한 점
