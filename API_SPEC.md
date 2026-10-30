## 📑 API 명세서

### 공통 사항

| 항목 | 내용 |
| --- | --- |
| Content-Type | `application/json` |
| 인증 | 로그인 시 발급되는 세션 쿠키 `JSESSIONID` (회원가입·로그인 제외) |
| 날짜 형식 | `yyyy-MM-dd'T'HH:mm:ss` |

<details>
<summary><b>Enum 정의</b></summary>

**관리자 역할 (`role`)**

| 값 | 설명 |
| --- | --- |
| `SUPER_ADMIN` | 슈퍼 관리자 |
| `OPERATION_ADMIN` | 운영 관리자 |
| `CS_ADMIN` | CS 관리자 |

**관리자 상태 (`status`)**

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
| `DELIVERING` | 배송중 |
| `DELIVERED` | 배송완료 |
| `CANCELLED` | 취소됨 |

`CANCELLED`는 위 순서와 별개의 분기 상태로, `PREPARING` 단계에서만 전이할 수 있습니다. 배송이 시작된 주문은 취소되지 않습니다.

</details>

---

## 🔐 관리자 / 인증

| Method | Endpoint | 기능 |
| :---: | --- | --- |
| `POST` | `/admins/signup` | 관리자 회원가입 |
| `POST` | `/admins/login` | 관리자 로그인 |
| `POST` | `/admins/logout` | 관리자 로그아웃 |
| `GET` | `/admins` | 관리자 리스트 조회 |
| `GET` | `/admins/{adminId}` | 관리자 상세 조회 |
| `PATCH` | `/admins/{adminId}` | 관리자 정보 수정 |
| `DELETE` | `/admins/{adminId}` | 관리자 삭제 |
| `GET` | `/admins/{adminId}/me` | 내 프로필 조회 |
| `PATCH` | `/admins/{adminId}/me` | 내 프로필 수정 |
| `PATCH` | `/admins/{adminId}/password` | 비밀번호 변경 |
| `PATCH` | `/admins/{adminId}/role` | 관리자 역할 변경 |
| `PATCH` | `/admins/{adminId}/status` | 관리자 상태 변경 |
| `PATCH` | `/admins/{adminId}/approve` | 관리자 승인 |
| `PATCH` | `/admins/{adminId}/reject` | 관리자 거부 |

<details>
<summary><code>POST</code> <b>/admins/signup</b> — 관리자 회원가입</summary>

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 요청 데이터 형식 |

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
| --- | --- | :---: | --- |
| `name` | String | ✅ | 관리자명 |
| `email` | String | ✅ | 관리자 이메일 (중복 불가) |
| `phone` | String | ✅ | 관리자 전화번호 (`010-XXXX-XXXX`) |
| `role` | String | ✅ | 관리자 역할 |
| `password` | String | ✅ | 관리자 비밀번호 (최소 8자 이상) |

```json
{
  "name": "관리자 이름",
  "email": "example@gmail.com",
  "phone": "010-XXXX-XXXX",
  "role": "SUPER_ADMIN",
  "password": "12345678"
}
```

**Response Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `message` | String | 전달 메시지 |

```json
{
  "message": "관리자 계정 신청이 완료되었습니다! 승인 후 로그인이 가능합니다."
}
```

**Error Response** — 중복 이메일, 형식 오류, 필수 값 누락

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
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

| 필드 | 타입 | 필수 | 설명 |
| --- | --- | :---: | --- |
| `email` | String | ✅ | 관리자 이메일 |
| `password` | String | ✅ | 관리자 비밀번호 (최소 8자 이상) |

```json
{
  "email": "example@gmail.com",
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
  "message": "로그인 성공!"
}
```

**Error Response** — 이메일 또는 비밀번호 불일치, 계정 승인대기 중, 계정 신청 거부됨, 계정 정지됨, 계정 비활성화됨

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
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

**Response Body** — 없음

