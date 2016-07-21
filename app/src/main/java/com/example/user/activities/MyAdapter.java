package com.example.user.activities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 7/19/16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.RecipeViewHolder>
{

    public static class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView title;
        protected TextView description;

        public RecipeViewHolder(View v){
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            //Log.d("viewholder", " " + (title != null));
            description = (TextView) v.findViewById(R.id.description);
        }
        public void onClick(View v){
            //int position = v.get;
            //Log.d(" ", " " + position);
        }
    }


    private List<Form> recipeList;

    public MyAdapter(List<Form> recipes){
        this.recipeList = recipes;
    }

    public int getItemCount(){
        return this.recipeList.size();
    }

    public void onBindViewHolder(RecipeViewHolder vh, int i){
        Form res = recipeList.get(i);
        //Log.d("adf", res.title);
        //Log.d("adfdf", " " + (vh.title != null));
        vh.title.setText(res.name);
        //vh.title.setText("Hello");
        vh.description.setText(res.description);



    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public RecipeViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item, viewGroup, false);

        return new RecipeViewHolder(itemView);
    }

}
//Contact GitHub API Training Shop Blog About
