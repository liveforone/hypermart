# hypermart
> E-Commerce 서비스, 프로젝트 생성 시점(2023.1.11)부터 계속 릴리즈(리팩터링)되고 있습니다.

# 1. 사용 기술 스택
* Spring Boot 3.0.1
* Java17
* Spring Data Jpa & MySql
* Spring Security & Jwt
* LomBok
* Gradle
* Junit5

# 2. 설명
* 비즈니스 도메인은 E-커머스입니다. 다양한 상품을 판매하는 오픈마켓 플랫폼입니다.
* Rest-Api 서버입니다. 결제로직은 분리하였습니다(현 프로젝트에 결제로직은 없습니다).
* 각 주요기능(엔티티)별로 집중하고 싶어서 실제 회사에서 부서를 나누듯,
* 해당 프로젝트도 엔티티별로 부서를 나누어서 작업하였습니다.
* 부서별(엔티티) 개발 문서는 위키에 기재가 되어있습니다.
* [위키](https://github.com/liveforone/hypermart/wiki)를 보시면 자세한 알고리즘, 상세 요구사항, 구현 기술 등이 기재되어있습니다.
* 모든 문서는 위키로 제작하였고, 링크를 달아놓았으니 클릭하셔서 보실 수 있습니다.
* 해당 프로젝트는 5번 문서에 있는 스타일 가이드를 따라 제작하였고, 해당 스타일 가이드는 모든 부서에 동일하게 적용됩니다.

# 3. 전체 설계
## 팀별 위키
* [회원팀](https://github.com/liveforone/hypermart/wiki/%ED%9A%8C%EC%9B%90%ED%8C%80)
* [상품팀](https://github.com/liveforone/hypermart/wiki/%EC%83%81%ED%92%88%ED%8C%80)
* [추천팀](https://github.com/liveforone/hypermart/wiki/%EC%B6%94%EC%B2%9C%ED%8C%80)
* [리뷰팀](https://github.com/liveforone/hypermart/wiki/%EB%A6%AC%EB%B7%B0%ED%8C%80) 
* [장바구니팀]()
* [주문팀]()
## 요구사항
* 회원은 3종류가 존재한다. MEMBER(일반회원), ADMIN(운영자), SELLER(판매자)
* 일반회원은 등급이 존재하며 등급에 따라 할인 폭이 달라진다.
* 회원등급은 구매를 할때마다 증가하며, 밤 11시에 모든 회원의 구매 횟수만큼 일괄 업데이트된다.
* 판매자는 다양한 상품을 판매 가능하며, 상품은 카테고리 태그를 달 수 있다.
* 상품 이미지의 갯수는 제한이 없으나, 총 크기는 20MB로 제한된다.
* 또한 상품 이미지가 없이는 상품 등록이 불가능하다.
* 상품은 추천을 누를 수 있습니다. 기본적으로 상품은 추천순으로 정렬된다.
* 상품은 리뷰를 달 수 있다.
* 리뷰와 추천은 모두 중복이 불가능하다.
* 리뷰와 추천은 모두 주문한 사람만 가능합니다.(컨트롤러에서 판별함)
* 상품은 품절이 아니라면 주문이 가능하다.
* 북마크는 존재하지 않는다. 대신 장바구니를 활용한다.
* 주문은 장바구니로도 할 수 있으며, 단품으로 할 수도 있다.
* 주문은 7일 안에 취소해야 정상적으로 취소된다.
* 주문시에 할인은 자동으로 들어간다. 
* 할인정책은 두 종류가 있다. 회원 등급할인과 상품 특가 할인이다.
* 상품 특가 할인은 저녁 9시부터 10시까지이며 전체 상품의 20%를 할인한다.
* 모든 수정, 삭제의 과정은 작성자인지 반드시 프론트와 서버 모두에서 판별한다.
* 서버는 프론트에서도 판별할 수 있게 현재 유저와 작성자를 반드시 내보낸다.

# 4. ER Diagram

# 5. 스타일 가이드
* 스타일 가이드는 필자가 프로젝트를 진행하며 느낀 좋은 코드와, 
* 책을 읽다가 필자 또한 공감할 수 있는 내용들에 대해 필자의 생각으로 작성하였다.
* [나만의 스타일 가이드](https://github.com/liveforone/study/tree/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D)에서 전문을 읽을 수 있다.
* [가독성](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/b.%20%EA%B0%80%EB%8F%85%EC%84%B1.md)
* [Null과 중복체크](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/c.%20Null%EA%B3%BC%20%EC%A4%91%EB%B3%B5%20%EC%B2%B4%ED%81%AC.md)
* [분기문은 gate-way style로 하라](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/d.%20%EB%B6%84%EA%B8%B0%EB%AC%B8%EC%9D%80%20gate-way%20%EC%8A%A4%ED%83%80%EC%9D%BC%EB%A1%9C%20%ED%95%98%EB%9D%BC.md)
* [Mapper 클래스](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/e.%20Mapper%20%ED%81%B4%EB%9E%98%EC%8A%A4.md)
* [매직넘버를 없애라](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/f.%20%EB%A7%A4%EC%A7%81%EB%84%98%EB%B2%84%EB%A5%BC%20%EC%97%86%EC%95%A0%EB%9D%BC.md)
* [Util 클래스](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/g.%20Util%20%ED%81%B4%EB%9E%98%EC%8A%A4.md)
* [네이밍](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/h.%20%EB%84%A4%EC%9D%B4%EB%B0%8D.md)
* [함수 규칙](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/i.%20%ED%95%A8%EC%88%98.md)
* [좋은 테스트 코드](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/j.%20%EC%A2%8B%EC%9D%80%20%ED%85%8C%EC%8A%A4%ED%8A%B8%20%EC%BD%94%EB%93%9C.md)
* [명시적 프로그래밍](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/k.%20%EB%AA%85%EC%8B%9C%EC%A0%81%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D.md)
* [문서화 가이드](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/l.%20%EB%AC%B8%EC%84%9C%ED%99%94%20%EA%B0%80%EC%9D%B4%EB%93%9C.md)

# 6. 나의 고민
* [Jwt 리다이렉트는 어떻게 해야할까?](https://github.com/liveforone/hypermart/wiki/Jwt-%EB%A6%AC%EB%8B%A4%EC%9D%B4%EB%A0%89%ED%8A%B8%EB%8A%94-%EC%96%B4%EB%96%BB%EA%B2%8C-%ED%95%B4%EC%95%BC%ED%95%A0%EA%B9%8C%3F)
* [중복체크는 어떻게 해야할까?](https://github.com/liveforone/hypermart/wiki/%EC%A4%91%EB%B3%B5%EC%B2%B4%ED%81%AC%EB%8A%94-%EC%96%B4%EB%96%BB%EA%B2%8C-%ED%95%B4%EC%95%BC%ED%95%A0%EA%B9%8C%3F)
* [작성자 판별은 어디서 해야할까?](https://github.com/liveforone/hypermart/wiki/%EC%9E%91%EC%84%B1%EC%9E%90-%ED%8C%90%EB%B3%84%EC%9D%80-%EC%96%B4%EB%94%94%EC%84%9C-%ED%95%B4%EC%95%BC%ED%95%A0%EA%B9%8C%3F)

[할일]
장바구니팀 만들기(주문보다 먼저)
Basket {
private Long id;
private Member member;
private Item item;
}
생성, 삭제(수정 없음)
페이징 말고 리스트로 가져옴. 조건은 이메일로 가져옴
장바구니로 주문이 끝나고 나면 삭제하기
장바구니 만료기한을 정할까 했지만, 하지 않음. 영구임.
추천처럼 로그성 테이블임.
상품 삭제시 장바구니도 삭제됨

Orders {
    private Long id;
    private Item item;
    private Member member;
    private OrderStatus status; //ORDER, CANCEL
    private int orderCount;
    private LocalDate createdDate;
}
리뷰와 추천에서 주문했는지 체크하는 메서드
주문 취소
할인(회원등급, 특가)
주문시 유저 orderCount + 1
판매자 페이지에 상품별 주문갯수(sum쿼리이용)
주문이 완성되면, 추천과 리뷰를 save할때 주문한 상품인지(where절로 member, item, state 판별해서 가져오기)
판단하고 주문했다면 가능하게 하도록 추가하기. 문서화도 추가!

하나의 주문에 여러개의 상품을 담을 순 없다.
장바구니처럼 여러 상품을 주문시에는 for-each사용해서 다중 연산을 하면된다.


[팀 종류]
회원팀, 상품팀, 추천팀(좋아요), 리뷰팀, 장바구니팀, 주문팀(주문 + 할인 시스템), 

[장바구니]
장바구니는 간단하다. 장바구니 테이블을 만들고 장바구니에 저장한다.
장바구니를 볼 수 있으며, 장바구니에서 필요하지 않은 상품은 삭제할 수 있다.
장바구니는 추가와 삭제만되는 구조이다.
장바구니로 주문하면 for-each문으로 주문()쿼리를 날린다.
for-each문안에 삭제 쿼리를 넣어서 주문하고 + 삭제하고 를 반복한다.