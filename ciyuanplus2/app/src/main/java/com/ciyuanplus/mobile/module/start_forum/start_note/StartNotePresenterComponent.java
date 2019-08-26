package com.ciyuanplus.mobile.module.start_forum.start_note;

import dagger.Component;

/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {StartNotePresenterModule.class})
public interface StartNotePresenterComponent {
    void inject(StartNoteActivity mActivity);
}
