package codesquad.web;

import codesquad.CannotDeleteException;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    private static final Logger log = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QnaService questionService;

    @GetMapping("/form")
    public String form(@LoginUser User loginUser) {
        log.debug("loginUser={}", loginUser);
        return "/qna/form";
    }

    @PostMapping("")
    public String create(@LoginUser User loginUser, String title, String contents) {
        Question question = new Question(title, contents);
        log.debug("loginUser={}, question={}", loginUser, question);

        questionService.create(loginUser, question);
        return "redirect:/questions";
    }

    @GetMapping("")
    public String list(Model model) {
        AddQuestions(model, questionService.findAll());
        return "/qna/show";
    }

    private void AddQuestions(Model model, Iterable<Question> questions) {
        model.addAttribute("questions", questions);
    }

    @GetMapping("/{id}")
    public String getOne(Model model, @PathVariable long id) {
        AddQuestions(model, Collections.singletonList(questionService.findById(id)));
        return "/qna/show";
    }

    @DeleteMapping("/{id}")
    public String delete(@LoginUser User loginUser, Model model, @PathVariable long id) throws CannotDeleteException {
        questionService.deleteQuestion(loginUser, id);

        AddQuestions(model, questionService.findAll());
        return "/qna/show";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@LoginUser User loginUser, Model model, @PathVariable long id) {
        AddQuestions(model, Collections.singletonList(questionService.findById(id)));
        model.addAttribute("id", id);
        return "/qna/updateForm";
    }

    @PostMapping("/{id}")
    public String update(@LoginUser User loginUser, Model model, @PathVariable long id, String title, String contents) {
        Question updatedQuestion = new Question(title, contents);
        questionService.update(loginUser, id, updatedQuestion);

        AddQuestions(model, Collections.singletonList(questionService.findById(id)));
        return "/qna/show";
    }
}
