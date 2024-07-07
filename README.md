# 월부 학습 관리 앱

## 1. 구현 범위에 대한 설명

1. **회원 정보를 입력받아 저장**하는 API
   - 파라미터 검증
   - 이메일 중복 검증
2. **이메일, 비밀번호를 입력받아 로그인하고 액세스 토큰을 발급**하는 API
   - 액세스 토큰 만료 시 리프레시 토큰으로 재발급
   - 액세스토큰 만료 시간 1시간, 리프레시 토큰 만료 시간 10시간
3. **강의 개설을 위한 정보를 입력받아 저장**하는 API
   - 회원 유형에 따라 기능 실행 권한을 제한
4. **강의 목록을 조회**하는 API
   - 페이지네이션 및 정렬 기능(최근 등록 순, 신청자 많은 순, 신청률 높은 순)
   - 정렬 조건
     - 최근 등록 순 : CREATED_DATE_TIME
     - 신청자 많은 순 : REGISTRATION_COUNT
     - 신청률 높은 순 : REGISTRATION_RATE
   - 조회 예시
       - /courses?page=0&size=2&sort=CREATED_DATE_TIME,desc : 첫번째 페이지, 개수 2개, 최근 등록 순, 내림차순
5. **강의 아이디 리스트를 입력받아 수강신청**하는 API
   - 단일스레드로 동작하는 성질을 가진 redis 사용
   - 많은 사람이 동시 신청 시에도 최대 수강 인원만큼만 처리

## 2. 코드 빌드, 테스트, 실행 방법

### 요구사항
- Java 17 이상
- Gradle 7.3 이상
- h2 database

### Build

프로젝트를 빌드 명령은 다음과 같습니다.

```bash
./gradlew build
```

### Test

단위 테스트를 실행 명령은 다음과 같습니다.

```bash
./gradlew test
```

### Run

애플리케이션을 실행 명령은 다음과 같습니다.
1. 프로젝트 폴더 이동
2. docker-compose 실행하여 redis 실행
3. gradle 사용하여 어플리케이션 실행

```bash
docker-compose up -d
./gradlew bootRun
```

## 기타 접속정보

1. H2 콘솔 접속
    - http://localhost:8080/h2-console
    - 애플리케이션이 시작될 때 H2 인메모리 데이터베이스가 data.sql 파일을 실행하여 자동으로 초기화합니다.
2. API 문서
    - http://localhost:8080/swagger-ui/index.html

## 추가 구현 내용

1. Unit test 및 Integration test 작성
    - 비즈니스 영역 테스트 커버리지 100% 수행
2. 전역 예외 핸들링