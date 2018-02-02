package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/questions")
public class ApiQuestionController {

    @Resource(name = "qnaService")
    private QnaService qnaService;

    @PostMapping("")
    public ResponseEntity<Void> create(@LoginUser User loginUser, @Valid @RequestBody QuestionDto questionDto) {
        Question question = new Question(questionDto);
        Question savedQuestion = qnaService.create(loginUser, question);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/questions/" + savedQuestion.getId()));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
    
    @GetMapping("{id}")
    public QuestionDto show(@PathVariable long id) {
        Question question = qnaService.findById(id);
        return question.toQuestionDto();
    }

    @PutMapping("{id}")
    public void update(@LoginUser User loginUser, @PathVariable long id, @Valid @RequestBody QuestionDto updatedQuestionDto) {
        Question updatedQuestion = new Question(updatedQuestionDto);
        qnaService.update(loginUser, id, updatedQuestion);
    }
}
