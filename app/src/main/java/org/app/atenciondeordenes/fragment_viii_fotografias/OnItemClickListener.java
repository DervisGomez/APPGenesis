package org.app.atenciondeordenes.fragment_viii_fotografias;

import org.app.appgenesis.dao.Fotografia;

/**
 * Created by Alexander Jimenez (alexanderenriquejm@gmail.com) on 12/29/16.
 */

public interface OnItemClickListener {

    void onItemClicked(Fotografia fotografia, Integer constant, Integer position);
}