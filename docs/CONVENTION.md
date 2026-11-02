[← README로 돌아가기](../README.md)

# 🤝 팀 협업 컨벤션

> 프로젝트를 진행하며 팀이 함께 정하고 지킨 **코드 작성 규칙**과 **GitHub 협업 규칙**을 정리한 문서입니다.

---

## 📌 Java 코드 컨벤션

[Java 코드 컨벤션 (Oracle Code Conventions)](https://www.oracle.com/technetwork/java/codeconventions-150003.pdf)을 따릅니다.

| 항목 | 규칙 | 예시 |
|------|------|------|
| 들여쓰기 | 스페이스 **4칸** | |
| 중괄호 | 여는 중괄호 `{`는 같은 줄 끝에 | `public void cancel() {` |
| 조건문 · 반복문 | 한 줄이어도 **중괄호 생략 금지** | `if (stock == 0) { ... }` |
| 변수 선언 | 한 줄에 하나씩 | `int a, b;` ❌ → 두 줄로 나누기 |
| 공백 | 키워드와 괄호 사이, 연산자 앞뒤에 한 칸 | `if (a == b)`, `for (int i = 0; i < n; i++)` |

### Lombok 미사용

Lombok 없이 생성자와 getter를 직접 작성합니다.

| 대상 | 규칙 |
|------|------|
| 생성자 | 직접 작성, 엔티티에는 빈 생성자를 `protected`로 하나 둠 (아래 설명 참고) |
| getter | 모든 필드에 직접 작성 |
| setter | 만들지 않고, 값 변경은 의미 있는 메서드로 처리 (`changeStock()`, `cancelOrder()`) |

## 📌 패키지 구조

도메인(`admin`, `customer`, `product`, `order`)별로 패키지를 나누고, 각 도메인 안에 계층 패키지를 둡니다.
DTO는 `dto` 안에서 `request` / `response`로 나눕니다.

```text
src/main/java/com/starter/
├── admin
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   │   ├── request
│   │   └── response
│   ├── enums
│   └── exception
├── customer      # admin과 같은 구조
├── product
├── order
└── common
    ├── config    # JPA Auditing, PasswordEncoder
    └── entity    # BaseEntity
```

| 패키지 | 역할 |
|--------|------|
| `controller` | 요청을 받고 응답을 반환 |
| `service` | 비즈니스 로직, 트랜잭션 처리 |
| `repository` | DB 접근 |
| `entity` | JPA 엔티티 |
| `dto/request` | 요청 DTO |
| `dto/response` | 응답 DTO |
| `enums` | 상태 · 역할 등 Enum |
| `exception` | 도메인별 커스텀 예외 |

---

## 📌 네이밍 규칙

| 대상 | 규칙 | 예시 |
|------|------|------|
| 클래스 · 인터페이스 | PascalCase | `OrderService`, `OrderRepository` |
| 메서드 | camelCase + **동사로 시작** | `createOrder()`, `getOrderList()`, `cancelOrder()` |
| 변수 | camelCase | `orderNumber`, `totalPrice` |
| DTO | PascalCase + `Request` / `Response` | `OrderCreateRequest`, `OrderDetailResponse` |
| 패키지 | 전부 소문자 | `com.starter.order.dto.request` |
| DB 테이블 · 컬럼 | snake_case | `order_items`, `created_at` |
| 엔티티 | PascalCase + **단수형** | `Order`, `OrderItem` |

---

## 📌 예외 처리

상황별로 아래 상태 코드와 메시지를 사용합니다.

| 상태 코드 | 상황 | message |
|:---:|------|------|
| `400 Bad Request` | 잘못된 요청값 | `Invalid request` |
| `401 Unauthorized` | 인증되지 않은 사용자 | `Unauthorized user` |
| `403 Forbidden` | 접근 권한 없음 | `Permission Denied` |
| `404 Not Found` | 요청한 데이터가 존재하지 않음 | `Resource Not Found` |
| `409 Conflict` | 데이터 중복 | `Already exists` |

### 에러 응답 형식

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Resource Not Found",
  "path": "/orders/999"
}
```

---

## 📌 주석

| 구분 | 기준 |
|------|------|
| 메서드 설명 | **필수** — 메서드 위에 Javadoc(`/** */`) 작성 (`@param`, `@return` 포함) |
| 로직 설명 | **권장** — 강제하진 않지만, 다른 팀원이 봤을 때 설명이 필요한 부분은 꼭 남겨주세요! |

### Javadoc 작성 규칙

| 항목 | 작성 기준 | 작성법 |
|------|------|------|
| 메서드 설명 (첫 줄) | 모든 메서드에 가능 | 메서드가 하는 일을 한 문장으로 (`~한다.`) |
| `@param` | 파라미터가 있으면 필수 | **파라미터마다 하나씩** `@param 파라미터명 설명` |
| `@return` | 반환값이 있으면 필수 (`void`면 생략) | `@return 반환하는 값 설명` |

### 예시

```java
/**
 * 주문 상세 정보를 조회한다.
 *
 * @param orderId 조회할 주문 ID
 * @return 주문 상세 정보
 */
