package com.crossover.techtrial.model;

import org.junit.Assert;
import org.junit.Test;

public class ArticleTest {

    @Test
    public void testArticle() throws Exception {
        // Equals by Id
        Article article1 = new Article();
        article1.setId(1L);

        Article article2 = new Article();
        article2.setId(1L);

        Assert.assertEquals(article1, article2);

        // Equals by email
        Article article3 = new Article();
        article3.setEmail("carlosthe19916@gmail.com");

        Article article4 = new Article();
        article4.setEmail("carlosthe19916@gmail.com");

        Assert.assertEquals(article3, article4);

        // Equals by title
        Article article5 = new Article();
        article5.setTitle("Camel in action");

        Article article6 = new Article();
        article6.setTitle("Camel in action");

        Assert.assertEquals(article5, article6);
    }

}
