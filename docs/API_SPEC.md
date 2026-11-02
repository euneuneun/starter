[← README로 돌아가기](../README.md)

## 📑 API 명세서

### 공통 사항

| 항목 | 내용 |
| --- | --- |
| Content-Type | `application/json` |
| 인증 | 로그인 시 발급되는 세션 쿠키 `JSESSIONID` (회원가입·로그인 제외) |
| 날짜 형식 | `yyyy-MM-dd'T'HH:mm:ss` |

<details>
<summary><b>Enum 정의</b></summary>

**관리자 역할 (`AdminRole`)**

| 값 | 설명 |
| --- | --- |
| `SUPER_ADMIN` | 슈퍼 관리자 |
| `OPERATION_ADMIN` | 운영 관리자 |
| `CS_ADMIN` | CS 관리자 |

**관리자 상태 (`AdminStatus`)**

| 값 | 설명 |
| --- | --- |
| `PENDING` | 승인 대기 |
| `ACTIVATE` | 계정 활성 |
| `DEACTIVATE` | 계정 비활성 |
| `SUSPENDED` | 계정 정지 |
| `REJECTED` | 계정 거부 |

**고객 상태 (`status`)**

| 값 | 설명 |
| --- | --- |
| `ACTIVE` | 활성 |
| `INACTIVE` | 비활성 |
| `SUSPENDED` | 정지 |

**상품 상태 (`status`)**

| 값 | 설명 |
| --- | --- |
| `ON_SALE` | 판매중 |
| `OUT_OF_STOCK` | 품절 |
| `DISCONTINUED` | 단종 |

**주문 상태 (`status`)** — 상태 변경 순서: 준비중 → 배송중 → 배송완료

| 값 | 설명 |
| --- | --- |
| `PREPARING` | 준비중 |
| `SHIPPING` | 배송중 |
| `DELIVERED` | 배송완료 |
| `CANCELLED` | 취소됨 |

`CANCELLED`는 위 순서와 별개의 분기 상태로, `PREPARING` 단계에서만 전이할 수 있습니다. 배송이 시작된 주문은 취소되지 않습니다.

</details>

---

## 🔐 관리자 / 인증

| Method | Endpoint                    | 기능 |
| :---: |-----------------------------| --- |
| `POST` | `/admins/signup`            | 관리자 회원가입 |
| `POST` | `/admins/login`             | 관리자 로그인 |
| `POST` | `/admins/logout`            | 관리자 로그아웃 |
| `GET` | `/admins`                   | 관리자 리스트 조회 |
| `GET` | `/admins/{adminId}`         | 관리자 상세 조회 |
| `PATCH` | `/admins/{adminId}`         | 관리자 정보 수정 |
| `DELETE` | `/admins/{adminId}`         | 관리자 삭제 |
| `GET` | `/admins/me`                | 내 프로필 조회 |
| `PATCH` | `/admins/me`                | 내 프로필 수정 |
| `PATCH` | `/admins/me/password`       | 비밀번호 변경 |
| `PATCH` | `/admins/{adminId}/role`    | 관리자 역할 변경 |
| `PATCH` | `/admins/{adminId}/status`  | 관리자 상태 변경 |


<details>
<summary><code>POST</code> <b>/admins/signup</b> — 관리자 회원가입</summary>

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 요청 데이터 형식 |

**Request Body**

| 필드         | 타입 | 필수 | 설명 |
|--------------| --- | :---: | --- |
| `adminName`  | String | ✅ | 관리자명 |
| `adminEmail` | String | ✅ | 관리자 이메일 (중복 불가) |
|`password`   | String | ✅ | 관리자 비밀번호 (최소 8자 이상) |
| `adminPhone` | String | ✅ | 관리자 전화번호 (`010-XXXX-XXXX`)|
| `role`       | String | ✅ | 관리자 역할 |

```json
{
  "adminName":"테스트",
  "adminEmail": "2@naver.com",
  "password": "12345678",
  "adminPhone": "010-8555-6786",
  "role": "SUPER_ADMIN"
}
```

**Response Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `message` | String | 전달 메시지 |

```json
{
  "message": "관리자 신청이 완료되었습니다."
}
```

**Error Response** — 중복 이메일, 형식 오류, 필수 값 누락

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request",
  "path": "/admins/signup"
}
```

</details>

<details>
<summary><code>POST</code> <b>/admins/login</b> — 관리자 로그인</summary>

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 요청 데이터 형식 |

**Request Body**

| 필드         | 타입 | 필수 | 설명 |
|--------------| --- | :---: | --- |
| `adminEmail` | String | ✅ | 관리자 이메일 |
| `password`   | String | ✅ | 관리자 비밀번호 (최소 8자 이상) |

```json
{
  "adminEmail": "example@gmail.com",
  "password": "12345678a!"
}
```

**Response Headers**

| Key | 타입 | 설명 |
| --- | --- | --- |
| `Set-Cookie` | String | 서버 세션 관리를 위한 쿠키 (`JSESSIONID`) |

```
Set-Cookie: JSESSIONID=AbCdEfGhIjKlMnOpQrStUvWxYz...
```

**Response Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `message` | String | 전달 메시지 |

```json
{
  "message": "로그인 성인에 성공하였습니다."
}
```

**Error Response** — 이메일 또는 비밀번호 불일치, 계정 승인대기 중, 계정 신청 거부됨, 계정 정지됨, 계정 비활성화됨

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request",
  "path": "/admins/login"
}
```

</details>

<details>
<summary><code>POST</code> <b>/admins/logout</b> — 관리자 로그아웃</summary>

**Request Headers**

| Key | 타입 | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Cookie` | String | ✅ | 로그인 시 발급받은 세션 쿠키 |

```
Cookie: JSESSIONID=AbCdEfGhIjKlMnOpQrStUvWxYz...
```

**Request Body** — 없음

**Response Headers**

| Key | 타입 | 설명 |
| --- | --- | --- |
| `Set-Cookie` | String | 만료 처리된 세션 쿠키 (쿠키 삭제) |

**Response Body** 


| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `message` | String | 전달 메시지 |

```json
{
  "message": "로그아웃 되었습니다."
}
```
**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request",
  "path": "/admins/logout"
}
```

