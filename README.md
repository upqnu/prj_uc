# prj_uc : Project under Control
## 프로젝트 관리 및 분석 시스템
- [개요](#개요)
- [Skils](#skils)
- [Installation](#Installation)
- [Running Tests](#running-tests)
- [API Reference](#api-reference)
- [프로젝트 진행 및 이슈 관리](#프로젝트-진행-및-이슈-관리)
- [ERD](#ERD)
- [구현과정(설계 및 의도)](#구현과정(설계-및-의도))
- [TIL 및 회고](#til-및-회고)
- [Authors](#authors)
## 개요
**Proejct under Contorl! 프로젝트 관리 및 분석을 한 번에!** 본 서비스는 기업 내부에서 사용되는 웹 서비스로, 프로젝트 및 작업 관리를 위한 직관적이고 유연한 도구로, 팀 간 협업과 업무 효율성을 향상 시키는데 사용됩니다. <br/>

## Skils
언어 및 프레임워크: ![Static Badge](https://img.shields.io/badge/Java-17-darkgreen) ![Static Badge](https://img.shields.io/badge/Spring_boot-REST-darkgreen)<br/>
데이터베이스 및 테스트: ![Static Badge](https://img.shields.io/badge/h2-2.1.214-blue) ![Static Badge](https://img.shields.io/badge/JUnit-red) <br/>
배포 : ![Static Badge](https://img.shields.io/badge/Gradle-039BC6) <br/>

## API Reference
### Member
<details>
<summary>회원가입</summary>

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
<summary>로그인 & 액세스 토큰 발급 </summary>

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
<summary>리프레시 토큰 발급 & 액세스 토큰 갱신 </summary>

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

### Team
<details>
<summary>팀 생성 </summary>

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