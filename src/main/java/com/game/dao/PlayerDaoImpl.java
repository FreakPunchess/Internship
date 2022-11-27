//package com.game.dao;
//
//import com.game.model.Player;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class PlayerDaoImpl implements PlayerDao{
//    private static final Logger logger = LoggerFactory.getLogger(PlayerDaoImpl.class);
//
//    private SessionFactory sessionFactory;
//
//    public void setSessionFactory(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    @Override
//    public void addPlayer(Player player) {
//        Session session = this.sessionFactory.getCurrentSession();
//        session.persist(player);
//        logger.info("Player successfuly added. Details: " + player);
//    }
//
//    @Override
//    public void updatePlayer(Player player) {
//        Session session = this.sessionFactory.getCurrentSession();
//        session.update(player);
//        logger.info("Player successfuly updated. Details: " + player);
//    }
//
//    @Override
//    public void removePlayer(Long id) {
//        Session session = this.sessionFactory.getCurrentSession();
//        Player player = (Player) session.load(Player.class, id);
//
//        if(player != null) {
//            session.delete(player);
//        }
//        logger.info("Player successfuly deleted. Details: " + player);
//    }
//
//    @Override
//    public Player getPlayerById(Long id) {
//        Session session = sessionFactory.getCurrentSession();
//        Player player = (Player) session.load(Player.class, id);
//        logger.info("Player successfuly loaded. Details: " + player);
//        return player;
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public List<Player> getPlayersList() {
//        Session session = sessionFactory.getCurrentSession();
//        List<Player> allPlayerList = session.createQuery("from Player").list();
//        return allPlayerList;
//    }
//}
