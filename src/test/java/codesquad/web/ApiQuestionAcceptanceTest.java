package codesquad.web;

import codesquad.dto.QuestionDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {

    @Test
    public void createAndSelect() {
        QuestionDto newQuestion = new QuestionDto("title test", "question test");
        String location = createQuestion(newQuestion);

        QuestionDto dbQuestion = basicAuthTemplate().getForObject(location, QuestionDto.class);
        assertThat(dbQuestion.getTitle(), is(newQuestion.getTitle()));
        assertThat(dbQuestion.getContents(), is(newQuestion.getContents()));
    }

    private String createQuestion(QuestionDto newQuestion) {
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        return response.getHeaders().getLocation().getPath();
    }

    @Test
    public void update() {
        QuestionDto newQuestion = new QuestionDto("title test2", "question test2");
        String location = createQuestion(newQuestion);

        QuestionDto updatedQuestion = new QuestionDto("updated title test2", "updated question test2");
        basicAuthTemplate().put(location, updatedQuestion);

        QuestionDto dbQuestion = basicAuthTemplate().getForObject(location, QuestionDto.class);
        assertThat(dbQuestion.getTitle(), is(updatedQuestion.getTitle()));
        assertThat(dbQuestion.getContents(), is(updatedQuestion.getContents()));
    }

    @Test
    public void updateFailedBecauseOfNotSameUser() {
        QuestionDto newQuestion = new QuestionDto("title test2", "question test2");
        String location = createQuestion(newQuestion);

        QuestionDto updatedQuestion = new QuestionDto("updated title test2", "updated question test2");
        basicAuthTemplate(defaultUserAsSANJIGI()).put(location, updatedQuestion);

        QuestionDto dbQuestion = basicAuthTemplate().getForObject(location, QuestionDto.class);
        assertThat(dbQuestion.getTitle(), is(newQuestion.getTitle()));
        assertThat(dbQuestion.getContents(), is(newQuestion.getContents()));
    }

    @Test
    public void delete() {
        QuestionDto newQuestion = new QuestionDto("title test3", "question test3");
        String location = createQuestion(newQuestion);

        basicAuthTemplate().delete(location);

        QuestionDto dbQuestion = basicAuthTemplate().getForObject(location, QuestionDto.class);
        assertNull(dbQuestion);
    }

    @Test
    public void deleteFailedBecauseOfNotSameUser() {
        QuestionDto newQuestion = new QuestionDto("title test4", "question test4");
        String location = createQuestion(newQuestion);

        basicAuthTemplate(defaultUserAsSANJIGI()).delete(location);

        QuestionDto dbQuestion = basicAuthTemplate().getForObject(location, QuestionDto.class);
        assertThat(dbQuestion.getTitle(), is(newQuestion.getTitle()));
        assertThat(dbQuestion.getContents(), is(newQuestion.getContents()));
    }
}

