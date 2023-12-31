# :pushpin: prj_uc : Project under Control
> **Proejct under Contorl! 프로젝트 관리 및 분석을 한 번에!**  
> 프로젝트 및 작업 관리를 통해 협업 및 업무 효율성을 향상을 추구하는 Spring MVC 기반 REST API 서비스  
> 본 서비스는 기업 내부에서 사용되는 웹 서비스로, 프로젝트 및 작업 관리를 위한 직관적이고 유연한 도구로, 팀 간 협업과 업무 효율성을 향상 시키는데 사용됩니다. <br/>
  
## <포트폴리오 순서>
1. [제작기간 및 참여인원](#1제작기간-및-참여인원) 
2. [개요](#2개요)  
3. [사용기술](#3사용기술)  
4. [ERD 설계](#4erd-설계)
5. [핵심기능](#5핵심기능)
6. [트러블 슈팅 & TIL](#6트러블-슈팅--til)
7. [회고](#7회고)
  
- [Git commit 메시지 컨벤션](#Git-commit-메시지-컨벤션)
- [API Reference](#api-reference)
  
## 1.제작기간 및 참여인원
- Phase 1 : 2023년 11월 16일 ~ 11월 24일 (완료)
- Phase 2 : 2023년 12월  1일 ~ 현재
  
- 개인 프로젝트
  
## 2.개요
**Proejct under Contorl! 프로젝트 관리 및 분석을 한 번에!**  
본 서비스는 기업 내부에서 사용되는 웹 서비스로, 프로젝트 및 작업 관리를 위한 직관적이고 유연한 도구로, 팀 간 협업과 업무 효율성을 향상 시키는데 사용됩니다. <br/>
  
## 3.사용기술
- 언어 및 프레임워크: ![Static Badge](https://img.shields.io/badge/Java-17-F58232) ![Static Badge](https://img.shields.io/badge/Spring_boot-3.1.1-6CB52C)<br/>
- 데이터베이스 및 테스트: ![Static Badge](https://img.shields.io/badge/h2-2.1.214-1021FF) ![Static Badge](https://img.shields.io/badge/Spring_Data_JPA-3.1.1-80E96E) ![Static Badge](https://img.shields.io/badge/JUnit-5.9.3-3F9B61)<br/>
- 보안, 배포 : ![Static Badge](https://img.shields.io/badge/Spring_Security-6.1.1-80E96E) ![Static Badge](https://img.shields.io/badge/Gradle-8.1.1-02303A) <br/>
  
## 4.ERD 설계
> ![image](https://github.com/upqnu/prj_uc/assets/101033614/fdcfc301-b0a5-44c2-bd16-2b684f2e277f)
  
<details>
<summary>ERD 설계 중요포인트 </summary>

- Member, Team의 관계는 다수 Member가 다수 Team의 구성원이 될 수 있는 N : M이므로 → <u>TeamSetting 엔티티(중간테이블)</u>를 생성하여 Member, Team과의 연관관계를 N:1, M:1 로 설정  
  
- TeamSetting 엔티티는 enum 타입의 InviteStatus를 설정하여 팀과 관련된 사용자의 권한을 4가지로 설정하는 역할을 수행  
   - INVITING ; 팀을 생성한 사용자에게만 주어짐 (팀장 권한)  
   - RECEIVED ; 팀장이 보낸 팀으로의 초대를 받은 사용자  
   - ACCEPTED ; 초대를 수락하여 팀원이 된 사용자  
   - REFUSED ; 초대를 거절한 사용자  
  
 - 하나의 팀이 하나의 칸반보드를 가질 수 있음.  
   - 따라서 칸반보드는 Team 엔티티의 필드로만 설정 (사실상, 칸반보드 = 팀)  
  
 - 팀을 만드는 사용자가 해당 팀의 팀장이 됨.  
   - 팀장만이 팀에 속하는 진행상황(Progress, 칸반보드에서 column)을 생성할 수 있다  
  
</details>
  
## 5.핵심기능

- 사용자(Member), 팀(Team), 작업상황(Progress), 티켓(Ticket) 대상으로 16개의 API 구현
  
<details>
<summary>1st phase (2023.11.16~11.24) ; 사용자 생성 및 JWT를 통한 인증, (프로젝트 진행할) 팀 생성 및 구성, 작업상황 및 티켓 구현을 통해  서비스 구현</summary>

1. 사용자 회원가입 및 <u>**JWT**를 통한 사용자 인증</u> 구현
    - 사용자 로그인과 동시에 액세스 토큰 발급 후, API 요청 헤더에 담아 사용자 인증
    - 액세스 토큰 유효시간(30분) 만료 시, 토큰 타입을 “refresh”로 입력하여 유효기간이 1주일로 연장된 새로운 액세스 토큰 발급
  
  
2. 팀 생성, 팀원 초대를 통한 팀 구성
    - <u>**팀 생성**</u> ; Team 및 TeamSetting 엔티티를 통해 팀의 정보를 구성
        - Member, Team의 관계는 다수 Member가 다수 Team의 구성원이 될 수 있는 N : M이므로  → TeamSetting 엔티티를 생성하여 Member, Team과의 연관관계를 N : 1, M : 1 로 설정
            - 관계형 데이터베이스에서 Member, Team 테이블은 연결할 TeamSetting 테이블을 엔티티로 생성한다.
        - TeamSetting 엔티티는 enum 타입의 InviteStatus를 설정하여 팀과 관련된 사용자의 권한을 4가지로 설정하는 역할을 수행
            - INVITING ; 팀을 생성한 사용자에게만 주어짐 (팀장 권한)
            - RECEIVED ; 팀장이 보낸 팀으로의 초대를 받은 사용자
            - ACCEPTED ; 초대를 수락하여 팀원이 된 사용자
            - REFUSED ; 초대를 거절한 사용자
        - 팀 생성 과정 중 팀명 등의 기본 정보는 Team 타입으로 저장되지만, 해당 팀의 구성원 정보는 TeamSetting 타입으로 저장됨.  
    - <u>**팀원으로 초대 및 수락/거절**</u> ; TeamSetting의 InviteStatus 필드(enum타입) 값에 따라 팀원을 초대, 초대장 수신, 초대를 수락, 초대를 거절한 팀원의 상태를 구분. 따라서 초대와 관련된 DB테이블을 별도로 만들 필요가 없음.
        - 팀장만이 다른 사용자를 팀원으로 초대할 수 있음
            - TeamSetting에서 InviteStatus.INVITING인 사용자만 초대가 가능하도록 구현
        - 특정 사용자가 특정 팀으로의 초대에 대해 수락 또는 거절
            - TeamSetting에서 InviteStatus.RECEIVED인 사용자만 초대에 대해 수락 또는 거절을 할 수 있음
            - InviteStatus는 수락 시 ACCEPTED, 거절 시 REFUSED로 변경됨
        - (본 프로젝트는 REST API 구현하므로, InviteStatus에 따라 초대와 관련된 액션을 구현하는 것은 프런트엔드에서 처리 가능)
  
  
3. 진행상황(Progress) / 티켓 (Ticket) ; 생성, 조회, 수정, 삭제
    - 팀 내부에 생성할 수 있는 진행상황(Progress)는 칸반보드에서 column과 같은 역할
    - 진행상황 내부에 생성할 수 있는 티켓(Ticket)은 칸반보드에서의 개별작업과 같은 역할
    - 진행상황/티켓 생성과 동시에 순서가 정해지며, 순서는 변경이 가능
  
  
4. 서버 구동과 동시에 dummy data 생성
    - ApplicationRunner 인터페이스를 구현한 DummyDataLoader 클래스, dummy로 생성되어야 할 객체의 정보들을 입력한 DummyDataService 클래스를 통해 Member, Team, TeamSetting, Progress, Ticket의 dummy data가 각각 20, 4, 15, 2, 5개가 서버 구동과 동시에 생성됨
</details>

<details>
<summary>2nd phase (2023.12. 1~현재) ; 메서드 모듈화를 통해 코드 중복 제거, Test code 작성, Swagger 도입</summary>
  
1. 메서드 모듈화를 통해 코드 중복 제거  
    - 1st phase에서 진행상황(Progress), 티켓(Ticket)의 Service 클래스에 작성된 메서드들은 내부에는 많은 중복이 존재. 이를 별도의 메서드로 작성하여 중복 제거.  
        - 중복되는 로직 : (1) 팀, 진행상황, 티켓의 존재 여부 (2) 로그인한 사용자의 팀내에서의 역할  
        - 중복 로직을 별도의 메서드로 작성하여 제거  
    - 개선 결과 : 총 9개의 비즈니스 로직 담당 메서드 별 최소 5라인, 최대 13라인의 코드 감소 및 가독성 개선  
</details>
  
## 6.트러블 슈팅 & TIL
> 
> 1. member, team 의 다대다 관계 (1) teamSetting 연결테이블로 풀어보기 (<a href="https://github.com/upqnu/prj_uc/wiki/member,%20team%20%EC%9D%98%20%EB%8B%A4%EB%8C%80%EB%8B%A4%20%EA%B4%80%EA%B3%84%20(1)%20teamSetting%20%EC%97%B0%EA%B2%B0%ED%85%8C%EC%9D%B4%EB%B8%94%EB%A1%9C%20%ED%92%80%EC%96%B4%EB%B3%B4%EA%B8%B0">클릭 & detail 확인</a>)
> 
> 2. member, team 의 다대다 관계 (2) teamSetting으로 초대 관련 기능을 쉽게 구현 (<a href="https://github.com/upqnu/prj_uc/wiki/member,%20team%20%EC%9D%98%20%EB%8B%A4%EB%8C%80%EB%8B%A4%20%EA%B4%80%EA%B3%84%20(2)%20teamSetting%EC%9C%BC%EB%A1%9C%20%EC%B4%88%EB%8C%80%20%EA%B4%80%EB%A0%A8%20%EA%B8%B0%EB%8A%A5%EC%9D%84%20%EC%89%BD%EA%B2%8C%20%EA%B5%AC%ED%98%84">클릭 & detail 확인</a>)
> 
> 3. 순환 참조 오류 ; 확실히 짚고 넘어가자 (<a href="https://github.com/upqnu/prj_uc/wiki/%EC%88%9C%ED%99%98%20%EC%B0%B8%EC%A1%B0%20%EC%98%A4%EB%A5%98%20;%20%ED%99%95%EC%8B%A4%ED%9E%88%20%EC%A7%9A%EA%B3%A0%20%EB%84%98%EC%96%B4%EA%B0%80%EC%9E%90%20(@JsonManagedReference,%20@JsonBackReference)">클릭 & detail 확인</a>)
>
> 4. 유닛테스트 간 순서 정하기…는 실패했지만, 유닛테스트는 대부분 성공! (<a href="https://github.com/upqnu/prj_uc/wiki/%EC%9C%A0%EB%8B%9B%ED%85%8C%EC%8A%A4%ED%8A%B8%20%EA%B0%84%20%EC%88%9C%EC%84%9C%20%EC%A0%95%ED%95%98%EA%B8%B0%E2%80%A6%EB%8A%94%20%EC%8B%A4%ED%8C%A8%ED%96%88%EC%A7%80%EB%A7%8C,%20%EC%9C%A0%EB%8B%9B%ED%85%8C%EC%8A%A4%ED%8A%B8%EB%8A%94%20%EB%8C%80%EB%B6%80%EB%B6%84%20%EC%84%B1%EA%B3%B5!">클릭 & detail 확인</a>)
  
## 7.회고
  <br>
<details>
<summary>백엔드 개발자에게 아무리 경험해도 쉽다고 느껴지지 않는 것이 바로 ERD라는 말을 들었는데 이 프로젝트에서 이를 제대로 체감할 수 있었다. 동시에 양방향 연관관계 및 연관관계의 주인 개념을 더 잘 이해할 수 있게 되었다.</summary>

- 프로젝트에서는 다수의 멤버가 다수의 팀에서 팀원이 될 수 있다. Member와 Team은 N : M 이므로 중간에 연결테이블을 만들어야 했는데 어떤 성격을 가진 연결테이블인지 정의하기가 어려웠다.
- TeamSetting이라는 중간테이블을 만들었다. Member, Team과 각각 양방향 참조하는 관계인데 여기에서 연관관계의 주인을 착각하여 mappedBy라는 속성을 잘못 설정하게 되었다. 곧 이를 감지하고 코드를 수정했지만, 양방향 연관관계에 대해서 학습이 필요하다는 생각이 들었다.
- 구글링을 통해 다음과 같이 명확히 이해할 수 있었다.
    - 연관관계의 주인은 테이블에 외래 키가 있는 곳으로 N:1에서 N쪽이라고 보면 된다.
    - 주인이 아니라면 `mappedBy 속성`을 사용해서 속성의 값으로 연관관계의 주인을 지정한다.
    - mappedBy의 뜻을 “나는 내 연관관계의 주인의 [~~~] 필드에 해당해!”로 해석한다면 이해가 어렵지 않다.
    - mappedBy 속성에 들어올 이름은, **연관관계 주인의 해당 속성의 필드명과 일치**해야 한다!
- (위 트러블 슈팅 1번 참고)
</details>
  <br>
<details>
<summary>N:M관계를 코드로 풀기 위해 생성한 연결테이블을 통해 필요 없는 DB테이블 생성을 방지할 수 있다는 값진 경험을 할 수 있었다.</summary>

- N:M 관계를 코드로 구현하기 위해 생성한 연결테이블인 TeamSetting이 연결 용도 외에 다른 역할을 하게 만들고 싶었다. 동시에 팀을 생성한 사용자가 다른 사용자를 팀원으로 초대하고, 초대받은 사용자는 이를 수락 또는 거절하는 기능을 구현하는 방향을 잡지 못하고 있었다.
- “TeamSetting”을 초대와 연관지어 사용해야겠다는 생각은 쉽게 떠올랐으나 코드 작성방향이 떠오르지 않아 1일 정도를 온전히 구글링 및 활용방안 구상에 사용했다.
- 초대는 발송 및 수신확인할 때만 사용되는 성질이 있기에, 초대(invitation) 테이블을 생성하는 것은 DB를 낭비하는 것으로 느꼈다. 대신 초대와 관련된 행위가 사용자의 어떤 ‘상태(status)’와 연관된다면 초대 테이블을 생성하지 않고도 구현이 가능하다는 판단을 하게 되었다.
- 몇 시간을 더 투자한 끝에 TeamSetting의 필드에 enum 타입의 InviteStatus를 4가지로 구분하는 것으로 코드를 작성할 수 있었다.
    - INVITING ; 팀을 생성한 사용자만 초대를 할 수 있다
    - RECEIVED ; 초대를 수신만 한 상태는 아직 팀원이 아니다
    - ACCEPTED ; 초대를 수락하면 팀원이 된다
    - REFUSED ; 초대를 거절하면 팀원이 아니다
- 특정 사용자가 특정 팀에서 어떤 역할을 하는지는 반드시 저장되어야 하는 정보이다. TeamSetting 클래스는 이를 저장하면서 초대와 관련된 상태를 지정하는 역할을 한다.
    ```json
    {
    	teamSettingId : 2,
    	teamId : 3,
    	memberId : 7,
    	InviteStatus : RECEIVED
    }
    
    /* 
    위 코드의 해석) “memberId 7번 사용자는 teamId 3번 팀에 대해서 ; 
    팀원으로 초대를 받았으나 아직 승낙이나 거절을 하지 않았다. 따라서 현재 아직 팀원이 아니다.”
    */
    ```
  
- 위와 같은 정보는 Member 테이블에도 저장할 수 있지만, Member 테이블에는 사용자의 기본적인 정보만 저장하고 - 자신과 관련된 팀들에 대한 정보는 TeamSetting 테이블에 저장하는 것이 더 효율적이라는 판단을 하게 되었다.
    - 이 프로젝트에서 Team은 하나의 프로젝트와 동일한 개념이며 / 프로젝트 운영을 위해 각 작업(Ticket)이 어떤 진행상황(Progress, 칸반보드에서 상태column 역할)에 속해있는지 설정하는 기능의 권한을 TeamSetting 을 통해 구현하려고 하였다.
    - 예를 들어, teamId 1번 팀의 작업 내용의 추가와 변경은 TeamSetting 테이블에서 [ teamId = 1 ]과 [ InviteStatus = INVITING 또는 ACCEPTED ]를 동시에 만족하는 경우에만 가능하게 할 수 있다.
- 위와 같이 구현하게 되면 단지 초대장의 내용, 초대에 대한 승락 또는 거절 여부만 저장하게 될 가능성이 높은 초대 테이블을 생성하지 않을 수 있다고 최종 결론짓게 되었다.
  
- (위 트러블 슈팅 2번 참고)
</details>
  <br>
<details>
<summary>순환 참조 에러가 발생하여 이를 해결하는 과정에서 에러의 원인을 정확하게 파악하고 문제를 해결하였다.</summary>

- 순환 참조 에러는 N:M 양방향 연관관계를 맺고 있는 Member, Team 엔티티와 이들 사이의 연결테이블인 TeamSetting 엔티티의 관계에서 발생할 수 있다.
- 각각의 객체를 조회할 때 연관된 두 엔티티 간의 무한 ‘직렬화’가 발행하여 무한 참조로 인한 Stack overflow가 발생하는 것이다.
- 따라서 부모인 Member, Team 내의 teamSettingList 필드에 @JsonManagedReference를 선언. 그러면 이 애너테이션이 지정된 Member, Team 엔티티는 JSON으로 직렬화될 때 참조된 TeamSetting엔티티까지 포함하여 직렬화.
- 또한 자식은 TeamSetting 내의 Member member, Team team 필드에 @JsonBackReference를 선언. 이 애너테이션이 지정된 TeamSetting 엔티티는 JSON으로 직렬화되지 않는다.
  
- (위 트러블 슈팅 3번 참고)
</details>
<br>
<details>
<summary>유닛테스트를 하려면 제대로, 충실하게 해야 한다는 것을 뼈저리게 깨달았다.</summary>

- "JUnit 테스트는 각각 독립적으로 실행됨. 각 테스트 메서드는 다른 테스트 메서드와 공유되는 상태를 가지지 않으며, 각 테스트 메서드 실행 전후로 테스트 환경은 초기화됨!"
- 위와 같은 JUnit 테스트의 기본을 망각하고, 테스트 메서드에서 생성되는 객체를 다른 테스트 메서드에 사용하기 위해서 `@Order` , `@BeforeEach`를 사용하려고 시도했음. 당연히 실패!
- 이에 테스트를 실행하기 위한 조건(given)을 테스트 클래스 내 별도의 메서드를 사용하여 작성하였음.
- 결론적으로 TeamController의 테스트 메서드의 대부분을 작성하여 그 결과는 성공이었음. 기본을 망각하다가 1주일 이상의 시간을 허비함. 시간을 아끼자.  
- (위 트러블 슈팅 4번 참고)
</details>
  
---
  
## Git commit 메시지 컨벤션
- `Feat` : 새로운 기능 추가
- `Fix` : 기능 개선 또는 수정, 버그 수정
- `Docs` : 문서 수정
- `Style` : 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
- `Refactor` : 코드 리펙토링
- `Test` : 테스트 코드, 리펙토링 테스트 코드 추가
- `Chore` : 빌드 업무 수정, 패키지 매니저 수정
  
## API Reference
### _Member_
<details>
<summary>회원가입 - click</summary>

#### Request
```javascript
  POST /api/members/sign-up
```
```http
Content-Type: application/json

{
    "name": "tester1",
    "password": "asdf1234",
    "email": "tester1@email.com",
    "authority": "ROLE_MEMBER"
}
```
#### Response
```http
HTTP/1.1 201
Content-Type: application/json

{
    "memberInfo": null,
    "status": 201,
    "message": "성공적으로 회원가입 되셨습니다."
}
```
</details>
<details>
<summary>로그인 & 액세스 토큰 발급 - click</summary>

#### Request
```javascript
  POST /api/members/sign-in
```
```http
Content-Type: application/json

{
    "name": "tester1",
    "password": "asdf1234"
}
```

#### Response
```http
    HTTP/1.1 200
    Content-Type: application/json

{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0ZXIxIiwiZXhwIjoxNzAwMzI5NTI4LCJpYXQiOjE3MDAzMjc3MjgsImF1dGhvcml0aWVzIjoiUk9MRV9NRU1CRVIiLCJuYW1lIjoidGVzdGVyMSJ9.gBERUQJ9zGkJcWdxehqw9MXCY7hRTR98CeXJZpsgvRU"
}
```
</details>
<details>
<summary>리프레시 토큰 발급 & 액세스 토큰 갱신 - click </summary>

#### Request
```javascript
  POST /api/members/refresh
```
```http
Content-Type: application/json

{
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0ZXIxIiwiZXhwIjoxNzAwMzExNzM4LCJpYXQiOjE3MDAzMDk5MzgsIm5hbWUiOiJ0ZXN0ZXIxIiwiYXV0aG9yaXRpZXMiOiJST0xFX01FTUJFUiJ9.XRxWNeFYMBq9_CDO2qVm_zRpC4-Uem2ytpSBt0GJVsM"
}
```

#### Response
```http
    HTTP/1.1 200
    Content-Type: application/json

{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0ZXIxIiwiZXhwIjoxNzAwMzExNzk1LCJpYXQiOjE3MDAzMDk5OTUsIm5hbWUiOiJ0ZXN0ZXIxIiwiYXV0aG9yaXRpZXMiOiJST0xFX01FTUJFUiJ9.olSJWEcCp0OQp8PAQmfoKFIYJeLIwfWv0Ox8F4LEis8"
}
```
</details>

### _Team_
<details>
<summary>팀 생성 - click </summary>

#### Request
```javascript
  POST /api/teams/create
```
```http
Content-Type: application/json

{
    "name": "first_team",
    "kanban": "first_kanban"
}
```

#### Response
```http
    HTTP/1.1 201
    Content-Type: application/json

{
    "status": 201,
    "message": "팀 first_team이 성공적으로 생성되었습니다."
}
```
</details>
<details>
<summary>팀 조회 - click </summary>

#### Request
```javascript
  GET /api/teams/{teamId}
```

| Path | Type   | Description             |
|:-----|:-------|:------------------------|
| `id` | `Long` | **Required**. Team's ID |

#### Response
```http
    HTTP/1.1 200
    Content-Type: application/json

{
    "createdAt": "2023-11-23T16:59:31.259798",
    "updatedAt": "2023-11-23T16:59:31.259798",
    "id": 1,
    "inviteStatus": "INVITING"
}
```
</details>
<details>
<summary>팀원으로 초대 - click </summary>

#### Request
```javascript
  POST /api/teams/{teamId}/invite
```

| Path | Type   | Description             |
|:-----|:-------|:------------------------|
| `id` | `Long` | **Required**. Team's ID |

Query Parameter

| Key               | Value type  | Value Description           |
|:------------------|:------------|:----------------------------|
| invitedMemberName | `String`    | **Required**. Member's name |


#### Response
```http
    HTTP/1.1 200
    Content-Type: text/plain;charset=UTF-8

    "member13님을 성공적으로 초대하였습니다."

```
</details>
<details>
<summary>팀원 초대에 대한 승락 또는 거절 - click </summary>

#### Request
```javascript
  POST /api/teams/{teamId}/invitation/{inviteeId}
```

| Path | Type   | Description             |
|:-----|:-------|:------------------------|
| `id` | `Long` | **Required**. Team's ID |

| Path | Type   | Description                       |
|:-----|:-------|:----------------------------------|
| `id` | `Long` | **Required**. Invited Member's ID |

Query Parameter

| Key    | Value type | Value Description           |
|:-------|:-----------|:----------------------------|
| accept | `boolean`  | **Required**. true or false |


#### Response
```http
    HTTP/1.1 200
    Content-Type: text/plain;charset=UTF-8

    "팀원 초대를 수락하셨습니다." or "팀원 초대를 거절하셨습니다."

```
</details>

### _Progress_
##### (각 팀마다 1개씩 나타나는 칸반보드의 Column과 같은 역할)

<details>
<summary>Progress 생성 - click </summary>

#### Request
```javascript
  POST /api/teams/{teamId}/progresses/create
```

| Path | Type   | Description             |
|:-----|:-------|:------------------------|
| `id` | `Long` | **Required**. Team's ID |

```http
Content-Type: application/json

{
    "name": "ToDo"
}
```

#### Response
```http
    HTTP/1.1 201
    Content-Type: application/json

{
    "status": 201,
    "message": "<ToDo2> 진행상황이 생성되었습니다."
}
```
</details>
<details>
<summary>Progress 조회 - click </summary>

#### Request
```javascript
  GET /api/teams/{teamId}/progresses/{progressId}
```

| Path | Type   | Description             |
|:-----|:-------|:------------------------|
| `id` | `Long` | **Required**. Team's ID |


| Path | Type   | Description                |
|:-----|:-------|:---------------------------|
| `id` | `Long` | **Required**. Progress' ID |

#### Response
```http
    HTTP/1.1 200
    Content-Type: application/json

{
    "createdAt": "2023-11-23T17:00:42.005935",
    "updatedAt": "2023-11-23T17:00:42.005935",
    "id": 1,
    "name": "QA",
    "numbering": 1,
    "team": {
        "createdAt": "2023-11-23T16:59:31.281974",
        "updatedAt": "2023-11-23T16:59:31.281974",
        "id": 1,
        "name": "team1",
        "kanban": "kanban1",
        "teamSettingList": [
            {
                "createdAt": "2023-11-23T16:59:31.259798",
                "updatedAt": "2023-11-23T16:59:31.259798",
                "id": 1,
                "inviteStatus": "INVITING"
            },
            {
                "createdAt": "2023-11-23T16:59:31.287628",
                "updatedAt": "2023-11-23T16:59:31.287628",
                "id": 5,
                "inviteStatus": "ACCEPTED"
            },
            {
                "createdAt": "2023-11-23T16:59:31.288001",
                "updatedAt": "2023-11-23T16:59:31.288001",
                "id": 6,
                "inviteStatus": "ACCEPTED"
            },
            {
                "createdAt": "2023-11-23T16:59:31.288853",
                "updatedAt": "2023-11-23T16:59:31.288853",
                "id": 7,
                "inviteStatus": "RECEIVED"
            },
            {
                "createdAt": "2023-11-23T16:59:31.289587",
                "updatedAt": "2023-11-23T16:59:31.289587",
                "id": 8,
                "inviteStatus": "REFUSED"
            }
        ]
    },
    "ticketList": [
        {
            "createdAt": "2023-11-23T17:01:51.900568",
            "updatedAt": "2023-11-23T17:01:51.900568",
            "id": 1,
            "title": "eee",
            "numbering": 1,
            "tag": "frontend",
            "personHour": 2.0,
            "dueDate": "2023-11-25T15:30:00",
            "memberId": 1
        }
    ]
}
```
</details>
<details>
<summary>Progress 삭제 - click </summary>

#### Request
```javascript
  DELETE /api/teams/{teamId}/progresses/{progressId}
```

| Path | Type   | Description             |
|:-----|:-------|:------------------------|
| `id` | `Long` | **Required**. Team's ID |

| Path | Type   | Description                |
|:-----|:-------|:---------------------------|
| `id` | `Long` | **Required**. Progress' ID |

#### Response
```http
    HTTP/1.1 200
    Content-Type: application/json

{
    "message": "진행상황 삭제가 완료되었습니다."
}
```
</details>
<details>
<summary>Progress 이름 수정 - click </summary>

#### Request
```javascript
  PUT /api/teams/{teamId}/progresses/{progressId}
```

| Path | Type   | Description             |
|:-----|:-------|:------------------------|
| `id` | `Long` | **Required**. Team's ID |

| Path | Type   | Description                |
|:-----|:-------|:---------------------------|
| `id` | `Long` | **Required**. Progress' ID |

```http
Content-Type: application/json

{
    "name": "Done"
}
```

#### Response
```http
    HTTP/1.1 200
    Content-Type: application/json

{
    "createdAt": "2023-11-22T16:51:05.203452",
    "updatedAt": "2023-11-22T16:51:46.012338",
    "id": 1,
    "name": "Done",
    "numbering": 1,
    "team": {
        "createdAt": "2023-11-22T16:50:14.573725",
        "updatedAt": "2023-11-22T16:50:14.573725",
        "id": 1,
        "name": "team1",
        "kanban": "kanban1",
        "teamSettingList": [
            {
                "createdAt": "2023-11-22T16:50:14.548695",
                "updatedAt": "2023-11-22T16:50:14.548695",
                "id": 1,
                "inviteStatus": "INVITING"
            },
            {
                "createdAt": "2023-11-22T16:50:14.578702",
                "updatedAt": "2023-11-22T16:50:14.578702",
                "id": 5,
                "inviteStatus": "ACCEPTED"
            },
            {
                "createdAt": "2023-11-22T16:50:14.579095",
                "updatedAt": "2023-11-22T16:50:14.579095",
                "id": 6,
                "inviteStatus": "ACCEPTED"
            },
            {
                "createdAt": "2023-11-22T16:50:14.579905",
                "updatedAt": "2023-11-22T16:50:14.579905",
                "id": 7,
                "inviteStatus": "RECEIVED"
            },
            {
                "createdAt": "2023-11-22T16:50:14.580602",
                "updatedAt": "2023-11-22T16:50:14.580602",
                "id": 8,
                "inviteStatus": "REFUSED"
            }
        ]
    },
    "ticketList": []
}
```
</details>
<details>
<summary>Progress 순서 수정 - click </summary>

#### Request
```javascript
  PATCH /api/teams/{teamId}/progresses/{progressId}
```

| Path | Type   | Description             |
|:-----|:-------|:------------------------|
| `id` | `Long` | **Required**. Team's ID |

| Path | Type   | Description                |
|:-----|:-------|:---------------------------|
| `id` | `Long` | **Required**. Progress' ID |

```http
Content-Type: application/json

{
    "numbering": "3"
}
```

#### Response
```http
    HTTP/1.1 200
    Content-Type: application/json

{
    "createdAt": "2023-11-22T20:12:10.813278",
    "updatedAt": "2023-11-22T20:14:44.100813",
    "id": 1,
    "name": "A",
    "numbering": 3,
    "team": {
        "createdAt": "2023-11-22T20:11:17.494867",
        "updatedAt": "2023-11-22T20:11:17.494867",
        "id": 1,
        "name": "team1",
        "kanban": "kanban1",
        "teamSettingList": [
            {
                "createdAt": "2023-11-22T20:11:17.470488",
                "updatedAt": "2023-11-22T20:11:17.470488",
                "id": 1,
                "inviteStatus": "INVITING"
            },
            {
                "createdAt": "2023-11-22T20:11:17.499473",
                "updatedAt": "2023-11-22T20:11:17.499473",
                "id": 5,
                "inviteStatus": "ACCEPTED"
            },
            {
                "createdAt": "2023-11-22T20:11:17.499835",
                "updatedAt": "2023-11-22T20:11:17.499835",
                "id": 6,
                "inviteStatus": "ACCEPTED"
            },
            {
                "createdAt": "2023-11-22T20:11:17.500566",
                "updatedAt": "2023-11-22T20:11:17.500566",
                "id": 7,
                "inviteStatus": "RECEIVED"
            },
            {
                "createdAt": "2023-11-22T20:11:17.501191",
                "updatedAt": "2023-11-22T20:11:17.501191",
                "id": 8,
                "inviteStatus": "REFUSED"
            }
        ]
    },
    "ticketList": []
}
```
</details>

### _Ticket_
<details>
<summary>Ticket 생성 - click </summary>

#### Request
```javascript
  POST /api/teams/{teamId}/progresses/{progressId}/tickets/create
```

| Path | Type   | Description             |
|:-----|:-------|:------------------------|
| `id` | `Long` | **Required**. Team's ID |

| Path | Type   | Description                |
|:-----|:-------|:---------------------------|
| `id` | `Long` | **Required**. Progress' ID |

| Path | Type   | Description               |
|:-----|:-------|:--------------------------|
| `id` | `Long` | **Required**. Ticket's ID |

```http
Content-Type: application/json

{
    "title": "VoC",
    "tag": "PM",
    "personHour": 2,
    "dueDate": "2023-11-23T17:30:00"
}
```

#### Response
```http
    HTTP/1.1 201
    Content-Type: application/json

{
    "status": 201,
    "message": "<VoC> 티켓이 생성되었습니다."
}
```
</details>
<details>
<summary>Ticket 삭제 - click </summary>

#### Request
```javascript
  DELETE /api/teams/{teamId}/progresses/{progressId}/tickets/{ticketId}
```

| Path | Type   | Description             |
|:-----|:-------|:------------------------|
| `id` | `Long` | **Required**. Team's ID |

| Path | Type   | Description                |
|:-----|:-------|:---------------------------|
| `id` | `Long` | **Required**. Progress' ID |

| Path | Type   | Description               |
|:-----|:-------|:--------------------------|
| `id` | `Long` | **Required**. Ticket's ID |

#### Response
```http
    HTTP/1.1 200
    Content-Type: application/json

{
    "message": "티켓 삭제가 완료되었습니다."
}
```
</details>
<details>
<summary>Ticket 수정 - click </summary>

#### Request
```javascript
  PUT /api/teams/{teamId}/progresses/{progressId}/tickets/{ticketId}
```

| Path | Type   | Description             |
|:-----|:-------|:------------------------|
| `id` | `Long` | **Required**. Team's ID |

| Path | Type   | Description                |
|:-----|:-------|:---------------------------|
| `id` | `Long` | **Required**. Progress' ID |

| Path | Type   | Description               |
|:-----|:-------|:--------------------------|
| `id` | `Long` | **Required**. Ticket's ID |

```http
Content-Type: application/json

{
    "title": "fff",
    "tag": "frontend",
    "personHour": 5,
    "dueDate": "2023-11-25T15:30:00",
    "memberId": 6
}
```

#### Response
```http
    HTTP/1.1 200
    Content-Type: application/json

{
    "createdAt": "2023-11-23T18:52:23.713056",
    "updatedAt": "2023-11-23T18:53:42.684485",
    "id": 1,
    "title": "fff",
    "numbering": 1,
    "tag": "frontend",
    "personHour": 5.0,
    "dueDate": "2023-11-25T15:30:00",
    "memberId": 6
}
```
</details>
<details>
<summary>Ticket 순서 수정 - click </summary>

#### Request
```javascript
  PATCH /api/teams/{teamId}/progresses/{progressId}/tickets/{ticketId}
```

| Path | Type   | Description             |
|:-----|:-------|:------------------------|
| `id` | `Long` | **Required**. Team's ID |

| Path | Type   | Description                |
|:-----|:-------|:---------------------------|
| `id` | `Long` | **Required**. Progress' ID |

| Path | Type   | Description               |
|:-----|:-------|:--------------------------|
| `id` | `Long` | **Required**. Ticket's ID |

```http
Content-Type: application/json

{
    "progressNum": 2,
    "ticketNum": 2
}
```

#### Response
```http
    HTTP/1.1 200
    Content-Type: application/json

{
    "createdAt": "2023-11-24T16:33:48.575983",
    "updatedAt": "2023-11-24T16:33:48.575983",
    "id": 2,
    "title": "ticket_b",
    "numbering": 2,
    "tag": "b",
    "personHour": 1.0,
    "dueDate": "2023-11-30T10:30:00",
    "memberId": 1
}
```
</details>
  