</details>

<details>
<summary><code>GET</code> <b>/admins</b> — 관리자 리스트 조회</summary>

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 데이터 형식 |

**Request Body** — 없음

**Response Body**

| 필드          | 타입 | 설명 |
|---------------| --- | --- |
| `adminId`     | Long | 관리자 고유 식별자 |
| `adminName`   | String | 관리자명 |
| `adminEmail`  | String | 관리자 이메일 |
| `adminPhone`  | String | 관리자 전화번호 |
| `role`        | String | 관리자 역할 |
| `adminStatus` | String | 관리자 상태 |
| `createdAt`   | LocalDateTime | 관리자 가입 일시 |
| `activateAt`  | LocalDateTime | 관리자 승인 일시 |
| `currentPage`  | Integer | 현재 페이지 번호(1부터 시작) |
| `pageSize`  | Integer | 한 페이지당 조회할 데이터 개수 |
| `totalElements`  | Long | 전체 검색 결과 데이터 총 개수 |
| `totalPages`  | Integer | 전체 페이지 총수 |

```json
{
  "adminList": [
    {
      "adminId": 3,
      "adminName": "테스트",
      "adminEmail": "2@naver.com",
      "adminPhone": "010-8555-6786",
      "role": "SUPER_ADMIN",
      "adminStatus": "PENDING",
      "createdAt": "2026-11-01T16:50:51.897",
      "activatedAt": null
    },
    {
      "adminId": 2,
      "adminName": "테스트",
      "adminEmail": "1@naver.com",
      "adminPhone": "010-8565-6786",
      "role": "SUPER_ADMIN",
      "adminStatus": "PENDING",
      "createdAt": "2026-11-01T16:50:44.220347",
      "activatedAt": null
    }
  ],
  "currentPage": 1,
  "pageSize": 10,
  "totalElements": 3,
  "totalPages": 1
}
```

**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request",
  "path": "/admins"
}
```

</details>

<details>
<summary><code>GET</code> <b>/admins/{adminId}</b> — 관리자 상세 조회</summary>

**Path Parameter**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `adminId` | Long | 관리자 고유 식별자 |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 데이터 형식 |

**Response Body**

| 필드          | 타입 | 설명 |
|---------------| --- | --- |
| `adminName`   | String | 관리자명 |
| `adminEmail`  | String | 관리자 이메일 (중복 불가) |
| `adminPhone`  | String | 관리자 전화번호 (`010-XXXX-XXXX`) |
| `role`        | String | 관리자 역할 |
| `adminStatus` | String | 관리자 상태 |
| `createdAt`   | LocalDateTime | 관리자 계정 생성 일시 |
| `activatedAt` | LocalDateTime | 관리자 승인 일시 |

```json
{
  "adminName": "테스트",
  "adminEmail": "2@naver.com",
  "adminPhone": "010-8555-6786",
  "role": "SUPER_ADMIN",
  "adminStatus": "PENDING",
  "createdAt": "2026-11-01T16:50:51.897",
  "activatedAt": null
}
```

**Error Response** — 중복 이메일, 형식 오류, 필수 값 누락

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request",
  "path": "/admins/{adminId}"
}
```

</details>

<details>
<summary><code>PATCH</code> <b>/admins/{adminId}</b> — 관리자 정보 수정</summary>

**Path Parameter**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `adminId` | Long | 관리자 고유 식별자 |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 데이터 형식 |

**Request Body**

| 필드         | 타입 | 설명 |
|--------------| --- | --- |
| `adminName`  | String | 관리자 이름 |
| `adminEmail` | String | 관리자 이메일 (중복 불가) |
| `adminPhone` | String | 관리자 전화번호 (`010-XXXX-XXXX`) |

```json
{
  "adminName": "TestMk2",
  "adminEmail": "99@naver.com",
  "adminPhone": "010-8999-9565"
}
```

**Response Body**

| 필드         | 타입 | 설명                   |
|--------------| --- |------------------------|
| `adminName`  | String | 수정된 관리자명        |
| `adminEmail` | String | 수정된 관리자 이메일   |
| `adminPhone` | String | 수정된 관리자 전화번호 |
| `createdAt`  | LocalDateTime | 관리자 계정 생성 일시  |
| `updatedAt`  | LocalDateTime | 관리자 정보 수정 일시  |

```json
{
  "adminName": "TestMk2",
  "adminEmail": "99@naver.com",
  "adminPhone": "010-8999-9565",
  "createdAt": "2026-11-01T16:50:44.220347",
  "updatedAt": "2026-11-01T17:18:17.878991"
}
```

**Error Response** — 중복 이메일, 형식 오류, 필수 값 누락

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request",
  "path": "/admins/{adminId}"
}
```

</details>

<details>
<summary><code>DELETE</code> <b>/admins/{adminId}</b> — 관리자 삭제</summary>

**Path Parameter**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `adminId` | Long | 관리자 고유 식별자 |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 데이터 형식 |

**Request Body** — 없음

**Response Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `message` | String | 전달 메시지 |

```json
{
  "message": "삭제에 성공하였습니다."
}
```

**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request",
  "path": "/admins/{adminId}"
}
```

</details>

<details>
<summary><code>GET</code> <b>/admins/me</b> — 내 프로필 조회</summary>

**Path Parameter** - 없음


**Request Headers**

