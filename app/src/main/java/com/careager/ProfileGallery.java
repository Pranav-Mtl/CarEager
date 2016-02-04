package com.careager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.careager.Constant.Constant;
import com.careager.Container.GalleryPagerContainer;
import com.careager.careager.R;
import com.squareup.picasso.Picasso;

public class ProfileGallery extends AppCompatActivity {

    GalleryPagerContainer mContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_gallery);
        initialize();
    }

    private void initialize(){
        mContainer = (GalleryPagerContainer) findViewById(R.id.pager_container_gallery);

        ViewPager pager = mContainer.getViewPager();
        //ViewPager pager=(ViewPager) view.findViewById(R.id.pager);
        mContainer=new GalleryPagerContainer(getApplicationContext());


        GalleryPageAdapter adapter=new GalleryPageAdapter(getApplicationContext());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(adapter.getCount());
        //A little space between pages

        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
        pager.setClipChildren(false);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {

            //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
            onBackPressed();
            return true;
        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
    private class GalleryPageAdapter extends PagerAdapter {
        Context context;
        LayoutInflater inflater;
        ImageView imgPager;

        GalleryPageAdapter(Context contex) {
            this.context = contex;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.gallery_zoom_pager, container,
                    false);

            imgPager= (ImageView) itemView.findViewById(R.id.gallery_images);

            Picasso.with(getApplicationContext())
                    .load(Constant.galleryBaseURL + Constant.galleryImages[position])
                    .placeholder(R.drawable.ic_default_loading)
                    .error(R.drawable.ic_default_loading)
                    .into(imgPager);


            ((ViewPager) container).addView(itemView);

            return itemView;
        }

        @Override
        public int getCount() {
            if(Constant.galleryImages==null)
                return 0;
            return Constant.galleryImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

      /*  public float getPageWidth(int position)
        {
            return .9f;
        }
*/
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
