[← README로 돌아가기](../../README.md)

# 🔥 트러블슈팅

Starter 프로젝트를 진행하며 겪은 문제와 해결 과정을 정리한 트러블슈팅 모음입니다.
각 글의 자세한 내용은 링크된 velog에서 확인할 수 있습니다.

<br>

| 목차 | 제목 | 내용 | 링크 | 작성자 |
| :---: | --- | --- | :---: | :---: |
| 1 | Enum 필드 오류 메시지 노출 | Enum 필드에 빈 문자열을 보내면 Jackson 내부 에러가 그대로 응답됨 → `HttpMessageNotReadableException` 처리로 400 응답 통일 | [velog](https://velog.io/@luke1495/TIL-오류-메시지-출력-트러블-슈팅) | 재원 |
| 2 | 필수 기능 구현 중 겪은 문제들 | IntelliJ 캐시 오류, 엔티티 테이블 미생성, 연관관계에 ID 대신 엔티티 전달, Postman 요청 Body 누락 | [velog](https://velog.io/@jun0zoo/Spring-Boot-팀-프로젝트-필수과제) | 영주 |
| 3 | 등록 관리자 없는 주문 조회 시 NPE | 등록 관리자가 없는(`admin_id`가 NULL) 주문의 상세·목록 조회 시 500 에러 → `registerAdmin` null 체크로 관리자 정보를 null 처리 | [velog](https://velog.io/@ddaaee/NPE) | 다은 |
<!--
  글이 더 있으면 아래 줄을 복사해서 목차 번호만 바꿔 추가하세요
  | 3 | 제목 | 한 줄 요약 | [velog](https://velog.io/@아이디/글-주소) | 이름 |
-->