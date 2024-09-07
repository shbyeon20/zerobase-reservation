## 개요
간단한 매장예약서비스

** Use : Spring, Jpa, MariaDb, Redis

** Goal : 사용자와 매장관리자 사이를 중개하는 매장 예약서비스를 구축해본다

## 회원관리 (파트너, 고객)
### 공통
- [ ] ID,PW를 기반으로 한 회원가입
- [ ] 로그인 토큰발행 (DAO authenticaition)
- [ ] 로그인 토큰을 통해 제어확인 (JWT와 필터사용)

## 매장
### 파트너
- [ ] 매장 등록하기
- [ ] 파트너명으로 매장의 리스트 조회하기

## 공통
- [ ] 매장명으로 매장조회하기

## 예약
### 고객
- [ ] 매장 예약 생성하기
- [ ] 매장 방문후 나의 예약조회
- [ ] 매장 방문후 나의 예약확정

### 파트너
- [ ] 매장별로 예약리스트 조회하기
- [ ] 특정 예약취소하기
  
## 리뷰
### 고객
- [ ] 자신의 예약에 대한 리뷰 생성,수정, 삭제하기
- * [ ] 리뷰 생성에 따라 매장의 평점이 변경하도록 연동시키기

### 파트너
- 자신의 예약에 대한 리뷰 삭제하기
