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
* E커머스 플랫폼입니다. 다양한 상품을 판매합니다.
* Rest-Api 서버입니다.
* 회사에서 부서를 나누듯 해당 프로젝트도 엔티티별로 부서를 나누어서 작업하였습니다.
* 부서별(엔티티) 개발 문서는 위키에 기재가 되어있습니다.
* 위키를 보시면 자세한 알고리즘, 상세 요구사항 등이 기재되어있습니다.
* 해당 프로젝트는 5번 문서에 있는 스타일 가이드를 따라 제작하였고, 해당 스타일 가이드는 모든 부서에 동일하게 적용됩니다.

# 3. 전체 설계
## 팀별 위키
* [회원팀](https://github.com/liveforone/hypermart/wiki/%ED%9A%8C%EC%9B%90%ED%8C%80)
* [상품팀]()
* [추천팀]()
* [리뷰팀]() 
* [주문팀]()
* [장바구니팀]()
## 요구사항
* 회원은 3종류가 존재한다. MEMBER(일반회원), ADMIN(운영자), SELLER(판매자)
* 일반회원은 등급이 존재하며 등급에 따라 할인 폭이 달라진다.
* 회원등급은 구매를 할때마다 증가하며, 밤 11시에 모든 회원의 구매 횟수만큼 일괄 업데이트된다.
* 판매자는 다양한 상품을 판매 가능하며, 상품은 태그를 달 수 있다.
* 상품 이미지의 갯수는 제한이 없으나, 총 크기는 20MB로 제한된다.
* 또한 상품 이미지가 없이는 상품 등록이 불가능하다.
* 상품은 좋아요를 누를 수 있습니다. 기본적으로 상품은 좋아요 순으로 정렬된다.
* 상품은 리뷰를 달 수 있다.
* 리뷰와 좋아요는 모두 중복이 불가능하다.
* 상품은 품절이 아니라면 주문이 가능하다.
* 북마크는 존재하지 않는다. 대신 장바구니를 활용한다.
* 주문은 장바구니로도 할 수 있으며, 단품으로 할 수도 있다.
* 주문은 7일 안에 취소해야 정상적으로 취소된다.
* 주문시에 할인은 자동으로 들어간다. 
* 할인정책은 두 종류가 있다. 회원 등급할인과 상품 특가 할인이다.
* 상품 특가 할인은 저녁 9시부터 10시까지이며 전체 상품의 20%를 할인한다.

# 4. ER Diagram

# 5. 스타일 가이드
* 함수와 긴 변수의 경우 [줄바꿈 가이드](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/b.%20%EC%A4%84%EB%B0%94%EA%BF%88%EC%9C%BC%EB%A1%9C%20%EA%B0%80%EB%8F%85%EC%84%B1%EC%9D%84%20%ED%96%A5%EC%83%81%ED%95%98%EC%9E%90.md)를 지켜 작성하라.
* 유저를 제외한 모든 객체의 [널체크](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/c.%20%EA%B0%9D%EC%B2%B4%EC%9D%98%20Null%EA%B3%BC%20%EC%A4%91%EB%B3%B5%EC%9D%84%20%EC%B2%B4%ED%81%AC%ED%95%98%EB%9D%BC.md) + 중복 체크를 꼭 하라.
* 분기문은 반드시 [게이트웨이](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/d.%20%EB%B6%84%EA%B8%B0%EB%AC%B8%EC%9D%80%20gate-way%20%EC%8A%A4%ED%83%80%EC%9D%BC%EB%A1%9C%20%ED%95%98%EB%9D%BC.md) 스타일로 하라.
* [Mapper 클래스](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/e.%20Mapper%20%ED%81%B4%EB%9E%98%EC%8A%A4%EB%A5%BC%20%EB%A7%8C%EB%93%A4%EC%96%B4%20Entity%EC%99%80%20Dto%EB%A5%BC%20%EC%83%81%ED%98%B8%20%EB%B3%80%ED%99%98%ED%95%98%EB%9D%BC.md)를 만들어 entity와 dto를 상호 변환하라.
* 단순 for-each문은 [람다](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/f.%20%EB%8B%A8%EC%88%9C%20for-each%EB%AC%B8%EC%9D%84%20%EB%9E%8C%EB%8B%A4%EB%A1%9C%20%EB%B0%94%EA%BE%B8%EC%9E%90.md)로 바꿔라.
* 매직넘버는 전부 [enum](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/h.%20%EB%A7%A4%EC%A7%81%EB%84%98%EB%B2%84%EB%A5%BC%20enum%EC%9C%BC%EB%A1%9C%20%ED%95%B4%EA%B2%B0%ED%95%98%EB%9D%BC.md)으로 처리하라.
* 스프링 시큐리티에서 권한 체크 필요한것만 매핑하고 나머지(anyRequest)는 authenticated 로 설정해 코드를 줄이고 가독성 향상하라.
* [Utils 클래스](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/i.%20Util%20%ED%81%B4%EB%9E%98%EC%8A%A4%EB%A5%BC%20%EB%A7%8C%EB%93%A4%EC%96%B4%20%ED%8E%B8%EC%9D%98%EC%84%B1%EC%9D%84%20%EB%86%92%EC%97%AC%EB%9D%BC.md)를 적극 활용하고, 서비스로직에서 트랜잭션이 걸리지 않는 로직은 Utils 클래스에 담아서 모듈화하라.
* [네이밍은 직관적이게 하라](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/j.%20%EB%84%A4%EC%9D%B4%EB%B0%8D%EC%9D%80%20%EC%A7%81%EA%B4%80%EC%A0%81%EC%9D%B4%EA%B2%8C%20%ED%95%98%EB%9D%BC.md)
* 주석은 c언어 스타일 주석으로 선언하라.
* [함수 규칙](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/k.%20%ED%95%A8%EC%88%98%20%EA%B7%9C%EC%B9%99.md)을 지켜라.
* [좋은 테스트 코드 작성법](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/l.%20%EC%A2%8B%EC%9D%80%20%ED%85%8C%EC%8A%A4%ED%8A%B8%20%EC%9E%91%EC%84%B1%ED%95%98%EA%B8%B0.md)
* [양방향 연관관계를 지양하라](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/m.%20%EC%96%91%EB%B0%A9%ED%96%A5%20%EC%97%B0%EA%B4%80%EA%B4%80%EA%B3%84%EB%A5%BC%20%EC%A7%80%EC%96%91%ED%95%98%EB%9D%BC.md)
* [시간적인 결합이 있다면 명시하라](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/n.%20%EC%8B%9C%EA%B0%84%EC%A0%81%EC%9D%B8%20%EA%B2%B0%ED%95%A9%EC%9D%84%20%EB%AA%85%EC%8B%9C%ED%95%98%EB%9D%BC.md)
* [문서화 가이드](https://github.com/liveforone/study/blob/main/%5B%EB%82%98%EB%A7%8C%EC%9D%98%20%EC%8A%A4%ED%83%80%EC%9D%BC%20%EA%B0%80%EC%9D%B4%EB%93%9C%5D/o.%20%EB%AC%B8%EC%84%9C%ED%99%94%20%EA%B0%80%EC%9D%B4%EB%93%9C.md)

[할일]
item바디 만들기
item은 판매자가 업로드 가능(시큐리티)
주문시 유저 orderCount + 1

[팀 종류]
회원팀, 상품팀, 추천팀(좋아요), 리뷰팀, 주문팀, 장바구니팀

[팀위키]
팀 위키에는 팀이 만드는 엔티티에 대해 간단 정리
예를 들어 상품팀의 경우 Item과 UploadFile엔티티를 다룸.
주문팀은 할인시스템과 주문에 대해 다룸
팀별 상세 요구사항과 구현, 기술등에 대해 정리

[추천]
좋아요는 게시글안에 good을 만들고 good 테이블도 만든다.
좋아요를 누르면 good테이블안에 게시글과 유저가 저장된다.
그리고 게시글에도 good +1 이 된다.
좋아요를 누를때 email로 멤버를 찾고 id로 게시글을 찾아서 멤버와 게시글이 모두 같은 데이터가 있다면 중복이고 아니면 중복이 아닌것으로 한다.
여기서 주의 할점은 멤버와 게시글 객체로 안찾고 email, id등을 쓰면 조인이 꼬이게되니 조심하자. 가장 좋은것은 컨트롤로에서 서비스로 호출하고 좋아요 서비스에 넘기는 것이다.

[장바구니]
장바구니는 간단하다. 장바구니 테이블을 만들고 장바구니에 저장한다.
장바구니를 볼 수 있으며, 장바구니에서 필요하지 않은 상품은 삭제할 수 있다.
장바구니는 추가와 삭제만되는 구조이다.
장바구니로 주문하면 for-each문으로 주문()쿼리를 날린다.
for-each문안에 삭제 쿼리를 넣어서 주문하고 + 삭제하고 를 반복한다.