| Key            | Value | 필수 | 설명                          |
|----------------| -- | :---: |-------------------------------|
| `Content-Type` | `application/json` | ✅ | 데이터 형식                   |
| `Cookie`       | `JSESSIONID=AbCdEfGhIjKlMnOpQrStUvWxYz...' | ✅ | 로그인 시 발급 받은 세션 쿠키 |

```
Cookie: JSESSIONID=AbCdEfGhIjKlMnOpQrStUvWxYz...
```

**Request Body** — 없음

**Response Body**

| 필드         | 타입 | 설명 |
|--------------| --- | --- |
| `adminName`  | String | 관리자명 |
| `adminEmail` | String | 관리자 이메일 (중복 불가) |
| `adminPhone` | String | 관리자 전화번호 (`010-XXXX-XXXX`) |
| `createdAt`   | LocalDateTime | 관리자 계정 생성 일시 |
| `activatedAt` | LocalDateTime | 관리자 승인 일시 |
```json
{
  "adminName": "테스트",
  "adminEmail": "0@naver.com",
  "adminPhone": "010-8965-6786",
  "createdAt": "2026-11-01T16:50:19.307874",
  "activatedAt": null
}
```

**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request",
  "path": "/admins/me"
}
```

</details>

<details>
<summary><code>PATCH</code> <b>/admins/me</b> — 내 프로필 수정</summary>

**Path Parameter** - 없음

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 데이터 형식 |
| `Cookie`       | `JSESSIONID=AbCdEfGhIjKlMnOpQrStUvWxYz...' | ✅ | 로그인 시 발급 받은 세션 쿠키 |

```
Cookie: JSESSIONID=AbCdEfGhIjKlMnOpQrStUvWxYz...
```

**Request Body**

| 필드         | 타입 | 설명 |
|--------------| --- | --- |
| `adminName`  | String | 변경할 관리자명 |
| `adminEmail` | String | 변경할 관리자 이메일 (중복 불가) |
| `adminPhone` | String | 변경할 관리자 전화번호 (`010-XXXX-XXXX`) |

```json
{
  "admibName": "관리자 이름",
  "adminEmail": "example@gmail.com",
  "adminPhone": "010-XXXX-XXXX"
}
```

**Response Body**

| 필드         | 타입 | 설명                   |
|--------------| --- |------------------------|
| `adminName`  | String | 변경된 관리자명        |
| `adminEmail` | String | 변경된 관리자 이메일   |
| `adminPhone` | String | 변경된 관리자 전화번호 |
| `createdAt`  | LocalDateTime | 관리자 계정 생성 일시  |
| `updatedAt`  | LocalDateTime | 내 정보 수정 일시      |

```json
{
  "adminName": "정재원",
  "adminEmail": "3@naver.com",
  "adminPhone": "010-8889-9968",
  "createdAt": "2026-11-01T16:50:19.307874",
  "updatedAt": "2026-11-01T17:05:32.045439"
}
```

**Error Response** — 중복 이메일, 형식 오류, 필수 값 누락

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request",
  "path": "/admins/me"
}
```

</details>

<details>
<summary><code>PATCH</code> <b>/admins/me/password</b> — 비밀번호 변경</summary>

**Path Parameter** - 없음

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 데이터 형식 |
| `Cookie`       | `JSESSIONID=AbCdEfGhIjKlMnOpQrStUvWxYz...' | ✅ | 로그인 시 발급 받은 세션 쿠키 |

```
Cookie: JSESSIONID=AbCdEfGhIjKlMnOpQrStUvWxYz...
```

**Request Body**

| 필드                 | 타입 | 설명                                        |
|----------------------| --- |---------------------------------------------|
| `oldPassword`        | String | 현재 관리자 비밀번호 (최소 8자 이상)        |
| `newPassword`        | String | 변경할 관리자 비밀번호 (최소 8자 이상)      |
| `newPasswordConfirm` | String | 변경할 관리자 비밀번호 확인 (최소 8자 이상) |
```json
{
  "oldPassword": "12345678",
  "newPassword": "123456789",
  "newPasswordConfirm" : "123456789"
}
```

**Response Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `message` | String | 전달 메시지 |

```json
{
  "message": "비밀번호 변경에 성공하였습니다."
}
```

**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request",
  "path": "/admins/me/password"
}
```

</details>

<details>
<summary><code>PATCH</code> <b>/admins/{adminId}/role</b> — 관리자 역할 변경</summary>

**Path Parameter**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `adminId` | Long | 관리자 고유 식별자 |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 데이터 형식 |

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
| --- | --- | :---: | --- |
| `role` | String | ✅ | 변경할 관리자 역할 |

```json
{
  "role": "CS_ADMIN"
}
```

**Response Body**

| 필드         | 타입   | 설명                       |
|--------------|--------|----------------------------|
| `adminId`    | Long   | 관리자  고유 식별자 |
| `adminName`  | String | 관리자명            |
| `role`       | String | 변경된 관리자 역할         |
|  `updatedAt` | LocalDateTime | 관리자 역할 수정 일시      |      
```json
{
  "adminId": 2,
  "adminName": "테스트",
  "role": "CS_ADMIN",
  "updatedAt": "2026-11-01T16:50:44.220347"
}
```

**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request",
  "path": "/admins/{adminId}/role"
}
```

</details>

<details>
<summary><code>PATCH</code> <b>/admins/{adminId}/status</b> — 관리자 상태 변경</summary>

**Path Parameter**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `adminId` | Long | 관리자 고유 식별자 |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 데이터 형식 |

**Request Body**

| 필드          | 타입 | 설명                                   |
|---------------| --- |----------------------------------------|
| `adminStatus` | String | 변경할 관리자 상태                     |
| `rejectReason` | String | 거부 시 사유 입력(거부 시엔 입력 필수) |

```json
{
  "adminStatus": "REJECTED",
  "rejectReason": "테스트 입니다."
}
```

**Response Body**

| 필드          | 타입          | 설명                  |
|---------------|---------------|-----------------------|
| `adminId`     | Long          | 관리자 고유 식별자    |
| `adminName`   | String        | 관리자명              |
| `adminStatus` | String        | 변경된 관리자 상태    |
| `updatedAt`   | LocalDateTime | 관리자 상태 수정 일시 |

```json
{
  "adminId": 3,
  "adminName": "테스트",
  "adminStatus": "REJECTED",
  "updatedAt": "2026-11-01T16:50:51.897"
}
```

**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request",
  "path": "/admins/{adminId}/status"
}
```

</details>

---


