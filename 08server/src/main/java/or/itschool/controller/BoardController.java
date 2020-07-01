package or.itschool.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.log4j.Log4j;
import or.itschool.model.BoardVO;
import or.itschool.service.BoardService;

@Controller //컨트롤러 빈등록해주는 어노테이션
@Log4j //오류시 log41.2.17.jar 변경 :pom.xml
@RequestMapping("/board/*")
public class BoardController {
	
	//컨트롤러와 서비스가 의존관계가 설정되어있으니 의존성주입해준다.
	
	@Autowired
	//@Setter(onMethod_=@Autowired)
	private BoardService service;
	
	///////열람 요청 [GET]////////
	//게시글 목록 페이지 열람요정(모든 게시물 조회) 처리 메서드->이 메세지
	//'/list' 라는 요청이 오면 이 메서드가 작동, GET방식만 받도록 메서드속성
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String list(Model model)	 throws Exception {
		log.info("/board/list : GET 요청 발생!!");
		System.out.println("게시글 페이지 열람 요청!!!");
		
		List<BoardVO>articles = service.getAllArticles();
		System.out.println("===================================");
		for(BoardVO vo : articles)
			System.out.println(vo);
		System.out.println("====================================");
		
		model.addAttribute("articles", service.getAllArticles());
		
		return "board/list";
	}
	
	//게시글 작성화면 열람요청
	@RequestMapping(value = "/write", method=RequestMethod.GET)
	public String write() {
		log.info("/board/write : GET 요청 발생!!!");
		return "board/write";
	}
	
	//상세 페이지 화면 열람 요청
	@RequestMapping(value = "/content", method=RequestMethod.GET)
	public String content(@RequestParam("boardNo")int boardNo, Model model) throws Exception{     //게시물의 번호를 requestparam으로 가져온다
		log.info("/board/content : GET 요청 발생!!!");
		//DB에서 연결해 가져온 게시물을 article이라는 이름의 모델에 담는다
		model.addAttribute("article", service.getArticle(boardNo));
		return "board/content";
	}
	
	//게시글 수정 화면 열람 요청
	@RequestMapping(value="/modify", method=RequestMethod.GET)
	public String modify(@RequestParam("boardNo")int boardNo, Model model) throws Exception{
		log.info("/board/modify : GET 요청 발생!!!");
		model.addAttribute("article", service.getArticle(boardNo));     //requestparam으로 글번호를 가져온 뒤 글번호로 게시글 전체를 조회하고 article이름
		return "board/modify";
	}
	
	//게시판 기능 요청[POST]
	//게시물 등록 기능 메서드 요청 (같은 이름으로 함수를 만들 수 있다.오버로딩)
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String write(BoardVO article, RedirectAttributes redirectAttr) throws Exception{
		log.info("/board/write : POST요청");
		log.info("가져온 게시글 확인: "+article.toString());
		service.insert(article);
		redirectAttr.addFlashAttribute("message", "regSuccess");
		return "redirect:/board/list";
	}
	
	//게시물 수정 기능 메서드 요청
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String modify(BoardVO article, RedirectAttributes redirectAttr) throws Exception{
		log.info("/board/modify");
		service.update(article);    //게시글 수정 요청
		redirectAttr.addFlashAttribute("message", "modSuccess");     //수정 성공 시 임시데이터
		return "redirect:/board/content?boardNo="+article.getBoardNo();
	}
	
	//게시물 삭제 기능 메서드 요청
	@RequestMapping(value = "/delete", method=RequestMethod.POST)
	public String delete(@RequestParam("boardNo") int boardNo, RedirectAttributes redirectAttr) throws Exception{
		log.info("/board/delete : POST 요청!!!");
		service.delete(boardNo);
		redirectAttr.addFlashAttribute("message", "delSuccess");     //삭제 성공 시 임시데이터
		return "redirect:/board/list";
	}
}