**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
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

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 관리자 고유 식별자 |
| `name` | String | 관리자명 |
| `email` | String | 관리자 이메일 |
| `phone` | String | 관리자 전화번호 |
| `role` | String | 관리자 역할 |
| `status` | String | 관리자 상태 |
| `createdAt` | LocalDateTime | 관리자 가입 일시 |
| `approvedAt` | LocalDateTime | 관리자 승인 일시 |

```json
[
  {
    "id": 1,
    "name": "홍길동",
    "email": "example@gmail.com",
    "phone": "010-XXXX-XXXX",
    "role": "SUPER_ADMIN",
    "status": "PENDING",
    "createdAt": "2026-10-27T10:00:00",
    "approvedAt": "2026-10-27T11:00:00"
  },
  {
    "id": 2,
    "name": "김흥부",
    "email": "example2@gmail.com",
    "phone": "010-XXXX-XXXX",
    "role": "SUPER_ADMIN",
    "status": "PENDING",
    "createdAt": "2026-10-27T10:30:00",
    "approvedAt": "2026-10-27T11:30:00"
  }
]
```

**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
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

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `name` | String | 관리자명 |
| `email` | String | 관리자 이메일 (중복 불가) |
| `phone` | String | 관리자 전화번호 (`010-XXXX-XXXX`) |
| `role` | String | 관리자 역할 |
| `status` | String | 관리자 상태 |
| `createdAt` | LocalDateTime | 관리자 계정 생성 일시 |
| `approvedAt` | LocalDateTime | 관리자 승인 일시 |

```json
{
  "name": "홍길동",
  "email": "example@gmail.com",
  "phone": "010-XXXX-XXXX",
  "role": "SUPER_ADMIN",
  "status": "PENDING",
  "createdAt": "2026-10-27T10:00:00",
  "approvedAt": "2026-10-27T11:00:00"
}
```

**Error Response** — 중복 이메일, 형식 오류, 필수 값 누락

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
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

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `name` | String | 관리자 이름 |
| `email` | String | 관리자 이메일 (중복 불가) |
| `phone` | String | 관리자 전화번호 (`010-XXXX-XXXX`) |

```json
{
  "name": "이름",
  "email": "example@gmail.com",
  "phone": "010-XXXX-XXXX"
}
```

**Response Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `name` | String | 수정된 관리자명 |
| `email` | String | 수정된 관리자 이메일 |
| `phone` | String | 수정된 관리자 전화번호 |

```json
{
  "name": "홍길동",
  "email": "example@gmail.com",
  "phone": "010-XXXX-XXXX"
}
```

**Error Response** — 중복 이메일, 형식 오류, 필수 값 누락

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
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
  "message": "유저가 삭제되었습니다."
}
```

**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
  "path": "/admins/{adminId}"
}
```

</details>

<details>
<summary><code>GET</code> <b>/admins/{adminId}/me</b> — 내 프로필 조회</summary>

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
| `name` | String | 관리자명 |
| `email` | String | 관리자 이메일 (중복 불가) |
| `phone` | String | 관리자 전화번호 (`010-XXXX-XXXX`) |

```json
{
  "name": "관리자명",
  "email": "example@gmail.com",
  "phone": "010-XXXX-XXXX"
}
```

**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
  "path": "/admins/{adminId}/me"
}
```

</details>

<details>
<summary><code>PATCH</code> <b>/admins/{adminId}/me</b> — 내 프로필 수정</summary>

**Path Parameter**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `adminId` | Long | 관리자 고유 식별자 |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 데이터 형식 |

**Request Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `name` | String | 변경할 관리자명 |
| `email` | String | 변경할 관리자 이메일 (중복 불가) |
| `phone` | String | 변경할 관리자 전화번호 (`010-XXXX-XXXX`) |

```json
{
  "name": "관리자 이름",
  "email": "example@gmail.com",
  "phone": "010-XXXX-XXXX"
}
```

**Response Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `name` | String | 변경된 관리자명 |
| `email` | String | 변경된 관리자 이메일 |
| `phone` | String | 변경된 관리자 전화번호 |

```json
{
  "name": "관리자 이름",
  "email": "example@gmail.com",
  "phone": "010-XXXX-XXXX"
}
```

**Error Response** — 중복 이메일, 형식 오류, 필수 값 누락

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
  "path": "/admins/{adminId}/me"
}
```

