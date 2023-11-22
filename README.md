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
### _Member_
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

### _Team_
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
<details>
<summary>팀원으로 초대 </summary>

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
<summary>팀원 초대에 대한 승락 또는 거절 </summary>

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
<summary>Progress 생성 </summary>

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
<summary>Progress 조회 </summary>

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
    "createdAt": "2023-11-21T23:25:08.71499",
    "updatedAt": "2023-11-21T23:25:08.71499",
    "id": 1,
    "name": "ToDo",
    "numbering": 1,
    "team": {
        "createdAt": "2023-11-21T23:04:33.389576",
        "updatedAt": "2023-11-21T23:04:33.389576",
        "id": 1,
        "name": "team1",
        "kanban": "kanban1",
        "teamSettingList": [
            {
                "createdAt": "2023-11-21T23:04:33.365887",
                "updatedAt": "2023-11-21T23:04:33.365887",
                "id": 1,
                "inviteStatus": "INVITING"
            },
            {
                "createdAt": "2023-11-21T23:04:33.394294",
                "updatedAt": "2023-11-21T23:04:33.394294",
                "id": 5,
                "inviteStatus": "ACCEPTED"
            },
            {
                "createdAt": "2023-11-21T23:04:33.39466",
                "updatedAt": "2023-11-21T23:04:33.39466",
                "id": 6,
                "inviteStatus": "ACCEPTED"
            },
            {
                "createdAt": "2023-11-21T23:04:33.395395",
                "updatedAt": "2023-11-21T23:04:33.395395",
                "id": 7,
                "inviteStatus": "RECEIVED"
            },
            {
                "createdAt": "2023-11-21T23:04:33.396089",
                "updatedAt": "2023-11-21T23:04:33.396089",
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
<summary>Progress 삭제 </summary>

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