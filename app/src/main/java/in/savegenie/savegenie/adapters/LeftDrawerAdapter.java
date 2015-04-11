package in.savegenie.savegenie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.Category;
import in.savegenie.savegenie.backgroundclasses.CategoryTree;

/**
 * Created by manish on 4/4/15.
 */
public class LeftDrawerAdapter extends ArrayAdapter<Integer>
{
    private Context context;
    ArrayList<Integer> categoryIndex;
    LayoutInflater layInf;
    private ArrayList<Boolean> status = new ArrayList<Boolean>();
    CategoryTree categoryTree;

    public LeftDrawerAdapter(Context context, int childLayoutId,
                             int listViewId, ArrayList<Integer> categoryIndex) {
        super(context, childLayoutId, categoryIndex);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.categoryIndex = categoryIndex;
        categoryTree = CategoryTree.getInstance();
        layInf = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = layInf.inflate(R.layout.list_row_leftdrawer, parent,
                    false);
        }

        Category category = categoryTree.getCategory(categoryIndex
                .get(position));

        final TextView tv = (TextView) convertView.findViewById(R.id.itemName);
        tv.setText(category.name);

        ImageView iv = (ImageView) convertView.findViewById(R.id.itemIcon);
        iv.setImageResource(context.getResources().getIdentifier(
                category.imgId, "drawable", context.getPackageName()));

        // BitmapWorkerTask bwt = new BitmapWorkerTask(iv);
        // bwt.execute(context.getResources().getIdentifier(category.imgId,
        // "drawable", context.getPackageName()),iv.getWidth(),iv.getHeight());

        //loadBitmap(context.getResources().getIdentifier(category.imgId,
        //"drawable", context.getPackageName()), iv);

        return convertView;

    }


	/*public void loadBitmap(int resId, ImageView imageView) {

		final String imageKey = String.valueOf(resId);
		final Bitmap bitmap = getBitmapFromMemCache(imageKey);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.drawable.ic_launcher);
			BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			task.execute(resId);
		}
	}


	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			lruCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return lruCache.get(key);
	}

	public ArrayList<Boolean> getListCheckedStatus() {
		return status;
	}
*/


}

