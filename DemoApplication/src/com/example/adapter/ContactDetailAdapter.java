package com.example.adapter;

import java.util.ArrayList;

import com.example.controller.MainActivity;
import com.example.controller.R;
import com.example.controller.R.bool;
import com.example.data.ContactDetailVO;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContactDetailAdapter extends RecyclerView.Adapter<ContactDetailAdapter.ContectHandler>
{
	private static String				LOG_TAG	= "ContectDetailAdapter";
	private ArrayList<ContactDetailVO>	mDataset;
	private static MyClickListener		myClickListener;

	public static class ContectHandler extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		ImageView	logo;
		TextView	title;
		TextView	desc;
		ImageView	arrowImage;
		boolean		arrowBool	= true;

		public ContectHandler(View itemView)
		{
			super(itemView);
			logo = (ImageView) itemView.findViewById(R.id.card_logo);
			title = (TextView) itemView.findViewById(R.id.card_title);
			desc = (TextView) itemView.findViewById(R.id.card_desc);
			arrowImage = (ImageView) itemView.findViewById(R.id.card_arrows);
			Log.i(LOG_TAG, "Adding Listener");
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v)
		{
			if (arrowBool)
			{
				arrowBool = false;
				LinearLayout description = (LinearLayout) v.findViewById(R.id.card_desc_layout);
				description.setVisibility(View.VISIBLE);
				arrowImage.setImageResource(R.drawable.arrow_up);
			}
			else
			{
				arrowBool = true;
				LinearLayout description = (LinearLayout) v.findViewById(R.id.card_desc_layout);
				description.setVisibility(View.GONE);
				arrowImage.setImageResource(R.drawable.arrow_down);
			}
			// myClickListener.onItemClick(getAdapterPosition(), v);
		}
	}

	public void setOnItemClickListener(MyClickListener myClickListener)
	{
		this.myClickListener = myClickListener;
	}

	public ContactDetailAdapter(ArrayList<ContactDetailVO> myDataset)
	{
		mDataset = myDataset;
	}

	@Override
	public ContectHandler onCreateViewHolder(ViewGroup parent, int viewType)
	{
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row, parent, false);

		ContectHandler dataObjectHolder = new ContectHandler(view);
		return dataObjectHolder;
	}

	@Override
	public void onBindViewHolder(ContectHandler holder, int position)
	{
		// TODO Auto-generated method stub
		holder.logo.setImageBitmap(mDataset.get(position).getImageLogo());
		holder.title.setText(mDataset.get(position).getTitle());
		holder.desc.setText(mDataset.get(position).getDesc());
		holder.arrowImage.setImageBitmap(mDataset.get(position).getImageArrow());
	}

	public void addItem(ContactDetailVO dataObj, int index)
	{
		mDataset.add(index, dataObj);
		notifyItemInserted(index);
	}

	public void deleteItem(int index)
	{
		mDataset.remove(index);
		notifyItemRemoved(index);
	}

	@Override
	public int getItemCount()
	{
		// TODO Auto-generated method stub
		return mDataset.size();
	}

	public interface MyClickListener
	{
		public void onItemClick(int position, View v);
	}
}
