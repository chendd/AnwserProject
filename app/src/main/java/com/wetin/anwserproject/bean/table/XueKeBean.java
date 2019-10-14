package com.wetin.anwserproject.bean.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class XueKeBean implements Serializable {
    static final long serialVersionUID = 42L;
    @Id
    private Long id;
    private String name;
    private int orderId;
    private int type;
    private int unlockType;
    private int questionCount;
    private int userQuestionCount;
    private String updateTime;
    private String createTime;
    @ToMany(referencedJoinProperty = "parentId")
    private List<ChapterBean> child;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 108714124)
    private transient XueKeBeanDao myDao;
    @Generated(hash = 994667474)
    public XueKeBean(Long id, String name, int orderId, int type, int unlockType,
            int questionCount, int userQuestionCount, String updateTime,
            String createTime) {
        this.id = id;
        this.name = name;
        this.orderId = orderId;
        this.type = type;
        this.unlockType = unlockType;
        this.questionCount = questionCount;
        this.userQuestionCount = userQuestionCount;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }
    @Generated(hash = 193437185)
    public XueKeBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getOrderId() {
        return this.orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getUnlockType() {
        return this.unlockType;
    }
    public void setUnlockType(int unlockType) {
        this.unlockType = unlockType;
    }
    public int getQuestionCount() {
        return this.questionCount;
    }
    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }
    public int getUserQuestionCount() {
        return this.userQuestionCount;
    }
    public void setUserQuestionCount(int userQuestionCount) {
        this.userQuestionCount = userQuestionCount;
    }
    public String getUpdateTime() {
        return this.updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1292588837)
    public List<ChapterBean> getChild() {
        if (child == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ChapterBeanDao targetDao = daoSession.getChapterBeanDao();
            List<ChapterBean> childNew = targetDao._queryXueKeBean_Child(id);
            synchronized (this) {
                if (child == null) {
                    child = childNew;
                }
            }
        }
        return child;
    }

    public void setChild(List<ChapterBean> datas){
        child=datas;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1024800465)
    public synchronized void resetChild() {
        child = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    @Override
    public String toString() {
        return "XueKeBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", orderId=" + orderId +
                ", type=" + type +
                ", unlockType=" + unlockType +
                ", questionCount=" + questionCount +
                ", userQuestionCount=" + userQuestionCount +
                ", updateTime='" + updateTime + '\'' +
                ", createTime='" + createTime + '\'' +
                ", child=" + getChild().size() +
                '}';
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2124443321)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getXueKeBeanDao() : null;
    }
}
