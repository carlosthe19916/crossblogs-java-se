package com.crossover.techtrial.model;

import org.junit.Assert;
import org.junit.Test;

public class CommentTest {

    @Test
    public void testComment() throws Exception {
        // Equals by Id
        Comment article1 = new Comment();
        article1.setId(1L);

        Comment article2 = new Comment();
        article2.setId(1L);

        Assert.assertEquals(article1, article2);

        // Equals by email
        Comment article3 = new Comment();
        article3.setEmail("carlosthe19916@gmail.com");

        Comment article4 = new Comment();
        article4.setEmail("carlosthe19916@gmail.com");

        Assert.assertEquals(article3, article4);

        // Equals by title
        Comment article5 = new Comment();
        article5.setMessage("Nice article");

        Comment article6 = new Comment();
        article6.setMessage("Nice article");

        Assert.assertEquals(article5, article6);
    }

}
