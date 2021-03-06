/**
 * Copyright 2014 Magnus Woxblom
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package interval.com.intervalapp.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;
import interval.com.intervalapp.adapter.ItemAdapter;
import interval.com.intervalapp.database.RealmSongsDataBase;
import interval.com.intervalapp.model.Song;
import io.realm.Realm;

public class BoardFragment extends Fragment {

    @BindView(R.id.board_view)
    protected BoardView boardView;

    private ItemAdapter fastAdapter;

    private ItemAdapter slowAdapter;

    private Realm realm = Realm.getDefaultInstance();

    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.board);

        addFastMusicColumnList();
        addSlowMusicColumnList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.board_layout, container, false);
        ButterKnife.bind(this, view);

        boardView.setSnapToColumnsWhenScrolling(true);
        boardView.setSnapToColumnWhenDragging(true);
        boardView.setSnapDragItemToTouch(true);
        boardView.setCustomDragItem(new MyDragItem(getActivity(),
                R.layout.user_songlists_column_item));
        boardView.setBoardListener(new BoardView.BoardListener() {
            @Override
            public void onItemDragStarted(int column, int row) {
            }

            @Override
            public void onItemChangedPosition(int fromColumn, int fromRow, int toColumn, int toRow) {
            }

            @Override
            public void onItemChangedColumn(int oldColumn, int newColumn) {
                TextView itemCount1 = (TextView) boardView.getHeaderView(oldColumn)
                        .findViewById(R.id.item_count);
                itemCount1.setText(Integer.toString(boardView.getAdapter(oldColumn)
                        .getItemCount()));
                TextView itemCount2 = (TextView) boardView.getHeaderView(newColumn)
                        .findViewById(R.id.item_count);
                itemCount2.setText(Integer.toString(boardView.getAdapter(newColumn)
                        .getItemCount()));
            }

            @Override
            public void onItemDragEnded(int fromColumn, final int fromRow, int toColumn, int toRow) {
                if (fromColumn != toColumn || fromRow != toRow) {
                    if (fromColumn == 0) {
                        final long id = slowAdapter.getItemId(toRow);
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Song byHash = new RealmSongsDataBase().findByHash((int) id);
                                byHash.setType(Song.SLOW);
                            }
                        });

                    } else {
                        final long id = fastAdapter.getItemId(toRow);
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Song byHash = new RealmSongsDataBase().findByHash((int) id);
                                byHash.setType(Song.FAST);
                            }
                        });
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_board, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_disable_drag).setVisible(boardView.isDragEnabled());
        menu.findItem(R.id.action_enable_drag).setVisible(!boardView.isDragEnabled());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_disable_drag:
                boardView.setDragEnabled(false);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_enable_drag:
                boardView.setDragEnabled(true);
                getActivity().invalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.approve_button)
    protected void buttonClicked() {
        startActivity(new Intent(getContext(), ModeActivity.class));
    }

    private void addFastMusicColumnList() {
        final List<Pair<Long, String>> songList = new ArrayList<>();

        for (Song song : new RealmSongsDataBase().readSongList(getString(R.string.fast))) {
            songList.add(new Pair<>((long) song.hashCode(), song.getTitle()));
        }

        fastAdapter = new ItemAdapter(songList, R.layout.user_songlists_column_item,
                R.id.item_layout, true);
        final View header = View.inflate(getActivity(), R.layout.column_header, null);
        ((TextView) header.findViewById(R.id.text)).setText(R.string.fast_songs);
        ((TextView) header.findViewById(R.id.item_count))
                .setText(String.format(Locale.US, "%d", songList.size()));

        boardView.addColumnList(fastAdapter, header, false);
    }

    private void addSlowMusicColumnList() {
        final ArrayList<Pair<Long, String>> songList = new ArrayList<>();
        for (Song song : new RealmSongsDataBase().readSongList(getString(R.string.slow))) {
            songList.add(new Pair<>((long) song.hashCode(), song.getTitle()));
        }

        slowAdapter = new ItemAdapter(songList, R.layout.user_songlists_column_item,
                R.id.item_layout, true);
        final View header = View.inflate(getActivity(), R.layout.column_header, null);
        ((TextView) header.findViewById(R.id.text)).setText(R.string.slow_songs);
        ((TextView) header.findViewById(R.id.item_count))
                .setText(String.format(Locale.US, "%d", songList.size()));

        boardView.addColumnList(slowAdapter, header, false);
    }

    private static class MyDragItem extends DragItem {

        private MyDragItem(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            CharSequence text = ((TextView) clickedView.findViewById(R.id.text)).getText();
            ((TextView) dragView.findViewById(R.id.text)).setText(text);
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            CardView clickedCard = ((CardView) clickedView.findViewById(R.id.card));

            dragCard.setMaxCardElevation(40);
            dragCard.setCardElevation(clickedCard.getCardElevation());
            dragCard.setForeground(clickedView.getResources()
                    .getDrawable(R.drawable.card_view_drag_foreground));
        }

        @Override
        public void onMeasureDragView(View clickedView, View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            CardView clickedCard = ((CardView) clickedView.findViewById(R.id.card));
            int widthDiff = dragCard.getPaddingLeft() - clickedCard.getPaddingLeft()
                    + dragCard.getPaddingRight() - clickedCard.getPaddingRight();
            int heightDiff = dragCard.getPaddingTop() - clickedCard.getPaddingTop()
                    + dragCard.getPaddingBottom() - clickedCard.getPaddingBottom();
            int width = clickedView.getMeasuredWidth() + widthDiff;
            int height = clickedView.getMeasuredHeight() + heightDiff;
            dragView.setLayoutParams(new FrameLayout.LayoutParams(width, height));

            int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            dragView.measure(widthSpec, heightSpec);
        }

        @Override
        public void onStartDragAnimation(View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            ObjectAnimator anim = ObjectAnimator.ofFloat(dragCard, "CardElevation",
                    dragCard.getCardElevation(), 40);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();
        }

        @Override
        public void onEndDragAnimation(View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            ObjectAnimator anim = ObjectAnimator.ofFloat(dragCard, "CardElevation",
                    dragCard.getCardElevation(), 6);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();
        }
    }
}
