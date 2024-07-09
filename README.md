![spartacodingclub](https://noticon-static.tammolo.com/dgggcrkxq/image/upload/v1719643111/noticon/yeqwdeuiybor5m4hh7zj.png)
# Hanghae99 Preonboarding Backend Course

**취업시장에 침투하기 전에, 실전과 같은 훈련으로 코딩의 감(떫음)을 찾아서 세상에 스파르타st를 보여주자.<br />
어렵다고 느끼는 제군들도 있겠지만, 힌트를 보면서 잘 따라 와주기를 바란다.**



### 🎖️ 훈련 메뉴

---
- [ ]  Junit를 이용한 테스트 코드 작성법 이해
- [ ]  Spring Security를 이용한 Filter에 대한 이해
- [ ]  JWT와 구체적인 알고리즘의 이해
- [ ]  PR 날려보기
- [ ]  리뷰 바탕으로 개선하기
- [ ]  EC2에 배포해보기

### Day 1 - 시나리오 설계 및 코딩 시작!

---
**Spring Security 기본 이해**

- [v]  Filter란 무엇인가?(with Interceptor, AOP)
  - Filter
    - 필터는 서블릿 기반의 애플리케이션에서 클라이언트 요청과 응답을 가로채어 전처리 및 후처리를 수행하는 데 사용됩니다.
    - 디스패쳐서블릿 전후로 실행되며, 서블릿 컨테이너에서 모든 요청과 응답을 가로채어 전처리 및 후처리를 수행합니다.
    - 필터는 서블릿 컨테이너의 필터 체인(Filter Chain)에 등록됩니다.
    - 요청(Request)이 서블릿에 도달하기 전에 필터가 실행되고, 응답(Response)이 클라이언트로 반환되기 전에 다시 필터가 실행됩니다.
    - 주로 인코딩 설정, 인증 및 인가, 로깅, 보안 관련 용도로 사용합니다.
    
  - Interceptor
    - Spring MVC에서 HTTP요청을 가로채어 컨트롤러 호출 전후에 공통 작업을 수행합니다.
    - 디스패쳐서블릿 내부에서 컨트롤러 메서드 호출 전후에 실행되며, 요청의 전처리(preHandle), 후처리(postHandle), 완료처리(afterCompletion)를 수행합니다.
    - 주로 인증 및 인가, 로깅 등에 사용합니다.
    
  - AOP
    - AOP는 횡단 관심사(cross-cutting concerns)를 모듈화하여 비즈니스 로직과 분리합니다.
    - Aspect는 공통 기능을 모듈화한 클래스로, 선택한 지점(Pointcut)의 지정한 시점(JoinPoint)에서 어떤 작업(Advice)를 수행할 것인지 정의한다.
    - 주로 로깅, 트랜잭션 관리, 성능 모니터링에 사용한다.

  - Spring 에서의 요청 및 응답 과정
    - 클라이언트의 요청 -> 서블릿 필터들이 요청을 가로채 필터 전 처리 -> 디스패쳐서블릿으로 전달 -> 적절한 핸들러를 찾음 -> 인터셉터 호출 -> AOP가 적용된 메서드라면 호출 -> 핸들러 메서드 실행 -> 인터셉터의 'postHandle', 'afterCompletion' 메서드 호출 -> 디스패쳐서블릿이 응답을 생성하여 반환 -> 서블릿 필터의 후 처리 -> 클라이언트로 응답 반환
    

- [v]  Spring Security란?
  - Spring의 보안 프레임워크로, 애플리케이션에 인증 및 인가 기능을 손쉽게 추가할 수 있도록 도와줍니다.
  - Spring Security는 여러 필터체인을 통해 보안을 구현합니다. 각 필터는 특정 보안기능을 처리하게 됩니다.
  - 인증(Authentication) : 사용자의 신원을 확인하는 과정
  - 인가(Authorization) : 인증된 사용자의 역할과 접근 권한을 확인하는 과정

**JWT 기본 이해**

- [v]  JWT란 무엇인가요?
  - JWT(Json Web Token)은 JSON 객체를 사용하여 정보 또는 클레임을 안전하게 전송하는 컴팩트하고 독립적인 방식의 토큰입니다.
  - 주로 인증 및 권한 부여와 관련된 시나리오에서 널리 사용됩니다.
  - Header : 토큰 타입과 해싱 알고리즘 정보를 포함
  - Payload : 토큰에 담길 클레임을 포함
  - Signature : 토큰의 무결성을 보장하기 위해 생성된 서명

### Day 2 - 백엔드 배포하기

---
**테스트 완성**

- [ ]  백엔드 유닛 테스트 완성하기

**로직 작성**

- [ ]  백엔드 로직을 Spring Boot로
    - [ ]  회원가입 - /signup
        - [ ]  Request Message

           ```json
           {
               "username": "JIN HO",
               "password": "12341234",
               "nickname": "Mentos"
           }
           ```

        - [ ]  Response Message

           ```json
           {
               "username": "JIN HO",
               "nickname": "Mentos",
               "authorities": [
                       {
                               "authorityName": "ROLE_USER"
                       }
               ]		
           }
           ```

    - [ ]  로그인 - /sign
        - [ ]  Request Message

           ```json
           {
               "username": "JIN HO",
               "password": "12341234"
           }
           ```

        - [ ]  Response Message

           ```json
           {
               "token": "eKDIkdfjoakIdkfjpekdkcjdkoIOdjOKJDFOlLDKFJKL"
           }
           ```


**배포해보기**

- [ ]  AWS EC2 에 배포하기

**API 접근과 검증**

- [ ]  Swagger UI 로 접속 가능하게 하기

### Day 3 - 백엔드 개선하기

---
[Git 커밋 메시지 잘 쓰는 법 | GeekNews](https://news.hada.io/topic?id=9178&utm_source=slack&utm_medium=bot&utm_campaign=TQ595477U)

**AI-assisted programming**

- [ ]  AI 에게 코드리뷰 받아보기

**Refactoring**

- [ ]  피드백 받아서 코드 개선하기

**마무리**

- [ ]  AWS EC2 재배포하기
