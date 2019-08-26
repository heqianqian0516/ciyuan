package com.ciyuanplus.mobile.module.start_forum.start_note;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class StartNotePresenterModule {
    private final StartNoteContract.View mView;

    public StartNotePresenterModule(StartNoteContract.View mView) {
        this.mView = mView;
    }

    @Provides
    StartNoteContract.View providesStartPostContractView(){
        return mView;
    }
}