</details>

<details>
<summary><code>PATCH</code> <b>/admins/{adminId}/password</b> — 비밀번호 변경</summary>

**Path Parameter**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `adminId` | Long | 관리자 고유 식별자 |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 데이터 형식 |

**Request Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `password` | String | 변경할 관리자 비밀번호 (최소 8자 이상) |

```json
{
  "password": "123456789a!"
}
```

**Response Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `message` | String | 전달 메시지 |

```json
{
  "message": "비밀번호가 변경되었습니다."
}
```

**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
  "path": "/admins/{adminId}/password"
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
  "role": "OPERATION_ADMIN"
}
```

**Response Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `role` | String | 변경된 관리자 역할 |

```json
{
  "role": "OPERATION_ADMIN"
}
```

**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
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

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `status` | String | 변경할 관리자 상태 |

```json
{
  "status": "SUSPENDED"
}
```

**Response Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `status` | String | 변경된 관리자 상태 |

```json
{
  "status": "SUSPENDED"
}
```

**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
  "path": "/admins/{adminId}/status"
}
```

</details>

<details>
<summary><code>PATCH</code> <b>/admins/{adminId}/approve</b> — 관리자 승인</summary>

**Path Parameter**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `adminId` | Long | 관리자 고유 식별자 |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 데이터 형식 |

**Request Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `status` | String | 관리자 상태 (계정 활성: `ACTIVATE`) |

```json
{
  "status": "ACTIVATE"
}
```

**Response Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `message` | String | 전달 메시지 |

```json
{
  "message": "승인 되었습니다."
}
```

**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
  "path": "/admins/{adminId}/approve"
}
```

</details>

<details>
<summary><code>PATCH</code> <b>/admins/{adminId}/reject</b> — 관리자 거부</summary>

**Path Parameter**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `adminId` | Long | 관리자 고유 식별자 |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 데이터 형식 |

**Request Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `status` | String | 관리자 상태 (계정 거부: `REJECTED`) |
| `reject_reason` | String | 승인 거부 이유 |

```json
{
  "status": "REJECTED",
  "reject_reason": "메세지 입력"
}
```

**Response Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `status` | String | 관리자 상태 (`REJECTED`) |
| `reject_reason` | String | 승인 거부 이유 |

```json
{
  "status": "REJECTED",
  "reject_reason": "메세지"
}
```

**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
  "path": "/admins/{adminId}/reject"
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
| `DELETE` | `/customers/{id}` | 고객 삭제 |

<details>
<summary><code>GET</code> <b>/customers</b> — 고객 리스트 조회</summary>

**Query String**

```text
?page=1&size=10&sortBy=createdAt&sortDirection=desc
```

| 키 | 설명 |
| --- | --- |
| `keyword` | 고객명 또는 이메일 검색 (선택) |
| `page` | 페이지 번호 (기본값 1, 1 이상) |
| `size` | 페이지당 개수 (기본값 10, 1 이상) |
| `sortBy` | 정렬 기준: `name`, `email`, `createdAt` (기본값 `createdAt`) |
| `sortDirection` | 정렬 방향: `asc`, `desc` (기본값 `desc`) |
| `status` | 고객 상태 필터: `ACTIVE`, `INACTIVE`, `SUSPENDED` (선택) |

**Request Headers / Body** — 별도 요청 본문 없음

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `content` | Array | 고객 목록 |
| `content[].id` | Long | 고객 고유 식별자 |
| `content[].name` | String | 고객 이름 |
| `content[].email` | String | 고객 이메일 |
| `content[].phone` | String | 고객 전화번호 (null 가능) |
| `content[].status` | String | 고객 상태 |
| `content[].createdAt` | LocalDateTime | 고객 가입일 |
| `page` | Integer | 현재 페이지 번호 (1부터 시작) |
| `size` | Integer | 페이지당 개수 |
| `totalElements` | Long | 검색 및 필터 조건에 해당하는 전체 고객 수 |
| `totalPages` | Integer | 전체 페이지 수 |

```json
{
  "content": [
    {
      "id": 2,
      "name": "수키",
      "email": "sooky@sparta.com",
      "phone": "010-7895-4564",
      "status": "ACTIVE",
      "createdAt": "2026-10-27T18:54:49"
    },
    {
      "id": 1,
      "name": "전영주",
      "email": "youngju@sparta.com",
      "phone": "010-1234-5678",
      "status": "ACTIVE",
      "createdAt": "2026-10-27T18:52:17"
    }
  ],
  "page": 1,
  "size": 10,
  "totalElements": 2,
  "totalPages": 1
}
```

**Error Response** — `400 Bad Request`

- `page` 또는 `size`가 1 미만인 경우
- `sortBy`가 허용된 정렬 기준이 아닌 경우
- `sortDirection`이 `asc`, `desc`가 아닌 경우
- `status`가 정의되지 않은 Enum 값인 경우

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
  "path": "/customers"
}
```

