package com.crossover.techtrial.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.crossover.techtrial.model.Article;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void testArticleTitleMaxLength() throws Exception {
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

    @Test
    public void testDeleteArticle() throws Exception {
        HttpEntity<Object> article = getHttpEntity("{\"email\": \"user1@gmail.com\", \"title\": \"hello\" }");
        ResponseEntity<Article> resultAsset = template.postForEntity("/articles", article, Article.class);

        Long articleId = resultAsset.getBody().getId();
        template.delete("/articles/" + articleId);

        resultAsset = template.getForEntity("/articles/" + articleId, Article.class);
        Assert.assertNull(resultAsset.getBody());
    }

    @Test
    public void testSearchArticle() {
        HttpEntity<Object> article1 = getHttpEntity("{\"email\": \"user1@gmail.com\", \"title\": \"Camel in action\" }");
        ResponseEntity<Article> resultAsset = template.postForEntity("/articles", article1, Article.class);
        Long id1 = resultAsset.getBody().getId();

        HttpEntity<Object> article2 = getHttpEntity("{\"email\": \"user1@gmail.com\", \"title\": \"Spring in action\" }");
        resultAsset = template.postForEntity("/articles", article2, Article.class);
        Long id2 = resultAsset.getBody().getId();

        ResponseEntity<List> searchResultAsset = template.getForEntity("/articles/search?text=camel", List.class);
        Assert.assertEquals(1, searchResultAsset.getBody().size());

        searchResultAsset = template.getForEntity("/articles/search?text=spring", List.class);
        Assert.assertEquals(1, searchResultAsset.getBody().size());

        searchResultAsset = template.getForEntity("/articles/search?text=action", List.class);
        Assert.assertEquals(2, searchResultAsset.getBody().size());

        template.delete("/articles/" + id1);
        template.delete("/articles/" + id2);
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
