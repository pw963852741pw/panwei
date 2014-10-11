package com.example.wordpk;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryFlow extends Gallery {
	/**
	 * Graphics Camera used for transforming the matrix of ImageViews
	 */
	private Camera mCamera = new Camera();
	/**
	 * The maximum angle the Child ImageView will be rotated by
	 */
	private int mMaxRotationAngle = 60;
	/**
	 * The maximum zoom on the centre Child
	 */
	private int mMaxZoom = -120;
	/**
	 * The Centre of the Coverflow
	 */
	private int mCoveflowCenter;

	int lastPosition;

	private boolean setRotateY = false;

	public boolean isSetRotateY() {
		return setRotateY;
	}

	public void setSetRotateY(boolean setRotateY) {
		this.setRotateY = setRotateY;
	}

	public GalleryFlow(Context context) {
		super(context);
		// �Ƿ��ڹ����� ���� getChildStaticTransformation����
		this.setStaticTransformationsEnabled(true);
	}

	public GalleryFlow(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);
	}

	public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);
	}

	/**
	 * Get the max rotational angle of the image
	 * 
	 * @return the mMaxRotationAngle
	 */
	public int getMaxRotationAngle() {
		return mMaxRotationAngle;
	}

	/**
	 * Set the max rotational angle of each image
	 * 
	 * @param maxRotationAngle
	 *            the mMaxRotationAngle to set
	 */
	public void setMaxRotationAngle(int maxRotationAngle) {
		mMaxRotationAngle = maxRotationAngle;
	}

	/**
	 * Get the Max zoom of the centre image
	 * 
	 * @return the mMaxZoom
	 */
	public int getMaxZoom() {
		return mMaxZoom;
	}

	/**
	 * Set the max zoom of the centre image
	 * 
	 * @param maxZoom
	 *            the mMaxZoom to set
	 */
	public void setMaxZoom(int maxZoom) {
		mMaxZoom = maxZoom;
	}

	/**
	 * ���gallery ���м��
	 * 
	 * @return The centre of this Coverflow.
	 */
	private int getCenterOfCoverflow() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
				+ getPaddingLeft();
	}

	/**
	 * ������� ��view ���м��
	 * 
	 * @return The centre of the given view.
	 */
	private static int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}

	/**
	 * 
	 * @see #setStaticTransformationsEnabled(boolean)
	 * 
	 *      gallery�л���ʱ�� ����õķ���
	 * 
	 *      child ��view Transformation t
	 */
	protected boolean getChildStaticTransformation(View child, Transformation t) {

		final int childCenter = getCenterOfView(child);

		final int childWidth = child.getWidth();
		int rotationAngle = 0;
		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);

		// �ж��Ƿ����е� �е�������� ѡ�е�view
		if (childCenter == mCoveflowCenter) {

			transformImageBitmap((ImageView) child, t, 0);

		} else {
			rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);
			if (Math.abs(rotationAngle) > mMaxRotationAngle) {
				rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle
						: mMaxRotationAngle;
			}

			int scale = Math.abs(mCoveflowCenter - childCenter);

			transformImageBitmap((ImageView) child, t, rotationAngle, scale);

		}
		return true;
	}

	/**
	 * ���������gallery size�����ĵ�ʱ�����
	 * 
	 * @param w
	 *            Current width of this view.
	 * @param h
	 *            Current height of this view.
	 * @param oldw
	 *            Old width of this view.
	 * @param oldh
	 *            Old height of this view.
	 */

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		mCoveflowCenter = getCenterOfCoverflow();

		super.onSizeChanged(w, h, oldw, oldh);
	}

	/**
	 * Transform the Image Bitmap by the Angle passed
	 * 
	 * @param imageView
	 *            ImageView the ImageView whose bitmap we want to rotate
	 * @param t
	 *            transformation
	 * @param rotationAngle
	 *            the Angle by which to rotate the Bitmap
	 */
	private void transformImageBitmap(ImageView child, Transformation t,
			int rotationAngle, int scale) {
		mCamera.save();

		final Matrix imageMatrix = t.getMatrix();
		final int imageHeight = child.getLayoutParams().height;
		final int imageWidth = child.getLayoutParams().width;
		final int rotation = Math.abs(rotationAngle);
		// ��Z���������ƶ�camera���ӽǣ�ʵ��Ч��Ϊ�Ŵ�ͼƬ��
		// �����Y�����ƶ�����ͼƬ�����ƶ���X���϶�ӦͼƬ�����ƶ���

		float f = (float) (scale * 1.1);
		mCamera.translate(0.0f, 0.0f, f);
		// As the angle of the view gets less, zoom in
		if (rotation < mMaxRotationAngle) {
			float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));
			mCamera.translate(0.0f, 0.0f, zoomAmount);
		}
		// ��Y������ת����ӦͼƬ�������﷭ת��
		// �����X������ת�����ӦͼƬ�������﷭ת��
		if (setRotateY) {
			mCamera.rotateY(rotationAngle);
		}
		mCamera.getMatrix(imageMatrix);
		imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
		imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
		mCamera.restore();
	}

	private void transformImageBitmap(ImageView child, Transformation t,
			int rotationAngle) {
		mCamera.save();

		final Matrix imageMatrix = t.getMatrix();
		final int imageHeight = child.getLayoutParams().height;
		final int imageWidth = child.getLayoutParams().width;
		final int rotation = Math.abs(rotationAngle);
		// ��Z���������ƶ�camera���ӽǣ�ʵ��Ч��Ϊ�Ŵ�ͼƬ��
		// �����Y�����ƶ�����ͼƬ�����ƶ���X���϶�ӦͼƬ�����ƶ���
		// mCamera.translate(0.0f, 0.0f, 100.0f);
		// As the angle of the view gets less, zoom in
		if (rotation < mMaxRotationAngle) {
			float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));
			mCamera.translate(0.0f, 0.0f, zoomAmount);
		}
		// ��Y������ת����ӦͼƬ�������﷭ת��
		// �����X������ת�����ӦͼƬ�������﷭ת��

		if (setRotateY) {
			mCamera.rotateY(rotationAngle);
		}

		mCamera.getMatrix(imageMatrix);
		imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
		imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
		mCamera.restore();
	}

	/*
	 * ����view ��˳�� ѡ�е���� ˳������ ѡ�е��ұߵ������� ��� �ص�˳�������
	 */
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		// TODO Auto-generated method stub

		int mFirstPosition = getFirstVisiblePosition();

		int mSelectedPosition = getSelectedItemPosition();

		int selectedIndex = mSelectedPosition - mFirstPosition;

		if (i == 0) {
			lastPosition = 0;
		}

		int ret = 0;

		if (selectedIndex < 0) {
			return i;
		}

		if (i == childCount - 1) {

			ret = selectedIndex;

		} else if (i >= selectedIndex) {

			lastPosition++;

			ret = childCount - lastPosition;

		} else {

			ret = i;

		}

		return ret;

	}
}