@Transactional(readOnly = true)
public OrderDetailResponse getOrderDetail(Long orderId) {

    // 1. orderId에 해당하는 주문 찾기 (없으면 404)
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));

    // 2. 응답 DTO 생성 후 반환
    return new OrderDetailResponse(...);
}

/**
 * 주문을 취소한다.
 *
 * @param orderId 취소할 주문 ID
 * @param request 주문 취소 요청 (취소 사유)
 */
@Transactional
public void cancelOrder(Long orderId, OrderCancelRequest request) {
    ...
}
```

---

## 📌 Issue

GitHub에서 이슈 생성 시 **자동으로 템플릿이 적용**됩니다.

### 📄 템플릿 파일
- [`.github/ISSUE_TEMPLATE/issue_template.md`](../.github/ISSUE_TEMPLATE/issue_template.md) — 기본 이슈 템플릿 (설명 / TO-DO / 기타)

### 이슈 제목 규칙

- 형식: `[유형] 작업 내용` (예: `[Feat] 로그인 API 구현`)
- 유형: `Feat` / `Fix` / `Refactor` / `Test` / `Docs` / `Chore`

### 사용 방법
1. GitHub > Issues > **New issue** 클릭
2. 템플릿 선택 후 내용 작성
3. 제출 후 생성된 **이슈 번호**로 브랜치 생성

---

## 📌 Branch

### 작업 흐름

```
기능 단위 이슈 생성 → 이슈 번호로 브랜치 생성 → 작업 · 커밋 → PR → 리뷰 · 승인 → develop에 merge
```

### 브랜치 종류

| 브랜치 | 용도 |
|--------|------|
| `main` | 최종 배포 및 심사 제출용 |
| `develop` | 개발 통합 브랜치 (모든 PR은 여기로) |
| `feature/*` | 새로운 기능 개발 |
| `fix/*` | `develop`에서 테스트하다 발견된 버그 수정 |
| `refactor/*` | 기능 변경 없는 코드 개선 |
| `test/*` | 테스트 코드 작성 |
| `docs/*` | 문서 작성 |
| `chore/*` | 기능 개발 외 작업 (의존성 버전 업, 빌드 스크립트 수정 등) |
| `release/*` | 배포 준비 최종 QA — `develop`을 `main`으로 넘기기 전 버전을 매기고 자잘한 버그 수정 |

### 브랜치 네이밍 규칙

- 형식: `[유형]/[이슈번호]-[이니셜]-[영문요약]`
- 예시: `feature/15-de-order-crud`, `fix/88-de-order-api-bugs`, `refactor/90-yj-session`, `docs/100-de-order-update-api-spec`
- `release`는 버전으로 이름 붙임 (예: `release/v1.0.0`)

```
--------main--------
    \
     --------develop--------
         \
          --------feature/15-de-order-crud
          \
           --------fix/88-de-order-api-bugs
```

---

## 📌 Commit

### 커밋 메시지 형식

```
<유형>: <작업요약> (#이슈번호)
```

> ⚠️ 작업요약과 `(#이슈번호)` 사이에 **꼭 띄어쓰기**를 해주세요.

### 예시

- `feat: 회원 유저 생성 API 구현 (#32)`
- `fix: 주문 취소 시 재고 복구 오류 수정 (#88)`
- `docs: 주문 API 명세 수정 (#100)`

### Commit Type

| 유형 | 설명 |
|------|------|
| `feat` | 새로운 기능 추가 |
| `fix` | 버그 수정 |
| `refactor` | 기능 변경 없는 코드 개선 |
| `test` | 테스트 코드 추가 · 수정 |
| `docs` | 문서 추가 · 수정 |
| `chore` | 설정 파일, 의존성, 빌드 스크립트 등 기타 작업 |

---

## 📌 PR

GitHub에서 PR 생성 시 **자동으로 템플릿이 적용**됩니다.

### 📄 템플릿 파일
- [`.github/pull_request_template.md`](../.github/pull_request_template.md) — 관련 이슈 / 작업 내용 / 테스트 / 리뷰어 체크 포인트

### PR 제목 규칙

- 형식: `[유형] 작업 내용` (예: `[Feat] 로그인 API 구현`)

### PR 규칙

- PR 템플릿을 빠짐없이 작성
- **`develop` 브랜치로** PR 생성
- **기능 단위로** 생성 — 너무 큰 PR 금지, **변경 코드 400줄 이하**
- 이슈가 완료되면 `Closes #이슈번호`, 진행 중이면 `Refs #이슈번호`로 연결
- PR을 올리면 **팀원에게 바로 공유**

### 리뷰 규칙

- **최소 1명 이상 승인** 후 머지
- 승인 없이 머지 금지

---

## 📌 업무 공유

| 방법 | 내용 |
|------|------|
| 📅 정기 회의 | 매일 **14:30** |
| 🗣 데일리 스크럼 | 오늘 할 일, 어제 한 일, 막힌 부분 공유 |
| 📝 GitHub Issue | 작업 단위를 이슈로 만들어 진행 상황 공유 |
