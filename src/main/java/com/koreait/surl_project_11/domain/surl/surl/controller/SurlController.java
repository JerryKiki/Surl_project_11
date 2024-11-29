package com.koreait.surl_project_11.domain.surl.surl.controller;

import com.koreait.surl_project_11.domain.surl.surl.entity.Surl;
import com.koreait.surl_project_11.domain.surl.surl.service.SurlService;
import com.koreait.surl_project_11.global.exceptions.GlobalException;
import com.koreait.surl_project_11.global.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
@Tag(name = "SurlController", description = "Surl 컨트롤러")
@Transactional(readOnly=true)
public class SurlController {

    private final Rq rq;
    private final SurlService surlService;

//    @GetMapping("/add")
//    @ResponseBody
//    @Transactional
//    public RsData<Surl> add(String body, String url) {
//
//        //프록시 객체로 get하기 때문에 이때는 깡통 객체고,
//        Member member = rq.getMember(); //현재 브라우저로 로그인 한 회원 정보
//
//        //이때 채운다. (안을 까보려고 했기 때문) ==> 이때 sql을 슬쩍 실행해서 진짜를 채운다
//        // ==> 필요에 의해서 효율적으로 sql을 실행할 수 있다!
//        //빈 깡통이라 발생하는 문제도 있으므로 완전히 이해하기 전까지는 주의해서 사용하자
//        //프로덕션 모드에서, sout보다 log.debug를 사용하는 것을 훨씬 추천
//        log.debug("before get id");
//        member.getId();
//        log.debug("after get id");
//
//        System.out.println("before get username");
//        member.getUsername();
//        System.out.println("after get username");
//
//        return surlService.add(member, body, url);
//    }
//
//    @GetMapping("/s/{body}/**")
//    @ResponseBody
//    @Transactional
//    public RsData<Surl> add(
//            @PathVariable String body,
//            HttpServletRequest req
//    ) {
//
//        Member member = rq.getMember();
//
//        String url = req.getRequestURI();
//
//        //복잡한 url이 들어왔을 때 queryString이 잘리지 않도록 하기 위함
//        if(req.getQueryString() != null) {
//            url += "?" + req.getQueryString();
//        }
//
//        String[] urlBits = url.split("/", 4);
//
//        url = urlBits[3];
//
//        return surlService.add(member, body, url);
//    }

    @GetMapping("/g/{id}")
    @Transactional
    @Operation(summary = "원본 URL로 리다이렉트")
    public String go(
            @PathVariable long id
    ){
        Surl surl = surlService.findById(id).orElseThrow(GlobalException.E404::new);

        surlService.increaseCount(surl);

        return "redirect:" + surl.getUrl();
    }

//    @GetMapping("/all")
//    @ResponseBody
//    public List<Surl> getAll() {
//        return surlService.findAll();
//    }
}