## 👥 고객 관리

| Method | Endpoint | 기능 |
| :---: | --- | --- |
| `GET` | `/customers` | 고객 리스트 조회 |
| `GET` | `/customers/{id}` | 고객 상세 조회 |
| `PATCH` | `/customers/{id}` | 고객 정보 수정 |
| `PATCH` | `/customers/{id}/status` | 고객 상태 변경 |
| `DELETE` | `/customers/{id}` | 고객 삭제 (논리 삭제) |

<details>
<summary><code>GET</code> <b>/customers</b> — 고객 리스트 조회</summary>

**Query String**

```text
?page=1&size=10&sortBy=createdAt&sortDirection=desc
```

| 키 | 타입 | 설명 |
| --- | --- | --- |
| `keyword` | String | 고객명 또는 이메일 부분 검색 (선택) |
| `page` | Integer | 페이지 번호 (기본값 1, 1 이상) |
| `size` | Integer | 페이지당 개수 (기본값 10, 1 이상) |
| `sortBy` | String | `name`, `email`, `createdAt` (기본값 `createdAt`) |
| `sortDirection` | String | `asc`, `desc` (기본값 `desc`) |
| `status` | String | 고객 상태 필터 (`ACTIVE`, `INACTIVE`, `SUSPENDED`, 선택) |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 관리자 로그인 세션 ID |

**Request Body** — 없음

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `content` | Array | 조회된 고객 목록 (논리 삭제된 고객 제외) |
| `content[].customerId` | Long | 고객 고유 식별자 |
| `content[].customerName` | String | 고객 이름 |
| `content[].customerEmail` | String | 고객 이메일 |
| `content[].customerPhone` | String | 고객 전화번호 (null 가능) |
| `content[].customerStatus` | String | 고객 상태 |
| `content[].createdAt` | LocalDateTime | 고객 가입일 |
| `page` | Integer | 현재 페이지 번호 |
| `size` | Integer | 페이지당 개수 |
| `totalElements` | Long | 검색 및 필터 조건에 해당하는 고객 수 |
| `totalPages` | Integer | 전체 페이지 수 |

```json
{
  "content": [
    {
      "customerId": 2,
      "customerName": "수키",
      "customerEmail": "sooky@sparta.com",
      "customerPhone": "010-7895-4564",
      "customerStatus": "ACTIVE",
      "createdAt": "2026-10-27T18:54:49"
    },
    {
      "customerId": 1,
      "customerName": "전영주",
      "customerEmail": "youngju@sparta.com",
      "customerPhone": "010-1234-5678",
      "customerStatus": "ACTIVE",
      "createdAt": "2026-10-27T18:52:17"
    }
  ],
  "page": 1,
  "size": 10,
  "totalElements": 2,
  "totalPages": 1
}
```

**Error Response** — `400`, `401`

`400 Bad Request` — 잘못된 조회 조건

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request",
  "path": "/customers"
}
```

`401 Unauthorized` — 관리자 로그인 세션 없음

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Unauthorized",
  "path": "/customers"
}
```

</details>

<details>
<summary><code>GET</code> <b>/customers/{id}</b> — 고객 상세 조회</summary>

**Path Parameter**

| 키 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 조회할 고객 ID |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 관리자 로그인 세션 ID |

**Request Body** — 없음

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `customerId` | Long | 고객 고유 식별자 |
| `customerName` | String | 고객 이름 |
| `customerEmail` | String | 고객 이메일 |
| `customerPhone` | String | 고객 전화번호 (null 가능) |
| `customerStatus` | String | 고객 상태 |
| `createdAt` | LocalDateTime | 고객 가입일 |

```json
{
  "customerId": 1,
  "customerName": "전영주",
  "customerEmail": "youngju@sparta.com",
  "customerPhone": "010-1234-5678",
  "customerStatus": "ACTIVE",
  "createdAt": "2026-10-27T18:52:17"
}
```

**Error Response** — `400`, `401`, `404`

`401 Unauthorized` — 관리자 로그인 세션 없음

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Unauthorized",
  "path": "/customers/1"
}
```

`404 Not Found` — 고객이 존재하지 않거나 논리 삭제된 경우

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Resource Not Found",
  "path": "/customers/999"
}
```

</details>

<details>
<summary><code>PATCH</code> <b>/customers/{id}</b> — 고객 정보 수정</summary>

**Path Parameter**

| 키 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 수정할 고객 ID |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 요청 데이터 형식 |
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 관리자 로그인 세션 ID |

**Request Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `customerName` | String | 수정할 고객 이름 (선택, 공백 불가, 최대 100자) |
| `customerEmail` | String | 수정할 고객 이메일 (선택, 공백 불가, 최대 255자) |
| `customerPhone` | String | 수정할 고객 전화번호 (선택, 공백 불가, 최대 15자) |

```json
{
  "customerName": "영주",
  "customerEmail": "0zoo@sparta.com",
  "customerPhone": "010-4541-1212"
}
```

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `customerId` | Long | 고객 고유 식별자 |
| `customerName` | String | 수정된 고객 이름 |
| `customerEmail` | String | 수정된 고객 이메일 |
| `customerPhone` | String | 수정된 고객 전화번호 |
| `customerStatus` | String | 고객 상태 |
| `createdAt` | LocalDateTime | 고객 가입일 |
| `updatedAt` | LocalDateTime | 고객 정보 수정일 |

```json
{
  "customerId": 1,
  "customerName": "영주",
  "customerEmail": "0zoo@sparta.com",
  "customerPhone": "010-4541-1212",
  "customerStatus": "ACTIVE",
  "createdAt": "2026-10-27T18:52:17",
  "updatedAt": "2026-10-27T19:06:11"
}
```

**Error Response** — `400`, `401`, `404`

`400 Bad Request` — 유효하지 않은 수정 데이터

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request",
  "path": "/customers/1"
}
```

`401 Unauthorized` — 관리자 로그인 세션 없음

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Unauthorized",
  "path": "/customers/1"
}
```

