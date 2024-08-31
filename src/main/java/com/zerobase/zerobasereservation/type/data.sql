 /*h2 db에서 다른 db로 변경시 문제 발생가능*/

 /* partner_entity 초기데이터*/

 INSERT INTO partner_entity (id, partner_id, partner_name, business_id, phone_number, registered_at)
 VALUES (1, 'partner1', 'james', '1234567890', '01012345678', '2023-01-01 10:00:00');

 INSERT INTO partner_entity (id, partner_id, partner_name, business_id, phone_number, registered_at)
 VALUES (2, 'partner2', 'john', '2345678901', '01023456789', '2023-01-02 11:00:00');

 INSERT INTO partner_entity (id, partner_id, partner_name, business_id, phone_number, registered_at)
 VALUES (3, 'partner3', 'michel', '3456789012', '01034567890', '2023-01-03 12:00:00');

 INSERT INTO partner_entity (id, partner_id, partner_name, business_id, phone_number, registered_at)
 VALUES (4, 'partner4', 'jacob', '4567890123', '01045678901', '2023-01-04 13:00:00');

 INSERT INTO partner_entity (id, partner_id, partner_name, business_id, phone_number, registered_at)
 VALUES (5, 'partner5', 'samuel', '5678901234', '01056789012', '2023-01-05 14:00:00');


/* store_entity 초기데이터*/

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (1, 'partner1', '투썸플레이스동대문', '서울시동대문구용두동', '청결한 매장', '2023-01-01T08:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (2, 'partner1', '투썸플레이스광화문', '서울시종로구세종로', '깔끔한 인테리어', '2023-01-02T09:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (3, 'partner1', '투썸플레이스강남', '서울시강남구역삼동', '트렌디한 공간', '2023-01-03T10:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (4, 'partner1', '투썸플레이스명동', '서울시중구명동1가', '편안한 분위기', '2023-01-04T11:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (5, 'partner2', '스타벅스강남', '서울시강남구역삼동', '조용한 분위기', '2023-01-05T12:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (6, 'partner2', '스타벅스광화문', '서울시종로구세종로', '프리미엄 음료', '2023-01-06T13:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (7, 'partner2', '스타벅스명동', '서울시중구명동2가', '활기찬 분위기', '2023-01-07T14:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (8, 'partner2', '스타벅스신촌', '서울시서대문구창천동', '젊은 층이 많음', '2023-01-08T15:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (9, 'partner3', '이디야커피홍대', '서울시마포구서교동', '넓은 공간', '2023-01-09T16:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (10, 'partner3', '이디야커피강남', '서울시강남구논현동', '친절한 직원들', '2023-01-10T17:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (11, 'partner3', '이디야커피종로', '서울시종로구종로1가', '깔끔한 인테리어', '2023-01-11T18:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (12, 'partner3', '이디야커피명동', '서울시중구명동3가', '훌륭한 커피', '2023-01-12T19:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (13, 'partner4', '카페베네종로', '서울시종로구종로1가', '좋은 위치', '2023-01-13T20:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (14, 'partner4', '카페베네강남', '서울시강남구역삼동', '트렌디한 메뉴', '2023-01-14T21:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (15, 'partner4', '카페베네명동', '서울시중구명동2가', '아늑한 분위기', '2023-01-15T22:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (16, 'partner4', '카페베네신촌', '서울시서대문구창천동', '학생들이 많이 찾는 곳', '2023-01-16T23:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (17, 'partner5', '커피빈광화문', '서울시종로구세종로', '탁 트인 전망', '2023-01-17T08:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (18, 'partner5', '커피빈강남', '서울시강남구역삼동', '넓고 조용한 공간', '2023-01-18T09:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (19, 'partner5', '커피빈명동', '서울시중구명동3가', '훌륭한 커피', '2023-01-19T10:00:00');

 INSERT INTO store_entity (id, partner_id, store_id, address, store_comment, registered_at)
 VALUES (20, 'partner5', '커피빈신촌', '서울시서대문구창천동', '편안한 분위기', '2023-01-20T11:00:00');
