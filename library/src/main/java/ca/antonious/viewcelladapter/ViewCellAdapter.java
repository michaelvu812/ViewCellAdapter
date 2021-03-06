package ca.antonious.viewcelladapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by George on 2016-11-15.
 */

public class ViewCellAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<ViewCell> viewCells;
    private ListenerCollection listenerCollection;
    private Map<Integer, Class<? extends BaseViewHolder>> layoutTypes;

    public ViewCellAdapter() {
        this.viewCells = new ArrayList<>();
        this.listenerCollection = new ListenerCollection();
        this.layoutTypes = new HashMap<>();
    }

    public void add(ViewCell viewCell) {
        this.viewCells.add(viewCell);
    }

    public void addAll(Collection<? extends ViewCell> viewCells) {
        this.viewCells.addAll(viewCells);
    }

    public void setAll(Collection<? extends ViewCell> viewCells) {
        this.viewCells.clear();
        this.viewCells.addAll(viewCells);
    }

    public void prependAll(Collection<? extends ViewCell> viewCells) {
        List<ViewCell> newList = new ArrayList<>();
        newList.addAll(viewCells);
        newList.addAll(this.viewCells);

        this.viewCells.clear();
        this.viewCells.addAll(newList);
    }

    public void addListener(Object listener) {
        listenerCollection.addListener(listener);
    }

    public void removeListener(Object listener) {
        listenerCollection.removeListener(listener);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

            Class<? extends BaseViewHolder> layoutViewHolder = layoutTypes.get(viewType);
            Constructor<? extends BaseViewHolder> viewHolderConstructor = layoutViewHolder.getConstructor(View.class);

            return viewHolderConstructor.newInstance(view);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int viewCellIndex = ViewCellUtils.getViewCellIndex(viewCells, position);
        int internalViewCellIndex = ViewCellUtils.getInternalViewCellIndex(viewCells, position);

        viewCells.get(viewCellIndex).bindListeners(holder, listenerCollection, internalViewCellIndex);
        viewCells.get(viewCellIndex).bindViewCell(holder, internalViewCellIndex);
    }

    @Override
    public int getItemCount() {
        return ViewCellUtils.getTotalCount(viewCells);
    }

    @Override
    @SuppressWarnings("unchecked")
    public int getItemViewType(int position) {
        int viewCellIndex = ViewCellUtils.getViewCellIndex(viewCells, position);
        int internalViewCellIndex = ViewCellUtils.getInternalViewCellIndex(viewCells, position);

        int itemId = viewCells.get(viewCellIndex).getLayoutId(internalViewCellIndex);
        Class<? extends BaseViewHolder> viewHolderClass = viewCells.get(viewCellIndex).getViewHolderClass(internalViewCellIndex);

        layoutTypes.put(itemId, viewHolderClass);

        return itemId;
    }

    @Override
    public long getItemId(int position) {
        int viewCellIndex = ViewCellUtils.getViewCellIndex(viewCells, position);
        int internalViewCellIndex = ViewCellUtils.getInternalViewCellIndex(viewCells, position);

        return viewCells.get(viewCellIndex).getItemId(internalViewCellIndex);
    }

    public ViewCell get(int position) {
        int viewCellIndex = ViewCellUtils.getViewCellIndex(viewCells, position);
        int internalViewCellIndex = ViewCellUtils.getInternalViewCellIndex(viewCells, position);

        return viewCells.get(viewCellIndex).get(internalViewCellIndex);
    }

    public void remove(int position) {
        int viewCellIndex = ViewCellUtils.getViewCellIndex(viewCells, position);
        int internalViewCellIndex = ViewCellUtils.getInternalViewCellIndex(viewCells, position);

        viewCells.get(viewCellIndex).remove(internalViewCellIndex);
    }
}
