package com.example.data;

import android.graphics.Bitmap;

public class ContactDetailVO
{
	private Bitmap imageLogo;
	private String title;
	private String desc;
	private Bitmap imageArrow;
	
	public ContactDetailVO(Bitmap mImage, String mTitle,String mDesc, Bitmap arrowBitmap)
	{
		// TODO Auto-generated constructor stub
		imageLogo=mImage;
		title=mTitle;
		desc=mDesc;
		imageArrow=arrowBitmap;
	}
	
	public Bitmap getImageLogo()
	{
		return imageLogo;
	}
	public void setImage(Bitmap image)
	{
		this.imageLogo = image;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public Bitmap getImageArrow()
	{
		return imageArrow;
	}

	public void setImageArrow(Bitmap imageArrow)
	{
		this.imageArrow = imageArrow;
	}
}
