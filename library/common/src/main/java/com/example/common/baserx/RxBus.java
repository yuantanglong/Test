package com.example.common.baserx;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * 用RxJava实现的EventBus
 * Created by Administrator on 2017/9/12.
 */

public class RxBus {
private static RxBus instance;

    /**
     * 单例模式
     * @return
     */
    public static synchronized RxBus getInstance(){
        if(null==instance) {
            instance=new RxBus();
        }
        return instance;
    }
    private RxBus(){}
    //事件源集合
    private ConcurrentHashMap<Object,List<Subject>> subjectMapper=new ConcurrentHashMap<>();

    /**
     * 订阅事件源
     * @param mObservable
     * @param mAction1
     * @return
     */
    public RxBus OnEvent(Observable<?> mObservable, Action1<Object> mAction1){
        mObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(mAction1, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        return getInstance();
    }

    /**
     * 注册事件源
     * @param tag
     * @param <T>
     * @return
     */
    public <T> Observable<T> register(Object tag){
        List<Subject> subjectList=subjectMapper.get(tag);
        if (null==subjectList) {
            subjectList=new ArrayList<>();
            subjectMapper.put(tag,subjectList);
        }
        Subject<T,T> subject;
        subjectList.add(subject= PublishSubject.<T>create());
        return subject;
    }

    /**
     * 取消注册事件源
     * @param tag
     */
    public void unregister(Object tag){
        List<Subject> subjects=subjectMapper.get(tag);
        if (null==subjects) {
            subjectMapper.put(tag,subjects);
        }
    }

}
