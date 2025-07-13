## 개요
간단한 매장예약서비스

** Use : Spring, Jpa, MariaDb, Redis

** Goal : 사용자와 매장관리자 사이를 중개하는 매장 예약서비스를 구축해본다

## 회원관리 (파트너, 고객)
### 공통
- [x] ID,PW를 기반으로 한 회원가입
- [x] 로그인 토큰발행 (DAO authenticaition)
- [x] 로그인 토큰을 통해 제어확인 (JWT와 필터사용)

## 매장
### 파트너 (권한제어 필요)
- [x] 매장 등록하기
- [x] 파트너명으로 매장의 리스트 조회하기

## 공통
- [x] 매장명으로 매장조회하기

## 예약

### 파트너
- [x] 매장별로 예약가능 슬롯 생성하기
- [x] 매장별로 예약리스트 조회하기
- [x] 특정 예약취소하기

### 고객
- [x] 매장 예약 생성하기 ( 예약가능여부 확인하는 로직 추가)
- [x] 매장 방문후 나의 예약조회 (예약시간 10분전에 방문하지 않으면 예약불가)
- [x] 매장 방문후 나의 예약확정 (예약이 확정가능한 상태인지 유효성 확인)

  
## 리뷰
### 고객
- [x] 자신의 예약에 대한 리뷰 생성,수정, 삭제하기 (권한제어)
- [x] 리뷰 생성에 따라 매장의 평점이 변경하도록 연동시키기

### 파트너
- [x] 자신의 예약에 대한 리뷰 삭제하기 (권한제어)

## ERD & FlowChart

![reservation (1)](https://github.com/user-attachments/assets/64746c4b-7cf5-4b06-946f-1abed967134d)


<img width="500" height="500" alt="UML (2)" src="https://github.com/user-attachments/assets/f1d9d712-4a79-43d2-b265-23af99156123" />


## 구현 및 문제상황 해결
<img width="250" height="250" alt="UML (1) (1)" src="https://github.com/user-attachments/assets/9292b8cf-904c-4ad1-b269-c590a323147d" />
<img width="250" height="250" alt="UML (1) (2)" src="https://github.com/user-attachments/assets/2f2f1a23-5042-4a96-9370-bd06b89a5c24" />


- 배경 :  매장의 평점은 리뷰 평점의 평균으로 산출하기 때문에 매장의 리뷰데이터 조회가 필요. 새로운 리뷰데이터 생성시 매장의 평점 재산출 필요
- 문제사항  :  리뷰 생성시, 매장 평점 재산출을 위해 리뷰데이터 전체 조회가 필요하며 정규화 구조에서는 예약 데이터를 거쳐야 매장의 리뷰데이터 접근가능 (시간복잡도 LogB*LogD*K)
- 개선안  :  역정규화를 통해 리뷰 테이블에 매장 아이디를 추가하여 예약 테이블 접근없이 즉시 조회가능 (시간복잡도 LogD)

<img width="250" height="250" alt="UML (3)" src="https://github.com/user-attachments/assets/444dead4-29a6-4bf5-beaf-3d41bdae13cc" />


- 문제점 :  JWT Library HandlerJwtHandler와 MemberAuthService 간의 순환참조 문제발생
- 문제원인 : 일관되지 못한 의존성 방향 설정
- 해결방안 : 계층분리와 다형성을 활용한 객체지향구조 적용
    - ‘AuthService’ 계층을 신규 도입하여 토큰 클래스와 유저정보 클래스를 통합관리
    - OCP, DIP 적용하여 강한결합관계 해체
