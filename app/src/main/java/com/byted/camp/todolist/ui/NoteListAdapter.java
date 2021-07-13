package com.byted.camp.todolist.ui;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.byted.camp.todolist.NoteOperator;
import com.byted.camp.todolist.R;
import com.byted.camp.todolist.beans.Note;
import com.byted.camp.todolist.beans.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Created on 2019/1/23.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public class NoteListAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private final NoteOperator operator;
    private final List<Note> notes = new ArrayList<Note>();

    public NoteListAdapter(NoteOperator operator) {
        this.operator = operator;
    }

    public void refresh(List<Note> newNotes) {
        notes.clear();
        if (newNotes != null) {
            notes.addAll(newNotes);
//            notes.sort(Comparator.comparing());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                notes.sort(new Comparator<Note>(){
                   @Override
                   public int compare(Note n1,Note n2){
                       if(n1.getState()!=n2.getState()){
                           return State.refrom(n1.getState()) - State.refrom(n2.getState());
                       }
                       else{
                           if (n1.getPriority()!=n2.getPriority()){
                               return n2.getPriority()-n1.getPriority();
                           }
                           else{
                               return n2.getDate().compareTo(n1.getDate());
                           }
                       }
                   }
                });
            }
            else
                Log.e("data","Build.VERSION.SDK_INT < Build.VERSION_CODES.N");
            for(Note item : notes){
                Log.d("data","In NoteListAdapter id:"+item.getId()+" state:"+item.getState()+" priority:"+item.getPriority());
            }
            //todo 根据${com.byted.camp.todolist.operation.activity.SettingActivity} 中设置的sp控制是否将已完成的完成排到最后，默认不排序
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int pos) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView, operator);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int pos) {
        holder.bind(notes.get(pos));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