`404 Not Found` — 고객이 존재하지 않거나 논리 삭제된 경우

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Resource Not Found",
  "path": "/customers/999"
}
```

</details>

<details>
<summary><code>PATCH</code> <b>/customers/{id}/status</b> — 고객 상태 변경</summary>

**Path Parameter**

| 키 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 상태를 변경할 고객 ID |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 요청 데이터 형식 |
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 관리자 로그인 세션 ID |

**Request Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `customerStatus` | String | 변경할 고객 상태 (`ACTIVE`, `INACTIVE`, `SUSPENDED`, 필수) |

```json
{
  "customerStatus": "SUSPENDED"
}
```

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `customerId` | Long | 고객 고유 식별자 |
| `customerStatus` | String | 변경된 고객 상태 |
| `updatedAt` | LocalDateTime | 상태 변경일 |

```json
{
  "customerId": 1,
  "customerStatus": "SUSPENDED",
  "updatedAt": "2026-10-27T19:18:25"
}
```

**Error Response** — `400`, `401`, `404`

`400 Bad Request` — 상태값이 없거나 유효하지 않은 경우

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request",
  "path": "/customers/1/status"
}
```

`401 Unauthorized` — 관리자 로그인 세션 없음

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Unauthorized",
  "path": "/customers/1/status"
}
```

`404 Not Found` — 고객이 존재하지 않거나 논리 삭제된 경우

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Resource Not Found",
  "path": "/customers/999/status"
}
```

</details>

<details>
<summary><code>DELETE</code> <b>/customers/{id}</b> — 고객 삭제</summary>

**Path Parameter**

| 키 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 삭제할 고객 ID |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 관리자 로그인 세션 ID |

**Request Body** — 없음

**Response** `200 OK`

고객을 DB에서 물리적으로 삭제하지 않고 논리 삭제 처리합니다.

삭제된 고객은 목록에서 제외되며, 상세 조회 및 수정 시 조회되지 않은 고객으로 처리합니다.

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `message` | String | 고객 삭제 처리 완료 메시지 |

```json
{
  "message": "요청하신 고객 정보가 삭제되었습니다."
}
```

**Error Response** — `400`, `401`, `404`

`401 Unauthorized` — 관리자 로그인 세션 없음

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Unauthorized",
  "path": "/customers/1"
}
```

`404 Not Found` — 고객이 존재하지 않거나 이미 논리 삭제된 경우

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Resource Not Found",
  "path": "/customers/999"
}
```

</details>

---


## 📦 상품 관리

| Method | Endpoint | 기능 |
| :---: | --- | --- |
| `POST` | `/products` | 상품 등록 |
| `GET` | `/products` | 상품 리스트 조회 |
| `GET` | `/products/{id}` | 상품 상세 조회 |
| `PATCH` | `/products/{id}` | 상품 정보 수정 |
| `PATCH` | `/products/{id}/stock` | 상품 재고 변경 |
| `PATCH` | `/products/{id}/status` | 상품 상태 변경 |
| `DELETE` | `/products/{id}` | 상품 삭제 (논리 삭제) |

<details>
<summary><code>POST</code> <b>/products</b> — 상품 등록</summary>

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 요청 데이터 형식 |
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 관리자 로그인 세션 ID |

**Request Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `productName` | String | 상품명 (필수, 공백 불가, 100자 이하) |
| `productCategory` | String | 상품 카테고리 (필수, 공백 불가, 15자 이하) |
| `price` | Long | 상품 가격 (필수, 0원 이상) |
| `stock` | Integer | 상품 재고 (필수, 0개 이상) |
| `productStatus` | String | 상품 상태 (선택, `DISCONTINUED` 지정 시 단종 처리) |

```json
{
  "productName": "레고세트",
  "productCategory": "완구/취미",
  "price": 89000,
  "stock": 32
}
```

**Response** `201 Created`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `productName` | String | 등록된 상품명 |
| `productCategory` | String | 등록된 카테고리 |
| `price` | String | 상품 가격 (천 단위 구분 및 원 표시) |
| `stock` | Integer | 상품 재고 |
| `productStatus` | String | 상품 상태 |

```json
{
  "productName": "레고세트",
  "productCategory": "완구/취미",
  "price": "89,000원",
  "stock": 32,
  "productStatus": "ON_SALE"
}
```

**Error Response** — `400`, `401`, `404`

`400 Bad Request` — 잘못된 상품 등록 정보

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
  "path": "/products"
}
```

`401 Unauthorized` — 관리자 로그인 세션 없음

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Unauthorized user",
  "path": "/products"
}
```

