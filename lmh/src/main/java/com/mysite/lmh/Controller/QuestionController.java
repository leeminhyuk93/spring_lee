package com.mysite.lmh.Controller;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.mysite.lmh.entity.Answer;

import com.mysite.lmh.entity.Question;
import com.mysite.lmh.entity.SiteUser;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.mysite.lmh.Service.QuestionService;
import com.mysite.lmh.Service.UserService;
import com.mysite.lmh.dto.AnswerCreateForm;
import com.mysite.lmh.dto.QuestionForm;

@Controller
@RequestMapping("/question")
public class QuestionController {

	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private	UserService userService;
	
	@GetMapping("/list")
	public String getQuestions(@RequestParam(name = "page", defaultValue = "0") int page, Model model
			, HttpSession session) {
		int listNums = 10; // 한 번에 보여질 게시물 수
		int blockSize = 5; // 한 번에 보여질 하단의 페이지 수
		
		Page<Question> questionPage = this.questionService.getQuestions(page, listNums);
		long totalQuestion = questionPage.getTotalElements(); // 총 개시물 수
		int totalPages = questionPage.getTotalPages(); // 총 페이지 수
		
		// 현재 를록 번호
		int currentBlock = page / blockSize;
		
		// 시작 페이지와 끝 페이지
		int startPage = currentBlock * blockSize;
		int endPage = Math.min(startPage + blockSize, totalPages);
		
		model.addAttribute("questionList", questionPage.getContent()); // 현재 페이지의 데이터
		model.addAttribute("totalPages", totalPages); // 총 페이지 수
		model.addAttribute("totalQuestion", totalQuestion); // 전체 게시물 수
		model.addAttribute("currentPage", page); // 현재 페이지 번호
		model.addAttribute("listNums", listNums); // 현재 페이지 출력 수
		
		// 페이지 블록 관련
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("hasPrevBlock", startPage > 0);
		model.addAttribute("hasNextBlock", endPage < totalPages);
		
		String message = (String) session.getAttribute("message");
		String messageIcon = (String) session.getAttribute("messageIcon");
		
		if (message != null) {
			model.addAttribute("message", message);
			session.removeAttribute("message");
		}
		
		if (messageIcon != null) {
			model.addAttribute("messageIcon", messageIcon);
			session.removeAttribute("messageIcon");
		}
		
		return "board/questionList";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String questionCreate(Model model, HttpSession session) {		
		model.addAttribute("questionForm", new QuestionForm());
		
		String message = (String) session.getAttribute("message");
		String messageIcon = (String) session.getAttribute("messageIcon");
		
		if (message != null) {
			model.addAttribute("message", message);
			session.removeAttribute("message");
		}
		
		if (messageIcon != null) {
			model.addAttribute("messageIcon", messageIcon);
			session.removeAttribute("messageIcon");
		}
		
		return "board/questionForm";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, 
			Model model, RedirectAttributes redirectAttribute, HttpSession session, Principal principal) {
	
		
		if (questionForm.getSubject() == null || questionForm.getSubject().trim().isEmpty()) {
			bindingResult.reject("subjectIsNothing", "제목이 입력되지 않았습니다.");
			
		} else if (questionForm.getContent() == null || questionForm.getContent().trim().isEmpty()) {
			bindingResult.reject("contentIsNothing", "내용이 입력되지 않았습니다.");
		}
		
		if (bindingResult.hasErrors()) {
			return "board/questionForm";
		}
		
		try {
			SiteUser author = this.userService.getUserByUsername(principal.getName()).orElse(null);
			this.questionService.create(questionForm.getSubject(), questionForm.getContent(), author);
			
		} catch (CannotCreateTransactionException e) {
			bindingResult.reject("createException", "게시물을 등록하지 못했습니다.");
			return "board/questionForm";
			
		} catch (Exception e) {
			bindingResult.reject("createException", "Error: " + e.getMessage());
			return "board/questionForm";
		}
		
		redirectAttribute.addFlashAttribute("message", "질문이 성공적으로 등록되었습니다.");
		redirectAttribute.addFlashAttribute("messageIcon", "success");
		return "redirect:/question/list";
	}
	
	@GetMapping("/detail/{id}")
	public String questionDetail(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttribute
			, HttpSession session, Principal principal) {
		Question question = this.questionService.getQuestion(id);
		if (question != null) {
			List<Answer> answerListReversed = question.getAnswerList();
			
			model.addAttribute("answerList", answerListReversed);
			model.addAttribute("question", question);
			model.addAttribute("answerCreateForm", new AnswerCreateForm());
			String loggedUserName = (principal != null) ? principal.getName() : null;
			boolean isOwner = (loggedUserName != null && loggedUserName.equals(question.getAuthor().getUsername()));
			model.addAttribute("isOwner", isOwner);
			
			String message = (String) session.getAttribute("message");
			if (message != null) {
				String messageIcon = (String) session.getAttribute("messageIcon");
				session.removeAttribute("message");
				session.removeAttribute("messageIcon");
				model.addAttribute("message", message);
				model.addAttribute("messageIcon", messageIcon);
			}
			
			return "board/questionDetail";
		} else {
			redirectAttribute.addFlashAttribute("messageIcon", "error");
			redirectAttribute.addFlashAttribute("message", "게시물을 찾을 수 없습니다.");
			return "redirect:/question/list";
		}
	}
	
	@GetMapping("/modify")
	public String questionModify(@RequestParam("id") Long id, Model model) {
		Question question = this.questionService.getQuestion(id);
		QuestionForm questionForm = new QuestionForm();
	    questionForm.setSubject(question.getSubject());
	    questionForm.setContent(question.getContent());
	    
		model.addAttribute("question", question);
		model.addAttribute("questionForm", questionForm);
		return "board/questionUpdate";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, 
			@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttribute) {
		Question question = this.questionService.getQuestion(id);
		
		if (bindingResult.hasErrors()) {
			return "board/questionUpdate";
		}
		
		this.questionService.update(id, questionForm.getSubject(), questionForm.getContent());
		redirectAttribute.addFlashAttribute("messageIcon", "success");
		redirectAttribute.addFlashAttribute("message", "수정이 완료되었습니다.");
		return "redirect:/question/detail/" + question.getId();
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete")
	public String questionDelete(@RequestParam("id") Long id, RedirectAttributes redirectAttribute) {
		Question question = this.questionService.getQuestion(id);
		if (question != null) {
			this.questionService.delete(question);
			redirectAttribute.addFlashAttribute("messageIcon", "success");
			redirectAttribute.addFlashAttribute("message", "게시물이 삭제되었습니다.");
			return "redirect:/question/list";
		} else {
			return "/";
		}
	}
}

