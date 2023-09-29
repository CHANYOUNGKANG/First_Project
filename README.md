# First_Project / Back-End
# 코드컨벤션
 - 이름정하기  
   1.기본적으로 패키지는 소문자만 사용한다.  
   2.Entity
   - 로그인 : User  
   - 게시글 : Post  
   - 댓글 : Comment  
   - 시간 : TimeStamped  
   - 관리자권한 : UserRoleEnum  
   - 좋아요 : Like (구현 미정)  
   3.커밋 전에 코드정리하기 Ctlr+Alt+O / Ctlr+Alt+L  
   4.커밋 메세지 잘 알려주기  (남이 이해할 수 있도록)
# 역할분담 공정하게 사다리타기로 정했습니다 ^^
 - 강찬영 : 회원가입
 - 김우응 : 댓글
 - 이형철 : 게시물

# URL
https://hh99-music-test.vercel.app/main
https://www.chanyoungkang.com/api/posts

# 기술 스택 
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"><img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"><img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"><img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"><img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/apache tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=white"><img src="https://img.shields.io/badge/Cloud-Google-blue.svg?style=for-the-badge&logo=Google Cloud Platform" alt="Cloud: Google">
  <img src="https://img.shields.io/badge/Cloud-AWS-9cf.svg?style=for-the-badge&logo=Amazon Web Services" alt="Cloud: AWS">  <img src="https://img.shields.io/badge/Spring Security-2023.0.0-orange.svg?style=for-the-badge&logo=Spring Security" alt="Spring Security: 2023.0.0">


# 서비스 아키텍쳐
![image](https://github.com/CHANYOUNGKANG/blog/assets/140377196/f74387e0-e51e-4087-8381-2161636d2e0d)

# API 명세서
https://mixed-chiller-3b3.notion.site/8a2bd5cdc94f496f8babe3881ea798a5?v=a2c039acfa0042b29f48aad74b8bf2c5&pvs=4

# ERD
![image](https://github.com/CHANYOUNGKANG/myselectshop/assets/140377196/9bb309e8-d8c7-4290-80f7-41da012578a3)

# 개발환경

Backend

IntelliJ
spring boot 3.1.3
spring-boot-jpa
Spring Security
Java 17
mysql
🖱CI/CD

cloudtype
github Action



# 트러블 슈팅

## CORS

백엔드 부분 첫번째로 소개해드릴 트러블 슈팅은 CORS(same origin policy) 부분으로
프로토콜, 포트번호, 호스트가 다르면 요청에 대한 응답을 브라우저에서 받을 수 없었습니다. 처음에는 스프링에서만 적용 시키면 될 줄 알고 코드를 작성하였습니다, 하지만 회원가입 하고 나서 로그인 하는 과정에 오류가 생겨 구글링 해보니 스프링 시큐리티를 사용 중일 경우, 스프링과 별개로 cors 설정을 해주어야 한다고 하여서 해결하게 되었습니다.

## 소셜 로그인

카카오 소셜 로그인 부분에서 백엔드 부분에서 카카오 측에 요청을 보내서 코드를 보내고
redirect uri에서 토큰을 받아오는 부분에서 트러블 슈팅이 발생했었는데 redirect uri를 프론트분들 서버로도 확인해보고 백엔드 서버에서도 확인해 봤는데 잘 적용되지 않았습니다.
카카오 develop 문서를 따라서 처음부터 다시 redirect uri를 잘 맞춰서 문제를 해결할 수 있었습니다.

## GIT SETTING

클라우드타입으로 배포과정에서 인텔리제이 setting.gradle에서 rootProject.name 세팅을 잘못해서 에러가 발생했습니다. github merge 과정에서 같은 팀원 분 setting을 그대로 가져왔기 때문입니다.
setting.gradle에서 프로젝트 이름을 올바르게 삽입하여 해결하였습니다. 이 트러블 슈팅을 통해 팀원의 코드를 가져올 때는 주의해야 한다는 교훈을 얻었습니다. 








