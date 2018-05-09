package com.crossover.techtrial.controller;

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

import com.crossover.techtrial.model.Article;

import java.time.LocalDateTime;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setup() throws Exception {

    }

    @Test
    public void testArticleShouldBeCreated() throws Exception {
        HttpEntity<Object> article = getHttpEntity("{\"email\": \"user1@gmail.com\", \"title\": \"hello\" }");
        ResponseEntity<Article> resultAsset = template.postForEntity("/articles", article, Article.class);
        Assert.assertNotNull(resultAsset.getBody().getId());
    }

    @Test
    public void testArticleTitleLength() throws Exception {
        String titleWith121Length = String.format("%0" + 121 + "d", 0).replace('0', '0');

        HttpEntity<Object> article = getHttpEntity("{\"email\": \"user1@gmail.com\", \"title\": \"" + titleWith121Length + "\" }");
        ResponseEntity<Article> resultAsset = template.postForEntity("/articles", article, Article.class);
        Assert.assertTrue(resultAsset.getStatusCode().is4xxClientError());
    }

    @Test
    public void testGetArticleById() throws Exception {
        ResponseEntity<Article> resultAsset = template.getForEntity("/articles/-1", Article.class);
        Assert.assertEquals(resultAsset.getStatusCodeValue(), 404);
        Assert.assertNull(resultAsset.getBody());

        resultAsset = template.getForEntity("/articles/1", Article.class);
        Assert.assertEquals(Long.valueOf(1L), resultAsset.getBody().getId());
    }

    @Test
    public void testUpdateArticle() throws Exception {
        HttpEntity<Object> article = getHttpEntity("{\"email\": \"carlosthe19916@gmail.com\", \"title\": \"Changed title\", \"content\": \"This content changed\", \"date\": \"2018-03-09T12:30\", \"published\": \"true\" }");
        template.put("/articles/1", article);

        ResponseEntity<Article> resultAsset = template.getForEntity("/articles/1", Article.class);
        Article articleModel = resultAsset.getBody();

        Assert.assertEquals(Long.valueOf(1L), articleModel.getId());
        Assert.assertEquals("carlosthe19916@gmail.com", articleModel.getEmail());
        Assert.assertEquals("Changed title", articleModel.getTitle());
        Assert.assertEquals("This content changed", articleModel.getContent());
        Assert.assertEquals(LocalDateTime.of(2018, 3, 9, 12, 30), articleModel.getDate());
        Assert.assertEquals(true, articleModel.getPublished());
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