`404 Not Found` — 세션에 저장된 관리자 ID에 해당하는 관리자가 없는 경우

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "해당 데이터를 찾을 수 없습니다.",
  "path": "/products"
}
```

</details>

<details>
<summary><code>GET</code> <b>/products</b> — 상품 리스트 조회</summary>

**Query String**

```text
?page=1&size=10&sortBy=price&sortDirection=asc
```

| 키 | 타입 | 설명 |
| --- | --- | --- |
| `keyword` | String | 상품명 부분 검색 (선택) |
| `page` | Integer | 페이지 번호 (기본값 1, 1 이상) |
| `size` | Integer | 페이지당 개수 (기본값 10, 1 이상) |
| `sortBy` | String | `price`, `stock`, `createdAt` (기본값 `createdAt`) |
| `sortDirection` | String | `asc`, `desc` (기본값 `desc`) |
| `category` | String | 카테고리 일치 검색 (선택) |
| `status` | String | 상품 상태 필터 (`ON_SALE`, `OUT_OF_STOCK`, `DISCONTINUED`, 선택) |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 관리자 로그인 세션 ID |

**Request Body** — 없음

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `content` | Array | 조회된 상품 목록 (논리 삭제된 상품 제외) |
| `content[].productId` | Long | 상품 고유 식별자 |
| `content[].productName` | String | 상품명 |
| `content[].productCategory` | String | 상품 카테고리 |
| `content[].price` | String | 상품 가격 (천 단위 구분 및 원 표시) |
| `content[].stock` | Integer | 상품 재고 |
| `content[].productStatus` | String | 상품 상태 |
| `content[].createdAt` | LocalDateTime | 상품 등록일 |
| `content[].adminName` | String | 상품 등록 관리자 이름 (관리자 정보 없으면 null) |
| `content[].adminEmail` | String | 상품 등록 관리자 이메일 (관리자 정보 없으면 null) |
| `page` | Integer | 현재 페이지 번호 |
| `size` | Integer | 페이지당 개수 |
| `totalElements` | Long | 검색/필터 조건에 해당하는 상품 수 |
| `totalPages` | Integer | 전체 페이지 수 |

```json
{
  "content": [
    {
      "productId": 2,
      "productName": "직소퍼즐",
      "productCategory": "완구/취미",
      "price": "25,000원",
      "stock": 46,
      "productStatus": "ON_SALE",
      "createdAt": "2026-10-27T23:15:38",
      "adminName": "김운영",
      "adminEmail": "manager@sparta.com"
    },
    {
      "productId": 1,
      "productName": "레고세트",
      "productCategory": "완구/취미",
      "price": "89,000원",
      "stock": 32,
      "productStatus": "ON_SALE",
      "createdAt": "2026-10-27T23:13:17",
      "adminName": "admin",
      "adminEmail": "admin@sparta.com"
    }
  ],
  "page": 1,
  "size": 10,
  "totalElements": 2,
  "totalPages": 1
}
```

**Error Response** — `400`, `401`

`400 Bad Request` — 잘못된 조회 조건

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
  "path": "/products"
}
```

`401 Unauthorized` — 관리자 로그인 세션 없음

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Unauthorized user",
  "path": "/products"
}
```

</details>

<details>
<summary><code>GET</code> <b>/products/{id}</b> — 상품 상세 조회</summary>

**Path Parameter**

| 키 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 조회할 상품 ID |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 관리자 로그인 세션 ID |

**Request Body** — 없음

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `productId` | Long | 상품 고유 식별자 |
| `productName` | String | 상품명 |
| `productCategory` | String | 상품 카테고리 |
| `price` | String | 상품 가격 (천 단위 구분 및 원 표시) |
| `stock` | Integer | 상품 재고 |
| `productStatus` | String | 상품 상태 |
| `createdAt` | LocalDateTime | 상품 등록일 |
| `adminName` | String | 상품 등록 관리자 이름 (관리자 정보 없으면 null) |
| `adminEmail` | String | 상품 등록 관리자 이메일 (관리자 정보 없으면 null) |

```json
{
  "productId": 1,
  "productName": "레고세트",
  "productCategory": "완구/취미",
  "price": "89,000원",
  "stock": 32,
  "productStatus": "ON_SALE",
  "createdAt": "2026-10-27T23:13:17",
  "adminName": "admin",
  "adminEmail": "admin@sparta.com"
}
```

**Error Response** — `400`, `401`, `404`

`401 Unauthorized` — 관리자 로그인 세션 없음

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Unauthorized user",
  "path": "/products/1"
}
```

`404 Not Found` — 상품이 존재하지 않거나 논리 삭제된 경우

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "해당 데이터를 찾을 수 없습니다.",
  "path": "/products/999"
}
```

</details>

<details>
<summary><code>PATCH</code> <b>/products/{id}</b> — 상품 정보 수정</summary>

**Path Parameter**

| 키 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 수정할 상품 ID |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 요청 데이터 형식 |
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 관리자 로그인 세션 ID |

**Request Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `productName` | String | 수정할 상품명 (선택, 공백 불가, 100자 이하) |
| `productCategory` | String | 수정할 상품 카테고리 (선택, 공백 불가, 15자 이하) |
| `price` | Long | 수정할 상품 가격 (선택, 0원 이상) |

```json
{
  "productName": "아이패드",
  "productCategory": "전자기기",
  "price": 790000
}
```

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `productId` | Long | 상품 고유 식별자 |
| `productName` | String | 수정된 상품명 |
| `productCategory` | String | 수정된 카테고리 |
| `price` | String | 수정된 가격 (천 단위 구분 및 원 표시) |
| `updatedAt` | LocalDateTime | 상품 정보 수정일 |

```json
{
  "productId": 1,
  "productName": "아이패드",
  "productCategory": "전자기기",
  "price": "790,000원",
  "updatedAt": "2026-10-27T23:29:41"
}
```

**Error Response** — `400`, `401`, `404`

`400 Bad Request` — 유효하지 않은 수정 데이터

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
  "path": "/products/1"
}
```

`401 Unauthorized` — 관리자 로그인 세션 없음

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Unauthorized user",
  "path": "/products/1"
}
```

`404 Not Found` — 상품이 존재하지 않거나 논리 삭제된 경우

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "해당 데이터를 찾을 수 없습니다.",
  "path": "/products/999"
}
```

</details>

<details>
<summary><code>PATCH</code> <b>/products/{id}/stock</b> — 상품 재고 변경</summary>

**Path Parameter**

| 키 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 재고를 변경할 상품 ID |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 요청 데이터 형식 |
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 관리자 로그인 세션 ID |

**Request Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `stock` | Integer | 변경할 재고 수량 (필수, 0 이상) |

```json
{
  "stock": 0
}
```

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `productId` | Long | 상품 고유 식별자 |
| `stock` | Integer | 변경된 재고 수량 |
| `productStatus` | String | 변경 후 상품 상태 |
| `updatedAt` | LocalDateTime | 상품 정보 수정일 |

```json
{
  "productId": 1,
  "stock": 0,
  "productStatus": "OUT_OF_STOCK",
  "updatedAt": "2026-10-27T23:32:32"
}
```

**Error Response** — `400`, `401`, `404`

`400 Bad Request` — 재고값이 없거나 음수인 경우

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
  "path": "/products/1/stock"
}
```

`401 Unauthorized` — 관리자 로그인 세션 없음

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Unauthorized user",
  "path": "/products/1/stock"
}
```

