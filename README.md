# prj_uc : Project under Control
  
## 프로젝트 관리 및 분석 시스템
- [개요](#개요)
- [Skils](#skils)
- [API Reference](#api-reference)
- [ERD](#ERD)
  
## 개요
**Proejct under Contorl! 프로젝트 관리 및 분석을 한 번에!** 본 서비스는 기업 내부에서 사용되는 웹 서비스로, 프로젝트 및 작업 관리를 위한 직관적이고 유연한 도구로, 팀 간 협업과 업무 효율성을 향상 시키는데 사용됩니다. <br/>
  
## Skils
언어 및 프레임워크: ![Static Badge](https://img.shields.io/badge/Java-17-darkgreen) ![Static Badge](https://img.shields.io/badge/Spring_boot-REST-darkgreen)<br/>
데이터베이스 및 테스트: ![Static Badge](https://img.shields.io/badge/h2-2.1.214-blue) ![Static Badge](https://img.shields.io/badge/JUnit-red) <br/>
배포 : ![Static Badge](https://img.shields.io/badge/Gradle-039BC6) <br/>
  
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
  
## ERD
> ![image](https://github.com/upqnu/prj_uc/assets/101033614/fdcfc301-b0a5-44c2-bd16-2b684f2e277f)
> - 특정 멤버(member)가 어떤 팀(team)에서는 팀장, 다른 팀에서는 팀원의 역할을 할 수 있다. 그래서 member : team = n : m의 관계이다.
>   - 따라서 TeamSetting을 member, team 사이에 두어 n:m 관계를 1:n, m:1 관계로 나누면서
>   - TeamSetting의 InviteStatus 필드의 값에 따라 팀장, 팀원 여부를 판단하게 하였다.
> - 하나의 팀이 하나의 칸반보드를 가질 수 있다. 따라서 칸반보드는 Team 엔티티의 필드로만 설정하였다. (사실상 칸반보드 = 팀의 개념)
> - 팀을 만드는 사용자가 해당 팀의 팀장이 된다. 팀장만이 팀에 속하는 진행상황(Progress, 칸반보드에서 column)을 생성할 수 있다.
  
