package com.newpos.recyclerviewevent.recyevent;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/*******************************************************************
 通过一个手势探测器 GestureDetectorCompat 来探测屏幕事件，然后通过手势监听器 SimpleOnGestureListener
 来识别手势事件的种类，然后调用我们设置的对应的回调方法。这里值得说的是：当获取到了 RecyclerView 的
 点击事件和触摸事件数据 MotionEvent，那么如何才能知道点击的是哪一个 item 呢？

 RecyclerView已经为我们提供了这样的方法：findChildViewUnder()。

 我们可以通过这个方法获得点击的 item ，同时我们调用 RecyclerView 的另一个方法 getChildViewHolder()，
 可以获得该 item 的 ViewHolder，最后再回调我们定义的虚方法 onItemClick() 就ok了，这样我们就可以在外
 部实现该方法来获得 item 的点击事件了。

 * Created by Wenson_Luo on 2017/9/18.
 *  *******************************************************************
 */

public abstract class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener{
    private GestureDetectorCompat mGestureDetectorCompat;//手势探测器
    private RecyclerView mRecyclerView;

    public OnRecyclerItemClickListener(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
        mGestureDetectorCompat = new GestureDetectorCompat(mRecyclerView.getContext(),
                new ItemTouchHelperGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
    }

    public abstract void onItemClick(RecyclerView.ViewHolder viewHolder);
    public abstract void onLongClick(RecyclerView.ViewHolder viewHolder);
    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View childViewUnder = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childViewUnder != null){
                RecyclerView.ViewHolder childViewHolder = mRecyclerView.getChildViewHolder(childViewUnder);
                onItemClick(childViewHolder);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View childViewUnder = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childViewUnder != null) {
                RecyclerView.ViewHolder childViewHolder = mRecyclerView.getChildViewHolder(childViewUnder);
                onLongClick(childViewHolder);
            }
        }
    }
}
