package com.elegion.tracktor.ui.results;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elegion.tracktor.R;
import com.elegion.tracktor.util.ScreenshotMaker;
import com.elegion.tracktor.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.elegion.tracktor.ui.results.ResultsActivity.DISTANCE_KEY;
import static com.elegion.tracktor.ui.results.ResultsActivity.SCREENSHOT_KEY;
import static com.elegion.tracktor.ui.results.ResultsActivity.TIME_KEY;

/**
 * @author Azret Magometov
 */
public class ResultsDetailsFragment extends Fragment {

    @BindView(R.id.tvTime) TextView mTimeText;
    @BindView(R.id.tvDistance) TextView mDistanceText;
    @BindView(R.id.ivScreenshot) ImageView mScreenshotImage;

    private Bitmap mImage;

    public static ResultsDetailsFragment newInstance(Bundle bundle) {
        Bundle args = new Bundle();
        args.putAll(bundle);

        ResultsDetailsFragment fragment = new ResultsDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fr_result_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        double distance = getArguments().getDouble(DISTANCE_KEY, 0.0);
        long time = getArguments().getLong(TIME_KEY, 0);

        mTimeText.setText(StringUtil.getTimeText(time));
        mDistanceText.setText(StringUtil.getDistanceText(distance));

        mImage = ScreenshotMaker.fromBase64(getArguments().getString(SCREENSHOT_KEY));
        mScreenshotImage.setImageBitmap(mImage);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionShare) {
            String path = MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), mImage, "Мой маршрут", null);
            Uri uri = Uri.parse(path);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.putExtra(Intent.EXTRA_TEXT, "Время: " + mTimeText.getText() + "\nРасстояние: " + mDistanceText.getText());
            startActivity(Intent.createChooser(intent, "Результаты маршрута"));
        }
        return super.onOptionsItemSelected(item);
    }
}