> 검색 키워드가 없으면 전체 고객을 조회합니다.
> 검색 키워드와 상태 필터를 함께 사용할 수 있습니다.
> 검색 결과가 없으면 `content`는 빈 배열로 반환됩니다.
> 현재 코드에는 `deletedAt`이 설정된 고객을 조회에서 제외하는 조건이 없습니다.

</details>

<details>
<summary><code>GET</code> <b>/customers/{id}</b> — 고객 상세 조회</summary>

**Path Parameter**

| 키 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 조회할 고객 ID |

**Request Headers / Body** — 별도 요청 본문 없음

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 고객 고유 식별자 |
| `name` | String | 고객 이름 |
| `email` | String | 고객 이메일 |
| `phone` | String | 고객 전화번호 (null 가능) |
| `status` | String | 고객 상태 |
| `createdAt` | LocalDateTime | 고객 가입일 |

```json
{
  "id": 1,
  "name": "전영주",
  "email": "youngju@sparta.com",
  "phone": "010-1234-5678",
  "status": "ACTIVE",
  "createdAt": "2026-10-27T18:52:17"
}
```

**Error Response** — `404 Not Found`

- 요청한 고객 ID가 존재하지 않는 경우

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "해당 데이터를 찾을 수 없습니다.",
  "path": "/customers/999"
}
```

> 현재 코드에서는 `deletedAt`이 설정된 고객도 ID가 존재하면 조회될 수 있습니다.

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

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
| --- | --- | :---: | --- |
| `name` | String | 선택 | 수정할 고객 이름 (공백 불가, 최대 100자) |
| `email` | String | 선택 | 수정할 고객 이메일 (공백 불가, 최대 255자) |
| `phone` | String | 선택 | 수정할 고객 전화번호 (공백 불가, 최대 15자) |

```json
{
  "name": "영주",
  "email": "0zoo@sparta.com",
  "phone": "010-4541-1212"
}
```

> PATCH 요청이므로 변경할 필드만 전달할 수 있습니다.
> 요청 필드가 `null`이거나 생략되면 기존 값이 유지됩니다.
> 빈 문자열 또는 공백만 있는 문자열은 허용되지 않습니다.

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 고객 고유 식별자 |
| `name` | String | 수정된 고객 이름 |
| `email` | String | 수정된 고객 이메일 |
| `phone` | String | 수정된 고객 전화번호 |
| `status` | String | 고객 상태 |
| `createdAt` | LocalDateTime | 고객 가입일 |
| `updatedAt` | LocalDateTime | 고객 정보 수정일 |

```json
{
  "id": 1,
  "name": "영주",
  "email": "0zoo@sparta.com",
  "phone": "010-4541-1212",
  "status": "ACTIVE",
  "createdAt": "2026-10-27T18:52:17",
  "updatedAt": "2026-10-27T19:06:11"
}
```

**Error Response**

| 상태 코드 | 상황 |
| --- | --- |
| `404 Not Found` | 요청한 고객 ID가 존재하지 않는 경우 |
| `400 Bad Request` | 요청 본문 형식이 잘못된 경우 |
| 확인 필요 | 이름, 이메일, 전화번호 유효성 검증 예외의 HTTP 응답 처리 |
| 확인 필요 | 중복 이메일에 대한 예외 응답 처리 |

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "해당 데이터를 찾을 수 없습니다.",
  "path": "/customers/999"
}
```

