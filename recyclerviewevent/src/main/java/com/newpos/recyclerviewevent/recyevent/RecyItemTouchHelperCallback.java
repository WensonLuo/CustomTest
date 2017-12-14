package com.newpos.recyclerviewevent.recyevent;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.newpos.recyclerviewevent.model.RecyAdapter;

import java.util.Collections;

/**
 * Created by Wenson_Luo on 2017/9/18.
 */

public class RecyItemTouchHelperCallback extends ItemTouchHelper.Callback{
    RecyclerView.Adapter mAdapter;
    boolean isSwipeEnable;
    boolean isFirstDragUnable;//首个固定

    public RecyItemTouchHelperCallback(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        isSwipeEnable = true;
        isFirstDragUnable = false;
    }

    public RecyItemTouchHelperCallback(RecyclerView.Adapter adapter, boolean isSwipeEnable, boolean isFirstDragUnable) {
        mAdapter = adapter;
        this.isSwipeEnable = isSwipeEnable;
        this.isFirstDragUnable = isFirstDragUnable;
    }

    //通过返回值来设置是否处理某次拖曳或者滑动事件
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager){
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            int swipFlags = 0;
            return makeMovementFlags(dragFlags, swipFlags);
        }else {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    //当长按并进入拖曳状态时，拖曳的过程中不断的回调此方法
    //获取当前拖拽的 item 和已经被拖拽到所处位置的 item 的ViewHolder，有了这2个 ViewHolder，我们就
    // 可以交换他们的数据集并调用 Adapter 的notifyItemMoved 方法来刷新 item。
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //拖动的 item 的下标
        int fromPosition = viewHolder.getAdapterPosition();
        //目标 item 的下标，目标 item 就是当拖曳过程中，不断和拖动的 item 做位置交换的条目。
        int toPosition = target.getAdapterPosition();
        //这里判断如果目标 item 是首个 item，那么就直接返回false，表示不响应此次拖曳移动
        if (isFirstDragUnable && toPosition == 0) {
            return false;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(((RecyAdapter) mAdapter).getDataList(), i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(((RecyAdapter) mAdapter).getDataList(), i, i - 1);
            }
        }
        mAdapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    //实现滑动删除
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int adapterPosition = viewHolder.getAdapterPosition();
        mAdapter.notifyItemRemoved(adapterPosition);
        ((RecyAdapter) mAdapter).getDataList().remove(adapterPosition);
    }

    //当长按 item 刚开始拖曳的时候调用
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    //当完成拖曳手指松开的时候调用
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //给已经完成拖曳的 item 恢复开始的背景。
        viewHolder.itemView.setBackgroundColor(Color.WHITE);
    }

    //告诉 ItemTouchHelper 是否需要 RecyclerView 支持长按拖拽，默认返回是 true，理所当然我们要支持。
    // 自定义拖拽 view 首先返回 false，所有都不允许拖拽，然后使用 startDrag(ViewHolder) 方法。
    @Override
    public boolean isLongPressDragEnabled() {
        return !isFirstDragUnable;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return isSwipeEnable;
    }
}
