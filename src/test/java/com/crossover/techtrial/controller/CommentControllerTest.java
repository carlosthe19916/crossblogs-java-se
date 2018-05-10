package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.Article;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setup() throws Exception {

    }

    @Test
    public void testCommentShouldBeCreated() throws Exception {
        HttpEntity<Object> comment = getHttpEntity("{\"email\": \"carlosthe19916@gmail.com\", \"message\": \"Congratulations this is a great post\" }");
        ResponseEntity<Article> resultAsset = template.postForEntity("/articles/1/comments", comment, Article.class);
        Assert.assertNotNull(resultAsset.getBody().getId());
    }

    @Test
    public void testGetCommentsByArticle() throws Exception {
        HttpEntity<Object> comment = getHttpEntity("{\"email\": \"carlosthe19916@gmail.com\", \"message\": \"This is a comment for the first blog\" }");
        ResponseEntity<Article> resultAsset = template.postForEntity("/articles/1/comments", comment, Article.class);

        ResponseEntity<List> searchResultAsset = template.getForEntity("/articles/1/comments", List.class);
        Assert.assertTrue(searchResultAsset.getBody().size() > 0);

        searchResultAsset = template.getForEntity("/articles/2/comments", List.class);
        Assert.assertEquals(0, searchResultAsset.getBody().size());
    }

    private HttpEntity<Object> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<Object>(headers);
    }

    private HttpEntity<Object> getHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<Object>(body, headers);
    }
}
