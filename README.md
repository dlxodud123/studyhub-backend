# 🌱 StudyHub Backend

스터디 모집 및 관리 플랫폼의 백엔드 서버입니다.  
사용자는 로그인 후 스터디를 모집하거나 참여할 수 있으며,  
관리자는 전체 회원 및 스터디를 관리할 수 있습니다.

---

## 📁 프로젝트 구조
> ⚙️ init 브랜치 단계에서는 클래스 생성 없이 패키지 구조와 설정 파일만 준비합니다.

---

## 🚀 주요 기능 (Planned Features)

- 회원가입 / 로그인 (JWT)
- 소셜 로그인 (Google, Kakao)
- 스터디 모집 게시판 CRUD
- 댓글 및 좋아요 기능
- 관리자 페이지 (회원 / 스터디 관리)
- 예외 처리 및 응답 통일화
- AWS 배포 및 CI/CD 구축

---

## 🛠️ 기술 스택

| 구분        | 기술                                |
|------------|-----------------------------------|
| Backend     | Spring Boot 3.x                   |
| DB / ORM    | MySQL, Spring Data JPA, Querydsl |
| 인증        | Spring Security, JWT, OAuth2      |
| Build       | Gradle                             |
| Language    | Java 17                            |
| Docs        | Swagger (Springdoc OpenAPI)       |

---

## 🌿 브랜치 전략

| 브랜치      | 설명                                         |
|------------|--------------------------------------------|
| main       | 최종 배포 브랜치                             |
| develop    | 개발 통합 브랜치                             |
| init       | 초기 세팅 브랜치 (패키지/설정 파일)         |
| feature/*  | 기능 단위 개발 브랜치 (ex. feature/login, feature/study-board) |

---