> Entity의 `changeCustomer()`에서 입력값을 검증합니다.
> 현재 Service에는 `IllegalArgumentException`을 HTTP 400으로 변환하는 별도 처리가 없습니다.
> 이메일은 DB에서 중복이 제한되지만, 중복 발생 시 HTTP 409 반환은 현재 코드에서 확인되지 않습니다.
> 수정 시 `flush()`를 호출하여 변경 내용을 DB에 반영합니다.

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

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
| --- | --- | :---: | --- |
| `status` | String | ✅ | 변경할 고객 상태 (`ACTIVE`, `INACTIVE`, `SUSPENDED`) |

```json
{
  "status": "SUSPENDED"
}
```

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 고객 고유 식별자 |
| `status` | String | 변경된 고객 상태 |
| `updatedAt` | LocalDateTime | 상태 변경일 |

```json
{
  "id": 1,
  "status": "SUSPENDED",
  "updatedAt": "2026-10-27T19:18:25"
}
```

**Error Response** — `400 Bad Request`, `404 Not Found`

| 상태 코드 | 상황 |
| --- | --- |
| `400 Bad Request` | 상태값이 null이거나 유효하지 않은 경우 |
| `404 Not Found` | 요청한 고객 ID가 존재하지 않는 경우 |

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "상태값이 비어있습니다.",
  "path": "/customers/1/status"
}
```

> 고객 상태는 Entity의 `changeStatus()` 메서드에서 변경합니다.
> 상태값이 `null`이면 `IllegalArgumentException`이 발생하며 Service에서 HTTP 400으로 변환합니다.
> 정의되지 않은 Enum 문자열은 요청 데이터 변환 과정에서 오류가 발생할 수 있습니다.
> 변경 시 `flush()`를 호출하여 변경 내용을 DB에 반영합니다.

</details>

<details>
<summary><code>DELETE</code> <b>/customers/{id}</b> — 고객 삭제</summary>

**Path Parameter**

| 키 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 삭제할 고객 ID |

**Request Headers / Body** — 별도 요청 본문 없음

**Response** `200 OK`

> 고객 레코드를 실제로 삭제하지 않고 `deletedAt`에 삭제 일시를 기록하는 논리 삭제 방식입니다.

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `message` | String | 고객 삭제 처리 완료 메시지 |

```json
{
  "message": "요청하신 고객 정보가 삭제되었습니다."
}
```

**Error Response** — `404 Not Found`

- 요청한 고객 ID가 존재하지 않는 경우

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "해당 데이터를 찾을 수 없습니다.",
  "path": "/customers/999"
}
```

> `Customer.deleteCustomer()` 메서드에서 `deletedAt`에 현재 시간을 저장합니다.
> `CustomerService.deleteCustomer()`는 `@Transactional`을 사용하므로 변경 감지로 DB에 반영됩니다.
> 실제 데이터는 DB에서 삭제되지 않습니다.
> 현재 조회 메서드에는 `deletedAt` 제외 조건이 없어 논리 삭제된 고객도 조회될 수 있습니다.
> 중복 삭제를 방지하는 검사는 현재 구현되어 있지 않습니다.

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
| `Cookie` | `JSESSIONID={sessionId}` | ✅ | 로그인한 관리자 세션 |

