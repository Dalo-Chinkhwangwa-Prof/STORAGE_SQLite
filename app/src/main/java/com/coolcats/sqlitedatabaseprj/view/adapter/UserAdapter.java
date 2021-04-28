package com.coolcats.sqlitedatabaseprj.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.coolcats.sqlitedatabaseprj.R;
import com.coolcats.sqlitedatabaseprj.databinding.UserItemLayoutBinding;
import com.coolcats.sqlitedatabaseprj.model.User;

import java.util.List;

import static com.coolcats.sqlitedatabaseprj.util.Constants.iconMap;

public class UserAdapter extends BaseAdapter {

    private List<User> users;

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        UserItemLayoutBinding binding  =
                UserItemLayoutBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                        viewGroup, false);

        User user = users.get(i);

        binding.nameTextview.setText(user.getName());

        Glide.with(viewGroup)
                .setDefaultRequestOptions(RequestOptions.centerCropTransform())
                .load(iconMap.get(user.getPosition()))
                .into(binding.positionIcon);

        binding.idTextview.setText(viewGroup.getContext().getString(R.string.id_d, user.getId()));
        binding.positionTextview.setText(user.getPosition().name());

        return binding.getRoot();
    }

    public void updateList(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }
}
