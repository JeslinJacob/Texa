package com.texaconnect.texa.ui.common;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.v7.util.DiffUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstraction over {@link BaseDataBoundAdapter} that keeps the list of children and can
 * support multiple item types.
 * <p>
 * This class relies on all layouts using the "data" variable to represent the item.
 * <p>
 * Although this class by itself just exists for demonstration purposes, it might be a useful idea
 * for an application to have a generic naming convention for their items to scale lists with
 * many view types.
 * <p>
 * Note that, by using this, you lose the compile time type checking for mapping your items to
 * layout files but at run time, it will still be checked. See
 * {@link android.databinding.ViewDataBinding#setVariable(int, Object)} for details.
 */
abstract public class MultiTypeDataBoundAdapter extends BaseDataBoundAdapter {
    private List<Object> mItems = new ArrayList<>();
    private int dataVersion = 0;

    public MultiTypeDataBoundAdapter(List<Object> items) {
//        Collections.addAll(mItems, items);
        mItems = items;
    }

    @Override
    protected void bindItem(DataBoundViewHolder holder, int position, List payloads) {
//        holder.binding.setVariable(BR.data, mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Object getItem(int position) {
        return mItems.get(position);
    }

    public void addItem(Object item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void addItem(int position, Object item) {
        mItems.add(position, item);
        notifyItemInserted(position);
    }

    @SuppressLint("StaticFieldLeak")
    @MainThread
    public void replace(List<Object> update) {
        dataVersion ++;
        if (mItems == null) {
            if (update == null) {
                return;
            }
            mItems = update;
            notifyDataSetChanged();
        } else if (update == null) {
            int oldSize = mItems.size();
            mItems = null;
            notifyItemRangeRemoved(0, oldSize);
        } else {
            final int startVersion = dataVersion;
            final List<Object> oldItems = mItems;
            new AsyncTask<Void, Void, DiffUtil.DiffResult>() {
                @Override
                protected DiffUtil.DiffResult  doInBackground(Void... voids) {
                    return DiffUtil.calculateDiff(new DiffUtil.Callback() {
                        @Override
                        public int getOldListSize() {
                            return oldItems.size();
                        }

                        @Override
                        public int getNewListSize() {
                            return update.size();
                        }

                        @Override
                        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                            Object oldItem = oldItems.get(oldItemPosition);
                            Object newItem = update.get(newItemPosition);
                            return MultiTypeDataBoundAdapter.this.areItemsTheSame(oldItem, newItem);
                        }

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            Object oldItem = oldItems.get(oldItemPosition);
                            Object newItem = update.get(newItemPosition);
                            return MultiTypeDataBoundAdapter.this.areContentsTheSame(oldItem, newItem);
                        }
                    });
                }

                @Override
                protected void onPostExecute(DiffUtil.DiffResult diffResult) {
                    if (startVersion != dataVersion) {
                        // ignore update
                        return;
                    }
                    mItems = update;
                    diffResult.dispatchUpdatesTo(MultiTypeDataBoundAdapter.this);

                }
            }.execute();
        }
    }


    protected abstract boolean areItemsTheSame(Object oldItem, Object newItem);

    protected abstract boolean areContentsTheSame(Object oldItem, Object newItem);
}