**Request Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `name` | String | 상품명 (필수, 공백 불가, 100자 이하) |
| `category` | String | 상품 카테고리 (필수, 공백 불가, 15자 이하) |
| `price` | Long | 상품 가격 (필수, 0원 이상) |
| `stock` | Integer | 상품 재고 (필수, 0개 이상) |
| `status` | String | 상품 상태 (선택, `DISCONTINUED` 지정 시 단종 처리) |

```json
{
  "name": "레고세트",
  "category": "완구/취미",
  "price": 89000,
  "stock": 32
}
```

**Response** `201 Created`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `name` | String | 등록된 상품명 |
| `category` | String | 등록된 카테고리 |
| `price` | String | 상품 가격 (천 단위 구분 및 원 표시) |
| `stock` | Integer | 상품 재고 |
| `status` | String | 상품 상태 |

```json
{
  "name": "레고세트",
  "category": "완구/취미",
  "price": "89,000원",
  "stock": 32,
  "status": "ON_SALE"
}
```

**Error Response** — `400`, `401`, `404`

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "인증되지 않은 사용자입니다.",
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

**Request Headers / Body** — 별도 요구 없음

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `content` | Array | 조회된 상품 목록 (논리 삭제된 상품 제외) |
| `content[].id` | Long | 상품 고유 식별자 |
| `content[].name` | String | 상품명 |
| `content[].category` | String | 상품 카테고리 |
| `content[].price` | String | 상품 가격 (천 단위 구분 및 원 표시) |
| `content[].stock` | Integer | 상품 재고 |
| `content[].status` | String | 상품 상태 |
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
      "id": 2,
      "name": "직소퍼즐",
      "category": "완구/취미",
      "price": "25,000원",
      "stock": 46,
      "status": "ON_SALE",
      "createdAt": "2026-10-27T23:15:38",
      "adminName": "김운영",
      "adminEmail": "manager@sparta.com"
    },
    {
      "id": 1,
      "name": "레고세트",
      "category": "완구/취미",
      "price": "89,000원",
      "stock": 32,
      "status": "ON_SALE",
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

**Error Response** — `400`

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
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

**Request Headers / Body** — 별도 요구 없음

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 상품 고유 식별자 |
| `name` | String | 상품명 |
| `category` | String | 상품 카테고리 |
| `price` | String | 상품 가격 (천 단위 구분 및 원 표시) |
| `stock` | Integer | 상품 재고 |
| `status` | String | 상품 상태 |
| `createdAt` | LocalDateTime | 상품 등록일 |
| `adminName` | String | 상품 등록 관리자 이름 (관리자 정보 없으면 null) |
| `adminEmail` | String | 상품 등록 관리자 이메일 (관리자 정보 없으면 null) |

```json
{
  "id": 1,
  "name": "레고세트",
  "category": "완구/취미",
  "price": "89,000원",
  "stock": 32,
  "status": "ON_SALE",
  "createdAt": "2026-10-27T23:13:17",
  "adminName": "admin",
  "adminEmail": "admin@sparta.com"
}
```

**Error Response** — `400`, `404`

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

**Request Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `name` | String | 수정할 상품명 (선택, 공백 불가, 100자 이하) |
| `category` | String | 수정할 상품 카테고리 (선택, 공백 불가, 15자 이하) |
| `price` | Long | 수정할 상품 가격 (선택, 0원 이상) |

```json
{
  "name": "아이패드",
  "category": "전자기기",
  "price": 790000
}
```

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 상품 고유 식별자 |
| `name` | String | 수정된 상품명 |
| `category` | String | 수정된 카테고리 |
| `price` | String | 수정된 가격 (천 단위 구분 및 원 표시) |
| `updatedAt` | LocalDateTime | 상품 정보 수정일 |

```json
{
  "id": 1,
  "name": "아이패드",
  "category": "전자기기",
  "price": "790,000원",
  "updatedAt": "2026-10-27T23:29:41"
}
```

**Error Response** — `400`, `404`

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
| `id` | Long | 상품 고유 식별자 |
| `stock` | Integer | 변경된 재고 수량 |
| `status` | String | 변경 후 상품 상태 |
| `updatedAt` | LocalDateTime | 상품 정보 수정일 |

```json
{
  "id": 1,
  "stock": 0,
  "status": "OUT_OF_STOCK",
  "updatedAt": "2026-10-27T23:32:32"
}
```

**Error Response** — `400`, `404`

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
  "path": "/products/1/stock"
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
| `id` | Long | 상품 고유 식별자 |
| `status` | String | 변경된 상품 상태 |
| `updatedAt` | LocalDateTime | 상품 정보 수정일 |

```json
{
  "id": 1,
  "status": "DISCONTINUED",
  "updatedAt": "2026-10-27T23:38:42"
}
```

**Error Response** — `400`, `404`

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
  "path": "/products/1/status"
}
```

</details>

<details>
<summary><code>DELETE</code> <b>/products/{id}</b> — 상품 삭제</summary>

**Path Parameter**

| 키 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 삭제할 상품 ID |

**Request Headers / Body** — 별도 요구 없음

**Response** `200 OK`

상품을 DB에서 물리적으로 삭제하지 않고 논리 삭제 처리합니다.
삭제된 상품은 목록 조회에서 제외되며, 상세 조회 및 수정 시 조회되지 않은 상품으로 처리합니다.

```json
{
  "message": "요청하신 상품이 삭제되었습니다."
}
```

**Error Response** — `400`, `404`

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

| Method | Endpoint | 기능 |
| :---: | --- | --- |
| `POST` | `/orders` | 주문 생성 |
| `GET` | `/orders` | 주문 리스트 조회 |
| `GET` | `/orders/{orderId}` | 주문 상세 조회 |
| `PATCH` | `/orders/{orderId}/status` | 주문 상태 수정 |
| `DELETE` | `/orders/{orderId}` | 주문 취소 |

<details>
<summary><code>POST</code> <b>/orders</b> — 주문 생성</summary>

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 요청 본문 형식 |

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
| --- | --- | :---: | --- |
| `customerId` | Long | ✅ | 고객 ID |
| `productId` | Long | ✅ | 상품 ID |
| `quantity` | Integer | ✅ | 수량 |

```json
{
  "customerId": 12,
  "productId": 7,
  "quantity": 5
}
```

**Response** `201 Created`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 주문 ID |
| `orderNumber` | String | 주문번호 |
| `customerName` | String | 고객명 |
| `productName` | String | 상품명 |
| `quantity` | Integer | 수량 |
| `totalPrice` | Long | 총 금액 |
| `status` | String | 주문 상태 |
| `registeredAdminName` | String | 등록 관리자명 |
| `orderedAt` | LocalDateTime | 주문일 |

```json
{
  "id": 101,
  "orderNumber": "22",
  "customerName": "고객명",
  "productName": "상품명",
  "quantity": 3,
  "totalPrice": 87000,
  "status": "PREPARING",
  "registeredAdminName": "등록관리자명",
  "orderedAt": "2026-10-27T19:10:00"
}
```

</details>

<details>
<summary><code>GET</code> <b>/orders</b> — 주문 리스트 조회</summary>

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Cookie` | `JSESSIONID={세션 ID}` | ✅ | 로그인 세션 ID |

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `orders` | Array | 주문 목록 |
| `orders[].orderId` | Long | 주문 ID |
| `orders[].orderNumber` | Integer | 주문번호 |
| `orders[].customerName` | String | 고객명 |
| `orders[].productName` | String | 상품명 |
| `orders[].quantity` | Integer | 수량 |
| `orders[].totalCount` | Long | 금액 |
| `orders[].createdAt` | String | 주문일 |
| `orders[].status` | String | 주문 상태 |
| `orders[].adminName` | String | 등록 관리자명 |
| `orders[].adminEmail` | String | 등록 관리자 이메일 |
| `orders[].adminRole` | String | 등록 관리자 역할 |

