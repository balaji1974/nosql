/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bala.nosql.redis.ui;

import com.bala.nosql.redis.Constants;
import com.bala.nosql.redis.dao.MovieDao;
import com.bala.nosql.redis.dao.RatingDao;
import com.bala.nosql.redis.dao.UserDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 *
 * @author Michael Enudi
 */
public final class AppFrame extends javax.swing.JFrame {

    private final MovieDao movieDao;
    private final RatingDao ratingDao;
    private final UserDao userDao;
    private final Jedis jedis;
    private final Jedis mJedis;

    private final ExecutorService threadExecutorService;

    /**
     * Creates new form AppFrame
     *
     * @param host
     * @param port
     */
    public AppFrame(String host, int port) {
        jedis = new Jedis(host, port);
        mJedis = new Jedis(host, port);

        this.movieDao = new MovieDao(jedis);
        this.ratingDao = new RatingDao(jedis);
        this.userDao = new UserDao(jedis);

        initComponents();
        pack();

        threadExecutorService = Executors.newFixedThreadPool(3);
        subscribeForMessages();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        moviePanel = new MoviePanel(this.movieDao);
        userPanel = new UserPanel(this.userDao);
        ratingPanel = new RatingPanel(this.ratingDao);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Movielens on Redis");
        setResizable(false);

        jTabbedPane1.addTab("Movie", moviePanel);
        jTabbedPane1.addTab("Users", userPanel);
        jTabbedPane1.addTab("Ratings", ratingPanel);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    void subscribeForMessages() {
        new Thread(() -> {
            this.mJedis.subscribe(new JedisPubSub() {
                @Override
                public void onPMessage(String pattern, String channel, String message) {
                    onMessage(channel, message);
                }

                @Override
                public void onMessage(String channel, String message) {
                    threadExecutorService.submit(() -> {
                        System.out.println("onMessage >>>>>>> " + channel + " >>>>>>> " + message);
                        switch (channel) {
                            case Constants.MOVIE_CHANNEL:

                                ((MessageConsumer) moviePanel).applyMessage(message);
                                break;
                            case Constants.RATING_CHANNEL:

                                ((MessageConsumer) ratingPanel).applyMessage(message);
                                break;
                            case Constants.USER_CHANNEL:

                                ((MessageConsumer) userPanel).applyMessage(message);
                                break;
                            default:
                            //do nothing
                        }
                    });
                }

            },
                    Constants.MOVIE_CHANNEL,
                    Constants.RATING_CHANNEL,
                    Constants.USER_CHANNEL);
        }).start();

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel moviePanel;
    private javax.swing.JPanel ratingPanel;
    private javax.swing.JPanel userPanel;
    // End of variables declaration//GEN-END:variables
}