`404 Not Found` — 상품이 존재하지 않거나 논리 삭제된 경우

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "해당 데이터를 찾을 수 없습니다.",
  "path": "/products/999/stock"
}
```

</details>

<details>
<summary><code>PATCH</code> <b>/products/{id}/status</b> — 상품 상태 변경</summary>

**Path Parameter**

| 키 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 상태를 변경할 상품 ID |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 요청 데이터 형식 |
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 관리자 로그인 세션 ID |

**Request Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `discontinued` | Boolean | 필수. `true`: 단종 처리, `false`: 단종 해제 |

```json
{
  "discontinued": true
}
```

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `productId` | Long | 상품 고유 식별자 |
| `productStatus` | String | 변경된 상품 상태 |
| `updatedAt` | LocalDateTime | 상품 정보 수정일 |

```json
{
  "productId": 1,
  "productStatus": "DISCONTINUED",
  "updatedAt": "2026-10-27T23:38:42"
}
```

**Error Response** — `400`, `401`, `404`

`400 Bad Request` — 단종 여부 값이 없는 경우

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
  "path": "/products/1/status"
}
```

`401 Unauthorized` — 관리자 로그인 세션 없음

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Unauthorized user",
  "path": "/products/1/status"
}
```

`404 Not Found` — 상품이 존재하지 않거나 논리 삭제된 경우

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "해당 데이터를 찾을 수 없습니다.",
  "path": "/products/999/status"
}
```

</details>

<details>
<summary><code>DELETE</code> <b>/products/{id}</b> — 상품 삭제</summary>

**Path Parameter**

| 키 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 삭제할 상품 ID |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 관리자 로그인 세션 ID |

**Request Body** — 없음

**Response** `200 OK`

상품을 DB에서 물리적으로 삭제하지 않고 논리 삭제 처리합니다.

삭제된 상품은 목록 조회에서 제외되며, 상세 조회 및 수정 시 조회되지 않은 상품으로 처리합니다.

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `message` | String | 상품 삭제 처리 완료 메시지 |

```json
{
  "message": "요청하신 상품이 삭제되었습니다."
}
```

**Error Response** — `400`, `401`, `404`

`401 Unauthorized` — 관리자 로그인 세션 없음

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Unauthorized user",
  "path": "/products/1"
}
```

`404 Not Found` — 상품이 존재하지 않거나 이미 논리 삭제된 경우

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "해당 데이터를 찾을 수 없습니다.",
  "path": "/products/999"
}
```

</details>

---

## 🧾 주문 관리

> 모든 주문 API는 로그인한 관리자 세션(`JSESSIONID`)이 필요합니다. 세션이 없으면 `401 Unauthorized`(`로그인이 필요합니다.`)를 반환합니다.

| Method | Endpoint | 기능 |
| :---: | --- | --- |
| `POST` | `/orders` | 주문 생성 (CS 주문) |
| `GET` | `/orders` | 주문 리스트 조회 |
| `GET` | `/orders/{orderId}` | 주문 상세 조회 |
| `PATCH` | `/orders/{orderId}/status` | 주문 상태 수정 (다음 단계로 변경) |
| `PATCH` | `/orders/{orderId}` | 주문 취소 |

<details>
<summary><code>POST</code> <b>/orders</b> — 주문 생성 (CS 주문)</summary>

> 로그인한 관리자가 고객 대신 주문을 생성합니다. 로그인한 관리자가 `등록 관리자`로 저장됩니다.
>
> - 주문 수량만큼 상품 재고가 차감되며, 재고가 0이 되면 상품 상태가 자동으로 `OUT_OF_STOCK`으로 전환됩니다.
> - 주문 상태는 `PREPARING`으로 생성됩니다.
> - 주문번호는 `yyyyMMdd-NNN` 형식으로 자동 생성됩니다. (예: `20261029-003`, 당일 주문 순번)
> - 총 금액 = 상품 단가 × 수량

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 요청 본문 형식 |
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 로그인한 관리자 세션 |

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
| --- | --- | :---: | --- |
| `customerId` | Long | ✅ | 고객 ID |
| `productId` | Long | ✅ | 상품 ID |
| `quantity` | Integer | ✅ | 주문 수량 (1 이상) |

```json
{
  "customerId": "고객 ID",
  "productId": "상품 ID",
  "quantity": "주문 수량"
}
```

**Response** `201 Created` — 응답 본문 없음

**Error Response**

| 상태 코드 | 상황 | message |
| --- | --- | --- |
| `400 Bad Request` | 필수 값 누락, 수량이 1 미만인 경우 | `Invalid request` |
| `401 Unauthorized` | 로그인하지 않은 경우 | `Unauthorized user` |
| `404 Not Found` | 존재하지 않는 관리자 / 고객 / 상품 | `Resource Not Found` |
| `400 Bad Request` | 단종된 상품 | `Invalid request` |
| `400 Bad Request` | 품절된 상품 | `Invalid request` |
| `400 Bad Request` | 주문 수량 > 재고 수량 | `Invalid request` |

</details>

<details>
<summary><code>GET</code> <b>/orders</b> — 주문 리스트 조회</summary>

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 로그인한 관리자 세션 |

**Query String**

```text
?keyword=20261029&status=PREPARING&sortBy=createdAt&direction=DESC&page=1
```

| 키 | 타입 | 필수 | 설명 |
| --- | --- | :---: | --- |
| `keyword` | String | | 주문번호 또는 고객명 부분 검색 (빈 값이면 전체) |
| `status` | String | | 주문 상태 필터 (`PREPARING`, `SHIPPING`, `DELIVERED`, `CANCELLED`, 미입력 시 전체) |
| `sortBy` | String | | `orderNumber`, `customerName`, `productName`, `quantity`, `totalPrice`, `createdAt`, `status` (기본값 `createdAt`) |
| `direction` | String | | `ASC`, `DESC` (기본값 `DESC`) |
| `page` | Integer | | 페이지 번호 (기본값 1, 1부터 시작) |