```json
{
  "orders": [
    {
      "orderId": 2,
      "orderNumber": 2,
      "customerName": "고객명",
      "productName": "상품명",
      "quantity": 2,
      "totalCount": 16000,
      "createdAt": "2026-10-27T19:10:00",
      "status": "PREPARING",
      "adminName": "등록 관리자명",
      "adminEmail": "admin@sparta.com",
      "adminRole": "CS_ADMIN"
    },
    {
      "orderId": 1,
      "orderNumber": 1,
      "customerName": "고객명",
      "productName": "상품명",
      "quantity": 1,
      "totalCount": 8000,
      "createdAt": "2026-10-27T18:10:00",
      "status": "DELIVERED",
      "adminName": "등록 관리자명",
      "adminEmail": "admin@sparta.com",
      "adminRole": "CS_ADMIN"
    }
  ]
}
```

**Error Response**

| 상태 코드 | 상황 |
| --- | --- |
| `404 Not Found` | 존재하지 않는 ID 요청 시 |

</details>

<details>
<summary><code>GET</code> <b>/orders/{orderId}</b> — 주문 상세 조회</summary>

> `CS 주문`이 아닌 고객의 직접 주문인 경우 `등록 관리자 정보`는 존재하지 않습니다.

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Cookie` | `JSESSIONID={세션 ID}` | ✅ | 로그인 세션 ID |

**Query Parameter**

| 필드 | 타입 | 필수 | 설명 |
| --- | --- | :---: | --- |
| `userId` | Long | ✅ | 작성자 ID |

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `orderNumber` | Integer | 주문번호 |
| `customerName` | String | 고객명 |
| `productName` | String | 상품명 |
| `quantity` | Integer | 수량 |
| `totalCount` | Long | 금액 |
| `createdAt` | String | 주문일 |
| `status` | String | 주문 상태 |
| `adminName` | String | 등록 관리자명 |
| `adminEmail` | String | 등록 관리자 이메일 |
| `adminRole` | String | 등록 관리자 역할 |

```json
{
  "orderNumber": 2,
  "customerName": "고객명",
  "productName": "상품명",
  "quantity": 2,
  "totalCount": 8000,
  "createdAt": "2026-10-27T19:10:00",
  "status": "PREPARING",
  "adminName": "등록 관리자명",
  "adminEmail": "admin@sparta.com",
  "adminRole": "CS_ADMIN"
}
```

**Error Response**

| 상태 코드 | 상황 |
| --- | --- |
| `404 Not Found` | 존재하지 않는 ID 요청 시 |

</details>

<details>
<summary><code>PATCH</code> <b>/orders/{orderId}/status</b> — 주문 상태 수정</summary>

> 상태 변경 순서: 준비중 → 배송중 → 배송완료

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 데이터 형식 |

**Request Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `status` | String | 주문 상태 (`PREPARING`, `DELIVERING`, `DELIVERED`) |

```json
{
  "status": "DELIVERING"
}
```

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `status` | String | 변경된 주문 상태 |

```json
{
  "status": "DELIVERED"
}
```

**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
  "path": "/orders/1/status"
}
```

</details>

<details>
<summary><code>DELETE</code> <b>/orders/{orderId}</b> — 주문 취소</summary>

> 주문 취소는 `준비중(PREPARING)` 상태에서만 허용됩니다.

**Path Parameter**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `orderId` | Long | 주문 ID |

**Request Headers**

| Key | Value | 필수 | 설명 |
| --- | --- | :---: | --- |
| `Content-Type` | `application/json` | ✅ | 데이터 형식 |

**Request Body**

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `status` | String | 주문 상태 (`PREPARING`) |

```json
{
  "status": "PREPARING"
}
```

**Response** `200 OK`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `message` | String | 전달 메시지 |

```json
{
  "message": "주문이 취소되었습니다."
}
```

**Error Response**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "잘못된 요청입니다.",
  "path": "/orders/1"
}
```

</details>