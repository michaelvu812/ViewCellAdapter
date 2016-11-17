package ca.antonious.viewcelladapter;

import java.lang.reflect.ParameterizedType;

/**
 * Created by George on 2016-11-17.
 */

public abstract class ViewCell<VH extends BaseViewHolder> {
    public abstract int getLayoutId(int position);
    public abstract int getItemCount();
    public abstract void bindViewCell(VH viewHolder, int position);

    @SuppressWarnings("unchecked")
    public Class<? extends VH> getViewHolderClass() {
        return (Class<? extends VH>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }
}