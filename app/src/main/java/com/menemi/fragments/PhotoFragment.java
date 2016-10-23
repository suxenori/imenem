package com.menemi.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.menemi.PictureSlideActivity;
import com.menemi.R;
import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.personobject.PhotoSetting;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by irondev on 22.06.16.
 */
public class PhotoFragment extends Fragment implements View.OnTouchListener {

    private static final String TAG = "PhotoFragment";
    public static final String TAG_ID = "idToShow";
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    private int pageNumber = -1;
    ArrayList<PhotoSetting> urlsArray = new ArrayList<>();



    View rootView = null;
    boolean isFullScreen = false;


    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;




    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setUrlsArray(ArrayList<PhotoSetting> urlsArray) {
        this.urlsArray = urlsArray;
    }

    public void setFullScreen(boolean isFullScreen) {
        this.isFullScreen = isFullScreen;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.setRetainInstance(false);
    }

    /**
     * fills data
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            rootView = inflater.inflate(R.layout.photo_item, null);

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
                parent.removeAllViews();
            }
        }


        //matrix.setValues(new float[]{ 0.7762298f, 0.0f, 1.9050169f, 0.0f, 0.7762298f, 263.37143f, 0.0f, 0.0f, 1.0f});
        //picture
        startProgressBar();

        //(int personId, int requestedId, int photoNumber, int count, String quality,

        if(urlsArray.size() > pageNumber) {
            new PictureLoader(urlsArray.get(pageNumber).getPhotoUrl(), (Bitmap picture) -> {

                ImageView pictureView = (ImageView) rootView.findViewById(R.id.imageView);
                pictureView.setImageBitmap(picture);

                if (isFullScreen) {
                    Log.v("PhotoFragment", "FULL SCREEN");
                    pictureView.setScaleType(ImageView.ScaleType.MATRIX);
                    // pictureView.setOnTouchListener(PhotoFragment.this);
                    new PhotoViewAttacher(pictureView);
                    RectF drawableRect = new RectF(0, 0, picture.getWidth(), picture.getHeight());
                    RectF viewRect = new RectF(0, 0, pictureView.getWidth(), pictureView.getHeight());
                    matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
                  //  Log.d(TAG, "matrix picture h= " + ((ArrayList<PhotoSetting>) object).get(0).getPhoto().getHeight() + " w=" + ((ArrayList<PhotoSetting>) object).get(0).getPhoto().getWidth());
                } else {
                    pictureView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }

                pictureView.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                       if (!isFullScreen) {
                                                           Intent pictureSlideActivity = new Intent(getActivity(), PictureSlideActivity.class);
                                                          // pictureSlideActivity.putExtra("personId", personId);
                                                           pictureSlideActivity.putExtra("page", pageNumber);
                                                           PictureSlideActivity.setPhotosUrls(urlsArray);
                                                           startActivity(pictureSlideActivity);
                                                       } else {
                                                           isFullScreen = !isFullScreen;
                                                           getActivity().finish();

                                                       }
                                                   }
                                               }

                );
                stopProgressBar();
            });
        } else{
            ImageView pictureView = (ImageView) rootView.findViewById(R.id.imageView);
            pictureView.setImageResource(R.drawable.empty_photo);
            pictureView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            stopProgressBar();
        }



        return rootView;
    }

    public void updatePhoto() {

    }

    public void startProgressBar() {
        ProgressBar loading = (ProgressBar) rootView.findViewById(R.id.loading);
        ImageView pictureView = (ImageView) rootView.findViewById(R.id.imageView);
        pictureView.setImageResource(R.color.text_second);
        loading.setVisibility(View.VISIBLE);
    }

    public void stopProgressBar() {
        rootView.findViewById(R.id.loading).setVisibility(View.GONE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        ImageView view = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d(TAG, "oldDist " + oldDist);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                MIN_ZOOM /= scale;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist;


                        scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM));
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }
        view.setImageMatrix(matrix);
        return true;
    }
    float scale;

    private static  float MIN_ZOOM = 1.0f;
    private static final float MAX_ZOOM = 5.0f;
    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
        Log.d(TAG, sb.toString());
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}