/*
도커가 뭐야?
개발한 앱과, 이를 실행하는데에 있어 필요한 '환경'까지 '함께 포장'해서 보내는 '포장지' 느낌
비유하자면 '휴대용 버너가 함께 들어있는 밀키트' 느낌.
집에 아무것도 없어도 괜찮고, 조리자가 백종원이든 이윤정이든 상관없어진다.
==> 서버 컴퓨터를 따로 세팅할 필요가 없다!! (혁-신)

도커 이미지는 밀키트와 비슷하지만 훨씬 더 편리합니다.
비유하자면, 밀키트는 밀키트인데 조리도구(가스레인지, 냄비 등)까지 포함된 밀키트라고 보시면 됩니다.
그래서 현대의 웹 서비스에서 주류기술로 자리잡았습니다.

도커 파일(Dockerfile) == 레시피(그대로 만들기 위함) ==> 이걸로 만드는 게 도커이미지
도커 파일은 도커 이미지를 빌드하기 위한 설정 파일로,
이미지 생성 과정에 필요한 명령어를 순서대로 담고 있습니다.
이 파일을 사용하면 이미지 빌드 과정을 자동화하고,
재사용 가능한 방식으로 관리할 수 있습니다.

이미지(Image) == '캠핑용' 밀키트(재료 + 조리도구)
이미지는 컨테이너를 생성하는 데 사용되는 템플릿으로,
애플리케이션 실행에 필요한 코드, 라이브러리, 환경설정 등이 포함됩니다.
읽기 전용이며, 컨테이너는 이 이미지를 기반으로 생성됩니다.

컨테이너(Container) == 음식(요리 결과물) == 도커 이미지로 만드는 게 컨테이너
컨테이너는 애플리케이션과 그 필요한 모든 것을 포함하는 격리된 환경입니다.
가볍고 빠르게 시작되며, 다른 컨테이너나 호스트 시스템과의 충돌 없이 독립적으로 실행됩니다.
이는 개발, 배포, 실행을 일관되게 만들어 줍니다.

* 도커 파일 - 1:N -> 도커 이미지 - 1:N -> 도커 컨테이너
도커 파일 하나로 도커 이미지 여러개를 만들 수 있고, 도커 이미지 하나로 여러개의 도커 컨테이너를 실행할 수 있다.

도커 데몬(Docker Daemon) ==> 도커의 핵심.
컴퓨터에 도커를 깐다, 서버에 도커를 설치한다라고 할때 깔리는 프로그램이 바로 도커 데몬.
도커 데몬은 컨테이너의 생성, 실행, 모니터링, 삭제 등을 관리하는
백그라운드 프로세스입니다.
도커 클라이언트와 통신하며,
도커의 모든 중요 작업을 담당합니다.

도커 허브(Docker Hub)
https://hub.docker.com/
도커 허브는 도커 이미지를 저장하고 공유할 수 있는 클라우드 서비스입니다.
사용자는 자신의 이미지를 업로드하여 공유할 수 있으며,
다른 사람이 만든 이미지를 검색하고 사용할 수 있습니다.
내가 필요한 도커 이미지를 다운로드 받을 수 있고, 내가 업로드할 수도 있음.
참고 : 도커 레지스트 ==> 사설.

<도커 이미지 만들기 ~깃배시에서 하면 됨~>
- HTML 파일을 원격지의 브라우저에게 전송하려면 웹서버가 필요
  - 웹서버 중 유명한 것이 nginx
  - nignx Docker Image는 이미 도커 허브에 있다.
- 목표: Docker Image 만들기
  - Docker Image는 DockerFile로부터 생성
- DockerFile 작성
  - 도커허브의 nginx:lastest를 기반으로 한다
- index.html 파일 생성 -> 소스코드
- 도커 이미지 생성
  - docker build -t {이미지이름} .
  - 도커 이미지 이름 : 리포지터리_이름:태그
    - 기본 태그 : latest
    - 참고 : latest는 생략 가능(태그를 latest로 쓰고 싶다면)
    - docker build -t nginx-1 . == docker build -t nginx-1:latest .
      - .은 현재 폴더를 나타냄, 특정 경로에 만들고 싶다면 경로를 써주면 됨)
      - 도커 파일 하나 당 이미지는 여러개 만들 수 있다!
- 생성된 도커 이미지 확인 명령어
  - docker images
- 생성된 도커 이미지 삭제 명령어
  - docker rmi {이미지이름(==리포지터리_이름:태그)(ex: nginx-1:latest)} or {IMAGE ID} .
  - 이름으로 입력할 때 태그가 latest라면 생략 가능
  - 이때, '현재 폴더'라는 의미의 .은 생략 가능 (만들 때는 경로를 따로 입력하지 않는 이상 생략하면 안됨!!!)

<도커 컨테이너 실행하기>
- $ docker run -d -p 80:80 --name nginx-1-1 nginx-1 (도커 이미지의 이름이 nginx-1라고 가정한다)
- 성공하면 localhost:80으로 접속 -> index.html에 적어둔 <Hello>가 출력되는 것을 확인할 수 있다
- 파일로 이미지를 여러개 만들 수 있듯, 이미지로 컨테이너를 여러개 만들 수도 있다
- $ docker run -d -p 8081:80 --name nginx-1-2 nginx-1 (도커 이미지의 이름이 nginx-1라고 가정한다)
==> localhost:8081로 접속하면 똑같이 Hello가 나온다!

<토막상식>
port랑 port forwarding ==> 포트를 할당해서 접근하는 것 : 나중에 찾아보기~

*/