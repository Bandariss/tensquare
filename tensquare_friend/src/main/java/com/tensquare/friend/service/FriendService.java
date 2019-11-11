package com.tensquare.friend.service;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {
    @Autowired
    private FriendDao friendDao;
    @Autowired
    private NoFriendDao noFriendDao;

    public int addFriend(String userid, String friendid) {
        //0，单项喜欢 1,互关  返回值中0表示重复添加,1表示成功
        //先判断userid到friendid是否有数据，有就是重复添加
        Friend friend=friendDao.findByUseridAndFriendid(userid,friendid);
        if(friend!=null){
            return 0;
        }
        //直接添加好友，在表中添加该条纪录，并且让好友表中userid到friendid为0
        friend=new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        friendDao.save(friend);
        //判断friendid到userid是否有数据，有的话双方状态都变为1(互粉)
        if(friendDao.findByUseridAndFriendid(friendid,userid)!=null){
            friendDao.updateIsLike("1",friendid,userid);
            friendDao.updateIsLike("1",userid,friendid);
        }
        return 1;
    }

    public int addNoFriend(String userid, String friendid) {
        //先判断是否已经是非好友
        NoFriend noFriend=noFriendDao.findByUseridAndFriendid(userid,friendid);
        if(noFriend!=null){
            return 0;
        }
        noFriend=new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
        return 1;
    }

    public void deleteFriend(String userid, String friendid) {
        //删除好友表中userid到friendid这一条
        friendDao.deletefriend(userid,friendid);
        friendDao.updateIsLike("0",friendid,userid);
    }
}
