package com.ciyuanplus.mobile.module.register.agreement;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class AgreementPresenterModule {
    private final AgreementContract.View mView;

    public AgreementPresenterModule(AgreementContract.View mView) {
        this.mView = mView;
    }

    @Provides
    AgreementContract.View providesAgreementContractView() {
        return mView;
    }
}