> 페이지당 개수는 **10개로 고정**입니다.

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `content` | Array | 주문 목록 |
| `content[].orderId` | Long | 주문 ID |
| `content[].orderNumber` | String | 주문번호 |
| `content[].customerName` | String | 고객명 |
| `content[].productName` | String | 상품명 (주문 시점) |
| `content[].quantity` | Integer | 수량 |
| `content[].totalPrice` | Long | 총 금액 |
| `content[].createdAt` | LocalDateTime | 주문일 |
| `content[].status` | String | 주문 상태 |
| `content[].adminName` | String | 등록 관리자명 (등록 관리자가 없으면 `null`) |
| `number` | Integer | 현재 페이지 번호 (**0부터 시작**, 요청 `page` - 1) |
| `size` | Integer | 페이지당 개수 (10) |
| `totalElements` | Long | 검색 및 필터 조건에 해당하는 주문 수 |
| `totalPages` | Integer | 전체 페이지 수 |
| `first` / `last` | Boolean | 첫 / 마지막 페이지 여부 |

> Spring Data `Page` 객체가 그대로 직렬화되므로 `pageable`, `sort`, `numberOfElements`, `empty` 필드도 함께 응답됩니다.

```json
{
  "content": [
    {
      "orderId": "주문 ID",
      "orderNumber": "주문번호",
      "customerName": "고객명",
      "productName": "상품명",
      "quantity": "수량",
      "totalPrice": "총 금액",
      "createdAt": "주문일",
      "status": "주문 상태",
      "adminName": "등록 관리자명"
    }
  ],
  "number": "현재 페이지 번호",
  "size": "페이지당 개수",
  "totalElements": "검색 및 필터 조건에 해당하는 주문 수",
  "totalPages": "전체 페이지 수",
  "first": "첫 페이지 여부",
  "last": "마지막 페이지 여부"
}
```

**Error Response**

| 상태 코드 | 상황 | message |
| --- | --- | --- |
| `400 Bad Request` | 정렬할 수 없는 `sortBy` 값, 잘못된 `status` / `direction` 값 | `Invalid request` |
| `401 Unauthorized` | 로그인하지 않은 경우 | `Unauthorized user` |

</details>

<details>
<summary><code>GET</code> <b>/orders/{orderId}</b> — 주문 상세 조회</summary>

> `CS 주문`이 아닌 고객의 직접 주문인 경우 등록 관리자 정보(`adminEmail`, `adminRole`)는 `null`입니다.

**Path Parameter**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `orderId` | Long | 주문 ID |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 로그인한 관리자 세션 |

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `orderNumber` | String | 주문번호 |
| `customerName` | String | 고객명 |
| `customerEmail` | String | 고객 이메일 |
| `productName` | String | 상품명 (주문 시점) |
| `quantity` | Integer | 수량 |
| `totalPrice` | Long | 총 금액 |
| `createdAt` | LocalDateTime | 주문일 |
| `status` | String | 주문 상태 |
| `adminEmail` | String | 등록 관리자 이메일 (없으면 `null`) |
| `adminRole` | String | 등록 관리자 역할 (없으면 `null`) |

```json
{
  "orderNumber": "주문번호",
  "customerName": "고객명",
  "customerEmail": "고객 이메일",
  "productName": "상품명",
  "quantity": "수량",
  "totalPrice": "총 금액",
  "createdAt": "주문일",
  "status": "주문 상태",
  "adminEmail": "등록 관리자 이메일",
  "adminRole": "등록 관리자 역할"
}
```

**Error Response**

| 상태 코드 | 상황 | message |
| --- | --- | --- |
| `401 Unauthorized` | 로그인하지 않은 경우 | `Unauthorized user` |
| `404 Not Found` | 존재하지 않는 주문 / 주문 상품 | `Resource Not Found` |

</details>

<details>
<summary><code>PATCH</code> <b>/orders/{orderId}/status</b> — 주문 상태 수정</summary>

> 요청 본문 없이 호출하면 주문 상태가 **다음 단계로 자동 변경**됩니다.
>
> `PREPARING`(준비중) → `SHIPPING`(배송중) → `DELIVERED`(배송완료)
>
> `DELIVERED`, `CANCELLED` 상태의 주문은 수정할 수 없습니다.

**Path Parameter**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `orderId` | Long | 주문 ID |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 로그인한 관리자 세션 |

**Request Body** — 없음

**Response** `200 OK` — 응답 본문 없음

**Error Response**

| 상태 코드 | 상황 | message |
| --- | --- | --- |
| `401 Unauthorized` | 로그인하지 않은 경우 | `Unauthorized user` |
| `404 Not Found` | 존재하지 않는 주문 | `Resource Not Found` |
| `400 Bad Request` | 배송완료 또는 취소된 주문 | `Invalid request` |

</details>

<details>
<summary><code>PATCH</code> <b>/orders/{orderId}</b> — 주문 취소</summary>

> 주문 취소는 `준비중(PREPARING)` 상태에서만 허용됩니다. 취소 시 상태가 `CANCELLED`로 변경되고, 취소 사유가 저장되며, 주문 수량만큼 상품 재고가 복구됩니다.

**Path Parameter**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `orderId` | Long | 주문 ID |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 요청 본문 형식 |
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 로그인한 관리자 세션 |

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
| --- | --- | :---: | --- |
| `cancelReason` | String | ✅ | 취소 사유 (공백 불가) |

```json
{
  "cancelReason": "취소 사유"
}
```

**Response** `200 OK` — 응답 본문 없음

**Error Response**

| 상태 코드 | 상황 | message |
| --- | --- | --- |
| `400 Bad Request` | 취소 사유 누락 또는 공백 | `Invalid request` |
| `401 Unauthorized` | 로그인하지 않은 경우 | `Unauthorized user` |
| `404 Not Found` | 존재하지 않는 주문 / 주문 상품 | `Resource Not Found` |
| `400 Bad Request` | 이미 취소된 주문 | `Invalid request` |
| `400 Bad Request` | 배송중 또는 배송완료된 주문 | `Invalid request` |

</details>
